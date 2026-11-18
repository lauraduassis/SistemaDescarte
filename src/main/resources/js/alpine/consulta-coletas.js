export default function consultaColetas() {
    return {
        termo: "",
        produtoId: "",
        patrimonio: "",
        equipamentosEncontrados: [],
        mensagemBusca: "Digite pelo menos 2 caracteres para procurar equipamentos.",
        registros: [],
        pesquisado: false,
        carregando: false,
        erro: "",
        buscaSequencia: 0,

        // Tabela premium: ordenacao e paginacao dos registros de coleta.
        colunaOrdenacao: "dataHora",
        direcaoOrdenacao: "desc",
        paginaAtual: 1,
        itensPorPagina: 10,

        get registrosOrdenados() {
            const coluna = this.colunaOrdenacao;
            const direcao = this.direcaoOrdenacao === "asc" ? 1 : -1;
            const comparador = new Intl.Collator("pt-BR", {numeric: true, sensitivity: "base"});
            return [...this.registros].sort((a, b) => {
                if (coluna === "dataHora") {
                    return ((new Date(a.dataHora || 0)).getTime() - (new Date(b.dataHora || 0)).getTime()) * direcao;
                }
                if (coluna === "retiradaId") {
                    return (Number(a.retiradaId) - Number(b.retiradaId)) * direcao;
                }
                return comparador.compare(String(a[coluna] ?? ""), String(b[coluna] ?? "")) * direcao;
            });
        },
        get totalPaginas() {
            return Math.max(1, Math.ceil(this.registros.length / Number(this.itensPorPagina)));
        },
        get registrosPaginados() {
            const pagina = Math.min(this.paginaAtual, this.totalPaginas);
            const inicio = (pagina - 1) * Number(this.itensPorPagina);
            return this.registrosOrdenados.slice(inicio, inicio + Number(this.itensPorPagina));
        },
        get textoRegistros() {
            return `${this.registros.length} registro(s)`;
        },
        get textoPagina() {
            return `${Math.min(this.paginaAtual, this.totalPaginas)} de ${this.totalPaginas}`;
        },
        get primeiraPagina() { return this.paginaAtual <= 1; },
        get ultimaPagina() { return this.paginaAtual >= this.totalPaginas; },
        ordenar(coluna) {
            if (this.colunaOrdenacao === coluna) {
                this.direcaoOrdenacao = this.direcaoOrdenacao === "asc" ? "desc" : "asc";
            } else {
                this.colunaOrdenacao = coluna;
                this.direcaoOrdenacao = "asc";
            }
            this.paginaAtual = 1;
        },
        alterarQuantidade(evento) {
            const quantidade = Number(evento.target.value);
            if (![5, 10, 20, 50].includes(quantidade)) return;
            this.itensPorPagina = quantidade;
            this.paginaAtual = 1;
        },
        paginaAnterior() { if (!this.primeiraPagina) this.paginaAtual--; },
        proximaPagina() { if (!this.ultimaPagina) this.paginaAtual++; },
        async atualizarBusca() {
            const numero = ++this.buscaSequencia;
            const termo = String(this.termo || "").trim();
            this.produtoId = "";
            this.patrimonio = "";
            this.equipamentosEncontrados = [];
            this.registros = [];
            this.paginaAtual = 1;
            this.pesquisado = false;
            this.erro = "";
            if (termo.length < 2) {
                this.mensagemBusca = "Digite pelo menos 2 caracteres para procurar equipamentos.";
                return;
            }
            this.mensagemBusca = "Procurando equipamentos...";
            try {
                const resposta = await fetch(`/coletas/produtos-pesquisa?termo=${encodeURIComponent(termo)}`, {
                    headers: { Accept: "application/json" }
                });
                const dados = await resposta.json().catch(() => null);
                if (!resposta.ok || !Array.isArray(dados)) {
                    throw new Error(dados?.erro || "Falha ao pesquisar equipamentos.");
                }
                if (numero !== this.buscaSequencia) return;
                this.equipamentosEncontrados = dados;
                this.mensagemBusca = dados.length
                    ? `${dados.length} equipamento(s) encontrado(s). Selecione o correto pelo patrimonio.`
                    : "Nenhum equipamento encontrado para esta pesquisa.";
            } catch (e) {
                if (numero !== this.buscaSequencia) return;
                this.equipamentosEncontrados = [];
                this.mensagemBusca = "Nao foi possivel pesquisar equipamentos.";
                this.erro = e.message || "Falha na pesquisa.";
            }
        },

        selecionarProduto() {
            const selecionado = this.equipamentosEncontrados.find(item => String(item.id) === String(this.produtoId));
            this.patrimonio = selecionado?.patrimonio || "";
            this.registros = [];
            this.paginaAtual = 1;
            this.pesquisado = false;
            this.erro = "";
        },

        async buscar() {
            this.selecionarProduto();
            if (!this.patrimonio) {
                this.erro = "Selecione um equipamento valido na lista.";
                return;
            }
            this.carregando = true;
            try {
                const resposta = await fetch(`/coletas/historico?patrimonio=${encodeURIComponent(this.patrimonio)}`,
                    { headers: { Accept: "application/json" } });
                const dados = await resposta.json().catch(() => null);
                if (!resposta.ok || !Array.isArray(dados)) {
                    throw new Error(dados?.erro || "Falha na consulta de coletas.");
                }
                this.registros = dados;
                this.paginaAtual = 1;
                this.pesquisado = true;
            } catch (e) {
                this.erro = e.message || "Falha na consulta de coletas.";
            } finally {
                this.carregando = false;
            }
        },

        formatarData(valor) {
            if (!valor) return "-";
            const data = new Date(valor);
            return Number.isNaN(data.getTime()) ? valor : data.toLocaleString("pt-BR");
        }
    };
}