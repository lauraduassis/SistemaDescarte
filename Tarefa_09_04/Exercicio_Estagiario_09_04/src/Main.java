
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static boolean emailValido(String email) {
        if (email.length() < 10)
            return false;
        if (!email.contains("@"))
            return false;
        if (!email.contains("."))
            return false;

        int posArroba = email.indexOf("@");
        if (posArroba == 0 || posArroba == email.length() - 1)
            return false;

        int ultimoPonto = email.lastIndexOf(".");
        if (ultimoPonto < posArroba)
            return false;
        if (ultimoPonto == email.length() - 1)
            return false;

        return true;

    }

    public static void main(String[]args) {
            Scanner ler = new Scanner(System.in);
           

        ArrayList<Estagiario>listaEstagiarios = new ArrayList<>();
        String continuar = "s";

        //podemos colocar do while para fazer as perguntas
        while(continuar.equalsIgnoreCase("s")) {

            String nome = "";
            while (nome.isEmpty()) {
                System.out.println("Nome:");
                nome= ler.nextLine().trim();
                if(nome.isEmpty())
                    System.out.println("Nome não pode estar vazio ");

            }

            String email = "";
            while (!emailValido(email)) {
                System.out.println("Email:");
                email = ler.nextLine().trim();

                
            }
            String curso = "";
            while (curso.isEmpty()) {
                System.out.println("Curso:");
                curso = ler.nextLine().trim();
                if(curso.isEmpty())
                    System.out.println("curso não pode estar vazio ");

            }

            double media = -1;
            while (media < 0 || media > 10 ) {
                try{
                    System.out.println("media geral(0 a 10):");
                    media = Double.parseDouble(ler.nextLine());

                } catch(NumberFormatException e) {
                    System.out.println("Digite apenas numeros");
                }
                
            }

            String sexo = "";
            while (!sexo.equalsIgnoreCase("M")&& !sexo.equalsIgnoreCase("F")) {
                System.out.println("Sexo (M/F):");
                sexo = ler.nextLine().trim();
                if (!sexo.equalsIgnoreCase("M")&& !sexo.equalsIgnoreCase("F")){
                    System.out.println("Digite apenas M ou F");
                }
                
            }
            int idade = -1;
            while (idade < 16 || idade > 100) {
              try {
              System.out.println("Idade (mínimo 16):");
              idade = Integer.parseInt(ler.nextLine());
                 if (idade < 16 || idade > 100)
                   System.out.println("Idade deve ser entre 16 e 100!");
             } catch (NumberFormatException e) {
                System.out.println("Digite apenas números!");
                  }
            }   


       //Precisa colocar isso? Não é só colocar o do while
            listaEstagiarios.add(new Estagiario(nome, email, curso,media, idade, sexo ));
            System.out.println("Cadastrar outro estagiario? (s/n)");
            continuar = ler.nextLine();}

        int opcao = 0;
        while (opcao != 5) {
            System.out.println("\n===== MENU =====");
            System.out.println("1 - Candidatos maiores que uma idade");
            System.out.println("2 - Candidatos com média acima de 7");
            System.out.println("3 - Total de candidatos por sexo");
            System.out.println("4 - Nomes e idades do curso ADS");
            System.out.println("5 - Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(ler.nextLine());

            }catch (NumberFormatException e) {
                System.out.println("digite apenas números");
                continue;
            }
             if (opcao == 1) {
    int idadeMin = -1;
    while (idadeMin < 0) {
        try {
            System.out.print("Digite a idade mínima: ");
            idadeMin = Integer.parseInt(ler.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Digite apenas números!");
        }
    }
    for (Estagiario e : listaEstagiarios) {
        if (e.getIdade() > idadeMin)
            System.out.println(e.getNome() + " - " + e.getIdade() + " anos");
    }
} else if (opcao == 2) {
    for (Estagiario e : listaEstagiarios) {
        if (e.getMediaGeral() > 7)
            System.out.println(e.getNome() + " - Média: " + e.getMediaGeral());
    }

} else if (opcao == 3) {
    int masc = 0, fem = 0;
    for (Estagiario e : listaEstagiarios) {
        if (e.getSexo().equalsIgnoreCase("M")) masc++;
        else fem++;
    }
    System.out.println("Masculino: " + masc);
    System.out.println("Feminino: " + fem);

} else if (opcao == 4) {
    for (Estagiario e : listaEstagiarios) {
        if (e.getCurso().equalsIgnoreCase("ADS"))
            System.out.println(e.getNome() + " - " + e.getIdade() + " anos");
    }

} else if (opcao != 5) {
    System.out.println("Opção inválida!");
}
        System.out.println("Encerrando o programa...");
        ler.close();


        }

    }
}

//fazer o programa por partes pra rodar por partes  e ocnseguir achar melhor os erros

//para fazer fazer a xinbiççao do menu podemos usar o siwth
