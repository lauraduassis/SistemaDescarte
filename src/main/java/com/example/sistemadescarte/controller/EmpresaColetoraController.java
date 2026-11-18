package com.example.sistemadescarte.controller;

import com.example.sistemadescarte.entities.EmpresaColetoraEntity;
import com.example.sistemadescarte.services.EmpresaColetoraService;
import com.example.sistemadescarte.services.EmpresaColetoraService.DadosEmpresa;
import com.example.sistemadescarte.services.EmpresaColetoraService.EmpresaNaoEncontradaException;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/empresas-coletoras")
public class EmpresaColetoraController {
    private final EmpresaColetoraService service;

    public EmpresaColetoraController(EmpresaColetoraService service) { this.service = service; }

    @GetMapping
    public List<EmpresaColetoraEntity> listar() { return service.listar(); }

    @PostMapping
    public ResponseEntity<EmpresaColetoraEntity> criar(@RequestBody DadosEmpresa dados) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dados));
    }

    @PutMapping("/{id}")
    public EmpresaColetoraEntity atualizar(@PathVariable Integer id, @RequestBody DadosEmpresa dados) {
        return service.atualizar(id, dados);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> entradaInvalida(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
    }

    @ExceptionHandler(EmpresaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> naoEncontrada(EmpresaNaoEncontradaException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", e.getMessage()));
    }
}