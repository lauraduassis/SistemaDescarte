import java.util.Scanner;

public class Aluno { // isso serve para representar o que cada classe vai ter por ex todo aluno tem que ter esses atributos que foram definidos
    private int RA;
    private String nome;
    private String curso;
    private String email;
    private String cpf;
    private String telefone;
    public Aluno(){ //construtor
         //inicializar os atributos

        RA = 0;
        nome = new String();
        email = new String();
        curso = new String();
        cpf =new String();
        telefone = new String();
    }
    public Aluno(int RA, String nome, String curso, String email, String cpf, String telefone){ //construtor
        //dentro da classe, pesque o atributo RA e atribua o valor do parâmetro RA

        this.RA = RA;
        this.nome = nome;
        this.curso =curso;
        this.email= email;
        this.cpf =cpf;
        this.telefone = telefone;


    }//quem pega o é o get e o set modifica
    

    public int getRA() {
        return RA;
    }
    public String getNome() {
        return nome;
    }
    public String getCurso() {
        return curso;
    }
    public String getEmail() {
        return email;
    }
    public String getCpf() {
        return cpf;
    }
    public String getTelefone() {
        return telefone;
    }
    public void setRA(int RA) {
        this.RA = RA;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setCurso(String curso) {
        this.curso = curso;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public void mostrarDados(){
        System.out.println("RA:"+ RA);
        System.out.println("Nome:"+nome);
        System.out.println("Email:"+email);
        System.out.println("Curso:"+curso);
        System.out.println("CPF:"+cpf);
        System.out.println("Telefone:"+telefone);
    }
    public void cadastrar(){
        Scanner ler = new Scanner(System.in);
        do{
            System.out.println("Digite o RA do aluno:");
            RA = ler.nextInt();
        }while(RA<=0);
        do{
            try{
            System.out.println("Digite o nome do aluno:");
            nome = ler.nextLine();
            }catch (Exception e ){
                System.out.println("Digite novamente");
            }
        }while (nome.isEmpty() || email.length() < 10);
         do{
            System.out.println("Digite o email do aluno:");
            email = ler.nextLine();
        }while (email.isEmpty() || email.length() < 10);
         do{
            System.out.println("Digite o curso do aluno:");
            curso = ler.nextLine();
        }while (curso.isEmpty());
         do{
            System.out.println("Digite o cpf do aluno:");
            cpf = ler.nextLine();
        }while (cpf.isEmpty() || cpf.length() < 15);
         do{
            System.out.println("Digite o telefone do aluno:");
            telefone = ler.nextLine();
        }while (telefone.charAt(5) != '-');





    }
    
}