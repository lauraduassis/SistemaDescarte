package com.example.sistemadescarte.controller;

import com.example.sistemadescarte.services.RelatorioEquipamentosStatusPdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RelatorioEquipamentosStatusController {
    private final RelatorioEquipamentosStatusPdfService pdfService;

    public RelatorioEquipamentosStatusController(RelatorioEquipamentosStatusPdfService pdfService) {
        this.pdfService = pdfService;
    }

    @GetMapping(value = "/relatorios/por-status.pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerar(@RequestParam("status") String status) {
        byte[] pdf = pdfService.gerar(status);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"equipamentos-por-status.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}