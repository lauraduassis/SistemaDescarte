package com.example.sistemadescarte.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sistemadescarte.entities.ProdutoEntity;
public interface ProdutoRepository
extends JpaRepository<ProdutoEntity, Integer>
{
    boolean existsByPatrimonio(String patrimonio);

    ProdutoEntity findByPatrimonio(String patrimonio);

    ProdutoEntity findByCodigoBarras(String codigoBarras);

    boolean existsByPatrimonioAndIdNot(String patrimonio, int id);

    long countByStatus(String status);

    @Query("""
        SELECT DISTINCT p.status
        FROM ProdutoEntity p
        WHERE p.status IS NOT NULL AND p.status <> ''
        ORDER BY p.status
    """)
    List<String> listarStatusDistintos();

    List<ProdutoEntity> findByStatusOrderByPatrimonioAsc(String status);
}

