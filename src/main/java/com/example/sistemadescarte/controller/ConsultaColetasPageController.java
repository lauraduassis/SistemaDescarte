package com.example.sistemadescarte.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConsultaColetasPageController {
    @GetMapping("/consulta-coletas")
    public String pagina(Model model, Authentication authentication) {
        model.addAttribute("titulo", "Consulta de Coletas");
        model.addAttribute("usuarioLogado", authentication.getName());
        String permissao = authentication.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .filter(a -> a.startsWith("ROLE_"))
            .map(a -> a.substring(5)).findFirst().orElse("");
        model.addAttribute("permissao", permissao);
        return "consulta-coletas";
    }
}