package com.example.sistemadescarte.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sistemadescarte.entities.PermissoesEntity;
import com.example.sistemadescarte.entities.UsuarioEntity;
import com.example.sistemadescarte.repositories.PermissoesRepository;
import com.example.sistemadescarte.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PermissoesRepository permissoesRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PermissoesRepository permissoesRepository,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.permissoesRepository = permissoesRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<UsuarioEntity> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> buscarPorId(int id) {
        return usuarioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<UsuarioEntity> buscarPorUsuario(String usuario) {
        return usuarioRepository.findByUsuario(usuario);
    }

    @Transactional
    public UsuarioEntity incluir(UsuarioEntity usuario) {
        PermissoesEntity permissao = validarDados(usuario, null);

        usuario.setUsuario(usuario.getUsuario().trim());
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario.setPermissoes(permissao);

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public UsuarioEntity editar(int id, UsuarioEntity dados) {
        UsuarioEntity existente = usuarioRepository.findById(id)
            .orElseThrow(() ->
                new IllegalArgumentException("Usuário não encontrado.")
            );

        PermissoesEntity permissao = validarDados(dados, id);

        existente.setUsuario(dados.getUsuario().trim());
        existente.setPermissoes(permissao);
        existente.setAtivo(dados.isAtivo());

        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dados.getSenha()));
        }

        return usuarioRepository.save(existente);
    }

    private PermissoesEntity validarDados(
            UsuarioEntity usuario,
            Integer idIgnorado) {

        if (usuario.getUsuario() == null ||
                usuario.getUsuario().isBlank()) {
            throw new IllegalArgumentException(
                "Informe o nome do usuário."
            );
        }

        String nomePermissao = usuario.getPermissao();

        if (nomePermissao == null || nomePermissao.isBlank()) {
            throw new IllegalArgumentException(
                "Informe a permissão do usuário."
            );
        }

        PermissoesEntity permissao = permissoesRepository
            .findByNomeIgnoreCase(nomePermissao.trim())
            .orElseThrow(() ->
                new IllegalArgumentException(
                    "Selecione uma permissão cadastrada."
                )
            );

        Optional<UsuarioEntity> encontrado = usuarioRepository
            .findByUsuario(usuario.getUsuario().trim());

        if (encontrado.isPresent() &&
                (idIgnorado == null ||
                 encontrado.get().getId() != idIgnorado)) {
            throw new IllegalArgumentException(
                "Já existe um usuário com esse nome."
            );
        }

        if (idIgnorado == null &&
                (usuario.getSenha() == null ||
                 usuario.getSenha().isBlank())) {
            throw new IllegalArgumentException(
                "Informe a senha do usuário."
            );
        }

        return permissao;
    }

    @Transactional
    public void excluir(int id) {
        usuarioRepository.deleteById(id);
    }
}