package com.example.sistemadescarte.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

@Service
public class RelatorioStatusPdfService {
    private static final PDType1Font REGULAR = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final PDType1Font BOLD = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final float MARGEM = 48f;
    private static final float LARGURA = PDRectangle.A4.getWidth() - MARGEM * 2f;
    private static final int POR_PAGINA = 22;

    private final EntityManager entityManager;

    public RelatorioStatusPdfService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public byte[] gerar() {
        List<Object[]> dados = entityManager.createQuery(
            "SELECT p.status, COUNT(p) FROM ProdutoEntity p GROUP BY p.status ORDER BY p.status",
            Object[].class
        ).getResultList();

        List<Linha> linhas = new ArrayList<>();
        long total = 0L;
        for (Object[] item : dados) {
            long quantidade = ((Number) item[1]).longValue();
            total += quantidade;
            String status = item[0] == null ? "NAO_INFORMADO" : String.valueOf(item[0]);
            linhas.add(new Linha(status, quantidade));
        }

        int paginas = Math.max(1, (linhas.size() + POR_PAGINA - 1) / POR_PAGINA);
        try (PDDocument documento = new PDDocument();
             ByteArrayOutputStream saida = new ByteArrayOutputStream()) {
            for (int numeroPagina = 0; numeroPagina < paginas; numeroPagina++) {
                PDPage pagina = new PDPage(PDRectangle.A4);
                documento.addPage(pagina);
                try (PDPageContentStream cs = new PDPageContentStream(documento, pagina)) {
                    float y = pagina.getMediaBox().getHeight() - 57f;
                    texto(cs, "SISTEMA DE DESCARTE", MARGEM, y, 16, BOLD);
                    y -= 26;
                    texto(cs, "RELATORIO DE EQUIPAMENTOS POR STATUS", MARGEM, y, 11, BOLD);
                    y -= 18;
                    linhaHorizontal(cs, y);
                    y -= 27;
                    texto(cs, "Situacao dos equipamentos", MARGEM, y, 11, BOLD);
                    y -= 21;
                    texto(cs, "Total de equipamentos: " + total, MARGEM, y, 10, REGULAR);
                    y -= 31;
                    y = cabecalhoTabela(cs, y);

                    int fim = Math.min((numeroPagina + 1) * POR_PAGINA, linhas.size());
                    for (int i = numeroPagina * POR_PAGINA; i < fim; i++) {
                        Linha item = linhas.get(i);
                        String percentual = total == 0 ? "0,0%" :
                            String.format(Locale.forLanguageTag("pt-BR"), "%.1f%%", item.quantidade * 100.0 / total);
                        y = linhaTabela(cs, y, nomeStatus(item.status),
                            String.valueOf(item.quantidade), percentual, false);
                    }
                    if (numeroPagina == paginas - 1) {
                        y = linhaTabela(cs, y, "TOTAL GERAL", String.valueOf(total),
                            total == 0 ? "0,0%" : "100,0%", true);
                    }
                    linhaHorizontalRodape(cs);
                    texto(cs, "Emitido em: " + LocalDateTime.now().format(DATA), MARGEM, 42, 8, REGULAR);
                    texto(cs, "Pagina " + (numeroPagina + 1) + " de " + paginas,
                        PDRectangle.A4.getWidth() - 105f, 42, 8, REGULAR);
                }
            }
            documento.save(saida);
            return saida.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel gerar o relatorio por status.", e);
        }
    }

    private float cabecalhoTabela(PDPageContentStream cs, float y) throws IOException {
        cs.setNonStrokingColor(231f / 255f, 235f / 255f, 240f / 255f);
        cs.addRect(MARGEM, y - 29, LARGURA, 29);
        cs.fill();
        borda(cs, y - 29, 29);
        texto(cs, "STATUS", MARGEM + 10, y - 19, 9, BOLD);
        texto(cs, "QUANTIDADE", MARGEM + 300, y - 19, 9, BOLD);
        texto(cs, "PERCENTUAL", MARGEM + 415, y - 19, 9, BOLD);
        return y - 29;
    }

    private float linhaTabela(PDPageContentStream cs, float y, String status,
                             String quantidade, String percentual, boolean destaque) throws IOException {
        if (destaque) {
            cs.setNonStrokingColor(241f / 255f, 244f / 255f, 248f / 255f);
            cs.addRect(MARGEM, y - 29, LARGURA, 29);
            cs.fill();
        }
        borda(cs, y - 29, 29);
        PDType1Font fonte = destaque ? BOLD : REGULAR;
        texto(cs, limitar(status, 46), MARGEM + 10, y - 19, 9, fonte);
        texto(cs, quantidade, MARGEM + 300, y - 19, 9, fonte);
        texto(cs, percentual, MARGEM + 415, y - 19, 9, fonte);
        return y - 29;
    }

    private void borda(PDPageContentStream cs, float y, float altura) throws IOException {
        cs.setStrokingColor(190f / 255f, 197f / 255f, 207f / 255f);
        cs.setLineWidth(0.7f);
        cs.addRect(MARGEM, y, LARGURA, altura);
        cs.stroke();
    }

    private void linhaHorizontal(PDPageContentStream cs, float y) throws IOException {
        cs.setStrokingColor(110f / 255f, 118f / 255f, 131f / 255f);
        cs.moveTo(MARGEM, y);
        cs.lineTo(MARGEM + LARGURA, y);
        cs.stroke();
    }

    private void linhaHorizontalRodape(PDPageContentStream cs) throws IOException {
        linhaHorizontal(cs, 54);
    }

    private String nomeStatus(String codigo) {
        if ("PENDENTE".equalsIgnoreCase(codigo)) return "Pendente";
        if ("AGUARDANDO_COLETA".equalsIgnoreCase(codigo)) return "Aguardando coleta";
        if ("COLETADO".equalsIgnoreCase(codigo)) return "Coletado";
        if ("NAO_INFORMADO".equalsIgnoreCase(codigo)) return "Nao informado";
        String valor = codigo.replace('_', ' ').toLowerCase(Locale.forLanguageTag("pt-BR"));
        return valor.isEmpty() ? "Nao informado" :
            Character.toUpperCase(valor.charAt(0)) + valor.substring(1);
    }

    private String limitar(String texto, int limite) {
        return texto.length() <= limite ? texto : texto.substring(0, limite - 3) + "...";
    }

    private String limpar(String valor) {
        if (valor == null) return "";
        return Normalizer.normalize(valor, Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "")
            .replaceAll("[^\\x20-\\x7E]", " ");
    }

    private void texto(PDPageContentStream cs, String valor, float x, float y,
                       float tamanho, PDType1Font fonte) throws IOException {
        cs.setNonStrokingColor(20f / 255f, 28f / 255f, 40f / 255f);
        cs.beginText();
        cs.setFont(fonte, tamanho);
        cs.newLineAtOffset(x, y);
        cs.showText(limpar(valor));
        cs.endText();
    }

    private static final class Linha {
        final String status;
        final long quantidade;
        Linha(String status, long quantidade) {
            this.status = status;
            this.quantidade = quantidade;
        }
    }
}