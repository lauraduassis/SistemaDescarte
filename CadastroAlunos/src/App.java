public class App {
    public static void main(String[] args) throws Exception {
        Aluno objAluno = new Aluno(); //Aluno -= classe, objaluno-= variavel, new Aluno -= instanciação  chama o construtor, que faz a inicialiação das classse tem o mesmo nome da vlasse  uqe faza insstanciação do objeto
        objAluno.mostrarDados();
        Aluno onbjAluno2 = new Aluno(123, "Maria", "ADS", "maria@email.com", "123.456.789-00", "(11)90909-7070");
        onbjAluno2.mostrarDados();
        objAluno.cadastrar();
        objAluno.mostrarDados();

        /*manualmente criar o get e set entrada de dado separada
         ALuno objAluno3 = new Aluno();
        Scanner ler = new Scanner(System.in);
        do{
            sout("Digite o nome do aluno:");
            objAluno3.setNome(ler.nextLine());
        } while(objAluno3.getNome().isEmpty() || objAluno3.getNome().length() < 10)*/

    }
}
