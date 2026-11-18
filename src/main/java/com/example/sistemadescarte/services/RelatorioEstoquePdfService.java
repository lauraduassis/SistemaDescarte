package com.example.sistemadescarte.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import com.example.sistemadescarte.entities.ProdutoEntity;
import com.example.sistemadescarte.services.EstoqueConsultaService.Quantidades;

@Service
public class RelatorioEstoquePdfService {
    private static final PDType1Font NORMAL = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final PDType1Font NEGRITO = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final String[] CABECALHO = {"Patrimônio", "Produto", "Entradas", "Retiradas", "Saldo"};
    private static final float[] LARGURAS = {95, 210, 70, 70, 70};
    private static final int LINHAS_POR_PAGINA = 31;

    public byte[] gerar(List<ProdutoEntity> produtos, Map<Integer, Quantidades> estoque, Quantidades totais) {
        var linhas = produtos.stream().filter(p -> estoque.containsKey(p.getId())).toList();
        int paginas = Math.max(1, (linhas.size() + LINHAS_POR_PAGINA - 1) / LINHAS_POR_PAGINA);

        try (var documento = new PDDocument(); var saida = new ByteArrayOutputStream()) {
            for (int pagina = 0; pagina < paginas; pagina++) {
                var folha = new PDPage(PDRectangle.A4);
                documento.addPage(folha);
                try (var cs = new PDPageContentStream(documento, folha)) {
                    texto(cs, "SISTEMA DE DESCARTE", 40, 790, 15, NEGRITO);
                    texto(cs, "Movimentação de estoque", 40, 766, 13, NEGRITO);
                    texto(cs, "Entradas: " + totais.entradas() + "    Retiradas: " + totais.retiradas()
                        + "    Descartes: " + totais.descartes() + "    Saldo atual: " + totais.saldo(),
                        40, 738, 10, NORMAL);

                    linha(cs, CABECALHO, 720, true);
                    int inicio = pagina * LINHAS_POR_PAGINA;
                    int fim = Math.min(inicio + LINHAS_POR_PAGINA, linhas.size());
                    for (int i = inicio; i < fim; i++) {
                        var produto = linhas.get(i);
                        var qtd = estoque.get(produto.getId());
                        linha(cs, new String[]{produto.getPatrimonio(), produto.getDescricao(),
                            Long.toString(qtd.entradas()), Long.toString(qtd.retiradas()),
                            Long.toString(qtd.saldo())},
                            700 - 20 * (i - inicio), false);
                    }
                    if (linhas.isEmpty()) {
                        texto(cs, "Nenhum produto encontrado.", 46, 684, 9, NORMAL);
                    }
                    texto(cs, "Página " + (pagina + 1) + " de " + paginas, 40, 45, 8, NORMAL);
                }
            }
            documento.save(saida);
            return saida.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("Não foi possível gerar o relatório de estoque.", ex);
        }
    }

    private void linha(PDPageContentStream cs, String[] valores, float topo, boolean cabecalho)
            throws IOException {
        if (cabecalho) {
            cs.setNonStrokingColor(231f / 255, 235f / 255, 240f / 255);
            cs.addRect(40, topo - 20, 515, 20);
            cs.fill();
        }
        cs.setStrokingColor(205f / 255, 211f / 255, 220f / 255);
        cs.addRect(40, topo - 20, 515, 20);
        cs.stroke();

        float x = 40;
        for (int i = 0; i < valores.length; i++) {
            var fonte = cabecalho ? NEGRITO : NORMAL;
            float tamanho = 8;
            String valor = valores[i] == null ? "-" : valores[i].replaceAll("[\\p{Cntrl}]", " ");
            // Helvetica padrão aceita os acentos do português e o conjunto Latin-1.
            valor = Normalizer.normalize(valor, Normalizer.Form.NFC)
                .replaceAll("[^\\x20-\\x7E\\xA0-\\xFF]", "?");
            float limite = LARGURAS[i] - 12;
            if (fonte.getStringWidth(valor) * tamanho / 1000 > limite) {
                while (fonte.getStringWidth(valor + "...") * tamanho / 1000 > limite) {
                    valor = valor.substring(0, valor.length() - 1);
                }
                valor += "...";
            }
            texto(cs, valor, x + 6, topo - 13, tamanho, fonte);
            x += LARGURAS[i];
        }
    }

    private void texto(PDPageContentStream cs, String valor, float x, float y, float tamanho,
                       PDType1Font fonte) throws IOException {
        cs.setNonStrokingColor(20f / 255, 28f / 255, 40f / 255);
        cs.beginText();
        cs.setFont(fonte, tamanho);
        cs.newLineAtOffset(x, y);
        cs.showText(valor);
        cs.endText();
    }
}
