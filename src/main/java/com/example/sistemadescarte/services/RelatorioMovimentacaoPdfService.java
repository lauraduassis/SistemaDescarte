package com.example.sistemadescarte.services;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import com.example.sistemadescarte.entities.NotaDescarteProduto;
import com.example.sistemadescarte.entities.ProdutoEntity;

@Service
public class RelatorioMovimentacaoPdfService {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final PDType1Font FONTE = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final PDType1Font NEGRITO = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final float MARGEM = 48f;
    private static final float TAMANHO_TEXTO = 9.5f;
    private static final float ENTRELINHAS = 13f;
    private static final Color CINZA_CABECALHO = new Color(238, 241, 245);
    private static final Color CINZA_ALTERNADO = new Color(250, 251, 253);
    private static final Color CINZA_BORDA = new Color(190, 195, 202);

    public byte[] gerar(NotaDescarteProduto movimentacao) {
        if (movimentacao == null || movimentacao.getProduto() == null
                || movimentacao.getNotaDescarte() == null) {
            throw new IllegalArgumentException("Dados da movimentacao incompletos.");
        }

        ProdutoEntity produto = movimentacao.getProduto();
        List<String[]> linhas = new ArrayList<>();
        linhas.add(new String[]{"Identificador da movimentacao", String.valueOf(movimentacao.getId())});
        linhas.add(new String[]{"Data e hora do descarte", data(movimentacao.getNotaDescarte().getDataHora())});
        linhas.add(new String[]{"Tipo de movimentacao", "Descarte"});
        linhas.add(new String[]{"Equipamento", produto.getDescricao()});
        linhas.add(new String[]{"Patrimonio", produto.getPatrimonio()});
        linhas.add(new String[]{"Codigo de barras", produto.getCodigoBarras()});
        linhas.add(new String[]{"Quantidade registrada", String.valueOf(movimentacao.getQuantidade())});
        linhas.add(new String[]{"Status atual do produto", produto.getStatus()});
        linhas.add(new String[]{"Motivo do descarte", movimentacao.getNotaDescarte().getMotivo()});

        try (PDDocument documento = new PDDocument();
             ByteArrayOutputStream saida = new ByteArrayOutputStream()) {

            PDPage pagina = new PDPage(PDRectangle.A4);
            documento.addPage(pagina);
            float largura = pagina.getMediaBox().getWidth() - 2 * MARGEM;
            float larguraRotulo = 210f;
            float larguraValor = largura - larguraRotulo;
            int numeroPagina = 1;
            int indice = 0;
            PDPageContentStream cs = new PDPageContentStream(documento, pagina);
            try {
                float y = desenharCabecalho(cs, pagina, movimentacao.getId());
                y = linhaTabela(cs, y, MARGEM, larguraRotulo, larguraValor,
                        "INFORMACAO", "DADOS DO REGISTRO", true, 0);

                for (String[] linha : linhas) {
                    List<String> rotulo = quebrarPorLargura(linha[0], NEGRITO,
                            TAMANHO_TEXTO, larguraRotulo - 20f);
                    List<String> valor = quebrarPorLargura(linha[1], FONTE,
                            TAMANHO_TEXTO, larguraValor - 20f);
                    float altura = Math.max(30f,
                            Math.max(rotulo.size(), valor.size()) * ENTRELINHAS + 16f);
                    if (y - altura < 95f) {
                        desenharRodape(cs, pagina, numeroPagina);
                        cs.close();
                        pagina = new PDPage(PDRectangle.A4);
                        documento.addPage(pagina);
                        numeroPagina++;
                        cs = new PDPageContentStream(documento, pagina);
                        y = desenharCabecalho(cs, pagina, movimentacao.getId());
                        y = linhaTabela(cs, y, MARGEM, larguraRotulo, larguraValor,
                                "INFORMACAO", "DADOS DO REGISTRO", true, 0);
                    }
                    y = linhaTabela(cs, y, MARGEM, larguraRotulo, larguraValor,
                            linha[0], linha[1], false, indice++);
                }
                desenharRodape(cs, pagina, numeroPagina);
            } finally {
                cs.close();
            }

            documento.save(saida);
            return saida.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Nao foi possivel gerar o relatorio PDF.", e);
        }
    }

    private float desenharCabecalho(PDPageContentStream cs, PDPage pagina, int id)
            throws IOException {
        float y = pagina.getMediaBox().getHeight() - 56f;
        centralizar(cs, "SISTEMA DE DESCARTE", y, 16f, NEGRITO,
                pagina.getMediaBox().getWidth());
        y -= 25f;
        centralizar(cs, "RELATORIO INDIVIDUAL DE MOVIMENTACAO - DESCARTE",
                y, 10.5f, NEGRITO, pagina.getMediaBox().getWidth());
        y -= 19f;
        cs.setStrokingColor(new Color(65, 70, 77));
        cs.setLineWidth(0.8f);
        cs.moveTo(MARGEM, y);
        cs.lineTo(pagina.getMediaBox().getWidth() - MARGEM, y);
        cs.stroke();
        y -= 25f;
        texto(cs, "Movimentacao n. " + id, MARGEM, y, 9f, FONTE);
        y -= 19f;
        return y;
    }

    private float linhaTabela(PDPageContentStream cs, float topo, float x,
            float larguraRotulo, float larguraValor, String rotulo, String valor,
            boolean cabecalho, int indice) throws IOException {
        List<String> linhasRotulo = quebrarPorLargura(rotulo, NEGRITO,
                TAMANHO_TEXTO, larguraRotulo - 20f);
        List<String> linhasValor = quebrarPorLargura(valor, cabecalho ? NEGRITO : FONTE,
                TAMANHO_TEXTO, larguraValor - 20f);
        float altura = Math.max(30f,
                Math.max(linhasRotulo.size(), linhasValor.size()) * ENTRELINHAS + 16f);
        float base = topo - altura;

        if (cabecalho || indice % 2 == 1) {
            cs.setNonStrokingColor(cabecalho ? CINZA_CABECALHO : CINZA_ALTERNADO);
            cs.addRect(x, base, larguraRotulo + larguraValor, altura);
            cs.fill();
        }
        cs.setStrokingColor(CINZA_BORDA);
        cs.setLineWidth(0.55f);
        cs.addRect(x, base, larguraRotulo + larguraValor, altura);
        cs.moveTo(x + larguraRotulo, base);
        cs.lineTo(x + larguraRotulo, topo);
        cs.stroke();

        float textoY = topo - 10f - TAMANHO_TEXTO;
        for (String parte : linhasRotulo) {
            texto(cs, parte, x + 10f, textoY, TAMANHO_TEXTO, NEGRITO);
            textoY -= ENTRELINHAS;
        }
        textoY = topo - 10f - TAMANHO_TEXTO;
        for (String parte : linhasValor) {
            texto(cs, parte, x + larguraRotulo + 10f, textoY, TAMANHO_TEXTO,
                    cabecalho ? NEGRITO : FONTE);
            textoY -= ENTRELINHAS;
        }
        return base;
    }

    private List<String> quebrarPorLargura(String original, PDType1Font fonte,
            float tamanho, float larguraMaxima) throws IOException {
        String valor = limpar(original);
        if (valor.isBlank()) valor = "Nao informado";
        List<String> linhas = new ArrayList<>();
        StringBuilder linha = new StringBuilder();
        for (String palavra : valor.split("\\s+")) {
            String candidato = linha.length() == 0 ? palavra : linha + " " + palavra;
            if (fonte.getStringWidth(candidato) * tamanho / 1000f <= larguraMaxima) {
                linha.setLength(0);
                linha.append(candidato);
                continue;
            }
            if (linha.length() > 0) {
                linhas.add(linha.toString());
                linha.setLength(0);
            }
            StringBuilder parte = new StringBuilder();
            for (int i = 0; i < palavra.length(); i++) {
                String proximo = parte.toString() + palavra.charAt(i);
                if (parte.length() > 0 &&
                        fonte.getStringWidth(proximo) * tamanho / 1000f > larguraMaxima) {
                    linhas.add(parte.toString());
                    parte.setLength(0);
                }
                parte.append(palavra.charAt(i));
            }
            linha.append(parte);
        }
        if (linha.length() > 0) linhas.add(linha.toString());
        return linhas;
    }

    private void desenharRodape(PDPageContentStream cs, PDPage pagina, int numero)
            throws IOException {
        float largura = pagina.getMediaBox().getWidth();
        cs.setStrokingColor(CINZA_BORDA);
        cs.moveTo(MARGEM, 78f);
        cs.lineTo(largura - MARGEM, 78f);
        cs.stroke();
        texto(cs, "Emitido em: " + data(LocalDateTime.now()), MARGEM, 62f, 8.5f, FONTE);
        texto(cs, "Documento gerado pelo Sistema de Descarte", MARGEM, 47f, 8f, FONTE);
        String paginaTexto = "Pagina " + numero;
        float textoLargura = FONTE.getStringWidth(paginaTexto) * 8f / 1000f;
        texto(cs, paginaTexto, largura - MARGEM - textoLargura, 47f, 8f, FONTE);
    }

    private void centralizar(PDPageContentStream cs, String valor,
            float y, float tamanho, PDType1Font fonte, float largura) throws IOException {
        String seguro = limpar(valor);
        float textoLargura = fonte.getStringWidth(seguro) * tamanho / 1000f;
        texto(cs, seguro, (largura - textoLargura) / 2f, y, tamanho, fonte);
    }

    private String data(LocalDateTime valor) {
        return valor == null ? "Nao informado" : valor.format(FORMATO);
    }

    private String limpar(String valor) {
        if (valor == null) return "";
        String normalizado = Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "");
        return normalizado.replaceAll("[^\\x20-\\x7E]", " ").replaceAll("\\s+", " ").trim();
    }

    private void texto(PDPageContentStream cs, String valor, float x, float y,
            float tamanho, PDType1Font fonte) throws IOException {
        cs.setNonStrokingColor(Color.BLACK);
        cs.beginText();
        cs.setFont(fonte, tamanho);
        cs.newLineAtOffset(x, y);
        cs.showText(limpar(valor));
        cs.endText();
    }
}
