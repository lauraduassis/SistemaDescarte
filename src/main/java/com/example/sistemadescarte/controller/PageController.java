package com.example.sistemadescarte.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.sistemadescarte.services.ProdutoService;
import com.example.sistemadescarte.services.NotaDescarteService;
import com.example.sistemadescarte.services.RastreabilidadeColetaService;

@Controller
public class PageController {

    private final ProdutoService produtoService;
    private final NotaDescarteService notaDescarteService;
    private final RastreabilidadeColetaService rastreabilidadeColetaService;

    public PageController(
            ProdutoService produtoService,
            NotaDescarteService notaDescarteService,
            RastreabilidadeColetaService rastreabilidadeColetaService) {
        this.produtoService = produtoService;
        this.notaDescarteService = notaDescarteService;
        this.rastreabilidadeColetaService = rastreabilidadeColetaService;
    }

    private void adicionarDadosUsuario(Model model, Authentication authentication) {

        String usuario = "Visitante";
        String permissao = "";

        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {

            usuario = authentication.getName();

            permissao = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("");
        }

        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("permissao", permissao);
    }

    @GetMapping("/login.html")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/index"})
    public String index(Model model, Authentication authentication) {
        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Dashboard");
        model.addAttribute("itensPendentes", produtoService.contarPorStatus("PENDENTE"));
        model.addAttribute("prontosParaColeta", produtoService.contarPorStatus("AGUARDANDO_COLETA"));
        model.addAttribute("itensColetados", produtoService.contarPorStatus("COLETADO"));
        model.addAttribute("totalRegistrado", produtoService.contarTodos());
        model.addAttribute("movimentacoes", notaDescarteService.listarHistoricoDescarte());
        return "index";
    }

    @GetMapping("/produtos")
    public String produtos(Model model, Authentication authentication) {
        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Produtos");
        model.addAttribute("produtos", produtoService.listarTodos());
        return "produtos";
    }

    @GetMapping("/produtos/{id}/visualizar")
    public String visualizarProduto(
            @PathVariable int id,
            Model model,
            Authentication authentication) {

        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Visualizar Produto");
        model.addAttribute("produto", produtoService.buscarPorId(id));

        return "visualizar-produto";
    }
    @GetMapping("/produtos/{id}/editar")
    public String editarProduto(
            @PathVariable int id,
            Model model,
            Authentication authentication) {

        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Editar Produto");
        model.addAttribute("produto", produtoService.buscarPorId(id));
        model.addAttribute("modoEdicao", true);

        return "cadastro-produto";
    }

    @GetMapping("/cadastro-produto")
    public String cadastroProduto(Model model, Authentication authentication) {
        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Cadastrar Produto");
        return "cadastro-produto";
    }

    @GetMapping("/efetuar-descarte")
    public String efetuarDescarte(Model model, Authentication authentication) {
        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Efetuar Descarte");
        return "efetuar-descarte";
    }

    @GetMapping("/historico")
    public String historico(Model model, Authentication authentication) {
        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Histórico");
        model.addAttribute("movimentacoes", notaDescarteService.listarHistoricoDescarte());
        return "historico";
    }
    @GetMapping("/relatorios")
    public String relatorios(
            @org.springframework.web.bind.annotation.RequestParam(
                name = "tipo",
                defaultValue = "visao-geral"
            ) String tipo,
            @org.springframework.web.bind.annotation.RequestParam(
                name = "status",
                defaultValue = "TODOS"
            ) String status,
            Model model,
            Authentication authentication) {

        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Relatórios");
        model.addAttribute("tipoRelatorio", tipo);

        if ("visao-geral".equals(tipo)) {
            model.addAttribute("produtosVisaoGeral", produtoService.listarTodos());
            model.addAttribute("responsaveisColeta", rastreabilidadeColetaService.responsaveisPorProduto());
        }

        if ("por-status".equals(tipo)) {
            boolean statusExiste = status != null
                && produtoService.listarStatusDistintos().contains(status);

            String statusAtual = statusExiste ? status : "";

            model.addAttribute("statusRelatorioAtual", statusAtual);

            model.addAttribute(
                "produtosStatus",
                statusExiste
                    ? produtoService.listarPorStatus(statusAtual)
                    : java.util.List.of()
            );
        }

        String filtroStatus = java.util.Set.of(
            "TODOS",
            "PENDENTE",
            "AGUARDANDO_COLETA",
            "COLETADO"
        ).contains(status) ? status : "TODOS";

        model.addAttribute("statusSelecionado", filtroStatus);

        if ("status".equals(tipo)) {
            var produtosPorStatus = produtoService.listarTodos()
                .stream()
                .filter(produto ->
                    "TODOS".equals(filtroStatus)
                    || filtroStatus.equals(produto.getStatus())
                )
                .toList();

            model.addAttribute("produtosPorStatus", produtosPorStatus);
        }

        if ("descartes".equals(tipo)) {
            model.addAttribute("descartes", notaDescarteService.listarHistoricoDescarte());
        }

        long pendentes = produtoService.contarPorStatus("PENDENTE");
        long aguardando = produtoService.contarPorStatus("AGUARDANDO_COLETA");
        long coletados = produtoService.contarPorStatus("COLETADO");
        long total = produtoService.contarTodos();

        model.addAttribute("itensPendentes", pendentes);
        model.addAttribute("prontosParaColeta", aguardando);
        model.addAttribute("itensColetados", coletados);
        model.addAttribute("totalRegistrado", total);

        int percentualPendentes = total == 0
            ? 0 : (int) Math.round(pendentes * 100.0 / total);

        int percentualColeta = total == 0
            ? 0 : (int) Math.round(aguardando * 100.0 / total);

        int percentualColetados = total == 0
            ? 0 : (int) Math.round(coletados * 100.0 / total);

        model.addAttribute("percentualPendentes", percentualPendentes);
        model.addAttribute("percentualColeta", percentualColeta);
        model.addAttribute("percentualColetados", percentualColetados);

        return "relatorios";
    }
    @GetMapping("/admin/usuarios")
    public String usuarios(Model model, Authentication authentication) {
        adicionarDadosUsuario(model, authentication);
        model.addAttribute("titulo", "Usuários");
        return "usuarios";
    }
}





















