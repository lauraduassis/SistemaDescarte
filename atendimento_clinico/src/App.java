import entities.AdiminEntity;
import entities.AtendimentoEntity;
import entities.FuncionarioEntity;
import entities.MedicoAntendimento;
import entities.MedicoEntity;
import entities.PacienteEntity;
import entities.ProcedimentoAtendimento;
import entities.ProcedimentosEntity;
import entities.StatusEntity;
import java.time.LocalDateTime;

public class App {
  public static void main(String[] args) throws Exception {
    StatusEntity status = new StatusEntity(1, "Ativo");
    AdiminEntity adiministrador = new AdiminEntity("123", "Administrador", "321", status, LocalDateTime.now());
    FuncionarioEntity funcionario = new FuncionarioEntity("456", "Atendente1", "At1", status, "Atendimento");
    MedicoEntity medico1 = new MedicoEntity("789", "Medico1", "Med1", status, "Cardiologista");
    MedicoEntity medico2 = new MedicoEntity("798", "Medico2", "Med2", status, "Cardiologista");
    PacienteEntity paciente1 = new PacienteEntity("cpf1", "Paciente1", "Pac1", status, "Plano1");
    PacienteEntity paciente2 = new PacienteEntity("cpf2", "Paciente2", "Pac2", status, "Plano2");

    AtendimentoEntity atendimento = new AtendimentoEntity();
    atendimento.setId(1);
    atendimento.setAtendente(funcionario);
    atendimento.setDataAbertura(LocalDateTime.now());
    atendimento.setConvenio("Unimed");
    atendimento.setObservacaoEntrada("Paciente com dor de cabeça");
    atendimento.setPaciente(paciente1);

    MedicoAntendimento MA = new MedicoAntendimento();
    MA.setAtendimento(atendimento);
    MA.setMedico(medico1);
    MA.setObservacao(
        "Paciente reclama de dor de cabeça aguda há 5 dias, tomou vários medicamentos, vamos fazer exames");

    atendimento.adicionarMedico(MA);
    ProcedimentoAtendimento PA = new ProcedimentoAtendimento();
    PA.setProcedimento(
        new ProcedimentosEntity(1, "Raio x do crânio", "Raio x para verificar possíveis lesões no crânio"));
    PA.setResultado("Sem informação disponível ");
    PA.setAtendimento(atendimento);
    atendimento.adicionarProcedimento(PA);
    PA = new ProcedimentoAtendimento();
    PA.setProcedimento(new ProcedimentosEntity(2, "Raio x do crânio", "Raio x para verificar possíveis lesões no crânio"));
    PA.setResultado("Executada ");
    atendimento.adicionarProcedimento(PA);
    PA.setAtendimento(atendimento);

    // tarefa exibir os dados, naoprecisa de menu, mostarr atendiemntos medicos
    // cadastados, e atendimentos também

  }
}
// continuar a fazer 4

/*
 * Explicacao sobre o final em um (atributo) vira uma constante,
 * se coloco o final no (metodo) nao pode ser sobrescrito(metodo),
 * e se coloco o final na (classe) nao pode ser herdada
 * 
 * Explicacao sobre o abstract - nao posso instanciar (classe),
 * objetos(new)
 * 
 * Quando eu crio um metodo eu obrigo que as subclasses implementem um
 * metodo(sobrescrita)
 * 
 * 
 */