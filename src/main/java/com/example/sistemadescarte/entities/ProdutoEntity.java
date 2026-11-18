package com.example.sistemadescarte.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbProdutos")
@Getter
@Setter
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String codigoBarras;
    @Column(unique = true)
    private String patrimonio;

    private String descricao;
    private String categoria;
    private String marca;
    private String modelo;
    private String status;
    private String observacoes;

    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    @PrePersist
    public void aoCadastrar() {
        LocalDateTime agora = LocalDateTime.now();
        dataCadastro = agora;
        dataAtualizacao = agora;
    }

    @PreUpdate
    public void aoAtualizar() {
        dataAtualizacao = LocalDateTime.now();
    }

    public ProdutoEntity() {
    }
}





