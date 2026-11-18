export default function permissoes() {
    return {
        registros: [], busca: "", form: {nome: "", descricao: ""}, editandoId: null,
        salvando: false, carregando: false,
        get filtrados() {
            const termo = this.busca.toLowerCase().trim();
            return this.registros.filter(r => `${r.nome} ${r.descricao}`.toLowerCase().includes(termo));
        },
                nomeExibicao(nome) {
    const texto = nome === "SUPERADMIN"
        ? "Administrador"
        : String(nome || "");

    return texto.charAt(0).toUpperCase() +
        texto.slice(1).toLowerCase();
},
        get nomeFormulario() {
            return this.nomeExibicao(this.form.nome);
        },
        set nomeFormulario(valor) {
            if (this.form.nome !== "SUPERADMIN") {
                this.form.nome = valor;
            }
        },
        async init() { await this.carregar(); },
        headers() {
            const h = {"Content-Type": "application/json"};
            const token = document.querySelector('meta[name="_csrf"]')?.content;
            const nome = document.querySelector('meta[name="_csrf_header"]')?.content;
            if (token && nome) h[nome] = token;
            return h;
        },
        async carregar() {
            this.carregando = true;
            try {
                const res = await fetch("/permissoes");
                if (!res.ok) throw new Error("Falha ao consultar permissões.");
                this.registros = await res.json();
            } catch (e) { this.$store.alert.erro(e.message); }
            finally { this.carregando = false; }
        },
        editar(role) {
            this.editandoId = role.id;
            this.form = {nome: role.nome, descricao: role.descricao || ""};
            document.getElementById("roleNome")?.focus();
        },
        limpar() { this.editandoId = null; this.form = {nome:"", descricao:""}; },
        async salvar() {
            if (this.salvando) return;
            this.salvando = true;
            try {
                const editar = this.editandoId !== null;
                const res = await fetch(editar ? `/permissoes/${this.editandoId}` : "/permissoes", {
                    method: editar ? "PUT" : "POST", headers: this.headers(),
                    body: JSON.stringify(this.form)
                });
                if (!res.ok) {
                    const dados = await res.json().catch(() => ({}));
                    throw new Error(dados.erro || "Não foi possível salvar a permissão.");
                }
                this.limpar(); await this.carregar();
                this.$store.alert.sucesso("Permissão salva com sucesso.");
            } catch (e) { this.$store.alert.erro(e.message); }
            finally { this.salvando = false; }
        },
        solicitarExclusao(role) {
            if (role.nome === "SUPERADMIN") return;
            window.dispatchEvent(new CustomEvent("abrir-confirmacao", {
                detail: {titulo: "Excluir permissão", mensagem: `Excluir ${role.nome}?`,
                    textoConfirmar: "Excluir", acao: () => this.excluir(role.id)}
            }));
        },
        async excluir(id) {
            try {
                const res = await fetch(`/permissoes/${id}`, {method: "DELETE", headers: this.headers()});
                if (!res.ok) {
                    const dados = await res.json().catch(() => ({}));
                    throw new Error(dados.erro || "Não foi possível excluir a permissão.");
                }
                await this.carregar(); this.$store.alert.sucesso("Permissão excluída com sucesso.");
            } catch (e) { this.$store.alert.erro(e.message); }
        }
    };
}


