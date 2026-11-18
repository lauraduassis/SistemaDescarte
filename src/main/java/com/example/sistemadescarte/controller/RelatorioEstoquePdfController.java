package com.example.sistemadescarte.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistemadescarte.services.EstoqueConsultaService;
import com.example.sistemadescarte.services.ProdutoService;
import com.example.sistemadescarte.services.RelatorioEstoquePdfService;

@RestController
public class RelatorioEstoquePdfController {
    private final ProdutoService produtos;
    private final EstoqueConsultaService estoque;
    private final RelatorioEstoquePdfService pdf;

    public RelatorioEstoquePdfController(ProdutoService produtos, EstoqueConsultaService estoque,
                                        RelatorioEstoquePdfService pdf) {
        this.produtos = produtos;
        this.estoque = estoque;
        this.pdf = pdf;
    }

    @GetMapping(value = "/relatorios/movimentacao-estoque.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerar() {
        byte[] conteudo = pdf.gerar(produtos.listarTodos(), estoque.consultarPorProduto(), estoque.consultarTotais());
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio-movimentacao-estoque.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(conteudo);
    }
}
