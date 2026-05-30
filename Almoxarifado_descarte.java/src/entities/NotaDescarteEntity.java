package entities;

import java.time.LocalDateTime;

public class NotaDescarteEntity {
    private int nrPatrimonio;
    private String observacao;
    private LocalDateTime dataHora;

    public NotaDescarteEntity(){
        nrPatrimonio = 0;
        observacao = "";
        dataHora = LocalDateTime.now();
    }

    public NotaDescarteEntity(int nrPatrimonio, String observacao) {
        this.nrPatrimonio = nrPatrimonio;
        this.observacao = observacao;
        this.dataHora = LocalDateTime.now();
    }

    public int getNrPatrimonio() {
        return this.nrPatrimonio;
    }

    public String getObservacao() {
        return this.observacao;
    }

    public LocalDateTime getDataHora() {
        return this.dataHora;
    }

    public void setNrPatrimonio(int nrPatrimonio) {
        this.nrPatrimonio = nrPatrimonio;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
    
    
}
