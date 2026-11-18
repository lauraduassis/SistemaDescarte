export default function modal() {
    return {
        mostrar: false,
        titulo: "",
        mensagem: "",
        textoConfirmar: "Confirmar",
        textoCancelar: "Cancelar",
        confirmando: false,
        acao: null,

        init() {
            window.addEventListener("abrir-confirmacao", (evento) => {
                const dados = evento.detail;

                this.abrir(
                    dados.titulo,
                    dados.mensagem,
                    dados.acao,
                    dados.textoConfirmar || "Confirmar"
                );
            });
        },

        abrir(titulo, mensagem, acao, textoConfirmar = "Confirmar") {
            this.titulo = titulo;
            this.mensagem = mensagem;
            this.textoConfirmar = textoConfirmar;
            this.acao = acao;
            this.mostrar = true;
        },
        async confirmar() {
            const acao = this.acao;
            this.confirmando = true;
            this.fechar();
            this.confirmando = false;
            if (typeof acao === "function") await acao();
        },

        fechar() {
            const confirmada = this.confirmando;
            this.mostrar = false;
            this.acao = null;
            window.dispatchEvent(new CustomEvent("confirmacao-fechada", {
                detail: { confirmada }
            }));
        }
    };
}
