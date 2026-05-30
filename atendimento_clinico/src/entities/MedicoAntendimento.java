package entities;
import java.time.LocalDateTime;
;
public class MedicoAntendimento {
    private AtendimentoEntity atendimento;//pk
    private MedicoEntity medico;//pk
    private LocalDateTime dataHoraInicio;
    private String observacao;
    private String status; // Exemplo: "Em andamento", "Concluído", etc.
  
  // Construtor, getters e setters
    public MedicoAntendimento() {
        atendimento = new AtendimentoEntity();
        medico = new MedicoEntity();
        dataHoraInicio = LocalDateTime.now();
        observacao = new String();
        status = new String();
    }
    public MedicoAntendimento(MedicoEntity medico, LocalDateTime dataHoraInicio, String observacao, String status) {
        this.atendimento = new AtendimentoEntity(); 
        this.medico = medico;
        this.dataHoraInicio = dataHoraInicio;
        this.observacao = observacao;
        this.status = status;
    }
    public AtendimentoEntity getAtendimento() {
        return atendimento;
    }
    public MedicoEntity getMedico() {
        return medico;
    }
    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }
    public String getObservacao() {
        return observacao;
    }
    public String getStatus() {
        return status;
    }
    public void setAtendimento(AtendimentoEntity atendimento) {
        this.atendimento = atendimento;
    }
    public void setMedico(MedicoEntity medico) {
        this.medico = medico;
    }
    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public void setStatus(String status) {
        this.status = status; 
}
}