package com.example.sistemadescarte.services;

import com.example.sistemadescarte.entities.UsuarioEntity;
import com.example.sistemadescarte.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PerfilService {
    private final UsuarioRepository repository;
    private final PasswordEncoder encoder;

    public PerfilService(UsuarioRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public UsuarioEntity atual(String login) {
        return repository.findByUsuario(login)
                .orElseThrow(() -> new IllegalArgumentException("Conta nao encontrada."));
    }

    // Apenas a coluna usuario; nunca aceita permissoes_id ou ativo do formulario.
    @Transactional
    public void alterarLogin(String loginAtual, String novoLogin, String senhaAtual) {
        UsuarioEntity conta = atual(loginAtual);
        String novo = novoLogin == null ? "" : novoLogin.trim();
        if (novo.length() < 3 || novo.length() > 100 || !novo.matches("[\\p{L}\\p{N}._@-]+")) {
            throw new IllegalArgumentException("Informe um login de 3 a 100 caracteres, sem espacos.");
        }
        if (senhaAtual == null || !encoder.matches(senhaAtual, conta.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }
        if (!conta.getUsuario().equals(novo) && repository.findByUsuario(novo).isPresent()) {
            throw new IllegalArgumentException("Esse login ja esta em uso.");
        }
        conta.setUsuario(novo);
        repository.save(conta);
    }

    @Transactional
    public void alterarSenha(String login, String senhaAtual, String novaSenha, String confirmacao) {
        UsuarioEntity conta = atual(login);
        if (senhaAtual == null || !encoder.matches(senhaAtual, conta.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }
        if (novaSenha == null || novaSenha.length() < 8 || novaSenha.length() > 72) {
            throw new IllegalArgumentException("A nova senha deve ter entre 8 e 72 caracteres.");
        }
        if (!novaSenha.equals(confirmacao)) {
            throw new IllegalArgumentException("A confirmacao nao corresponde a nova senha.");
        }
        if (encoder.matches(novaSenha, conta.getSenha())) {
            throw new IllegalArgumentException("A nova senha precisa ser diferente da atual.");
        }
        conta.setSenha(encoder.encode(novaSenha));
        repository.save(conta);
    }
}