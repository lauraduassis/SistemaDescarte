package entities;

public abstract class PessoaEntity {

    private int id;
    private String nome;
    private String cpf;
    private String email;
    private String fone;

    public PessoaEntity() {
        id = 0;
        nome = "";
        cpf = "";
        email = "";
        fone = "";
    }

    public PessoaEntity(int id, String nome, String cpf,
            String email, String fone) {

        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.fone = fone;
    }

    public abstract String mostrarStatus();

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getFone() {
        return fone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    

}