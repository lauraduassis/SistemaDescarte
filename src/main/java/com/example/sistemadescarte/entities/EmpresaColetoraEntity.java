package com.example.sistemadescarte.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_empresas_coletoras", schema = "dbo")
public class EmpresaColetoraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empresaColetoraSeq")
    @SequenceGenerator(name = "empresaColetoraSeq", sequenceName = "seq_empresas_coletoras", schema = "dbo", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nome_fantasia", length = 150)
    private String nomeFantasia;

    @Column(name = "responsavel_coleta", length = 150)
    private String responsavelColeta;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }
    public String getResponsavelColeta() { return responsavelColeta; }
    public void setResponsavelColeta(String responsavelColeta) { this.responsavelColeta = responsavelColeta; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}