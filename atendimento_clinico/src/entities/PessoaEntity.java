package entities;

public abstract class PessoaEntity {
    public abstract String mostrarStatus();
    
    private String cpf;
    private String nome;
    private String senha;
    private StatusEntity status;

    // Inicialização dos atributos
    public PessoaEntity() {
        cpf = new String();
        nome = new String();
        senha = new String();
        status = new StatusEntity();
    }

    // Construtores, getters e setters, colocando o valor para os atributos, para
    // falar que é a mesma coisa, usamos o this.
    public PessoaEntity(String cpf, String nome, String senha, StatusEntity status) {
        this.cpf = cpf;
        this.nome = nome;
        this.senha = senha;
        this.status = status;
    }

    public String getCpf() {
        return cpf;

    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }
    
    //forco a subclasse a sobrescrever
}