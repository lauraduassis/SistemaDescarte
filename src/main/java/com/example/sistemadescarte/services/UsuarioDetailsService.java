package com.example.sistemadescarte.services;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sistemadescarte.entities.UsuarioEntity;
import com.example.sistemadescarte.repositories.UsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        UsuarioEntity usuario = usuarioRepository
            .findByUsuario(username)
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuário não encontrado.")
            );

        if (usuario.getPermissoes() == null ||
                usuario.getPermissoes().getNome() == null ||
                usuario.getPermissoes().getNome().isBlank()) {
            throw new UsernameNotFoundException(
                "Usuário sem permissão cadastrada."
            );
        }

        return User
            .withUsername(usuario.getUsuario())
            .password(usuario.getSenha())
            .roles(usuario.getPermissoes().getNome())
            .disabled(!usuario.isAtivo())
            .build();
    }
}