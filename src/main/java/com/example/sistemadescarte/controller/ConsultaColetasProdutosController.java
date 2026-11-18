package com.example.sistemadescarte.controller;

import com.example.sistemadescarte.entities.ProdutoEntity;
import com.example.sistemadescarte.repositories.ConsultaColetasProdutoRepository;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coletas")
public class ConsultaColetasProdutosController {
    private final ConsultaColetasProdutoRepository produtos;

    public ConsultaColetasProdutosController(ConsultaColetasProdutoRepository produtos) {
        this.produtos = produtos;
    }

    @GetMapping("/produtos-pesquisa")
    public List<OpcaoProduto> pesquisar(@RequestParam String termo) {
        String busca = termo == null ? "" : termo.trim();
        if (busca.length() < 2) return List.of();
        if (busca.length() > 80) busca = busca.substring(0, 80);
        return produtos.pesquisar(busca, PageRequest.of(0, 30)).stream()
            .map(p -> new OpcaoProduto(p.getId(), p.getPatrimonio(),
                    p.getDescricao(), p.getDescricao() + " - Patrimonio " + p.getPatrimonio()))
            .toList();
    }

    public record OpcaoProduto(int id, String patrimonio, String descricao, String rotulo) {}
}