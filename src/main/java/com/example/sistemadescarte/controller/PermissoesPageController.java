package com.example.sistemadescarte.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PermissoesPageController {

    @GetMapping("/admin/permissoes")
    public String pagina(Model model, Authentication authentication) {
        model.addAttribute("titulo", "Gerenciar Permissões");
        model.addAttribute("permissao", "ADMIN");
        model.addAttribute("usuarioLogado", authentication.getName());
        return "permissoes";
    }
}
