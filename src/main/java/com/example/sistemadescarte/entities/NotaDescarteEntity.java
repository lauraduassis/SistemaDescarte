package com.example.sistemadescarte.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbNotasDescarte")
@Getter
@Setter
public class NotaDescarteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime dataHora;
    private String motivo;

    @ManyToOne
    @JoinColumn(name = "empresa_coletora_id")
    private EmpresaColetoraEntity empresaColetora;

    public NotaDescarteEntity() {
        this.dataHora = LocalDateTime.now();
    }
}


