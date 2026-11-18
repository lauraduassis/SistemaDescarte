package com.example.sistemadescarte.services;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class EntradaEstoqueService {

    private static final String ORIGEM_CADASTRO = "Entrada pelo cadastro de produto";

    private final JdbcTemplate jdbc;

    public EntradaEstoqueService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public BigDecimal totalEntradas(int produtoId) {
        return jdbc.queryForObject("""
            SELECT COALESCE(SUM(CAST(quantidade AS decimal(38, 8))), 0)
            FROM dbo.tb_entradas_produtos
            WHERE produto_id = ?
            """, BigDecimal.class, produtoId).stripTrailingZeros();
    }

    public void registrar(int produtoId, int quantidade) {
        int entradaId = jdbc.queryForObject("""
            INSERT INTO dbo.tb_entradas (data_hora, observacao)
            OUTPUT INSERTED.id
            VALUES (SYSDATETIME(), ?)
            """, Integer.class, ORIGEM_CADASTRO);
        jdbc.update("""
            INSERT INTO dbo.tb_entradas_produtos (entrada_id, produto_id, quantidade)
            VALUES (?, ?, ?)
            """, entradaId, produtoId, quantidade);
    }

    public void ajustar(int produtoId, int quantidade) {
        BigDecimal diferenca = BigDecimal.valueOf(quantidade).subtract(totalEntradas(produtoId));
        if (diferenca.signum() == 0) {
            return;
        }

        int alteradas = jdbc.update("""
            WITH ultima_entrada AS (
                SELECT TOP (1) quantidade
                FROM dbo.tb_entradas_produtos WITH (UPDLOCK, HOLDLOCK)
                WHERE produto_id = ?
                ORDER BY entrada_id DESC
            )
            UPDATE ultima_entrada SET quantidade = quantidade + ?
            WHERE quantidade + ? > 0
            """, produtoId, diferenca, diferenca);

        if (alteradas == 0) {
            if (diferenca.compareTo(BigDecimal.valueOf(quantidade)) != 0) {
                throw new IllegalArgumentException(
                    "A quantidade informada é menor que o total das entradas anteriores.");
            }
            registrar(produtoId, quantidade);
        }
    }

    public void removerEntradaDoCadastro(int produtoId) {
        jdbc.update("""
            DECLARE @entradas TABLE (id int);
            DELETE item
                OUTPUT DELETED.entrada_id INTO @entradas
            FROM dbo.tb_entradas_produtos item
            JOIN dbo.tb_entradas entrada ON entrada.id = item.entrada_id
            WHERE item.produto_id = ? AND entrada.observacao = ?;
            DELETE entrada
            FROM dbo.tb_entradas entrada
            JOIN @entradas removidas ON removidas.id = entrada.id
            WHERE NOT EXISTS (
                SELECT 1 FROM dbo.tb_entradas_produtos item WHERE item.entrada_id = entrada.id
            );
            """, produtoId, ORIGEM_CADASTRO);
    }
}
