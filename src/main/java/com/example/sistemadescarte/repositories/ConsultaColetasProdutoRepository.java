package com.example.sistemadescarte.repositories;

import com.example.sistemadescarte.entities.ProdutoEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConsultaColetasProdutoRepository extends JpaRepository<ProdutoEntity, Integer> {
    @Query("SELECT p FROM ProdutoEntity p WHERE LOWER(p.descricao) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(p.patrimonio) LIKE LOWER(CONCAT('%', :termo, '%')) OR LOWER(p.codigoBarras) LIKE LOWER(CONCAT('%', :termo, '%')) ORDER BY p.descricao ASC, p.patrimonio ASC")
    List<ProdutoEntity> pesquisar(@Param("termo") String termo, Pageable limite);
}