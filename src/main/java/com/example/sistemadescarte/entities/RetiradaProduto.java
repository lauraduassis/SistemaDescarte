package com.example.sistemadescarte.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbRetiradasProdutos")
@Getter
@Setter
public class RetiradaProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "retirada_id")
    private RetiradaEntity retirada;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoEntity produto;

    private double quantidade;

    public RetiradaProduto() {
    }
}

