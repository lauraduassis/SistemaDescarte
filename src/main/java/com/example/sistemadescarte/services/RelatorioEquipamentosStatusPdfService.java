package com.example.sistemadescarte.services;

import com.example.sistemadescarte.entities.ProdutoEntity;
import com.example.sistemadescarte.repositories.ProdutoRepository;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

@Service
public class RelatorioEquipamentosStatusPdfService {
    private static final PDType1Font REGULAR = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final PDType1Font BOLD = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final float ESQUERDA = 40f;
    private static final float DIREITA = PDRectangle.A4.getWidth() - 40f;
    private static final float TOPO = PDRectangle.A4.getHeight() - 52f;
    private final ProdutoRepository repository;

    public RelatorioEquipamentosStatusPdfService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public byte[] gerar(String statusRecebido) {
        if (statusRecebido == null || !statusRecebido.trim().matches("[A-Za-z_]{2,60}")) {
            throw new IllegalArgumentException("Informe um status valido na URL.");
        }
        String status = statusRecebido.trim().toUpperCase(Locale.ROOT);
        List<ProdutoEntity> produtos = new ArrayList<>();
        repository.findAll().forEach(produto -> {
            if (produto.getStatus() != null && produto.getStatus().equalsIgnoreCase(status)) produtos.add(produto);
        });
        produtos.sort(Comparator.comparing(p -> nulo(p.getPatrimonio()), String.CASE_INSENSITIVE_ORDER));
        try (PDDocument doc = new PDDocument(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage page = novaPagina(doc);
            PDPageContentStream cs = new PDPageContentStream(doc, page);
            float y = cabecalho(cs, status, produtos.size());
            y = cabecalhoTabela(cs, y);
            for (ProdutoEntity p : produtos) {
                String patrimonio = nulo(p.getPatrimonio());
                String descricao = nulo(p.getDescricao());
                String cadastro = p.getDataCadastro() == null ? "-" : p.getDataCadastro().format(DATA);
                List<String> descricaoLinhas = quebrar(descricao, 250f, REGULAR, 9f);
                float altura = Math.max(28f, descricaoLinhas.size() * 13f + 14f);
                if (y - altura < 78f) {
                    cs.close();
                    page = novaPagina(doc);
                    cs = new PDPageContentStream(doc, page);
                    y = cabecalho(cs, status, produtos.size());
                    y = cabecalhoTabela(cs, y);
                }
                linhaRetangulo(cs, y - altura, altura);
                texto(cs, patrimonio, ESQUERDA + 8f, y - 17f, 9f, REGULAR);
                float yd = y - 17f;
                for (String d : descricaoLinhas) { texto(cs, d, ESQUERDA + 113f, yd, 9f, REGULAR); yd -= 13f; }
                texto(cs, cadastro, DIREITA - 125f, y - 17f, 8f, REGULAR);
                y -= altura;
            }
            if (produtos.isEmpty()) {
                texto(cs, "Nenhum equipamento encontrado para este status.", ESQUERDA + 8f, y - 20f, 10f, REGULAR);
            }
            cs.close();
            for (int i = 0; i < doc.getNumberOfPages(); i++) {
                try (PDPageContentStream footer = new PDPageContentStream(doc, doc.getPage(i), PDPageContentStream.AppendMode.APPEND, true, true)) {
                    footer.setStrokingColor(0.75f, 0.77f, 0.81f);
                    footer.moveTo(ESQUERDA, 59f); footer.lineTo(DIREITA, 59f); footer.stroke();
                    texto(footer, "Emitido em: " + LocalDateTime.now().format(DATA), ESQUERDA, 43f, 8f, REGULAR);
                    String numero = "Pagina " + (i + 1) + " de " + doc.getNumberOfPages();
                    texto(footer, numero, DIREITA - 67f, 43f, 8f, REGULAR);
                }
            }
            doc.save(out);
            return out.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Nao foi possivel gerar o PDF de equipamentos por status.", e);
        }
    }

    private PDPage novaPagina(PDDocument doc) {
        PDPage page = new PDPage(PDRectangle.A4);
        doc.addPage(page);
        return page;
    }

    private float cabecalho(PDPageContentStream cs, String status, int total) throws IOException {
        texto(cs, "SISTEMA DE DESCARTE", ESQUERDA, TOPO, 15f, BOLD);
        texto(cs, "RELATORIO DE EQUIPAMENTOS POR STATUS", ESQUERDA, TOPO - 23f, 10f, BOLD);
        cs.setStrokingColor(0.43f, 0.46f, 0.51f);
        cs.moveTo(ESQUERDA, TOPO - 34f); cs.lineTo(DIREITA, TOPO - 34f); cs.stroke();
        texto(cs, "Status: " + status.replace('_', ' '), ESQUERDA, TOPO - 56f, 10f, BOLD);
        texto(cs, "Total de equipamentos: " + total, ESQUERDA, TOPO - 73f, 9f, REGULAR);
        return TOPO - 91f;
    }

    private float cabecalhoTabela(PDPageContentStream cs, float y) throws IOException {
        cs.setNonStrokingColor(0.91f, 0.93f, 0.95f);
        cs.addRect(ESQUERDA, y - 28f, DIREITA - ESQUERDA, 28f); cs.fill();
        cs.setStrokingColor(0.72f, 0.75f, 0.8f);
        cs.addRect(ESQUERDA, y - 28f, DIREITA - ESQUERDA, 28f); cs.stroke();
        texto(cs, "Patrimonio", ESQUERDA + 8f, y - 18f, 9f, BOLD);
        texto(cs, "Equipamento", ESQUERDA + 113f, y - 18f, 9f, BOLD);
        texto(cs, "Data de cadastro", DIREITA - 125f, y - 18f, 9f, BOLD);
        return y - 28f;
    }

    private void linhaRetangulo(PDPageContentStream cs, float fundo, float altura) throws IOException {
        cs.setStrokingColor(0.75f, 0.78f, 0.83f);
        cs.addRect(ESQUERDA, fundo, DIREITA - ESQUERDA, altura); cs.stroke();
    }

    private List<String> quebrar(String valor, float largura, PDType1Font font, float tamanho) throws IOException {
        String normalizado = limpar(valor);
        if (normalizado.isBlank()) return List.of("-");
        List<String> linhas = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        for (String palavra : normalizado.split("\\s+")) {
            if (font.getStringWidth(palavra) * tamanho / 1000f > largura) {
                for (char letra : palavra.toCharArray()) {
                    String candidato = atual.toString() + letra;
                    if (!atual.isEmpty() && font.getStringWidth(candidato) * tamanho / 1000f > largura) {
                        linhas.add(atual.toString()); atual.setLength(0);
                    }
                    atual.append(letra);
                }
                continue;
            }
            String candidato = atual.isEmpty() ? palavra : atual + " " + palavra;
            if (!atual.isEmpty() && font.getStringWidth(candidato) * tamanho / 1000f > largura) {
                linhas.add(atual.toString()); atual.setLength(0);
            }
            if (!atual.isEmpty()) atual.append(' ');
            atual.append(palavra);
        }
        if (!atual.isEmpty()) linhas.add(atual.toString());
        return linhas;
    }

    private String nulo(String valor) { return valor == null || valor.isBlank() ? "-" : valor; }

    private String limpar(String valor) {
        if (valor == null) return "";
        return Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replaceAll("[^\\x20-\\x7E]", " ")
                .replaceAll("\\s+", " ").trim();
    }

    private void texto(PDPageContentStream cs, String str, float x, float y, float tamanho, PDType1Font font) throws IOException {
        cs.setNonStrokingColor(20f/255f, 28f/255f, 40f/255f);
        cs.beginText(); cs.setFont(font, tamanho); cs.newLineAtOffset(x, y); cs.showText(limpar(str)); cs.endText();
    }
}