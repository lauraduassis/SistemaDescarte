package com.example.sistemadescarte.services;

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

import com.example.sistemadescarte.entities.ProdutoEntity;
import com.example.sistemadescarte.services.RastreabilidadeColetaService.ResponsavelColeta;

@Service
public class RelatorioVisaoGeralPdfService {
    private static final PDType1Font NORMAL = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
    private static final PDType1Font NEGRITO = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final float ESQUERDA = 40;
    private static final float DIREITA = 555;
    private static final float[] COLUNAS = {65, 136, 82, 71, 47, 114};
    private static final String[] TITULOS = {"Patrimonio", "Produto / categoria", "Cadastro", "Status", "Marca", "Coletado por"};

    private final RastreabilidadeColetaService rastreabilidadeColetaService;

    public RelatorioVisaoGeralPdfService(RastreabilidadeColetaService rastreabilidadeColetaService) {
        this.rastreabilidadeColetaService = rastreabilidadeColetaService;
    }

    public byte[] gerar(List<ProdutoEntity> produtos) {
        var responsaveis = rastreabilidadeColetaService.responsaveisPorProduto();
        List<ProdutoEntity> lista = new ArrayList<>(produtos);
        lista.sort(Comparator.comparing(ProdutoEntity::getId));

        long pendentes = lista.stream().filter(p -> "PENDENTE".equals(p.getStatus())).count();
        long aguardando = lista.stream().filter(p -> "AGUARDANDO_COLETA".equals(p.getStatus())).count();
        long coletados = lista.stream().filter(p -> "COLETADO".equals(p.getStatus())).count();

        try (PDDocument doc = new PDDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PDPage pagina = new PDPage(PDRectangle.A4);
            doc.addPage(pagina);
            PDPageContentStream cs = new PDPageContentStream(doc, pagina);
            float y = cabecalho(cs, lista.size(), pendentes, aguardando, coletados);
            y = cabecalhoTabela(cs, y);

            if (lista.isEmpty()) {
                texto(cs, "Nenhum equipamento cadastrado.", ESQUERDA + 8, y - 20, 10, NORMAL);
            }

            for (ProdutoEntity p : lista) {
                String[] valores = {
                    p.getPatrimonio(),
                    p.getDescricao() + (vazio(p.getCategoria()) ? "" : " / " + p.getCategoria()),
                    p.getDataCadastro() == null ? "-" : p.getDataCadastro().format(DATA),
                    status(p.getStatus()),
                    p.getMarca()
                };

                List<List<String>> celulas = new ArrayList<>();
                int maxLinhas = 1;
                for (int i = 0; i < valores.length; i++) {
                    List<String> linhas = quebrar(valores[i], COLUNAS[i] - 12, 8.5f, 3);
                    celulas.add(linhas);
                    maxLinhas = Math.max(maxLinhas, linhas.size());
                }
                ResponsavelColeta coleta = "COLETADO".equals(p.getStatus()) ? responsaveis.get(p.getId()) : null;
                float larguraColeta = COLUNAS[valores.length] - 12;
                List<String> linhasColeta = new ArrayList<>();
                if (coleta == null) {
                    linhasColeta.add("\u2014");
                } else {
                    linhasColeta.addAll(quebrar(vazio(coleta.nome()) ? "Não informado" : coleta.nome(),
                        larguraColeta, 8.5f, 3));
                    linhasColeta.addAll(quebrar(coleta.empresa(), larguraColeta, 8.5f, 3));
                }
                celulas.add(linhasColeta);
                maxLinhas = Math.max(maxLinhas, linhasColeta.size());
                float altura = Math.max(29, maxLinhas * 11 + 12);
                if (y - altura < 65) {
                    cs.close();
                    pagina = new PDPage(PDRectangle.A4);
                    doc.addPage(pagina);
                    cs = new PDPageContentStream(doc, pagina);
                    y = tituloContinua(cs);
                    y = cabecalhoTabela(cs, y);
                }

                cs.setStrokingColor(205f / 255f, 211f / 255f, 220f / 255f);
                cs.addRect(ESQUERDA, y - altura, DIREITA - ESQUERDA, altura);
                cs.stroke();
                float x = ESQUERDA;
                for (int i = 0; i < COLUNAS.length; i++) {
                    if (i > 0) {
                        cs.moveTo(x, y);
                        cs.lineTo(x, y - altura);
                        cs.stroke();
                    }
                    float ty = y - 14;
                    for (String linha : celulas.get(i)) {
                        texto(cs, linha, x + 6, ty, 8.5f, NORMAL);
                        ty -= 11;
                    }
                    x += COLUNAS[i];
                }
                y -= altura;
            }
            cs.close();

            int paginas = doc.getNumberOfPages();
            for (int i = 0; i < paginas; i++) {
                try (PDPageContentStream rodape = new PDPageContentStream(doc, doc.getPage(i),
                        PDPageContentStream.AppendMode.APPEND, true, true)) {
                    rodape.setStrokingColor(190f / 255f, 197f / 255f, 207f / 255f);
                    rodape.moveTo(ESQUERDA, 52);
                    rodape.lineTo(DIREITA, 52);
                    rodape.stroke();
                    texto(rodape, "Emitido em: " + LocalDateTime.now().format(DATA), ESQUERDA, 37, 8, NORMAL);
                    texto(rodape, "Pagina " + (i + 1) + " de " + paginas, DIREITA - 73, 37, 8, NORMAL);
                }
            }
            doc.save(out);
            return out.toByteArray();
        } catch (Exception ex) {
            throw new IllegalStateException("Nao foi possivel gerar o relatorio da visao geral.", ex);
        }
    }

    private float cabecalho(PDPageContentStream cs, int total, long pend, long agu, long col) throws IOException {
        texto(cs, "SISTEMA DE DESCARTE", ESQUERDA, 785, 16, NEGRITO);
        texto(cs, "RELATORIO - VISAO GERAL DE EQUIPAMENTOS", ESQUERDA, 762, 11, NEGRITO);
        linha(cs, 751);
        texto(cs, "RESUMO DOS REGISTROS", ESQUERDA, 725, 10, NEGRITO);
        String[] rotulos = {"Situacao", "Quantidade", "Percentual"};
        String[][] dados = {
            {"Pendente", String.valueOf(pend), percentual(pend, total)},
            {"Aguardando coleta", String.valueOf(agu), percentual(agu, total)},
            {"Coletado", String.valueOf(col), percentual(col, total)},
            {"Total geral", String.valueOf(total), "100%"}
        };
        float[] larguras = {275, 120, 120};
        float y = 708;
        y = linhaResumo(cs, rotulos, larguras, y, true);
        for (String[] d : dados) y = linhaResumo(cs, d, larguras, y, false);
        texto(cs, "EQUIPAMENTOS REGISTRADOS", ESQUERDA, y - 27, 10, NEGRITO);
        return y - 40;
    }

    private float tituloContinua(PDPageContentStream cs) throws IOException {
        texto(cs, "SISTEMA DE DESCARTE", ESQUERDA, 785, 14, NEGRITO);
        texto(cs, "RELATORIO - VISAO GERAL (CONTINUACAO)", ESQUERDA, 764, 10, NEGRITO);
        linha(cs, 751);
        return 731;
    }

    private float cabecalhoTabela(PDPageContentStream cs, float y) throws IOException {
        cs.setNonStrokingColor(231f / 255f, 235f / 255f, 240f / 255f);
        cs.addRect(ESQUERDA, y - 27, DIREITA - ESQUERDA, 27);
        cs.fill();
        cs.setStrokingColor(170f / 255f, 179f / 255f, 191f / 255f);
        cs.addRect(ESQUERDA, y - 27, DIREITA - ESQUERDA, 27);
        cs.stroke();
        float x = ESQUERDA;
        for (int i = 0; i < COLUNAS.length; i++) {
            if (i > 0) {
                cs.moveTo(x, y);
                cs.lineTo(x, y - 27);
                cs.stroke();
            }
            texto(cs, TITULOS[i], x + 6, y - 17, 8, NEGRITO);
            x += COLUNAS[i];
        }
        return y - 27;
    }

    private float linhaResumo(PDPageContentStream cs, String[] dados, float[] larguras,
                              float y, boolean titulo) throws IOException {
        float altura = 24;
        if (titulo) {
            cs.setNonStrokingColor(231f / 255f, 235f / 255f, 240f / 255f);
            cs.addRect(ESQUERDA, y - altura, DIREITA - ESQUERDA, altura);
            cs.fill();
        }
        cs.setStrokingColor(185f / 255f, 194f / 255f, 205f / 255f);
        cs.addRect(ESQUERDA, y - altura, DIREITA - ESQUERDA, altura);
        cs.stroke();
        float x = ESQUERDA;
        for (int i = 0; i < larguras.length; i++) {
            if (i > 0) {
                cs.moveTo(x, y);
                cs.lineTo(x, y - altura);
                cs.stroke();
            }
            texto(cs, dados[i], x + 8, y - 16, 9, titulo ? NEGRITO : NORMAL);
            x += larguras[i];
        }
        return y - altura;
    }

    private void linha(PDPageContentStream cs, float y) throws IOException {
        cs.setStrokingColor(110f / 255f, 118f / 255f, 131f / 255f);
        cs.moveTo(ESQUERDA, y);
        cs.lineTo(DIREITA, y);
        cs.stroke();
    }

    private String percentual(long valor, int total) {
        return total == 0 ? "0%" : String.format(Locale.ROOT, "%.1f%%", valor * 100.0 / total);
    }

    private String status(String valor) {
        if ("PENDENTE".equals(valor)) return "Pendente";
        if ("AGUARDANDO_COLETA".equals(valor)) return "Aguard. coleta";
        if ("COLETADO".equals(valor)) return "Coletado";
        return vazio(valor) ? "Nao informado" : valor.replace('_', ' ');
    }

    private List<String> quebrar(String valor, float maxWidth, float tamanho, int maxLinhas) throws IOException {
        String str = limpar(valor);
        if (str.isEmpty()) str = "-";
        List<String> linhas = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char letra = str.charAt(i);
            String tentativa = atual.toString() + letra;
            if (NORMAL.getStringWidth(tentativa) / 1000 * tamanho > maxWidth && atual.length() > 0) {
                linhas.add(atual.toString().trim());
                atual.setLength(0);
                if (linhas.size() == maxLinhas) {
                    String ultima = linhas.get(maxLinhas - 1);
                    while (!ultima.isEmpty() && NORMAL.getStringWidth(ultima + "...") / 1000 * tamanho > maxWidth)
                        ultima = ultima.substring(0, ultima.length() - 1);
                    linhas.set(maxLinhas - 1, ultima + "...");
                    return linhas;
                }
            }
            atual.append(letra);
        }
        if (atual.length() > 0) linhas.add(atual.toString().trim());
        return linhas;
    }

    private boolean vazio(String s) { return s == null || s.isBlank(); }

    private String limpar(String valor) {
        if (valor == null) return "";
        return Normalizer.normalize(valor, Normalizer.Form.NFD)
            .replaceAll("\\p{M}+", "")
            .replaceAll("[^\\x20-\\x7E\\u2014]", " ")
            .replaceAll("\\s+", " ").trim();
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
}