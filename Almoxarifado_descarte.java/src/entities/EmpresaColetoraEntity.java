package entities;

public class EmpresaColetoraEntity {

    private int codigo;
    private String nome;
    private String cnpj;
    private String email;
    private String fone;

    public EmpresaColetoraEntity(){
        codigo = 0;
        nome = "";
        cnpj = "";
        email = "";
        fone = "";  

    }

    public EmpresaColetoraEntity(int codigo, String nome, String cnpj, String email, String fone) {
        this.codigo = codigo;
        this.nome = nome;
        this.cnpj = cnpj;
        this.email = email;
        this.fone = fone;
    }

    public int getCodigo() {
        return this.codigo;
    }

    public String getNome() {
        return this.nome;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFone() {
        return this.fone;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }
    

    

}