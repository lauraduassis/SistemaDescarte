// Leitura apenas: reaproveita a API existente da Consulta de Coletas.
export default function coletaDetalhesProduto() {
    return {
        carregando: false,
        aguardando: false,
        semRegistro: false,
        temRegistro: false,
        erro: "",
        nomeEmpresa: "-",
        responsavel: "-",
        dataRetirada: "-",
        numeroRetirada: "-",
        telefone: "-",
        email: "-",

        async carregar() {
            const patrimonio = String(this.$el.dataset.patrimonio || "").trim();
            const status = String(this.$el.dataset.status || "").toUpperCase();
            if (status === "AGUARDANDO_COLETA") {
                this.aguardando = true;
                return;
            }
            if (!patrimonio) {
                this.erro = "Patrimonio indisponivel para consultar a retirada.";
                return;
            }
            this.carregando = true;
            try {
                const resposta = await fetch(`/coletas/historico?patrimonio=${encodeURIComponent(patrimonio)}`, {
                    headers: { Accept: "application/json" }
                });
                const dados = await resposta.json().catch(() => null);
                if (!resposta.ok || !Array.isArray(dados)) {
                    throw new Error(dados?.erro || "Nao foi possivel consultar a empresa coletora.");
                }
                if (dados.length === 0) {
                    this.semRegistro = true;
                    return;
                }
                // Se houver mais de uma retirada, exibe a mais recente.
                const registros = [...dados].sort((a, b) => {
                    const dataA = Date.parse(a.dataHora || "") || 0;
                    const dataB = Date.parse(b.dataHora || "") || 0;
                    return dataB - dataA;
                });
                const ultimo = registros[0];
                this.nomeEmpresa = ultimo.empresaColetora || "-";
                this.responsavel = ultimo.responsavelColeta || "-";
                this.telefone = ultimo.telefone || "-";
                this.email = ultimo.email || "-";
                this.numeroRetirada = ultimo.retiradaId == null ? "-" : String(ultimo.retiradaId);
                if (ultimo.dataHora) {
                    const data = new Date(ultimo.dataHora);
                    this.dataRetirada = Number.isNaN(data.getTime())
                        ? String(ultimo.dataHora)
                        : data.toLocaleString("pt-BR");
                }
                this.temRegistro = true;
            } catch (e) {
                this.erro = e.message || "Erro ao consultar a retirada.";
            } finally {
                this.carregando = false;
            }
        }
    };
}