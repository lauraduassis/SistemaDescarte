export default function selectPremium() {
    return {
        select: null,
        botao: null,
        menu: null,
        aberto: false,
        opcoes: [],

        init() {
            this.select = this.$el.querySelector("select");
            if (!this.select) return;

            if (this.select.dataset.optionsFromTable === "true") {
                const tiposDisponiveis = new Set(
                    Array.from(
                        document.querySelectorAll(
                            "tr[data-paginacao-item][data-tipo]"
                        )
                    )
                    .map((linha) => linha.dataset.tipo)
                    .filter(Boolean)
                );

                Array.from(this.select.options).forEach((opcao) => {
                    if (opcao.value && !tiposDisponiveis.has(opcao.value)) {
                        opcao.remove();
                    }
                });

                if (!Array.from(this.select.options).some(
                    (opcao) => opcao.value === this.select.value
                )) {
                    this.select.selectedIndex = 0;
                }
            }

            this.opcoes = Array.from(this.select.options);

            this.botao = document.createElement("button");
            this.botao.type = "button";
            this.botao.disabled = this.select.disabled;
            this.botao.setAttribute("aria-disabled", String(this.select.disabled));
            this.botao.className = "select-premium-trigger";
            this.botao.setAttribute("aria-haspopup", "listbox");
            this.botao.setAttribute("aria-expanded", "false");

            this.menu = document.createElement("div");
            this.menu.className = "select-premium-menu";
            this.menu.setAttribute("role", "listbox");
            this.menu.hidden = true;

            this.opcoes.forEach((opcao, indice) => {
                const item = document.createElement("button");

                item.type = "button";
                item.className = "select-premium-option";
                item.textContent = opcao.label;
                item.dataset.value = opcao.value;
                item.setAttribute("role", "option");

                item.addEventListener("click", () => {
                    this.selecionar(indice);
                });

                this.menu.appendChild(item);
            });

            this.botao.addEventListener("click", () => {
                this.alternar();
            });

            this.botao.addEventListener("keydown", (evento) => {
                if (evento.key === "ArrowDown" ||
                    evento.key === "ArrowUp") {
                    evento.preventDefault();
                    this.abrir();
                }
            });

            this.menu.addEventListener("keydown", (evento) => {
                const itens = Array.from(this.menu.children);
                const atual = itens.indexOf(document.activeElement);

                if (evento.key === "Escape") {
                    evento.preventDefault();
                    this.fechar();
                    this.botao.focus();
                }

                if (evento.key === "ArrowDown") {
                    evento.preventDefault();
                    itens[(atual + 1) % itens.length].focus();
                }

                if (evento.key === "ArrowUp") {
                    evento.preventDefault();
                    itens[(atual - 1 + itens.length) % itens.length].focus();
                }

                if (evento.key === "Home") {
                    evento.preventDefault();
                    itens[0].focus();
                }

                if (evento.key === "End") {
                    evento.preventDefault();
                    itens[itens.length - 1].focus();
                }
            });

            this.aoClicarFora = (evento) => {
                if (!this.$el.contains(evento.target)) {
                    this.fechar();
                }
            };

            document.addEventListener("pointerdown", this.aoClicarFora);

            this.select.before(this.botao);
            this.select.after(this.menu);
            this.select.hidden = true;

            this.select.addEventListener("change", () => {
                this.atualizarVisual();
            });
            this.observadorOpcoes = new MutationObserver(() => {
                const desativado = this.select.disabled;
                this.botao.disabled = desativado;
                this.botao.setAttribute("aria-disabled", String(desativado));
                if (desativado && this.aberto) this.fechar();
                this.sincronizarOpcoesDinamicas();
            });

            this.observadorOpcoes.observe(this.select, {
                childList: true,
                subtree: true,
                characterData: true,
                attributes: true,
                attributeFilter: ["disabled"]
            });


            this.atualizarVisual();
        },

        sincronizarOpcoesDinamicas() {
            if (!this.select || !this.menu) return;

            const novasOpcoes = Array.from(this.select.options);

            const mudou =
                novasOpcoes.length !== this.opcoes.length ||
                novasOpcoes.some((opcao, indice) =>
                    opcao.value !== this.opcoes[indice]?.value ||
                    opcao.label !== this.menu.children[indice]?.textContent ||
                    opcao !== this.opcoes[indice]
                );

            if (!mudou) {
                this.atualizarVisual();
                return;
            }

            this.opcoes = novasOpcoes;
            this.menu.replaceChildren();

            this.opcoes.forEach((opcao, indice) => {
                const item = document.createElement("button");

                item.type = "button";
                item.className = "select-premium-option";
                item.textContent = opcao.label;
                item.dataset.value = opcao.value;
                item.setAttribute("role", "option");

                item.addEventListener("click", () => {
                    this.selecionar(indice);
                });

                this.menu.appendChild(item);
            });

            this.atualizarVisual();
        },
        atualizarVisual() {
            if (!this.select || !this.botao) return;

            const selecionada = this.select.selectedOptions[0];

            this.botao.textContent = this.select.dataset.rotuloSelecionado || selecionada?.label || "";

            const seta = document.createElement("span");
            seta.className = "select-premium-chevron";
            seta.setAttribute("aria-hidden", "true");
            this.botao.appendChild(seta);

            Array.from(this.menu.children).forEach((item) => {
                const ativo = item.dataset.value === this.select.value;
                item.classList.toggle("is-selected", ativo);
                item.setAttribute("aria-selected", String(ativo));
            });
        },

        selecionar(indice) {
            this.select.selectedIndex = indice;
            this.select.dispatchEvent(new Event("change", {
                bubbles: true
            }));

            this.atualizarVisual();
            this.fechar();
            this.botao.focus();
        },

        alternar() {
            if (this.select?.disabled) return;
            this.aberto ? this.fechar() : this.abrir();
        },

        abrir() {
            this.aberto = true;
            this.menu.hidden = false;
            this.botao.setAttribute("aria-expanded", "true");

            const selecionado = this.menu.querySelector(".is-selected");
            (selecionado || this.menu.firstElementChild)?.focus();
        },

        fechar() {
            this.aberto = false;
            this.menu.hidden = true;
            this.botao.setAttribute("aria-expanded", "false");
        },

        destroy() {
            document.removeEventListener("pointerdown", this.aoClicarFora);
            this.observadorOpcoes?.disconnect();
        }
    };
}


