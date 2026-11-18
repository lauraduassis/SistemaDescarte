import permissoes from "./alpine/permissoes.js";
import "../css/app.css";
import Alpine from "@alpinejs/csp";

import usuarios from "./alpine/usuarios.js";
import consultaColetas from "./alpine/consulta-coletas.js";
import coletaDetalhesProduto from "./alpine/coleta-detalhes-produto.js";
import empresasColetoras from "./alpine/empresas-coletoras.js";
import produto from "./alpine/produto.js";
import coletaProduto from "./alpine/coleta-produto.js";
import descarte from "./alpine/descarte.js";
import toast from "./alpine/toast.js";
import modal from "./alpine/modal.js";
import codigoBarras from "./alpine/codigo-barras.js";

import paginacao from "./alpine/paginacao.js";

import selectPremium from "./alpine/select-premium.js";

window.Alpine = Alpine;

Alpine.data("usuarios", usuarios);
Alpine.data("consultaColetas", consultaColetas);
Alpine.data("coletaDetalhesProduto", coletaDetalhesProduto);
Alpine.data("empresasColetoras", empresasColetoras);
Alpine.data("permissoes", permissoes);
Alpine.data("produto", produto);
Alpine.data("coletaProduto", coletaProduto);
Alpine.data("descarte", descarte);
Alpine.data("codigoBarras", codigoBarras);
Alpine.store("alert", toast());

Alpine.store("sidebar", {
    collapsed: false,
    mobileOpen: false,
    relatoriosAberto: window.location.pathname.startsWith("/relatorios"),

    alternarRelatorios() {
        this.relatoriosAberto = !this.relatoriosAberto;
    },
    abrirMenuMobile() {
        this.mobileOpen = true;
    },

    fecharMenuMobile() {
        this.mobileOpen = false;
    },

    alternarMenu() {
        this.collapsed = !this.collapsed;
    },

    textoAlternarMenu() {
        return this.collapsed ? "Expandir menu lateral" : "Recolher menu lateral";
    }
});

Alpine.data("modal", modal);

Alpine.data("paginacao", paginacao);

Alpine.data("selectPremium", selectPremium);

Alpine.start();

if (import.meta.env.MODE === "recarga-local") {
    const versao = new URL("./versao-frontend.txt", window.location.origin + "/js/");
    let anterior;
    async function conferirVersao() {
        try {
            const resposta = await fetch(versao, { cache: "no-store" });
            if (resposta.ok && !resposta.redirected) {
                const atual = await resposta.text();
                if (anterior && atual !== anterior) {
                    window.location.reload();
                    return;
                }
                anterior = atual;
            }
        } catch {
            // O servidor local pode ficar indisponível enquanto reinicia.
        }
        window.setTimeout(conferirVersao, 1000);
    }
    void conferirVersao();
}
