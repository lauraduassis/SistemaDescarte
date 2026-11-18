package com.example.sistemadescarte.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbEntradas")
@Getter
@Setter
public class EntradaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime dataHora;
    private String observacao;

    public EntradaEntity() {
        this.dataHora = LocalDateTime.now();
        this.observacao = "";
    }

    public EntradaEntity(int id, LocalDateTime dataHora, String observacao) {
        this.id = id;
        this.dataHora = dataHora;
        this.observacao = observacao;
    }
}

