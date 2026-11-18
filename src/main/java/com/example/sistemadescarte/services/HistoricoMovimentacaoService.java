package com.example.sistemadescarte.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HistoricoMovimentacaoService {

    public record Movimento(String tipo, String rotulo, LocalDateTime dataHora, int produtoId,
                            String descricao, String patrimonio, BigDecimal quantidade,
                            String responsavel, String observacao, Integer descarteId) {}

    private final JdbcTemplate jdbc;
    private final NotaDescarteService descartes;

    public HistoricoMovimentacaoService(JdbcTemplate jdbc, NotaDescarteService descartes) {
        this.jdbc = jdbc;
        this.descartes = descartes;
    }

    @Transactional(readOnly = true)
    public List<Movimento> listar() {
        List<Movimento> movimentos = jdbc.query("""
            SELECT 'ENTRADA' AS tipo, e.data_hora, p.id AS produto_id,
                   p.descricao, p.patrimonio, ep.quantidade,
                   CAST(NULL AS nvarchar(150)) AS responsavel, e.observacao
            FROM dbo.tb_entradas_produtos ep
            JOIN dbo.tb_entradas e ON e.id = ep.entrada_id
            JOIN dbo.tb_produtos p ON p.id = ep.produto_id
            UNION ALL
            SELECT 'RETIRADA', r.data_hora, p.id, p.descricao, p.patrimonio,
                   rp.quantidade, ec.nome_fantasia, r.observacao
            FROM dbo.tb_retiradas_produtos rp
            JOIN dbo.tb_retiradas r ON r.id = rp.retirada_id
            JOIN dbo.tb_produtos p ON p.id = rp.produto_id
            LEFT JOIN dbo.tb_empresas_coletoras ec ON ec.id = r.empresa_coletora_id
            """, (rs, linha) -> {
                String tipo = rs.getString("tipo");
                return new Movimento(tipo, "ENTRADA".equals(tipo) ? "Entrada" : "Retirada",
                    rs.getObject("data_hora", LocalDateTime.class), rs.getInt("produto_id"),
                    rs.getString("descricao"), rs.getString("patrimonio"),
                    rs.getBigDecimal("quantidade"), rs.getString("responsavel"),
                    rs.getString("observacao"), null);
            });

        for (var item : descartes.listarHistoricoDescarte()) {
            var nota = item.getNotaDescarte();
            var produto = item.getProduto();
            movimentos.add(new Movimento("DESCARTE", "Descarte", nota.getDataHora(),
                produto.getId(), produto.getDescricao(), produto.getPatrimonio(),
                BigDecimal.valueOf(item.getQuantidade()), null, nota.getMotivo(), item.getId()));
        }

        movimentos.sort(Comparator.comparing(Movimento::dataHora,
            Comparator.nullsLast(Comparator.reverseOrder())));
        return movimentos;
    }
}
