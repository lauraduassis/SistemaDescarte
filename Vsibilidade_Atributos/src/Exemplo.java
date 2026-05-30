import javax.swing.JOptionPane;;

public class Exemplo {


    int contador;
    public final String CURSO = "INFORMÁTICA"; // constante
    public int getContador(){
        return contador;
    }

    void mostrar() {
        contador++;
        System.out.println("Atributo"+contador + "" + CURSO);
        System.out.println("Linha 15(Variável):"+contador);

    }

    void contar() {
        System.out.println("rodou a linha 13");
        int contador = 0;
        contador++;
        System.out.println("Linha 22(Atributo):"+contador);
    }

    void contar(int cont) {
        System.out.println("rodou a linha 19");
        contador++;
        System.out.println("Linha 28(Atributo):"+contador);
    }

    void contarUtilizacao() {
        System.out.println("rodou a linha 26");
        contador++;
        System.out.println("Linha 34(Atributo):"+contador);
    }

    
    void contarUtilizacao(int contador) {
        System.out.println("rodou a linha 32");
        this.contador++;
        contador++;
        System.out.println("Linha 42(Atributo):"+contador);
    }
}


