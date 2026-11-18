package com.example.sistemadescarte.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistemadescarte.entities.NotaDescarteEntity;
import com.example.sistemadescarte.services.NotaDescarteService;

@RestController
@RequestMapping("/descartes")
public class NotaDescarteController {

    private final NotaDescarteService notaDescarteService;
    private final com.example.sistemadescarte.repositories.NotaDescarteProdutoRepository movimentacaoRepository;
    private final com.example.sistemadescarte.services.RelatorioMovimentacaoPdfService relatorioPdfService;

    public NotaDescarteController(
            NotaDescarteService notaDescarteService,
            com.example.sistemadescarte.repositories.NotaDescarteProdutoRepository movimentacaoRepository,
            com.example.sistemadescarte.services.RelatorioMovimentacaoPdfService relatorioPdfService) {
        this.notaDescarteService = notaDescarteService;
        this.movimentacaoRepository = movimentacaoRepository;
        this.relatorioPdfService = relatorioPdfService;
    }

    @org.springframework.web.bind.annotation.GetMapping(
            value = "/movimentacoes/{id}/relatorio.pdf",
            produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> imprimirRelatorio(@PathVariable int id) {
        com.example.sistemadescarte.entities.NotaDescarteProduto movimentacao = movimentacaoRepository
                .findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND, "Movimentacao nao encontrada."));
        byte[] pdf = relatorioPdfService.gerar(movimentacao);
        return ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=relatorio-movimentacao-" + id + ".pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/{produtoId}")
    public ResponseEntity<?> salvar(
            @PathVariable int produtoId,
            @RequestBody NotaDescarteEntity notaDescarte) {
        try {
            return ResponseEntity.ok(
                notaDescarteService.salvar(produtoId, notaDescarte)
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("erro", e.getMessage())
            );
        }
    }
}
