export default function paginacao() {
    return {
        colunaOrdenacao: "data",
        direcaoOrdenacao: "desc",
        tratarCliqueOrdenacao: null,
        paginaAtual: 1,
        itensPorPagina: 10,
        totalRegistros: 0,
        totalGeral: 0,

        busca: "",
        tipo: "",
        periodo: "",

        eventos: [],

        init() {
            this.conectarFiltro(this.$el.dataset.buscaId || "buscaHistorico", "input", (evento) => {
                this.busca = evento.target.value;
                this.reiniciar();
            });

            this.conectarFiltro(this.$el.dataset.tipoId || "tipoMovimentacao", "change", (evento) => {
                this.tipo = evento.target.value;
                this.reiniciar();
            });

            this.conectarFiltro(this.$el.dataset.periodoId || "periodoHistorico", "change", (evento) => {
                this.periodo = evento.target.value;
                this.reiniciar();
            });

            this.busca = document.getElementById(this.$el.dataset.buscaId || "buscaHistorico")?.value || "";
            this.tipo = document.getElementById(this.$el.dataset.tipoId || "tipoMovimentacao")?.value || "";
            this.periodo = document.getElementById(this.$el.dataset.periodoId || "periodoHistorico")?.value || "";
            this.tratarCliqueOrdenacao = (evento) => {
                this.ordenarPorClique(evento);
            };

            this.$el.addEventListener("click", this.tratarCliqueOrdenacao);

            this.atualizar();
        },

        conectarFiltro(id, evento, callback) {
            const elemento = document.getElementById(id);

            if (!elemento) return;

            elemento.addEventListener(evento, callback);

            this.eventos.push({
                elemento,
                evento,
                callback
            });
        },

        obterLinhas() {
            return Array.from(
                this.$el.querySelectorAll("tbody tr[data-paginacao-item]")
            );
        },

        get totalPaginas() {
            return Math.max(
                1,
                Math.ceil(this.totalRegistros / this.itensPorPagina)
            );
        },

        get estaNaPrimeiraPagina() {
            return this.paginaAtual <= 1;
        },

        get estaNaUltimaPagina() {
            return this.paginaAtual >= this.totalPaginas;
        },

        get textoPagina() {
            return `${this.paginaAtual} de ${this.totalPaginas}`;
        },

        get textoRegistros() {
            if (this.totalRegistros === this.totalGeral) {
                return `${this.totalRegistros} registros`;
            }

            return `${this.totalRegistros} de ${this.totalGeral} registros`;
        },

        normalizar(texto) {
            return String(texto || "")
                .normalize("NFD")
                .replace(/[\u0300-\u036f]/g, "")
                .toLowerCase()
                .trim();
        },

        correspondeBusca(linha) {
            const termo = this.normalizar(this.busca);

            if (!termo) return true;

            const conteudo = this.normalizar(linha.textContent);

            return conteudo.includes(termo);
        },

        correspondeTipo(linha) {
            if (!this.tipo) return true;

            return linha.dataset.tipo === this.tipo;
        },

        correspondePeriodo(linha) {
            if (!this.periodo) return true;

            const valor = linha.dataset.data;

            if (!valor) return false;

            const data = new Date(valor);
            const agora = new Date();

            if (Number.isNaN(data.getTime())) return false;

            if (data > agora) return false;

            if (this.periodo === "HOJE") {
                return data.toDateString() === agora.toDateString();
            }

            let dias = 0;

            if (this.periodo === "7_DIAS") dias = 7;
            if (this.periodo === "30_DIAS") dias = 30;

            if (dias === 0) return true;

            const limite = new Date(agora);
            limite.setDate(limite.getDate() - dias);

            return data >= limite;
        },

        correspondeFiltros(linha) {
            return this.correspondeBusca(linha)
                && this.correspondeTipo(linha)
                && this.correspondePeriodo(linha);
        },

        reiniciar() {
            this.paginaAtual = 1;
            this.atualizar();
        },

        atualizar() {
            const linhas = this.obterLinhas();

            this.ordenarLinhas(linhas);

            this.totalGeral = linhas.length;

            const filtradas = linhas.filter(
                (linha) => this.correspondeFiltros(linha)
            );

            this.totalRegistros = filtradas.length;

            if (this.paginaAtual > this.totalPaginas) {
                this.paginaAtual = this.totalPaginas;
            }

            const inicio = (this.paginaAtual - 1) * this.itensPorPagina;
            const fim = inicio + this.itensPorPagina;

            const visiveis = new Set(filtradas.slice(inicio, fim));

            linhas.forEach((linha) => {
                linha.style.display = visiveis.has(linha) ? "" : "none";
            });

            this.destacarResultados(linhas);

            const vazio = this.$el.querySelector(".history-filter-empty");

            if (vazio) {
                vazio.style.display = this.totalRegistros === 0 ? "" : "none";
            }
        },

        removerDestaques() {
            this.$el.querySelectorAll("mark[data-busca-destaque]").forEach((marca) => {
                marca.replaceWith(
                    document.createTextNode(marca.textContent)
                );
            });

            this.$el.normalize();
        },

        destacarTexto(elemento, termo) {
            const walker = document.createTreeWalker(
                elemento,
                NodeFilter.SHOW_TEXT,
                {
                    acceptNode: (no) => {
                        if (!no.textContent.trim()) {
                            return NodeFilter.FILTER_REJECT;
                        }

                        if (no.parentElement.closest("button, a, script, style, mark")) {
                            return NodeFilter.FILTER_REJECT;
                        }

                        return NodeFilter.FILTER_ACCEPT;
                    }
                }
            );

            const nos = [];

            while (walker.nextNode()) {
                nos.push(walker.currentNode);
            }

            nos.forEach((no) => {
                const texto = no.textContent;
                const normalizado = this.normalizar(texto);
                const busca = this.normalizar(termo);

                let posicao = normalizado.indexOf(busca);

                if (posicao === -1) return;

                const fragmento = document.createDocumentFragment();
                let inicio = 0;

                while (posicao !== -1) {
                    fragmento.appendChild(
                        document.createTextNode(texto.slice(inicio, posicao))
                    );

                    const marca = document.createElement("mark");
                    marca.dataset.buscaDestaque = "true";
                    marca.className = "search-highlight";
                    marca.textContent = texto.slice(posicao, posicao + busca.length);

                    fragmento.appendChild(marca);

                    inicio = posicao + busca.length;
                    posicao = normalizado.indexOf(busca, inicio);
                }

                fragmento.appendChild(
                    document.createTextNode(texto.slice(inicio))
                );

                no.replaceWith(fragmento);
            });
        },

        destacarResultados(linhas) {
            this.removerDestaques();

            if (!this.busca.trim()) return;

            linhas.forEach((linha) => {
                if (linha.style.display !== "none") {
                    linha.querySelectorAll("td").forEach((celula) => {
                        this.destacarTexto(celula, this.busca);
                    });
                }
            });
        },
        ordenarPorClique(evento) {
            const botao = evento.target.closest("[data-sort-key]");

            if (!botao || !this.$el.contains(botao)) return;

            const coluna = botao.dataset.sortKey;

            if (this.colunaOrdenacao === coluna) {
                this.direcaoOrdenacao =
                    this.direcaoOrdenacao === "asc" ? "desc" : "asc";
            } else {
                this.colunaOrdenacao = coluna;
                this.direcaoOrdenacao = coluna === "data" ? "desc" : "asc";
            }

            this.paginaAtual = 1;
            this.atualizar();
        },

        obterValorOrdenacao(linha, coluna) {
            if (coluna === "data") {
                const data = new Date(linha.dataset.data || "");
                return Number.isNaN(data.getTime()) ? 0 : data.getTime();
            }

            const indices = {
                tipo: 1,
                produto: 2,
                responsavel: 3,
                observacao: 4
            };

            const indiceConfigurado = this.$el.querySelector(
                `[data-sort-key="${coluna}"]`
            )?.dataset.sortIndex;

            const indice = indiceConfigurado === undefined
                ? indices[coluna]
                : Number(indiceConfigurado);

            if (indice === undefined) return "";

            return linha.cells[indice]?.textContent.trim() || "";
        },

        ordenarLinhas(linhas) {
            const coluna = this.colunaOrdenacao;
            const direcao = this.direcaoOrdenacao === "asc" ? 1 : -1;

            const comparador = new Intl.Collator("pt-BR", {
                numeric: true,
                sensitivity: "base"
            });

            linhas.sort((a, b) => {
                const valorA = this.obterValorOrdenacao(a, coluna);
                const valorB = this.obterValorOrdenacao(b, coluna);

                if (coluna === "data") {
                    return (valorA - valorB) * direcao;
                }

                return comparador.compare(valorA, valorB) * direcao;
            });

            const corpo = this.$el.querySelector("tbody");
            if (!corpo) return;

            const referencia = corpo.querySelector("tr.empty-row");

            linhas.forEach((linha) => {
                corpo.insertBefore(linha, referencia);
            });

            this.$el.querySelectorAll("[data-sort-key]").forEach((botao) => {
                const ativo = botao.dataset.sortKey === coluna;

                botao.dataset.sortDirection =
                    ativo ? this.direcaoOrdenacao : "none";

                botao.closest("th")?.setAttribute(
                    "aria-sort",
                    ativo
                        ? this.direcaoOrdenacao === "asc"
                            ? "ascending"
                            : "descending"
                        : "none"
                );
            });
        },
        selecionarStatus(evento) {
            const status = evento.target.value;

            if (!status) return;

            const url = new URL("/relatorios", window.location.origin);
            url.searchParams.set("tipo", "por-status");
            url.searchParams.set("status", status);

            window.location.assign(url.toString());
        },
        proximaPagina() {
            if (this.estaNaUltimaPagina) return;

            this.paginaAtual++;
            this.atualizar();
        },

        paginaAnterior() {
            if (this.estaNaPrimeiraPagina) return;

            this.paginaAtual--;
            this.atualizar();
        },

        alterarQuantidade(evento) {
            const quantidade = Number(evento.target.value);

            if (![5, 10, 20, 50].includes(quantidade)) return;

            this.itensPorPagina = quantidade;
            this.reiniciar();
        },

        destroy() {
            if (this.tratarCliqueOrdenacao) {
                this.$el.removeEventListener(
                    "click",
                    this.tratarCliqueOrdenacao
                );
            }
            this.eventos.forEach(({ elemento, evento, callback }) => {
                elemento.removeEventListener(evento, callback);
            });

            this.eventos = [];
        }
    };
}




