
export default function usuarios() {
    return {

        usuarios: [],
        permissoes: [],
        usuarioVisualizado: null,

        buscaUsuarios: "",
        filtroNivelUsuarios: "",
        filtroStatusUsuarios: "",
        colunaOrdenacaoUsuarios: "id",
        direcaoOrdenacaoUsuarios: "asc",
        paginaAtualUsuarios: 1,
        itensPorPaginaUsuarios: 10,
        editandoId: null,
        carregando: false,
        salvando: false,

        form: {
            usuario: "",
            senha: "",
            permissao: "",
            ativo: true
        },

        normalizarUsuario(valor) {
            return String(valor ?? "")
                .normalize("NFD")
                .replace(/[\u0300-\u036f]/g, "")
                .toLowerCase()
                .trim();
        },

        get usuariosFiltrados() {
            const busca = this.normalizarUsuario(this.buscaUsuarios);

            const filtrados = this.usuarios.filter((usuario) => {
                const texto = this.normalizarUsuario(
                    `${usuario.id} ${usuario.usuario} ${usuario.permissao}`
                );

                const correspondeBusca = !busca || texto.includes(busca);

                const correspondeNivel =
                    !this.filtroNivelUsuarios ||
                    usuario.permissao === this.filtroNivelUsuarios;

                const correspondeStatus =
                    !this.filtroStatusUsuarios ||
                    (this.filtroStatusUsuarios === "ATIVO"
                        ? usuario.ativo === true
                        : usuario.ativo === false);

                return correspondeBusca &&
                    correspondeNivel &&
                    correspondeStatus;
            });

            const coluna = this.colunaOrdenacaoUsuarios;
            const direcao =
                this.direcaoOrdenacaoUsuarios === "asc" ? 1 : -1;

            const comparador = new Intl.Collator("pt-BR", {
                numeric: true,
                sensitivity: "base"
            });

            return filtrados.sort((a, b) => {
                if (coluna === "id") {
                    return (Number(a.id) - Number(b.id)) * direcao;
                }

                if (coluna === "ativo") {
                    return (Number(a.ativo) - Number(b.ativo)) * direcao;
                }

                return comparador.compare(
                    String(a[coluna] ?? ""),
                    String(b[coluna] ?? "")
                ) * direcao;
            });
        },

        get totalPaginasUsuarios() {
            return Math.max(
                1,
                Math.ceil(
                    this.usuariosFiltrados.length /
                    Number(this.itensPorPaginaUsuarios)
                )
            );
        },

        get usuariosPaginados() {
            const pagina = Math.min(
                this.paginaAtualUsuarios,
                this.totalPaginasUsuarios
            );

            const inicio =
                (pagina - 1) * Number(this.itensPorPaginaUsuarios);

            return this.usuariosFiltrados.slice(
                inicio,
                inicio + Number(this.itensPorPaginaUsuarios)
            );
        },

        get textoRegistrosUsuarios() {
            const encontrados = this.usuariosFiltrados.length;
            const total = this.usuarios.length;

            return encontrados === total
                ? `${total} registros`
                : `${encontrados} de ${total} registros`;
        },

        get textoPaginaUsuarios() {
            return `${Math.min(
                this.paginaAtualUsuarios,
                this.totalPaginasUsuarios
            )} de ${this.totalPaginasUsuarios}`;
        },

        get primeiraPaginaUsuarios() {
            return this.paginaAtualUsuarios <= 1;
        },

        get ultimaPaginaUsuarios() {
            return this.paginaAtualUsuarios >= this.totalPaginasUsuarios;
        },

        reiniciarPaginacaoUsuarios() {
            this.paginaAtualUsuarios = 1;
        },

        alterarQuantidadeUsuarios(evento) {
            const quantidade = Number(evento.target.value);

            if (![5, 10, 20, 50].includes(quantidade)) return;

            this.itensPorPaginaUsuarios = quantidade;
            this.reiniciarPaginacaoUsuarios();
        },

        ordenarUsuarios(coluna) {
            if (this.colunaOrdenacaoUsuarios === coluna) {
                this.direcaoOrdenacaoUsuarios =
                    this.direcaoOrdenacaoUsuarios === "asc"
                        ? "desc"
                        : "asc";
            } else {
                this.colunaOrdenacaoUsuarios = coluna;
                this.direcaoOrdenacaoUsuarios = "asc";
            }

            this.reiniciarPaginacaoUsuarios();
        },

        paginaAnteriorUsuarios() {
            if (this.primeiraPaginaUsuarios) return;

            this.paginaAtualUsuarios--;
        },

        proximaPaginaUsuarios() {
            if (this.ultimaPaginaUsuarios) return;

            this.paginaAtualUsuarios++;
        },
        async init() {
            await this.carregarPermissoes();
            await this.carregarUsuarios();
        },
        nomePermissao(nome) {
            const valor = nome === "SUPERADMIN" ? "Administrador" : String(nome || "").replace(/_/g, " ");
            return valor.charAt(0).toUpperCase() + valor.slice(1).toLowerCase();
        },
        async carregarPermissoes() {
            try {
                const resposta = await fetch("/usuarios/permissoes");
                if (!resposta.ok) throw new Error("Não foi possível carregar as permissões.");
                this.permissoes = await resposta.json();
            } catch (erro) {
                this.$store.alert.erro(erro.message);
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

        novoFormulario() {
            return {
                usuario: "",
                senha: "",
                permissao: "",
                ativo: true
            };
        },

        async carregarUsuarios() {
            this.carregando = true;

            try {
                const resposta = await fetch("/usuarios");

                if (!resposta.ok) {
                    throw new Error("Erro ao carregar usuários.");
                }

                this.usuarios = await resposta.json();
                this.reiniciarPaginacaoUsuarios();
            } catch (erro) {
                console.error("Usuários:", erro);
                this.$store.alert.erro("Não foi possível carregar os usuários.");
            } finally {
                this.carregando = false;
            }
        },

        visualizarUsuario(usuario) {
            this.usuarioVisualizado = {
                id: usuario.id,
                usuario: usuario.usuario,
                permissao: usuario.permissao,
                ativo: usuario.ativo
            };
        },

        fecharVisualizacao() {
            this.usuarioVisualizado = null;
        },
        editar(usuario) {
            this.editandoId = usuario.id;

            this.form = {
                usuario: usuario.usuario,
                senha: "",
                permissao: usuario.permissao,
                ativo: usuario.ativo
            };

            document.getElementById("novaSenha")?.removeAttribute("required");

            document.querySelector("form")?.scrollIntoView({
                behavior: "smooth",
                block: "start"
            });
        },

        cancelarEdicao() {
            this.editandoId = null;
            this.form = this.novoFormulario();

            const campoSenha = document.getElementById("novaSenha");

            if (campoSenha) {
                campoSenha.setAttribute("required", "");
            }
        },

        solicitarExclusao(usuario) {
            window.dispatchEvent(new CustomEvent("abrir-confirmacao", {
                detail: {
                    titulo: "Excluir usuário",
                    mensagem: "Deseja excluir o usuário " + usuario.usuario + "?",
                    textoConfirmar: "Excluir",
                    acao: () => this.excluir(usuario.id)
                }
            }));
        },

        async excluir(id) {
            try {
                const resposta = await fetch(`/usuarios/${id}`, {
                    method: "DELETE",
                    headers: this.obterHeaders()
                });

                if (!resposta.ok) {
                    throw new Error("Não foi possível excluir o usuário.");
                }

                await this.carregarUsuarios();
                this.$store.alert.sucesso("Usuário excluído com sucesso.");
            } catch (erro) {
                console.error("Usuários:", erro);
                this.$store.alert.erro("Não foi possível excluir o usuário.");
            }
        },

        async salvar() {
            if (this.salvando) return;

            this.salvando = true;

            const editando = this.editandoId !== null;
            const url = editando
                ? `/usuarios/${this.editandoId}`
                : "/usuarios";

            try {
                const resposta = await fetch(url, {
                    method: editando ? "PUT" : "POST",
                    headers: this.obterHeaders(),
                    body: JSON.stringify(this.form)
                });

                if (!resposta.ok) {
                    let mensagem = editando
                        ? "Erro ao atualizar usuário."
                        : "Erro ao cadastrar usuário.";

                    try {
                        const dados = await resposta.json();
                        mensagem = dados.erro || dados.message || mensagem;
                    } catch {
                        // Mantém a mensagem padrão se a resposta não for JSON.
                    }

                    throw new Error(mensagem);
                }

                this.cancelarEdicao();
                await this.carregarUsuarios();

                this.$store.alert.sucesso(
                    editando
                        ? "Usuário atualizado com sucesso."
                        : "Usuário cadastrado com sucesso."
                );
            } catch (erro) {
                console.error("Usuários:", erro);
                this.$store.alert.erro(erro.message || "Não foi possível salvar o usuário.");
            } finally {
                this.salvando = false;
            }
        }
    };
}


