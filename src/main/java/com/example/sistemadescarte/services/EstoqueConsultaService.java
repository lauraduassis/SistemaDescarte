package com.example.sistemadescarte.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class EstoqueConsultaService {

    public record Quantidades(long entradas, long retiradas, long descartes, long saldo) {}

    private final JdbcTemplate jdbcTemplate;

    public EstoqueConsultaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Quantidades consultarTotais() {
        return jdbcTemplate.queryForObject("""
            SELECT COALESCE(SUM(CAST(Total_Entradas AS bigint)), 0),
                   COALESCE(SUM(CAST(Total_Retiradas AS bigint)), 0),
                   COALESCE(SUM(CAST(Total_Descartes AS bigint)), 0),
                   COALESCE(SUM(CAST(Estoque_Atual AS bigint)), 0)
            FROM dbo.vw_EstoqueAtual
            """, (rs, rowNum) -> new Quantidades(
                rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getLong(4)));
    }

    public Map<Integer, Quantidades> consultarPorProduto() {
        return jdbcTemplate.query("""
            SELECT Cod_Produto, Total_Entradas, Total_Retiradas, Total_Descartes, Estoque_Atual
            FROM dbo.vw_EstoqueAtual
            """, rs -> {
                Map<Integer, Quantidades> estoque = new HashMap<>();
                while (rs.next()) {
                    estoque.put(rs.getInt("Cod_Produto"), new Quantidades(
                        rs.getLong("Total_Entradas"), rs.getLong("Total_Retiradas"),
                        rs.getLong("Total_Descartes"), rs.getLong("Estoque_Atual")));
                }
                return estoque;
            });
    }
}
