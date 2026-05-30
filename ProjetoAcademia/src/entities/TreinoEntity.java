package entities;
import java.time.LocalDate; 
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TreinoEntity {
    private int id;
    private ClienteEntity cliente;
    private TreinadorEntity treinador;
    private LocalDate dataTreino;
    private LocalDateTime dataHoraFim;
    private List<ExerciciosEntity> exercicios;


    public TreinoEntity(){//method
        id=0;
        cliente=new ClienteEntity();
        treinador=new TreinadorEntity();
        exercicios=new ArrayList<>();
        dataTreino=LocalDate.now();
        dataHoraFim=null;

    }//constructor
    public TreinoEntity(int id, ClienteEntity cliente, TreinadorEntity treinador, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, List<ExerciciosEntity> exercicios) {
        this.id = id;
        this.cliente = cliente;
        this.treinador = treinador;
        this.dataTreino = dataTreino;
        this.dataHoraFim = dataHoraFim;
        this.exercicios = exercicios;
    }
    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public ClienteEntity getCliente() {
        return cliente;
    }
    public void setCliente(ClienteEntity cliente) {
        this.cliente = cliente;
    }
    public TreinadorEntity getTreinador() {
        return treinador;
    }
    public void setTreinador(TreinadorEntity treinador) {
        this.treinador = treinador;
    }
    public LocalDate getDataTreino() {
        return dataTreino;
    }
    public void setDataTreino(LocalDate dataTreino) {
        this.dataTreino = dataTreino;
    }
    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }
    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }
    public List<ExerciciosEntity> getExercicios() {
        return exercicios;
    }
    public void setExercicios(List<ExerciciosEntity> exercicios) {
        this.exercicios = exercicios;
    }
    public void adicionarExercicio(ExerciciosEntity exercicio) {
        this.exercicios.add(exercicio);
    }
    public void removerExercicio(ExerciciosEntity exercicio){
        this.exercicios.remove(exercicio);
    }
   
}