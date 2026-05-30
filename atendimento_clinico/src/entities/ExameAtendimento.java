package entities;
import java.time.LocalDateTime;

public class ExameAtendimento {
private AtendimentoEntity atendimento;
private ExameEntity exame;
private LocalDateTime dataHoraRealizacao;
private String laudo;
private String observacoes;

//inicialização dos atributos
public ExameAtendimento(){
    atendimento = new AtendimentoEntity();
    exame = new ExameEntity();
    dataHoraRealizacao = LocalDateTime.now();
    laudo = new String();
    observacoes = new String();   

}
// Construtor, getters e setters
public ExameAtendimento(AtendimentoEntity atendimento, ExameEntity exame, LocalDateTime dataHoraRealizacao, String laudo, String observacoes) {
    this.atendimento = atendimento;
    this.exame = exame;
    this.dataHoraRealizacao = dataHoraRealizacao;
    this.laudo = laudo;
    this.observacoes = observacoes;
}

//getters
public AtendimentoEntity getAtendimento() {
    return atendimento; 
}
public ExameEntity getExame() {
    return exame;
}
public LocalDateTime getDataHoraRealizacao() {
    return dataHoraRealizacao;
}
public String getLaudo() {
    return laudo;
}
public String getObservacoes() {
    return observacoes;
}

//setters
public void setAtendimento(AtendimentoEntity atendimento) {
    this.atendimento = atendimento;
}
public void setExame(ExameEntity exame) {
    this.exame = exame;
}
public void setDataHoraRealizacao(LocalDateTime dataHoraRealizacao) {
    this.dataHoraRealizacao = dataHoraRealizacao;
}
public void setLaudo(String laudo) {
    this.laudo = laudo;
}
public void setObservacoes(String observacoes) {
    this.observacoes = observacoes;
}    
} 