/*import java.util.Scanner;

//a classe é uma estrutura e dentro tem atributos e metodos

public class App {// aqui dentro é onde vai oocrrer as ações, onde vai interagir com o usuário
    public static void main(String[] args) throws Exception { //o main é um executor e quando há o main é disponibilizada a função de run e debug
        //todo metodo main tem que ser static, porque ele executa na memoria e sobe        
        //void é um tipo de retorno mas que não retorna nada
         
        String cep = "";
        Scanner ler = new Scanner (System.in);
        System.out.println("Digite o CEP:");
        cep = ler.nextLine();
        if(Validacoes.validarCEP(cep)){
            System.out.println("CEP válido");
        }else {
            System.out.println("CEP inválido");

        }
    }

}*/

/*-----------exercicio validação notas------------ 
import java.util.Scanner;

//classe
public class App{

//metodo
public class Validacoes {// onde vai realizar as validacoes

    public static boolean validarNumero(double numero) {
        
        if (numero < 0) {
            return false;
        } else {
            return true;
        }
    }
    

public static void main(String[]args){

//leitor/teclado
Scanner ler = new Scanner(System.in);

//declaração de variaveis
double nota;

//impressao e leitura do teclado
System.out.println("Digite a nota:");
nota = ler.nextDouble();// colocar no lugar do line colocar double

if(Validacoes.validarNota(nota)){
System.out.println("Nota correta");
}else{
    System.out.println("Nota inválida");
}

}
}
*/

//exercicio ---------------verificação de número positivo--------------
/* 
import java.util.Scanner;

public class App { // declaração da classe App

    public static void main(String[] args) { // declaração do metodo
        Scanner ler = new Scanner(System.in); // declaração do leitor

        // delaração de variaveis

        String entrada;
        double numero = 0;
        int continuar = 1;
        int sair = 2;
        int opcao;

        do {
            // impressao da e leitura do teclado
            System.out.println("--------------------------------------\n");
            System.out.println("Será que esse número é positivo ou não (°<°)?");
            System.out.println("--------------------------------------\n");
            System.out.print("Digite um número:");

            do {
                try { // tenta executar depois que recebe

                    entrada = ler.nextLine(); // lê o que o usuario digi4tou e guardar para depois ser usada na parte
                                              // debaixo onde vai entar converter o textovalor da entrada em
                    numero = Double.parseDouble(entrada); // vai tentar transformar a entrada em numero, o Double é uma
                                                          // classe e dentro tem um metodo e dentro tem o paratmetro
                                                          // argumentos, e isso se rona em conjunto uma classe objeto
                    break; // sai do loop para não fica infinito

                } catch (NumberFormatException erro) { // identifica o erro e guarda na variavel erro e trata o erro
                    System.out.print("***Você não digitou um número***\nDigite novamente o número:");

                }

            } while (true);// se for verdadeiro entra no if

            if (Validacoes.validarNumero(numero)) {
                System.out.println("Número positivo! (^+^)");
            } else {
                System.out.println("Número não positivo (°-°)");
            }

            System.out.println("Quer continuar? Digite 1 para continuar ou 2 para sair:");
            opcao = Integer.parseInt(ler.nextLine());

        } while (opcao == continuar);
        ler.close();

    }
}*/

//validação se é impar ou par 

    import java.util.Scanner;
    
    public class App {
    
        public static void main (String[] args){

            Scanner ler = new Scanner(System.in);

            //declaracao de variaveis

            int numero;
            String entrada;
            int continuar = 1;
            int opcao;

            do{
                //impressao da mensagem

                System.out.println("----------------------------\n");
                System.out.println("Número Impar ou Par ?");
                System.out.println("----------------------------\n");
                System.out.print("Digite o número:");

                do{
                    try{
                        entrada = ler.nextLine();
                        numero = Integer.parseInt(entrada);
                        break;


                    }catch(NumberFormatException erro) { // identifica o erro e guarda na variavel erro e trata o erro
                    System.out.print("***Você não digitou um número***\nDigite novamente o número:");


                    }
                    


                }while(true);
                if (Validacoes.validarNumero(numero)){
                    System.out.println("Número par");
                }else{
                    System.out.println("Numero Ímpar");
                }
                System.out.println("Para continuar digite 1 para sair digite 2");
                opcao = ler.nextInt();
                ler.nextLine();//limpa o enter guardado no buffer

            }while (opcao == continuar);

            ler.close();

        }
    }


    //classe é composta por atributos (características) e métodos (ações / funções)

    //nos métodos tem as variáveis que são declaradas dentro dos métodos

    //constantes não podem sofrer uma nova modificaçã mo decorrer do programa
    /*Ex:
    
    public class Exemplo {
    int contador = 0; --- é um atributo 
    final String CURSO = "Info"; --- é um atributo mas nã pode ser alterada no decorrer do código e o nome tem que ser em maiúscula por ser constante

    Public ststic void mostrar(){
    
        System.out.println(contador + "" + CURSO);
        contador ++;
        CURSO = "Informática"; ---- Isso não pode fazer poruqe a constante não pode ser alterada na outra clsasse porque ela já foi criada e atribuida um "valor" para ela.
    
    
    }
    
    
   void contar () {
    int contador = 0; --variavel
    contador ++; --variavel
    sout(contador);

    }
    void contar (int contador){ --variavel
        coontador ++; -- variavel
        sout(contador);}

    *QUando dentro temalguemcom o mesmo nmome d e for ao de dentro vai ocultar o nome de fora, porque ele sempre pega o mais pròximo

já nesse caso 
public contadorUtilizacao (){
contador ++; --- atributo porque os nomes são diferentes, nao tem ninguem substitindo ou ocultando
}
public contarUtlizacao (int  contador){
contador ++; } -- atributo


}
    */