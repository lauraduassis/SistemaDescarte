package com.example.sistemadescarte.controller;

import java.util.List;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import com.example.sistemadescarte.services.ProdutoService;

@ControllerAdvice
public class StatusRelatoriosAdvice {

    private final ProdutoService produtoService;

    public StatusRelatoriosAdvice(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @ModelAttribute("statusRelatorios")
    public List<String> statusRelatorios() {
        return produtoService.listarStatusDistintos();
    }
}
