package entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteEntity extends PessoaEntity implements impressao {
    private String email;
    private String cpf;
    private List<TreinoEntity> treinos;

    public ClienteEntity() {
        treinos = new ArrayList<>();
    }

    public ClienteEntity(int id, String nome, String telefone, String email, String cpf) {
        super(id, nome, telefone);
        this.email = email;
        this.cpf = cpf;
        this.treinos = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void adicionarTreino(TreinoEntity treino) {
        treinos.add(treino);
    }


    @Override
    public void imprimirTreino(LocalDate data) {
        System.out.print(CABECALHO);
        for (TreinoEntity treino : treinos) {
           System.out.println("Treino do dia: " + treino.getDataTreino());
            for (ExerciciosEntity exercicio : treino.getExercicios()) {
                System.out.println(" - " + exercicio.getNome() + " - " + exercicio.getSeries() + " - " + exercicio.getRepeticoes());
            }
        }
    }
    @Override
    public void imprimirTreino(LocalDate data) {
        System.out.print(CABECALHO);
        for (TreinoEntity treino : treinos) {
            if (treino.getDataTreino().equals(data)) {
                System.out.println("Treino do dia: " + treino.getDataTreino());
                for (ExerciciosEntity exercicio : treino.getExercicios()) {
                    System.out.println(" - " + exercicio.getNome() + " - " + exercicio.getSeries() + " - " + exercicio.getRepeticoes());
                }
            }
        }
    }
}@Override
public void imprimirTreino() {
    imprimirTreino(null);
}

@Override
public void imprimirTreino(LocalDate data) {
    System.out.print(CABECALHO);

    for (TreinoEntity treino : treinos) {
        if (data != null && !treino.getDataTreino().equals(data)) {
            continue;
        }

        System.out.println("Treino do dia: " + treino.getDataTreino());

        for (ExerciciosEntity exercicio : treino.getExercicios()) {
            System.out.println(" - " + exercicio.getNome() + " - " + exercicio.getDescricao());
        }
    }
}