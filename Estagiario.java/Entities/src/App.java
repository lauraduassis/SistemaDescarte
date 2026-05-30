import java.util.ArrayList;
import java.util.Scanner;

import entities.Estagiario;


public class App {

    public static void main(String[] args) {

        ArrayList<Estagiario> estagiarios = new ArrayList<>();
        Scanner ler = new Scanner(System.in);

        String continuar = "S";

        do {
            Estagiario estagiario = new Estagiario();

            estagiario.cadastrar(ler); 
            estagiarios.add(estagiario);

            System.out.println("Deseja cadastrar outro estagiário? (S|N)");
            continuar = ler.next().charAt(0) + "";
            ler.nextLine(); 
        } while (continuar.equalsIgnoreCase("S"));

        mostrarMenu();  
    }

    public static void mostrarMenu() {
        System.out.println("=== MENU ===");
        System.out.println("1 - Mostrar nome de estagiários acima de uma idade");
        System.out.println("2 - Mostrar nome de estagiários com média geral acima de 7");
        System.out.println("3 - Mostrar total de estagiários por sexo");
        System.out.println("4 - Mostrar nome e idade dos estagiários de ADS");
        System.out.println("5 - SAIR");
    }
    
}