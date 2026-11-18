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
@Table(name = "tbNotasDescarteProdutos")
@Getter
@Setter
public class NotaDescarteProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "nota_descarte_id")
    private NotaDescarteEntity notaDescarte;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private ProdutoEntity produto;

    private double quantidade;

    public NotaDescarteProduto() {
    }
}
