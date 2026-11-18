package com.example.sistemadescarte.controller;

import com.example.sistemadescarte.services.RastreabilidadeColetaService;
import com.example.sistemadescarte.services.RastreabilidadeColetaService.DadosConfirmacao;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coletas")
public class RastreabilidadeColetaController {
    private final RastreabilidadeColetaService service;

    public RastreabilidadeColetaController(RastreabilidadeColetaService service) {
        this.service = service;
    }

    @GetMapping("/empresas-ativas")
    public ResponseEntity<?> empresasAtivas() {
        return ResponseEntity.ok(service.empresasAtivas());
    }

    @PostMapping("/confirmar")
    public ResponseEntity<?> confirmar(@RequestBody DadosConfirmacao dados) {
        try {
            if (dados == null) throw new IllegalArgumentException("Informe os dados da coleta.");
            return ResponseEntity.ok(service.confirmar(dados.produtoId(), dados.empresaColetoraId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/quantidade-solicitada")
    public ResponseEntity<Double> quantidadeSolicitada(@RequestParam int produtoId) {
        return ResponseEntity.of(service.consultarQuantidadeSolicitada(produtoId));
    }

    @GetMapping("/historico")
    public ResponseEntity<?> historico(@RequestParam String patrimonio) {
        try {
            return ResponseEntity.ok(service.consultarPatrimonio(patrimonio));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}