export default function produto() {
    return {
        salvando: false,
        id: null,
        quantidade: "",

        form: {
            patrimonio: "",
            descricao: "",
            categoria: "",
            marca: "",
            modelo: "",
            observacoes: ""
        },

        init() {
            const dados = document.getElementById("dados-produto");

            if (!dados || !dados.dataset.id) {
                return;
            }

            this.id = Number(dados.dataset.id);
            this.quantidade = Number(dados.dataset.quantidade) || "";
            this.form.patrimonio = dados.dataset.patrimonio || "";
            this.form.descricao = dados.dataset.descricao || "";
            this.form.categoria = dados.dataset.categoria || "";
            this.form.marca = dados.dataset.marca || "";
            this.form.modelo = dados.dataset.modelo || "";
            this.form.observacoes = dados.dataset.observacoes || "";
        },

        solicitarEdicao(botao) {
            const id = botao.dataset.id;

            window.dispatchEvent(new CustomEvent("abrir-confirmacao", {
                detail: {
                    titulo: "Editar produto",
                    mensagem: "Deseja editar este produto?",
                    textoConfirmar: "Editar",
                    acao: () => {
                        window.location.href = `/produtos/${id}/editar`;
                    }
                }
            }));
        },

        solicitarExclusao(botao) {
            const id = botao.dataset.id;

            window.dispatchEvent(new CustomEvent("abrir-confirmacao", {
                detail: {
                    titulo: "Excluir produto",
                    mensagem: "Deseja excluir este produto?",
                    textoConfirmar: "Excluir",
                    acao: () => this.excluir(id)
                }
            }));
        },

        async excluir(id) {
            try {
                const resposta = await fetch(`/produtos/${id}`, {
                    method: "DELETE",
                    headers: this.obterHeaders()
                });

                if (!resposta.ok) throw new Error("Erro ao excluir produto.");

                this.$store.alert.sucesso("Produto excluído com sucesso.");
                setTimeout(() => window.location.href = "/produtos", 1200);
            } catch (erro) {
                console.error("Produtos:", erro);
                this.$store.alert.erro(erro.message || "Não foi possível excluir o produto.");
            }
        },

        obterHeaders() {
            const headers = {
                "Content-Type": "application/json"
            };

            const csrfToken = document.querySelector('meta[name="_csrf"]')?.content;
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.content;

            if (csrfToken && csrfHeader) {
                headers[csrfHeader] = csrfToken;
            }

            return headers;
        },

        async salvar() {
            if (this.salvando) return;

            this.salvando = true;

            try {
                const editando = this.id !== null;
                const url = editando ? `/produtos/${this.id}` : "/produtos";
                const metodo = editando ? "PUT" : "POST";

                const parametros = new URLSearchParams({ quantidade: this.quantidade });
                const resposta = await fetch(`${url}?${parametros}`, {
                    method: metodo,
                    headers: this.obterHeaders(),
                    body: JSON.stringify(this.form)
                });

                if (!resposta.ok) {
                    let mensagem = editando
                        ? "Erro ao atualizar produto."
                        : "Erro ao cadastrar produto.";

                    try {
                        const dados = await resposta.json();
                        mensagem = dados.erro || dados.message || mensagem;
                    } catch {
                        // Mantém a mensagem padrão.
                    }

                    throw new Error(mensagem);
                }

                const produtoSalvo = await resposta.json();

                this.$store.alert.sucesso(
                    editando
                        ? "Produto atualizado com sucesso."
                        : "Produto cadastrado com sucesso."
                );

                setTimeout(() => {
                    window.location.href =
                        `/produtos/${produtoSalvo.id}/visualizar`;
                }, 1200);
            } catch (erro) {
                console.error("Produtos:", erro);

                this.$store.alert.erro(
                    erro.message || "Não foi possível salvar o produto."
                );
            } finally {
                this.salvando = false;
            }
        }
    };
}







