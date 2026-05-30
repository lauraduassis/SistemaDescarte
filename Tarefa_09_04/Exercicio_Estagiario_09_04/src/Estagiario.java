public class Estagiario {

    // declaracao Atributos
    private String nome;
    private String email;
    private String curso;
    private double mediaGeral;
    private int idade;
    private String sexo;

    //  definir Metodo Construtor
    public Estagiario(String nome, String email, String curso, 
                      double mediaGeral, int idade, String sexo) {
        this.nome = nome;
        this.email = email;
        this.curso = curso;
        this.mediaGeral = mediaGeral;
        this.idade = idade;
        this.sexo = sexo;
    }

    //definit getters e setters
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

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public double getMediaGeral() {
        return mediaGeral;
    }

    public void setMediaGeral(double mediaGeral) {
        this.mediaGeral = mediaGeral;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
//no estagiario tem que ter o construtor
//pra os sexos no app tem que fazer os cont, e precisa ter somente os get aqui no estagiario
//no final tem que usar o exibir menu


//o erro undefined , a parti do momento que vc conseguiu fazer um construtor com parametros , e precisa usar o sem parametros, ele quer que crie um com parametros tambem  para o que nao tem .. mas da pra forçar nno local a parçar pelo construtor sem fazer isso 