package com.example.sistemadescarte.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.sistemadescarte.entities.ProdutoEntity;
import com.example.sistemadescarte.repositories.ProdutoRepository;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoEntity incluir(ProdutoEntity produto) {
        validarDados(produto);

        produto.setPatrimonio(produto.getPatrimonio().trim());

        if (produtoRepository.existsByPatrimonio(produto.getPatrimonio())) {
            throw new IllegalArgumentException(
                "Já existe um produto cadastrado com este patrimônio."
            );
        }
        produto.setDescricao(produto.getDescricao().trim());
        produto.setCategoria(limpar(produto.getCategoria()));
        produto.setMarca(limpar(produto.getMarca()));
        produto.setModelo(limpar(produto.getModelo()));
        produto.setObservacoes(limpar(produto.getObservacoes()));
        produto.setStatus("PENDENTE");

        ProdutoEntity produtoSalvo = produtoRepository.save(produto);

        produtoSalvo.setCodigoBarras(
            gerarEan13(produtoSalvo.getId())
        );

        return produtoRepository.save(produtoSalvo);
    }

    @Transactional
    public ProdutoEntity atualizar(int id, ProdutoEntity dados) {
        validarDados(dados);

        ProdutoEntity produto = buscarPorId(id);
        String patrimonio = dados.getPatrimonio().trim();

        if (produtoRepository.existsByPatrimonioAndIdNot(patrimonio, id)) {
            throw new IllegalArgumentException(
                "Já existe outro produto cadastrado com este patrimônio."
            );
        }

        produto.setPatrimonio(patrimonio);
        produto.setDescricao(dados.getDescricao().trim());
        produto.setCategoria(limpar(dados.getCategoria()));
        produto.setMarca(limpar(dados.getMarca()));
        produto.setModelo(limpar(dados.getModelo()));
        produto.setObservacoes(limpar(dados.getObservacoes()));

        return produtoRepository.save(produto);
    }

    public java.util.List<ProdutoEntity> listarParaGestao() {
        return listarTodos().stream()
            .filter(produto -> !"COLETADO".equals(produto.getStatus()))
            .toList();
    }

    public java.util.List<ProdutoEntity> listarTodos() {
        return produtoRepository.findAll();
    }

    public ProdutoEntity buscarPorId(int id) {
        return produtoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Produto não encontrado."
            ));
    }

    private String gerarEan13(int id) {
        String base = String.format("2%011d", id);

        int soma = 0;

        for (int i = 0; i < base.length(); i++) {
            int numero = Character.getNumericValue(base.charAt(i));
            soma += numero * (i % 2 == 0 ? 1 : 3);
        }

        int digitoVerificador = (10 - (soma % 10)) % 10;

        return base + digitoVerificador;
    }

    private void validarDados(ProdutoEntity produto) {
        if (produto.getPatrimonio() == null
                || produto.getPatrimonio().isBlank()) {
            throw new IllegalArgumentException(
                "Informe o número do patrimônio."
            );
        }

        if (produto.getDescricao() == null
                || produto.getDescricao().isBlank()) {
            throw new IllegalArgumentException(
                "Informe a descrição do produto."
            );
        }
    }

    private String limpar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    @Transactional
    public void excluir(int id) {
        ProdutoEntity produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
    public long contarPorStatus(String status) {
        return produtoRepository.countByStatus(status);
    }

    public long contarTodos() {
        return produtoRepository.count();
    }
    public ProdutoEntity buscarPorPatrimonio(String patrimonio) {
        ProdutoEntity produto = produtoRepository.findByPatrimonio(patrimonio);
        if (produto == null) throw new IllegalArgumentException("Produto não encontrado.");
        return produto;
    }

    public ProdutoEntity buscarPorCodigoBarras(String codigoBarras) {
        ProdutoEntity produto = produtoRepository.findByCodigoBarras(codigoBarras);
        if (produto == null) throw new IllegalArgumentException("Produto não encontrado.");
        return produto;
    }



    @Transactional(readOnly = true)
    public java.util.List<String> listarStatusDistintos() {
        return produtoRepository.listarStatusDistintos();
    }

    @Transactional(readOnly = true)
    public java.util.List<ProdutoEntity> listarPorStatus(String status) {
        return produtoRepository.findByStatusOrderByPatrimonioAsc(status);
    }
}

