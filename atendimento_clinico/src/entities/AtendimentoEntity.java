package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AtendimentoEntity {
    private int id;
    private LocalDateTime dataAbertura;
    private String dataAlta;
    private String convenio;
    private String observacaoEntrada;
    private String observacaoAlta; // como se fosse um feedback
    // estamos criando um relacionamento n para n entre as entidades,
    private ArrayList<MedicoAntendimento> atendimentosMedicos; // Lista de médicos no atendimento
    private ArrayList<ProcedimentoAtendimento> procedimentos; // Lista de procedimentos durante o atendimento
    private ArrayList<ExameAtendimento> exames;

    // Construtor, getters e setters
    public AtendimentoEntity() {
        id = 0;
        dataAbertura = LocalDateTime.now();
        dataAlta = new String();
        convenio = new String();
        observacaoEntrada = new String();
        observacaoAlta = new String();
        atendimentosMedicos = new ArrayList<>();
        procedimentos = new ArrayList<>();
        exames = new ArrayList<>();
        paciente = new PacienteEntity();
        atendente = new FuncionarioEntity();
    }

    private PacienteEntity paciente;
    private FuncionarioEntity atendente;

    public AtendimentoEntity(int id, LocalDateTime dataAbertura, String dataAlta, String convenio,
            String observacaoEntrada, String observacaoAlta, ArrayList<MedicoAntendimento> atendimentoMedicos,
            ArrayList<ProcedimentoAtendimento> procedimentos, ArrayList<ExameAtendimento> exames,
            FuncionarioEntity atendente, PacienteEntity paciente) {
        this.id = id;
        this.dataAbertura = dataAbertura;
        this.dataAlta = dataAlta;
        this.convenio = convenio;
        this.observacaoEntrada = observacaoEntrada;
        this.observacaoAlta = observacaoAlta;
        this.atendimentosMedicos = atendimentoMedicos;
        this.procedimentos = procedimentos;
        this.exames = exames;
        this.paciente = paciente;
        this.atendente = atendente;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public String getDataAlta() {
        return dataAlta;
    }

    public String getConvenio() {
        return convenio;
    }

    public String getObservacaoEntrada() {
        return observacaoEntrada;
    }

    public String getObservacaoAlta() {
        return observacaoAlta;
    }

    public ArrayList<MedicoAntendimento> getAtendimentoMedicos() {
        return atendimentosMedicos;
    }

    public ArrayList<ProcedimentoAtendimento> getProcedimentos() {
        return procedimentos;
    }

    public ArrayList<ExameAtendimento> getExames() {
        return exames;
    }

    public PacienteEntity getPaciente() {
        return paciente;
    }

    public FuncionarioEntity getAtendente() {
        return atendente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public void setDataAlta(String dataAlta) {
        this.dataAlta = dataAlta;
    }

    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public void setObservacaoEntrada(String observacaoEntrada) {
        this.observacaoEntrada = observacaoEntrada;
    }

    public void setObservacaoAlta(String observacaoAlta) {
        this.observacaoAlta = observacaoAlta;
    }

    public void setAtendimentoMedicos(ArrayList<MedicoAntendimento> atendimentoMedicos) {
        this.atendimentosMedicos = atendimentoMedicos;
    }

    public void setProcedimentos(ArrayList<ProcedimentoAtendimento> procedimentos) {
        this.procedimentos = procedimentos;
    }

    public void setExames(ArrayList<ExameAtendimento> exames) {
        this.exames = exames;
    }

    public void setPaciente(PacienteEntity paciente) {
        this.paciente = paciente;
    }

    public void setAtendente(FuncionarioEntity atendente) {
        this.atendente = atendente;
    }

    public void adicionarExame(ExameAtendimento exame) {
        exames.add(exame);
    }

    public void removerExame(ExameAtendimento exame) {
        exames.remove(exame);
    }

    public void adicionarProcedimento(ProcedimentoAtendimento procedimento) {
        procedimentos.add(procedimento);

    }

    public void removerProcedimento(ProcedimentoAtendimento procedimento) {
        procedimentos.remove(procedimento);
    }

    public void adicionarMedico(MedicoAntendimento medico) {
        atendimentosMedicos.add(medico);
    }

    public void removerMedico(MedicoAntendimento medico) {
        atendimentosMedicos.remove(medico);
    }

}