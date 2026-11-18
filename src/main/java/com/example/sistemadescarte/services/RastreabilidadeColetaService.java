package com.example.sistemadescarte.services;

import com.example.sistemadescarte.entities.EmpresaColetoraEntity;
import com.example.sistemadescarte.entities.ProdutoEntity;
import com.example.sistemadescarte.entities.RetiradaEntity;
import com.example.sistemadescarte.entities.RetiradaProduto;
import com.example.sistemadescarte.repositories.EmpresaColetoraRepository;
import com.example.sistemadescarte.repositories.RetiradaProdutoRepository;
import com.example.sistemadescarte.repositories.RetiradaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RastreabilidadeColetaService {
    private final EntityManager entityManager;
    private final EmpresaColetoraRepository empresas;
    private final RetiradaRepository retiradas;
    private final RetiradaProdutoRepository itens;

    public RastreabilidadeColetaService(EntityManager entityManager,
                                      EmpresaColetoraRepository empresas,
                                      RetiradaRepository retiradas,
                                      RetiradaProdutoRepository itens) {
        this.entityManager = entityManager;
        this.empresas = empresas;
        this.retiradas = retiradas;
        this.itens = itens;
    }

    @Transactional(readOnly = true)
    public List<EmpresaOpcao> empresasAtivas() {
        return empresas.findAll().stream()
            .filter(EmpresaColetoraEntity::isAtivo)
            .sorted(java.util.Comparator.comparing(EmpresaColetoraEntity::getNomeFantasia,
                String.CASE_INSENSITIVE_ORDER))
            .map(e -> new EmpresaOpcao(e.getId(), e.getNomeFantasia()))
            .toList();
    }

    @Transactional
    public ColetaConfirmada confirmar(Integer produtoId, Integer empresaColetoraId) {
        if (produtoId == null || empresaColetoraId == null)
            throw new IllegalArgumentException("Selecione o produto e a empresa coletora.");

        ProdutoEntity produto = entityManager.find(ProdutoEntity.class, produtoId, LockModeType.PESSIMISTIC_WRITE);
        if (produto == null) throw new IllegalArgumentException("Produto nao encontrado.");
        if (!"AGUARDANDO_COLETA".equals(produto.getStatus()))
            throw new IllegalArgumentException("O produto deve estar aguardando coleta e nao pode ter sido coletado anteriormente.");

        EmpresaColetoraEntity empresa = empresas.findById(empresaColetoraId)
            .orElseThrow(() -> new IllegalArgumentException("Empresa coletora nao encontrada."));
        if (!empresa.isAtivo()) throw new IllegalArgumentException("Selecione uma empresa coletora ativa.");

        RetiradaEntity retirada = new RetiradaEntity();
        retirada.setDataHora(LocalDateTime.now());
        retirada.setObservacao("");
        retirada.setEmpresaColetora(empresa);
        retirada = retiradas.saveAndFlush(retirada);

        RetiradaProduto item = new RetiradaProduto();
        item.setRetirada(retirada);
        item.setProduto(produto);
        item.setQuantidade(1.0);
        itens.save(item);

        produto.setStatus("COLETADO");
        entityManager.flush();

        return new ColetaConfirmada(retirada.getId(), produto.getId(), produto.getPatrimonio(),
            "COLETADO", empresa.getId(), empresa.getNomeFantasia(), retirada.getDataHora());
    }

    @Transactional(readOnly = true)
    public List<RegistroColeta> consultarPatrimonio(String patrimonio) {
        if (patrimonio == null || patrimonio.isBlank())
            throw new IllegalArgumentException("Informe o patrimonio para consultar.");

        List<RetiradaProduto> registros = entityManager.createQuery(
            "select item from RetiradaProduto item " +
            "join fetch item.produto produto " +
            "join fetch item.retirada retirada " +
            "join fetch retirada.empresaColetora empresa " +
            "where produto.patrimonio = :patrimonio order by retirada.dataHora desc", RetiradaProduto.class)
            .setParameter("patrimonio", patrimonio.trim())
            .getResultList();

        return registros.stream().map(item -> {
            RetiradaEntity retirada = item.getRetirada();
            EmpresaColetoraEntity empresa = retirada.getEmpresaColetora();
            ProdutoEntity produto = item.getProduto();
            return new RegistroColeta(retirada.getId(), produto.getId(), produto.getPatrimonio(),
                produto.getDescricao(), retirada.getDataHora(), empresa.getId(),
                empresa.getNomeFantasia(), empresa.getResponsavelColeta(),
                empresa.getTelefone(), empresa.getEmail());
        }).toList();
    }

    @Transactional(readOnly = true)
    public Map<Integer, ResponsavelColeta> responsaveisPorProduto() {
        List<RetiradaProduto> registros = entityManager.createQuery("""
            select item from RetiradaProduto item
            join fetch item.produto produto
            join fetch item.retirada retirada
            join fetch retirada.empresaColetora empresa
            where produto.status = 'COLETADO'
            order by retirada.dataHora desc, retirada.id desc
            """, RetiradaProduto.class).getResultList();

        Map<Integer, ResponsavelColeta> responsaveis = new HashMap<>();
        for (RetiradaProduto item : registros) {
            EmpresaColetoraEntity empresa = item.getRetirada().getEmpresaColetora();
            responsaveis.putIfAbsent(item.getProduto().getId(),
                new ResponsavelColeta(empresa.getResponsavelColeta(), empresa.getNomeFantasia()));
        }
        return responsaveis;
    }

    public record ResponsavelColeta(String nome, String empresa) { }

    public record EmpresaOpcao(Integer id, String nomeFantasia) { }
    public record DadosConfirmacao(Integer produtoId, Integer empresaColetoraId) { }
    public record ColetaConfirmada(Integer retiradaId, Integer produtoId, String patrimonio,
                                   String status, Integer empresaColetoraId,
                                   String empresaColetora, LocalDateTime dataHora) { }
    public record RegistroColeta(Integer retiradaId, Integer produtoId, String patrimonio,
                                String descricao, LocalDateTime dataHora, Integer empresaColetoraId,
                                String empresaColetora, String responsavelColeta,
                                String telefone, String email) { }
}