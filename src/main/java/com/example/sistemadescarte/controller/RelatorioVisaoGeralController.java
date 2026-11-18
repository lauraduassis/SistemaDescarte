package com.example.sistemadescarte.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.sistemadescarte.services.ProdutoService;
import com.example.sistemadescarte.services.RelatorioVisaoGeralPdfService;

@RestController
public class RelatorioVisaoGeralController {
    private final ProdutoService produtoService;
    private final RelatorioVisaoGeralPdfService pdfService;

    public RelatorioVisaoGeralController(ProdutoService produtoService,
                                       RelatorioVisaoGeralPdfService pdfService) {
        this.produtoService = produtoService;
        this.pdfService = pdfService;
    }

    @GetMapping(value = "/relatorios/visao-geral.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerar() {
        byte[] pdf = pdfService.gerar(produtoService.listarTodos());
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio-visao-geral.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}