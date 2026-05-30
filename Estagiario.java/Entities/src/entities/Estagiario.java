package entities;

import java.util.Scanner;

public class Estagiario {

    private String nome;
    private String curso;
    private float mediaGeral;
    private String email;
    private int idade;
    private String sexo;

    public void cadastrar(Scanner ler){

        System.out.println("Digite o nome:");
        nome = ler.nextLine();
    if(nome.isEmpty()){
            System.out.println("Nome não pode ser vazio!");
        }

    while(nome.isEmpty());

        System.out.println("Digite o curso:");
        curso = ler.nextLine();
    if(curso.isEmpty()){
            System.out.println("Curso não pode ser vazio!");
        }

    while(curso.isEmpty());

    do

    {
        try {
            System.out.println("Digite a média:");
            mediaGeral = ler.nextFloat();
        } catch (Exception e) {
            System.out.println("Digite um número válido!");
            ler.nextLine(); 
        }
    }while(mediaGeral<0||mediaGeral>10);

    ler.nextLine(); 

     do {
        System.out.println("Digite o email:");
        email = ler.nextLine();

        if(!email.contains("@") || !email.contains(".")){
            System.out.println("Email inválido!");
        }

    } while(!email.contains("@") || !email.contains("."));

    do {
        try {
            System.out.println("Digite a idade:");
            idade = ler.nextInt();

            if(idade <= 0){
                System.out.println("Idade inválida!");
            }

        } catch (Exception e) {
            System.out.println("Digite um número válido!");
            ler.nextLine();
            idade = -1;
        }

    } while(idade <= 0);
        ler.nextLine(); 

     do {
        System.out.println("Digite o sexo (F/M)");
        sexo = ler.nextLine().toUpperCase();
        if(!sexo.equals("M") && !sexo.equals("F")){
            System.out.println("Digite apenas F ou M");
        }
        while (!sexo.equals("F") && !sexo.equals("M")) {
            
        }
    } while(!sexo.equals("M") && !sexo.equals("F"));
}
}