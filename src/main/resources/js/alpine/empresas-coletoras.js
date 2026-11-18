export default function empresasColetoras() {
    return {
        empresas: [],
        busca: "",
        situacao: "",
        editandoId: null,
        carregando: false,
        salvando: false,
        form: { nomeFantasia: "", responsavelColeta: "", telefone: "", email: "", ativo: true },

        async init() { await this.carregar(); },

        get tituloFormulario() { return this.editandoId === null ? "01 - Cadastrar empresa coletora" : "01 - Editar empresa coletora"; },
        get textoSalvar() { return this.salvando ? "Salvando..." : (this.editandoId === null ? "Cadastrar empresa" : "Salvar alteracoes"); },
        get empresasFiltradas() {
            const termo = this.normalizar(this.busca);
            return this.empresas.filter(empresa => {
                const todos = this.normalizar(`${empresa.id} ${empresa.nomeFantasia} ${empresa.responsavelColeta} ${empresa.telefone} ${empresa.email}`);
                const situacaoOK = !this.situacao || (this.situacao === "ATIVA" ? empresa.ativo : !empresa.ativo);
                return situacaoOK && (!termo || todos.includes(termo));
            });
        },
        normalizar(texto) { return String(texto || "").normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLowerCase(); },
        headers() {
            const h = { "Content-Type": "application/json" };
            const token = document.querySelector('meta[name="_csrf"]')?.content;
            const header = document.querySelector('meta[name="_csrf_header"]')?.content;
            if (token && header) h[header] = token;
            return h;
        },
        async carregar() {
            this.carregando = true;
            try {
                const resposta = await fetch("/empresas-coletoras");
                if (!resposta.ok) throw new Error("Nao foi possivel listar as empresas.");
                this.empresas = await resposta.json();
            } catch (erro) { this.$store.alert.erro(erro.message); }
            finally { this.carregando = false; }
        },
        limpar() {
            this.editandoId = null;
            this.form = { nomeFantasia: "", responsavelColeta: "", telefone: "", email: "", ativo: true };
        },
        editar(empresa) {
            this.editandoId = empresa.id;
            this.form = { nomeFantasia: empresa.nomeFantasia || "", responsavelColeta: empresa.responsavelColeta || "",
                          telefone: empresa.telefone || "", email: empresa.email || "", ativo: !!empresa.ativo };
            document.getElementById("empresaNome")?.focus();
            document.getElementById("empresaNome")?.scrollIntoView({ behavior: "smooth", block: "center" });
        },
        async requisicao(url, metodo, dados) {
            const resposta = await fetch(url, { method: metodo, headers: this.headers(), body: JSON.stringify(dados) });
            if (!resposta.ok) {
                const erro = await resposta.json().catch(() => ({}));
                throw new Error(erro.erro || "Nao foi possivel salvar a empresa.");
            }
            return resposta.json();
        },
        async salvar() {
            if (this.salvando) return;
            this.salvando = true;
            try {
                const editando = this.editandoId !== null;
                await this.requisicao(editando ? `/empresas-coletoras/${this.editandoId}` : "/empresas-coletoras",
                                      editando ? "PUT" : "POST", this.form);
                this.limpar();
                await this.carregar();
                this.$store.alert.sucesso(editando ? "Empresa atualizada com sucesso." : "Empresa cadastrada com sucesso.");
            } catch (erro) { this.$store.alert.erro(erro.message); }
            finally { this.salvando = false; }
        },
        situacaoTexto(empresa) { return empresa.ativo ? "Ativa" : "Inativa"; },
        textoAcaoSituacao(empresa) { return empresa.ativo ? "Inativar" : "Reativar"; },
        async alternarSituacao(empresa) {
            if (empresa.ativo && !window.confirm(`Inativar ${empresa.nomeFantasia}? O historico sera preservado.`)) return;
            try {
                await this.requisicao(`/empresas-coletoras/${empresa.id}`, "PUT", {
                    nomeFantasia: empresa.nomeFantasia, responsavelColeta: empresa.responsavelColeta,
                    telefone: empresa.telefone, email: empresa.email, ativo: !empresa.ativo
                });
                await this.carregar();
                this.$store.alert.sucesso(empresa.ativo ? "Empresa inativada." : "Empresa reativada.");
            } catch (erro) { this.$store.alert.erro(erro.message); }
        }
    };
}