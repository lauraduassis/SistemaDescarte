package com.example.sistemadescarte.services;

import com.example.sistemadescarte.entities.EmpresaColetoraEntity;
import com.example.sistemadescarte.repositories.EmpresaColetoraRepository;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaColetoraService {
    private final EmpresaColetoraRepository repository;

    public EmpresaColetoraService(EmpresaColetoraRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EmpresaColetoraEntity> listar() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "nomeFantasia"));
    }

    @Transactional
    public EmpresaColetoraEntity criar(DadosEmpresa dados) {
        EmpresaColetoraEntity empresa = new EmpresaColetoraEntity();
        preencher(empresa, dados);
        return repository.save(empresa);
    }

    @Transactional
    public EmpresaColetoraEntity atualizar(Integer id, DadosEmpresa dados) {
        EmpresaColetoraEntity empresa = repository.findById(id)
            .orElseThrow(() -> new EmpresaNaoEncontradaException("Empresa coletora nao encontrada."));
        preencher(empresa, dados);
        return repository.save(empresa);
    }

    private void preencher(EmpresaColetoraEntity empresa, DadosEmpresa dados) {
        if (dados == null) throw new IllegalArgumentException("Informe os dados da empresa.");
        empresa.setNomeFantasia(obrigatorio(dados.nomeFantasia(), "Nome fantasia", 150));
        empresa.setResponsavelColeta(obrigatorio(dados.responsavelColeta(), "Responsavel pela coleta", 150));
        String telefone = obrigatorio(dados.telefone(), "Telefone", 20);
        int digitos = telefone.replaceAll("\\D", "").length();
        if (digitos < 10 || digitos > 11) throw new IllegalArgumentException("Informe um telefone com DDD valido.");
        empresa.setTelefone(telefone);
        String email = obrigatorio(dados.email(), "E-mail", 150).toLowerCase(Locale.ROOT);
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new IllegalArgumentException("Informe um e-mail valido.");
        empresa.setEmail(email);
        empresa.setAtivo(dados.ativo() == null || dados.ativo());
    }

    private String obrigatorio(String valor, String campo, int limite) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("Informe: " + campo + ".");
        String texto = valor.trim();
        if (texto.length() > limite) throw new IllegalArgumentException(campo + " excede " + limite + " caracteres.");
        return texto;
    }

    public record DadosEmpresa(String nomeFantasia, String responsavelColeta,
                                String telefone, String email, Boolean ativo) { }

    public static class EmpresaNaoEncontradaException extends RuntimeException {
        public EmpresaNaoEncontradaException(String mensagem) { super(mensagem); }
    }
}