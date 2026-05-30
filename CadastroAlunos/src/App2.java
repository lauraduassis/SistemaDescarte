import java.util.ArrayList;
import java.util.Scanner;

public class App2 {
public static void main(String[] args) {
    ArrayList<Aluno> listaAlunos = new ArrayList<>(); //tipo do elemento é aluno dentro do arraylist
    Scanner ler = new Scanner(System.in);
    String resposta = new String();
    do{
        Aluno objAluno = new Aluno();
        objAluno.cadastrar();
        listaAlunos.add(objAluno);
        System.out.println("Deseja cadastrar outro aluno? (S/N)");
        resposta = ler.next();
    }while(resposta.equalsIgnoreCase("S"));
    //mostar os dados de todos os alunos cadastrados com for it
    String alunos = "";

        // Ação 3
        // Exibir o conteúdo do array utilizando o comando 'for' (convencional, que utiliza um contador)
        // e os métodos 'getters' previstos na classe Aluno.


   /*   for(int i = 0; i < listaAlunos.size(); i++) {
    System.out.printf("Aluno %d:\n", (i+1));
    System.out.println("- Nome: " + listaAlunos.get(i).getNome());
    System.out.println("- RA: " + listaAlunos.get(i).getRA());
    System.out.println("- Curso: " + listaAlunos.get(i).getCurso());
}*/

//outra forma de fazer 
/*for (Aluno x: listaAlunos){
    x.mostrarDados();
    System.out.println("-------------------------");
}*/

//outra forma
for(int i = 0; i < listaAlunos.size();i++){
    System.out.println(i+1 + "ºaluno");
    listaAlunos.get(i).mostrarDados();
    System.out.println("-------------------------");

}


}
}
