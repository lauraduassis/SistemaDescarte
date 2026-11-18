package com.example.sistemadescarte.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

import com.example.sistemadescarte.entities.UsuarioEntity;
import com.example.sistemadescarte.services.UsuarioService;
import com.example.sistemadescarte.repositories.PermissoesRepository;
import com.example.sistemadescarte.entities.PermissoesEntity;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PermissoesRepository permissoesRepository;

    public UsuarioController(UsuarioService usuarioService, PermissoesRepository permissoesRepository) {
        this.usuarioService = usuarioService;
        this.permissoesRepository = permissoesRepository;
    }
    @GetMapping("/permissoes")
    public ResponseEntity<List<PermissoesEntity>> listarPermissoes() {
        return ResponseEntity.ok(permissoesRepository.findAll());
    }

    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> buscarPorId(@PathVariable int id) {
        Optional<UsuarioEntity> usuario = usuarioService.buscarPorId(id);

        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<UsuarioEntity> incluir(@RequestBody UsuarioEntity usuario) {
        UsuarioEntity novoUsuario = usuarioService.incluir(usuario);

        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable int id) {
        usuarioService.excluir(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(
            @PathVariable int id,
            @RequestBody UsuarioEntity usuario) {
        try {
            return ResponseEntity.ok(usuarioService.editar(id, usuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("erro", e.getMessage()));
        }
    }
}
