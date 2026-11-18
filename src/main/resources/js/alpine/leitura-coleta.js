export default function leituraColeta() {
    return {
        codigo: "",
        produto: null,
        consultando: false,
        processando: false,
        mostrarBaixaAnterior: false,
        erro: "",

        async consultar() {
            if (this.consultando || this.processando || this.mostrarBaixaAnterior) return;
            const codigo = this.codigo.trim();
            if (!codigo) {
                this.$store.alert.erro("Leia ou informe o codigo de barras.");
                return;
            }
            this.erro = "";
            this.produto = null;
            this.consultando = true;
            try {
                const resposta = await fetch(`/produtos/buscar/codigo-barras?valor=${encodeURIComponent(codigo)}`, {
                    headers: { "Accept": "application/json" }
                });
                const dados = await resposta.json();
                if (!resposta.ok) throw new Error(dados.erro || dados.message || "Produto nao encontrado.");
                this.produto = dados;
                const status = String(dados.status || "").toUpperCase();
                if (status === "COLETADO") {
                    this.mostrarBaixaAnterior = true;
                    return;
                }
                if (status !== "AGUARDANDO_COLETA") {
                    this.$store.alert.erro("Este equipamento nao esta aguardando coleta. Status: " + (dados.status || "nao informado"));
                    return;
                }
                window.dispatchEvent(new CustomEvent("abrir-confirmacao", {
                    detail: {
                        titulo: "Confirmar coleta",
                        mensagem: `Marcar ${dados.descricao || "o equipamento"} (patrimonio ${dados.patrimonio || "nao informado"}) como coletado?`,
                        textoConfirmar: "Confirmar coleta",
                        acao: () => this.confirmarColeta(dados.id)
                    }
                }));
            } catch (e) {
                this.erro = e.message || "Falha ao consultar o equipamento.";
                this.$store.alert.erro(this.erro);
            } finally {
                this.consultando = false;
            }
        },

        async confirmarColeta(id) {
            if (this.processando) return;
            this.processando = true;
            try {
                const headers = { "Accept": "application/json" };
                const token = document.querySelector('meta[name="_csrf"]')?.content;
                const header = document.querySelector('meta[name="_csrf_header"]')?.content;
                if (token && header) headers[header] = token;
                const resposta = await fetch(`/produtos/${encodeURIComponent(id)}/coletado`, {
                    method: "PUT", headers
                });
                let dados = {};
                try { dados = await resposta.json(); } catch (_) {}
                if (!resposta.ok) {
                    throw new Error(dados.erro || dados.message || "Nao foi possivel registrar a coleta.");
                }
                this.$store.alert.sucesso("Produto marcado como coletado com sucesso!");
                this.produto = dados;
                this.codigo = "";
                this.$nextTick(() => this.$refs.campoCodigo?.focus());
            } catch (e) {
                this.$store.alert.erro(e.message || "Falha ao atualizar o produto.");
            } finally {
                this.processando = false;
            }
        },

        fecharBaixaAnterior() {
            this.mostrarBaixaAnterior = false;
            this.codigo = "";
            this.$nextTick(() => this.$refs.campoCodigo?.focus());
        }
    };
}