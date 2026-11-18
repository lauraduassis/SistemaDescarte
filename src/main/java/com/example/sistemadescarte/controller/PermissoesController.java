package com.example.sistemadescarte.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.sistemadescarte.entities.PermissoesEntity;
import com.example.sistemadescarte.repositories.PermissoesRepository;

@RestController
@RequestMapping("/permissoes")
public class PermissoesController {
    private final PermissoesRepository repository;

    public PermissoesController(PermissoesRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<PermissoesEntity> listar() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody PermissoesEntity dados) {
        try {
            validar(dados, null);
            PermissoesEntity nova = new PermissoesEntity();
            nova.setNome(dados.getNome().trim().toUpperCase(java.util.Locale.ROOT));
            nova.setDescricao(descricao(dados.getDescricao()));
            return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(nova));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@PathVariable Integer id, @RequestBody PermissoesEntity dados) {
        var atual = repository.findById(id);
        if (atual.isEmpty()) return ResponseEntity.notFound().build();

        try {
                        PermissoesEntity permissoes = atual.get();
            validar(dados, id);
            permissoes.setNome(dados.getNome().trim().toUpperCase(java.util.Locale.ROOT));
            permissoes.setDescricao(descricao(dados.getDescricao()));
            return ResponseEntity.ok(repository.save(permissoes));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Integer id) {
        var permissoes = repository.findById(id);
        if (permissoes.isEmpty()) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void validar(PermissoesEntity dados, Integer idAtual) {
        if (dados == null || dados.getNome() == null || dados.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome da Permissao.");
        }
        String nome = dados.getNome().trim().toUpperCase(java.util.Locale.ROOT);
        if (!nome.matches("[A-Z0-9_]{2,40}")) {
            throw new IllegalArgumentException("Use 2 a 40 letras, numeros ou underline.");
        }
        repository.findByNomeIgnoreCase(nome).ifPresent(existente -> {
            if (!existente.getId().equals(idAtual)) {
                throw new IllegalArgumentException("Ja existe uma Permissao com esse nome.");
            }
        });
    }

    private String descricao(String valor) {
        String texto = valor == null ? "" : valor.trim();
        if (texto.length() > 255) throw new IllegalArgumentException("Descricao muito longa.");
        return texto;
    }
}