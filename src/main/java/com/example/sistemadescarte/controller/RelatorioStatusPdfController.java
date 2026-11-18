package com.example.sistemadescarte.controller;

import com.example.sistemadescarte.services.RelatorioStatusPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RelatorioStatusPdfController {
    private final RelatorioStatusPdfService relatorioStatusPdfService;

    public RelatorioStatusPdfController(RelatorioStatusPdfService relatorioStatusPdfService) {
        this.relatorioStatusPdfService = relatorioStatusPdfService;
    }

    @GetMapping(value = "/relatorios/status.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerar() {
        byte[] pdf = relatorioStatusPdfService.gerar();
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=relatorio-por-status.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(pdf);
    }
}