package com.example.sistemadescarte.services;

import java.io.ByteArrayOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import com.example.sistemadescarte.entities.ProdutoEntity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;

@Service
public class EtiquetaPdfService {

    private static final float MM = 72f / 25.4f;

    private static final float LARGURA_ETIQUETA = 45 * MM;
    private static final float ALTURA_ETIQUETA = 25 * MM;
    private static final float MARGEM = 5 * MM;

    public byte[] gerarEtiquetaTeste(ProdutoEntity produto) {
        try (PDDocument documento = new PDDocument();
             ByteArrayOutputStream saida = new ByteArrayOutputStream()) {

            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);

            try (PDPageContentStream conteudo =
                    new PDPageContentStream(documento, pagina)) {

                int colunas = 4;
                int linhas = 10;

                float espacamento = 3f * 72f / 25.4f;

                float larguraGrade = colunas * LARGURA_ETIQUETA
                        + (colunas - 1) * espacamento;

                float alturaGrade = linhas * ALTURA_ETIQUETA
                        + (linhas - 1) * espacamento;

                float inicioX = (pagina.getMediaBox().getWidth()
                        - larguraGrade) / 2;

                float inicioY = (pagina.getMediaBox().getHeight()
                        + alturaGrade) / 2
                        - ALTURA_ETIQUETA;

                for (int linha = 0; linha < linhas; linha++) {
                    for (int coluna = 0; coluna < colunas; coluna++) {

                        float x = inicioX
                                + coluna * (LARGURA_ETIQUETA + espacamento);

                        float y = inicioY
                                - linha * (ALTURA_ETIQUETA + espacamento);

                        desenharEtiqueta(conteudo, produto, x, y);
                    }
                }
            }

            documento.save(saida);
            return saida.toByteArray();

        } catch (Exception e) {
            throw new IllegalStateException(
                "Não foi possível gerar a etiqueta do produto.",
                e
            );
        }
    }

    /** Gera uma etiqueta por produto, ate 40 por pagina A4. */
    public byte[] gerarEtiquetasTodos(java.util.List<ProdutoEntity> produtos) {
        if (produtos == null || produtos.isEmpty()) {
            throw new IllegalArgumentException("Nenhum produto cadastrado para imprimir.");
        }

        final int colunas = 4;
        final int linhas = 10;
        final int porPagina = colunas * linhas;
        final float espacamento = 3 * MM;
        final float larguraGrade = colunas * LARGURA_ETIQUETA
                + (colunas - 1) * espacamento;
        final float alturaGrade = linhas * ALTURA_ETIQUETA
                + (linhas - 1) * espacamento;

        try (PDDocument documento = new PDDocument();
             ByteArrayOutputStream saida = new ByteArrayOutputStream()) {

            for (int inicio = 0; inicio < produtos.size(); inicio += porPagina) {
                PDPage pagina = new PDPage(PDRectangle.A4);
                documento.addPage(pagina);

                float inicioX = (pagina.getMediaBox().getWidth() - larguraGrade) / 2;
                float inicioY = (pagina.getMediaBox().getHeight() + alturaGrade) / 2
                        - ALTURA_ETIQUETA;

                try (PDPageContentStream conteudo =
                        new PDPageContentStream(documento, pagina)) {
                    int fim = Math.min(inicio + porPagina, produtos.size());

                    for (int indice = inicio; indice < fim; indice++) {
                        ProdutoEntity produto = produtos.get(indice);
                        int posicao = indice - inicio;
                        int linha = posicao / colunas;
                        int coluna = posicao % colunas;

                        float x = inicioX + coluna * (LARGURA_ETIQUETA + espacamento);
                        float y = inicioY - linha * (ALTURA_ETIQUETA + espacamento);
                        desenharEtiqueta(conteudo, produto, x, y);
                    }
                }
            }

            documento.save(saida);
            return saida.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Nao foi possivel gerar as etiquetas de todos os produtos.", e);
        }
    }
    private void desenharEtiqueta(
            PDPageContentStream conteudo,
            ProdutoEntity produto,
            float x,
            float y) throws Exception {

        conteudo.addRect(
            x,
            y,
            LARGURA_ETIQUETA,
            ALTURA_ETIQUETA
        );
        conteudo.stroke();

        float margemInterna = 3 * MM;

        escreverTexto(
            conteudo,
            limitarTexto(produto.getDescricao(), 27),
            x + margemInterna,
            y + ALTURA_ETIQUETA - (6 * MM),
            8
        );

        desenharCodigoBarras(
            conteudo,
            produto.getCodigoBarras(),
            x + margemInterna,
            y + (7 * MM),
            LARGURA_ETIQUETA - (2 * margemInterna),
            9 * MM
        );

        escreverTexto(
            conteudo,
            produto.getCodigoBarras(),
            x + (12 * MM),
            y + (3 * MM),
            7
        );
    }

    private String limitarTexto(String texto, int limite) {
        if (texto == null) {
            return "";
        }

        if (texto.length() <= limite) {
            return texto;
        }

        return texto.substring(0, limite - 3) + "...";
    }

    private void escreverTexto(
            PDPageContentStream conteudo,
            String texto,
            float x,
            float y,
            float tamanho) throws Exception {

        conteudo.beginText();
        conteudo.setFont(
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),
            tamanho
        );
        conteudo.newLineAtOffset(x, y);
        conteudo.showText(texto == null ? "" : texto);
        conteudo.endText();
    }

    private void desenharCodigoBarras(
            PDPageContentStream conteudo,
            String codigo,
            float x,
            float y,
            float largura,
            float altura) throws Exception {

        if (codigo == null || codigo.isBlank()) {
            return;
        }

        BitMatrix matriz = codificarBarras(codigo);

        float larguraBarra = largura / matriz.getWidth();

        for (int coluna = 0; coluna < matriz.getWidth(); coluna++) {
            if (matriz.get(coluna, 0)) {
                conteudo.addRect(
                    x + (coluna * larguraBarra),
                    y,
                    larguraBarra,
                    altura
                );
            }
        }

        conteudo.fill();
    }

    private BitMatrix codificarBarras(String codigo) {
        if (codigo.matches("[0-9]{13}")) {
            try {
                return new EAN13Writer().encode(codigo, BarcodeFormat.EAN_13, 95, 1);
            } catch (IllegalArgumentException codigoEanInvalido) {
                // Code 128 conserva o valor original se o dígito verificador for inválido.
            }
        }

        return new Code128Writer().encode(codigo, BarcodeFormat.CODE_128, 95, 1);
    }
}


