package com.example.sistemadescarte.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.sistemadescarte.entities.NotaDescarteProduto;

public interface NotaDescarteProdutoRepository extends JpaRepository<NotaDescarteProduto, Integer> {

    java.util.Optional<NotaDescarteProduto> findFirstByProduto_IdOrderByIdDesc(int produtoId);

    @Query("""
        SELECT ndp
        FROM NotaDescarteProduto ndp
        JOIN FETCH ndp.notaDescarte
        JOIN FETCH ndp.produto
        ORDER BY ndp.notaDescarte.dataHora DESC
    """)
    List<NotaDescarteProduto> buscarHistoricoDescarte();
}

