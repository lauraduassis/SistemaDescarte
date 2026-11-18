package com.example.sistemadescarte.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbRetiradas")
@Getter
@Setter
public class RetiradaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime dataHora;
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "empresa_coletora_id")
    private EmpresaColetoraEntity empresaColetora;

    public RetiradaEntity() {
        this.dataHora = LocalDateTime.now();
        this.observacao = "";
    }

    public RetiradaEntity(int id, LocalDateTime dataHora, String observacao) {
        this.id = id;
        this.dataHora = dataHora;
        this.observacao = observacao;
    }
}

