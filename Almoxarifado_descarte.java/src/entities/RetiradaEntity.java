package entities;

import java.time.LocalDateTime;

public class RetiradaEntity {

    private int codigo;
    private LocalDateTime dataHora;
    private String observacao;
    private String status;

    public RetiradaEntity() {
        codigo = 0;
        dataHora = LocalDateTime.now();
        observacao = "";
        status = "";
    }

    public RetiradaEntity(int codigo, LocalDateTime dataHora, String observacao, String status) {
        this.codigo = codigo;
        this.dataHora = dataHora;
        this.observacao = observacao;
        this.status = status;
    }

    public int getCodigo() {
        return this.codigo;
    }

    public LocalDateTime getDataHora() {
        return this.dataHora;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public String getStatus() {
        return this.status;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
