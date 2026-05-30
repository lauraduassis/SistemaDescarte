import java.util.InputMismatchException;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {

        // declaracao de scanner

        Scanner ler = new Scanner(System.in);

        // declaracao de variaveis

        String nome = "";
        int idade = 0;
        int tempo = 0;
        int area = 0;
        int continuar = 1;
        boolean entradaValida = false;

        int pontuacao[] = new int[3];
        String candidatos[] = new String[3];
        int contadorCandidatos = 0;

        // chama a parte de validações
        Validacoes validar = new Validacoes();

        // nome programa

        System.out.println("\n==========================");
        System.out.println("|Pontuação dos Professores|");
        System.out.println("==========================");

        // inicio do loop
        do {

            entradaValida = false;
            do {

                try {
                    System.out.print("\nDigite o nome do professor:");
                    nome = ler.nextLine();

                    if (!nome.matches("[a-zA-Z ]+")) {
                        throw new InputMismatchException();
                    }
                    candidatos[contadorCandidatos] = nome; // para guardar o valor do nome e nome dentro do array
                    entradaValida = true;// se chego até aqui a ntrada é valida. pula o catch

                } catch (InputMismatchException e) {
                    System.out.print("\nErro: Por favor, digite apenas letras.");
                }

            } while (!entradaValida);

            entradaValida = false;
            do {
                try {
                    System.out.print("\nDigite a idade do professor:");
                    idade = ler.nextInt();
                    if (idade < 18) {
                        System.out.println("\nO professor deve ter uma idade mínima de 18 anos.");
                        continue;
                    } else {
                        entradaValida = true;
                    }

                } catch (InputMismatchException e) {
                    System.out.println("\nErro: Entrada inválida.Por favor, digite apenas números.");
                    ler.nextLine();
                }

            } while (!entradaValida);

            entradaValida = false;
            do {
                try {
                    System.out.print("\nDigite o tempo de experiência do professor:");
                    tempo = ler.nextInt();
                    if (tempo > idade) {
                        System.out.println("\nO tempo de Experiência não pode ser maior que a idade. Digite novamente");
                        continue;
                    }
                    entradaValida = true;// se chegou aqui ele pula o catch e while e já vai para o proóximo

                } catch (InputMismatchException e) {
                    System.out.print("\nErro: Entrada inválida. Por favor, digite apenas números.");
                    ler.nextLine();
                }

            } while (!entradaValida);

            entradaValida = false;
            do {
                try {
                    System.out.print("\nDigite a área do professor (1- Exatas, 2-Biológicas, 3-Outras)");
                    area = ler.nextInt();
                    entradaValida = true;

                } catch (InputMismatchException e) {
                    System.out.println("\nErro: Entrada Inválida.Por favor, digite apenas números.");
                    ler.nextLine();
                }

            } while (!entradaValida);
            int pontos = validar.calcularPontuacao(idade, tempo, area);
            pontuacao[contadorCandidatos] = pontos;
            contadorCandidatos++;
            if (contadorCandidatos == 3) {
                System.out.println("\nLimite de profesores cadastrados já foi atingido");
                break;
            }

            System.out.println("\nDeseja cadastrar outro professor? Digite 1 para continuar ou 2 para sair.");
            continuar = ler.nextInt();
            ler.nextLine(); // limpar Buffer

        } while (continuar == 1);
        // final loop

        // impressão de nomes e pontuação
        System.out.println("--------------------------------------");
        System.out.println("\nA pontuação dos professores foram:");

        for (int i = 0; i < contadorCandidatos; i++) {
            System.out.println("--------------------------------------");
            System.out.println("\n\nProfessor:" + candidatos[i]);
            System.out.println("\nPontuação:" + pontuacao[i]);
            System.out.println("=======================================");

        }

        ler.close();
    }
}