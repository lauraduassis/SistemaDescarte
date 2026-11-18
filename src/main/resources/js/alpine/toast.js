export default function toast() {

    return {

        mostrar: false,
        tipo: "success",
        titulo: "",
        mensagem: "",

        abrir(tipo, titulo, mensagem) {

            this.tipo = tipo;
            this.titulo = titulo;
            this.mensagem = mensagem;
            this.mostrar = true;

            setTimeout(() => {
                this.mostrar = false;
            }, 4000);

        },

        sucesso(mensagem) {
            this.abrir("success","Sucesso",mensagem);
        },

        erro(mensagem) {
            this.abrir("error","Erro",mensagem);
        },

        aviso(mensagem) {
            this.abrir("warning","Aviso",mensagem);
        },

        info(mensagem) {
            this.abrir("info","Informação",mensagem);
        }

    };

}
