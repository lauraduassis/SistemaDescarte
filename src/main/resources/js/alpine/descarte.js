
export default function descarte() {
    return {
        patrimonio: "",
        codigoBarras: "",
        produto: null,
        sincronizandoStatus: false,
        motivo: "",
        empresasColetoras: [],
        empresaColetoraId: "",
        erro: "",
        salvando: false,
        consultandoColeta: false,
        processandoColeta: false,
        mostrarBaixaAnterior: false,
        confirmacaoColetaAberta: false,

        init() {
            void this.carregarEmpresasColetoras();
            this.$nextTick(() => this.$refs.campoCodigoDescarte?.focus());
            window.addEventListener("confirmacao-fechada", (evento) => {
                if (!this.confirmacaoColetaAberta) return;
                if (evento.detail?.confirmada) return; // Aguarda concluir o PUT.
                this.confirmacaoColetaAberta = false;
                this.produto = null;
                this.$nextTick(() => this.sincronizarStatusAtual());
                this.codigoBarras = "";
                this.$nextTick(() => this.$refs.campoCodigoDescarte?.focus());
            });
        },

        async carregarEmpresasColetoras() {
            try {
                const resposta = await fetch("/coletas/empresas-ativas", {
                    headers: { Accept: "application/json" }
                });
                if (!resposta.ok) throw new Error("Nao foi possivel carregar as empresas coletoras.");
                const empresas = await resposta.json();
                if (!Array.isArray(empresas)) throw new Error("Resposta invalida das empresas coletoras.");
                this.empresasColetoras = empresas;
                if (!empresas.some(e => String(e.id) === String(this.empresaColetoraId))) {
                    this.empresaColetoraId = "";
                }
            } catch (erro) {
                this.$store.alert.erro(erro.message || "Falha ao carregar empresas coletoras.");
            }
        },
        empresaColetoraValida() {
            if (this.empresaColetoraId && this.empresasColetoras.some(e => String(e.id) === String(this.empresaColetoraId))) {
                return true;
            }
            this.$store.alert.erro("Selecione a empresa responsavel pela coleta.");
            this.$nextTick(() => this.$refs.empresaColetoraSelect?.focus());
            return false;
        },
        async buscarPorPatrimonio() {
            await this.buscar("patrimonio", this.patrimonio);
        },
        async buscarPorCodigoBarras() {
            if (this.consultandoColeta || this.processandoColeta || this.mostrarBaixaAnterior || this.confirmacaoColetaAberta) return;
            const codigoLido = String(this.codigoBarras || "").trim();
            if (!codigoLido) {
                this.$store.alert.erro("Leia ou informe o codigo de barras.");
                this.$nextTick(() => this.$refs.campoCodigoDescarte?.focus());
                return;
            }
            // Campo liberado visualmente para a proxima leitura, sem perder o codigo consultado.
            this.codigoBarras = "";
            this.consultandoColeta = true;
            try {
                await this.buscar("codigo-barras", codigoLido);
                if (!this.produto) {
                    if (this.erro) this.$store.alert.erro(this.erro);
                    this.$nextTick(() => this.$refs.campoCodigoDescarte?.focus());
                    return;
                }
                const dados = this.produto;
                const status = String(dados.status || "").trim().toUpperCase();
                if (status === "COLETADO") {
                    this.mostrarBaixaAnterior = true;
                    return;
                }
                if (status !== "AGUARDANDO_COLETA") {
                    this.$store.alert.erro("Este equipamento nao esta aguardando coleta. Status: " + (dados.status || "nao informado"));
                    this.$nextTick(() => this.$refs.campoCodigoDescarte?.focus());
                    return;
                }
                if (!this.empresaColetoraValida()) return;
                this.confirmacaoColetaAberta = true;
                window.dispatchEvent(new CustomEvent("abrir-confirmacao", {
                    detail: {
                        titulo: "Confirmar coleta",
                        mensagem: `Marcar ${dados.descricao || "o equipamento"} (patrimonio ${dados.patrimonio || "nao informado"}) como coletado?`,
                        textoConfirmar: "Confirmar coleta",
                        acao: () => this.confirmarColetaPorLeitura(dados.id)
                    }
                }));
            } finally {
                this.consultandoColeta = false;
            }
        },

        sincronizarStatusAtual() {
            const campo = document.getElementById("statusAtual");
            if (!campo) return;

            const status = String(this.produto?.status || "")
                .trim()
                .toUpperCase();

            const existe = Array.from(campo.options)
                .some(opcao => opcao.value === status);

            campo.value = existe ? status : "";

            this.sincronizandoStatus = true;
            try {
                campo.dispatchEvent(new Event("change", {
                bubbles: true
            }));
            } finally {
                this.sincronizandoStatus = false;
            }
        },
        obterHeaders() {
            const headers = {
                "Content-Type": "application/json",
                "Accept": "application/json"
            };

            const token = document.querySelector('meta[name="_csrf"]')?.content;
            const header = document.querySelector('meta[name="_csrf_header"]')?.content;

            if (token && header) {
                headers[header] = token;
            }

            return headers;
        },

        solicitarAlteracaoStatus() {
            if (this.sincronizandoStatus || !this.produto) return;

            if (this.salvando || this.processandoColeta ||
                this.confirmacaoColetaAberta) {
                this.sincronizarStatusAtual();
                return;
            }

            const atual = String(this.produto.status || "").toUpperCase();
            const novo = this.$refs.statusAtual?.value;

            if (!novo || novo === atual) return;

            // Volta ao status real enquanto aguarda a confirmacao.
            this.sincronizarStatusAtual();

            if (atual === "PENDENTE" && novo === "AGUARDANDO_COLETA") {
                if (!String(this.motivo || "").trim()) {
                    this.$store.alert.erro(
                        "Informe o motivo do descarte antes de alterar o status."
                    );
                    return;
                }

                window.dispatchEvent(new CustomEvent("abrir-confirmacao", {
                    detail: {
                        titulo: "Registrar descarte",
                        mensagem: "Confirmar o descarte deste equipamento e alterar o status para Aguardando coleta?",
                        textoConfirmar: "Confirmar descarte",
                        acao: () => this.registrarDescarte()
                    }
                }));
                return;
            }

            if (atual === "AGUARDANDO_COLETA" && novo === "COLETADO") {
                if (!this.empresaColetoraValida()) return;
                this.confirmacaoColetaAberta = true;

                window.dispatchEvent(new CustomEvent("abrir-confirmacao", {
                    detail: {
                        titulo: "Confirmar coleta",
                        mensagem: "Confirmar a coleta deste equipamento?",
                        textoConfirmar: "Confirmar coleta",
                        acao: () => this.confirmarColetaPorLeitura(this.produto.id)
                    }
                }));
                return;
            }

            this.$store.alert.erro(
                "Transicao de status nao permitida: " + atual + " para " + novo
            );
        },

        async registrarDescarte() {
            if (this.salvando) return;

            if (!this.produto?.id) {
                this.$store.alert.erro("Localize um equipamento primeiro.");
                return;
            }

            if (this.produto.status !== "PENDENTE") {
                this.$store.alert.erro(
                    "Somente equipamentos pendentes podem registrar descarte."
                );
                return;
            }

            const motivo = String(this.motivo || "").trim();

            if (!motivo) {
                this.$store.alert.erro("Informe o motivo do descarte.");
                return;
            }

            this.salvando = true;

            try {
                const resposta = await fetch(
                    `/descartes/${encodeURIComponent(this.produto.id)}`,
                    {
                        method: "POST",
                        headers: this.obterHeaders(),
                        body: JSON.stringify({ motivo })
                    }
                );

                let dados = {};
                try {
                    dados = await resposta.json();
                } catch (_) {}

                if (!resposta.ok) {
                    throw new Error(
                        dados.erro || dados.message ||
                        "Nao foi possivel registrar o descarte."
                    );
                }

                this.produto = {
                    ...this.produto,
                    status: "AGUARDANDO_COLETA"
                };

                this.sincronizarStatusAtual();

                this.$store.alert.sucesso(
                    "Descarte registrado. Status: Aguardando coleta."
                );

            } catch (erro) {
                this.$store.alert.erro(
                    erro.message || "Erro ao registrar descarte."
                );
                this.sincronizarStatusAtual();
            } finally {
                this.salvando = false;
            }
        },
        async buscar(tipo, valor) {
            this.erro = "";
            this.produto = null;
                this.$nextTick(() => this.sincronizarStatusAtual());

            if (!valor || !valor.trim()) {
                this.erro = "Informe o patrimônio ou o código de barras.";
                return;
            }

            try {
                const resposta = await fetch(
                    `/produtos/buscar/${tipo}?valor=${encodeURIComponent(valor.trim())}`
                );

                const dados = await resposta.json();

                if (!resposta.ok) {
                    throw new Error(dados.erro || "Produto não encontrado.");
                }

                this.produto = dados;
                this.$nextTick(() => this.sincronizarStatusAtual());
            } catch (erro) {
                this.erro = erro.message || "Não foi possível localizar o produto.";
            }
        },
        async confirmarColetaPorLeitura(id) {
            if (this.processandoColeta) return;
            if (!this.empresaColetoraValida()) {
                this.confirmacaoColetaAberta = false;
                return;
            }
            this.processandoColeta = true;
            try {
                const resposta = await fetch("/coletas/confirmar", {
                    method: "POST",
                    headers: this.obterHeaders(),
                    body: JSON.stringify({
                        produtoId: Number(id),
                        empresaColetoraId: Number(this.empresaColetoraId)
                    })
                });
                const dados = await resposta.json().catch(() => ({}));
                if (!resposta.ok) throw new Error(dados.erro || dados.message || "Falha ao registrar a coleta.");
                this.$store.alert.sucesso(`Coleta registrada pela empresa ${dados.empresaColetora}.`);
                this.produto = { ...this.produto, status: "COLETADO" };
                this.sincronizarStatusAtual();
            } catch (erro) {
                this.$store.alert.erro(erro.message || "Falha ao registrar a coleta.");
            } finally {
                this.processandoColeta = false;
                this.confirmacaoColetaAberta = false;
                this.codigoBarras = "";
                this.$nextTick(() => this.$refs.campoCodigoDescarte?.focus());
            }
        },        fecharBaixaAnterior() {
            this.mostrarBaixaAnterior = false;
            this.produto = null;
                this.$nextTick(() => this.sincronizarStatusAtual());
            this.codigoBarras = "";
            this.$nextTick(() => this.$refs.campoCodigoDescarte?.focus());
        }
    };
}



