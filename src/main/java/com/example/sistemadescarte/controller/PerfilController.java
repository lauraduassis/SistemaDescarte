package com.example.sistemadescarte.controller;

import com.example.sistemadescarte.entities.UsuarioEntity;
import com.example.sistemadescarte.services.PerfilService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PerfilController {
    private final PerfilService service;

    public PerfilController(PerfilService service) {
        this.service = service;
    }

    private void preparar(Model model, Authentication authentication, String titulo) {
        UsuarioEntity usuario = service.atual(authentication.getName());
        model.addAttribute("titulo", titulo);
        model.addAttribute("usuarioLogado", usuario.getUsuario());
        model.addAttribute("permissao", usuario.getPermissao());
        model.addAttribute("perfilUsuario", usuario);
    }

    @GetMapping("/meu-perfil")
    public String meuPerfil(Model model, Authentication auth) {
        preparar(model, auth, "Meu Perfil");
        return "meu-perfil";
    }

    @GetMapping("/editar-perfil")
    public String editarPerfil(Model model, Authentication auth) {
        preparar(model, auth, "Editar Perfil");
        return "editar-perfil";
    }

    @PostMapping("/perfil/editar")
    public String salvarPerfil(@RequestParam String usuario,
                               @RequestParam String senhaAtual,
                               Authentication auth,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Model model) {
        try {
            service.alterarLogin(auth.getName(), usuario, senhaAtual);
            // A identidade de seguranca anterior precisa ser renovada.
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return "redirect:/login.html?perfilAtualizado";
        } catch (IllegalArgumentException e) {
            preparar(model, auth, "Editar Perfil");
            model.addAttribute("erroPerfil", e.getMessage());
            return "editar-perfil";
        }
    }

    @GetMapping("/configuracoes")
    public String configuracoes(Model model, Authentication auth) {
        preparar(model, auth, "Configurações");
        return "configuracoes";
    }

    @PostMapping("/perfil/senha")
    public String salvarSenha(@RequestParam String senhaAtual,
                              @RequestParam String novaSenha,
                              @RequestParam String confirmarSenha,
                              Model model,
                              Authentication auth) {
        try {
            service.alterarSenha(auth.getName(), senhaAtual, novaSenha, confirmarSenha);
            preparar(model, auth, "Configurações");
            model.addAttribute("sucessoPerfil", "Senha atualizada com sucesso.");
            return "configuracoes";
        } catch (IllegalArgumentException e) {
            preparar(model, auth, "Configurações");
            model.addAttribute("erroPerfil", e.getMessage());
            return "configuracoes";
        }
    }
}