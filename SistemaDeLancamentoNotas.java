import java.util.Scanner;

public class SistemaDeLancamentoNotas {

    public static void main(String[] args) {

        // Declaração de variáveis
        String nome;
        float nota, soma = 0, maiorNota = 0, menorNota = 0, mediaGeral = 0;
        String nomeMaiorNota = "", nomeMenorNota = "", aprovados = "";

        int contNotas = 0, contAbaixoDeSeis = 0, continuar = 0;

        Scanner ler = new Scanner(System.in);

        do {

            System.out.println("Digite o nome do aluno:");
            nome = ler.nextLine();

            System.out.println("Digite a nota:");
            nota = ler.nextFloat();
            ler.nextLine(); // limpar buffer

            // Validação da nota
            if (nota < 0 || nota > 10) {
                System.out.println("Nota inválida!");
            } else {

                soma += nota;
                contNotas++;

                if (contNotas == 1) {
                    maiorNota = nota;
                    menorNota = nota;
                    nomeMaiorNota = nome;
                    nomeMenorNota = nome;
                } else {

                    if (nota > maiorNota) {
                        maiorNota = nota;
                        nomeMaiorNota = nome;
                    }

                    if (nota < menorNota) {
                        menorNota = nota;
                        nomeMenorNota = nome;
                    }
                }

                if (nota < 6) {
                    contAbaixoDeSeis++;
                } else {
                    aprovados += nome + " - " + nota + "\n";
                }
            }
           System.out.println("Se quer continuar digite 0, senão 1:");
            continuar = ler.nextInt();

            while (continuar != 0 && continuar != 1) {
     System.out.println("Valor inválido! Digite 0 para continuar ou 1 para sair:");
     continuar = ler.nextInt();
        }

        ler.nextLine(); // limpa buffer

        } while (continuar == 0);

        // Cálculo da média
        if (contNotas > 0) {
            mediaGeral = soma / contNotas;
        }

        // Impressão dos resultados
        System.out.println("A maior nota: " + maiorNota);
        System.out.println("A menor nota: " + menorNota);
        System.out.println("A média geral das notas: " + mediaGeral);
        System.out.println("Aluno que tirou a maior nota: " + nomeMaiorNota);
        System.out.println("Aluno que tirou a menor nota: " + nomeMenorNota);
        System.out.println("Quantidade de alunos abaixo de 6: " + contAbaixoDeSeis);
        System.out.println("Alunos aprovados:\n" + aprovados);

        ler.close();
    }
}