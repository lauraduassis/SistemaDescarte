package entities;

public class Admin extends Usuario {
    private String nome;
    private String email;
    private String senha;
    private String departamento;
  

    //construtor
  public Admin() {
    super();
    nome = new String();
    email = new String();
    senha = new String();
    departamento = new String();
    
    }


  public Admin(String nome, String email, String senha, String departamento) {
    this.nome = nome;
    this.email = email;
    this.senha = senha;
    this.departamento = departamento;
  }


  public String getNome() {
    return nome;
  }


  public void setNome(String nome) {
    this.nome = nome;
  }


  public String getEmail() {
    return email;
  }


  public void setEmail(String email) {
    this.email = email;
  }


  public String getSenha() {
    return senha;
  }


  public void setSenha(String senha) {
    this.senha = senha;
  }


  public String getDepartamento() {
    return departamento;
  }


  public void setDepartamento(String departamento) {
    this.departamento = departamento;
  }
    

}

