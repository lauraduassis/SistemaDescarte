// Seletor compartilhado por ambas as telas de confirmacao de coleta.
export async function confirmarColetaComEmpresa(produtoId, headers = {}) {
    const respostaEmpresas = await fetch("/coletas/empresas-ativas", {headers: {Accept: "application/json"}});
    if (!respostaEmpresas.ok) throw new Error("Nao foi possivel consultar as empresas coletoras ativas.");
    const empresas = await respostaEmpresas.json();
    if (!Array.isArray(empresas) || empresas.length === 0)
        throw new Error("Cadastre e ative uma empresa coletora antes de confirmar a coleta.");

    return new Promise(resolve => {
        const dialogo = document.createElement("dialog");
        dialogo.style.cssText = "border:0;border-radius:16px;padding:0;max-width:440px;width:calc(100% - 32px);box-shadow:0 20px 70px #0005;";
        const quadro = document.createElement("div");
        quadro.style.cssText = "padding:24px;background:white;color:#0f172a;font-family:inherit;";
        const titulo = document.createElement("h2");
        titulo.textContent = "Confirmar coleta";
        titulo.style.cssText = "font-size:21px;font-weight:700;margin-bottom:8px;";
        const explicacao = document.createElement("p");
        explicacao.textContent = "Escolha a empresa que realizou a retirada deste equipamento.";
        explicacao.style.cssText = "font-size:14px;color:#475569;margin-bottom:18px;";
        const rotulo = document.createElement("label");
        rotulo.textContent = "Empresa coletora ativa";
        rotulo.style.cssText = "display:block;font-weight:600;font-size:14px;margin-bottom:6px;";
        const selecao = document.createElement("select");
        selecao.style.cssText = "display:block;width:100%;border:1px solid #cbd5e1;border-radius:9px;padding:11px;background:white;";
        const inicial = document.createElement("option");
        inicial.value = "";
        inicial.textContent = "Selecione uma empresa...";
        selecao.appendChild(inicial);
        for (const empresa of empresas) {
            const opt = document.createElement("option");
            opt.value = String(empresa.id);
            opt.textContent = empresa.nomeFantasia;
            selecao.appendChild(opt);
        }
        const erro = document.createElement("p");
        erro.setAttribute("role", "alert");
        erro.style.cssText = "color:#b91c1c;font-size:13px;min-height:24px;margin-top:10px;";
        const botoes = document.createElement("div");
        botoes.style.cssText = "display:flex;justify-content:flex-end;gap:10px;margin-top:12px;";
        const cancelar = document.createElement("button");
        cancelar.type = "button";
        cancelar.textContent = "Cancelar";
        cancelar.style.cssText = "padding:10px 16px;border:1px solid #cbd5e1;border-radius:8px;background:white;cursor:pointer;";
        const confirmar = document.createElement("button");
        confirmar.type = "button";
        confirmar.textContent = "Confirmar retirada";
        confirmar.style.cssText = "padding:10px 16px;border:0;border-radius:8px;background:#1d4ed8;color:white;cursor:pointer;";
        botoes.append(cancelar, confirmar);
        quadro.append(titulo, explicacao, rotulo, selecao, erro, botoes);
        dialogo.appendChild(quadro);
        document.body.appendChild(dialogo);

        let resolvido = false;
        let ocupada = false;
        const encerrar = dados => {
            if (resolvido) return;
            resolvido = true;
            dialogo.close();
            dialogo.remove();
            resolve(dados);
        };
        cancelar.addEventListener("click", () => { if (!ocupada) encerrar(null); });
        dialogo.addEventListener("cancel", evento => {
            if (ocupada) { evento.preventDefault(); return; }
            evento.preventDefault();
            encerrar(null);
        });
        confirmar.addEventListener("click", async () => {
            if (ocupada) return;
            if (!selecao.value) { erro.textContent = "Selecione uma empresa coletora."; return; }
            ocupada = true;
            confirmar.disabled = true;
            cancelar.disabled = true;
            erro.textContent = "Registrando retirada...";
            try {
                const resposta = await fetch("/coletas/confirmar", {
                    method: "POST",
                    headers: {...headers, "Content-Type": "application/json", Accept: "application/json"},
                    body: JSON.stringify({produtoId: Number(produtoId), empresaColetoraId: Number(selecao.value)})
                });
                const dados = await resposta.json().catch(() => ({}));
                if (!resposta.ok) throw new Error(dados.erro || dados.message || "Nao foi possivel confirmar a coleta.");
                encerrar(dados);
            } catch (ex) {
                erro.textContent = ex.message || "Erro ao confirmar a coleta.";
                ocupada = false;
                confirmar.disabled = false;
                cancelar.disabled = false;
            }
        });
        try { dialogo.showModal(); } catch (ex) { dialogo.remove(); resolve(null); }
    });
}