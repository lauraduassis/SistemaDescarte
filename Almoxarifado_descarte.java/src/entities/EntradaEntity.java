package entities;

import java.time.LocalDateTime;

public class EntradaEntity {
    private int codigo;
    private String status;
    private String obs;
    private LocalDateTime dataHora;

    public EntradaEntity() {
        codigo = 0;
        status = "";
        obs = "";
        dataHora = LocalDateTime.now();
    }

    public EntradaEntity(int codigo, String status, String obs, LocalDateTime dataHora) {
        this.codigo = codigo;
        this.status = status;
        this.obs = obs;
        this.dataHora = dataHora;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
