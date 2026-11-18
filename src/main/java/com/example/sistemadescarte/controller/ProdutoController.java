package com.example.sistemadescarte.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistemadescarte.entities.ProdutoEntity;
import com.example.sistemadescarte.services.ProdutoService;
import com.example.sistemadescarte.services.EtiquetaPdfService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final EtiquetaPdfService etiquetaPdfService;

    public ProdutoController(ProdutoService produtoService, EtiquetaPdfService etiquetaPdfService) {
        this.produtoService = produtoService;
        this.etiquetaPdfService = etiquetaPdfService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestBody ProdutoEntity produto) {
        try {
            ProdutoEntity produtoAtualizado = produtoService.atualizar(id, produto);
            return ResponseEntity.ok(produtoAtualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("erro", e.getMessage())
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> incluir(@RequestBody ProdutoEntity produto) {
        try {
            ProdutoEntity novoProduto = produtoService.incluir(produto);

            return new ResponseEntity<>(
                novoProduto,
                HttpStatus.CREATED
            );
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("erro", e.getMessage())
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        try {
            produtoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
    @GetMapping("/buscar/patrimonio")
    public ResponseEntity<?> buscarPorPatrimonio(@RequestParam String valor) {
        try {
            return ResponseEntity.ok(produtoService.buscarPorPatrimonio(valor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/buscar/codigo-barras")
    public ResponseEntity<?> buscarPorCodigoBarras(@RequestParam String valor) {
        try {
            return ResponseEntity.ok(produtoService.buscarPorCodigoBarras(valor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    // Rota antiga desativada: a coleta agora exige identificar a empresa.
    @PutMapping("/{id}/coletado")
    public ResponseEntity<?> marcarComoColetado(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
            Map.of("erro", "A coleta exige empresa coletora. Utilize a confirmacao com empresa na tela."));
    }

    @GetMapping(value = "/etiquetas", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarTodasEtiquetas() {
        var produtos = new java.util.ArrayList<>(produtoService.listarParaGestao());
        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        produtos.sort(java.util.Comparator.comparingInt(ProdutoEntity::getId));
        byte[] pdf = etiquetaPdfService.gerarEtiquetasTodos(produtos);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=etiquetas-produtos.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    @GetMapping(value = "/{id}/etiqueta", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarEtiqueta(@PathVariable int id) {
        ProdutoEntity produto = produtoService.buscarPorId(id);
        byte[] pdf = etiquetaPdfService.gerarEtiquetaTeste(produto);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=etiqueta-" + id + ".pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }}


