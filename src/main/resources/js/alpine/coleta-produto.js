import { confirmarColetaComEmpresa } from "./confirmar-coleta.js";

export default function coletaProduto() {
    return {
        processando: false,
        async confirmar() {
            if (this.processando || this.$el.disabled) return;
            const botao = this.$el;
            const id = botao.dataset.id;
            if (!id) { this.$store.alert.erro("Identificador do produto nao encontrado."); return; }
            this.processando = true;
            botao.disabled = true;
            try {
                const headers = {};
                const token = document.querySelector('meta[name="_csrf"]')?.content;
                const nome = document.querySelector('meta[name="_csrf_header"]')?.content;
                if (token && nome) headers[nome] = token;
                const dados = await confirmarColetaComEmpresa(id, headers);
                if (!dados) return;
                this.$store.alert.sucesso(`Produto coletado por ${dados.empresaColetora}. Retirada ${dados.retiradaId}.`);
                window.location.reload();
            } catch (erro) {
                this.$store.alert.erro(erro.message || "Nao foi possivel registrar a coleta.");
            } finally { this.processando = false; botao.disabled = false; }
        }
    };
}