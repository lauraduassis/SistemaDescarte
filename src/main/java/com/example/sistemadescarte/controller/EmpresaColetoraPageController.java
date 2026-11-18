package com.example.sistemadescarte.controller;

import java.util.Objects;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmpresaColetoraPageController {

    @GetMapping("/admin/empresas-coletoras")
    public String pagina(Model model, Authentication authentication) {
        model.addAttribute("titulo", "Empresas Coletoras");
        model.addAttribute("usuarioLogado", authentication.getName());
        String permissao = authentication.getAuthorities().stream()
            .map(a -> a.getAuthority())
            .filter(Objects::nonNull)
            .filter(a -> a.startsWith("ROLE_"))
            .map(a -> a.substring(5))
            .findFirst().orElse("");
        model.addAttribute("permissao", permissao);
        return "empresas-coletoras";
    }
}