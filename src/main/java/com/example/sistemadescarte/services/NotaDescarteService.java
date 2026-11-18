package com.example.sistemadescarte.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sistemadescarte.entities.NotaDescarteEntity;
import com.example.sistemadescarte.entities.NotaDescarteProduto;
import com.example.sistemadescarte.entities.ProdutoEntity;
import com.example.sistemadescarte.repositories.NotaDescarteProdutoRepository;
import com.example.sistemadescarte.repositories.NotaDescarteRepository;

@Service
public class NotaDescarteService {

    private final NotaDescarteRepository notaDescarteRepository;
    private final NotaDescarteProdutoRepository notaDescarteProdutoRepository;
    private final ProdutoService produtoService;

    public NotaDescarteService(
            NotaDescarteRepository notaDescarteRepository,
            NotaDescarteProdutoRepository notaDescarteProdutoRepository,
            ProdutoService produtoService) {
        this.notaDescarteRepository = notaDescarteRepository;
        this.notaDescarteProdutoRepository = notaDescarteProdutoRepository;
        this.produtoService = produtoService;
    }

    @Transactional
    public NotaDescarteEntity salvar(int produtoId, NotaDescarteEntity notaDescarte) {
        if (notaDescarte.getMotivo() == null || notaDescarte.getMotivo().isBlank()) {
            throw new IllegalArgumentException("Informe o motivo do descarte.");
        }

        ProdutoEntity produto = produtoService.buscarPorId(produtoId);

        if (!"PENDENTE".equals(produto.getStatus())) {
            throw new IllegalArgumentException("Este produto já possui uma solicitação de descarte.");
        }

        notaDescarte.setMotivo(notaDescarte.getMotivo().trim());
        NotaDescarteEntity notaSalva = notaDescarteRepository.save(notaDescarte);

        NotaDescarteProduto item = new NotaDescarteProduto();
        item.setNotaDescarte(notaSalva);
        item.setProduto(produto);
        item.setQuantidade(1);

        notaDescarteProdutoRepository.save(item);

        produto.setStatus("AGUARDANDO_COLETA");

        return notaSalva;
    }

    @Transactional(readOnly = true)
    public List<NotaDescarteProduto> listarHistoricoDescarte() {
        return notaDescarteProdutoRepository.buscarHistoricoDescarte();
    }
}


