import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        int qtdAlunos = 0;
        Scanner ler = new Scanner(System.in);
        do{
            try{
                System.out.println("Digite a quantidade de alunos:");
                qtdAlunos = ler.nextInt();
            }catch(Exception e) {
            System.out.println("Erro...");
            ler.nextLine();
        }
        }while(qtdAlunos < 0);

    float notaAlunos[][] = new float[qtdAlunos][3];
        for(int i=0;i<qtdAlunos;i++){
            for(int j=0;j<3;j++){
                do{
                    try{
                    System.out.println("Digite a nota"+ (j+1)
                +"do aluno" + (i+1)+":");
                        notaAlunos[i][j] = ler.nextFloat();
                    }catch(Exception erro){
                        System.out.println("Erro:"+erro.getMessage());
                    }
                System.out.println("A maior nota"+(j+1)+"do aluno"+(i+1)+":");
                System.out.println("A maior nota"+(j+1)+"do aluno"+(i+1)+":");

                }while(notaAlunos[i][j]<0 || notaAlunos[i][j]>10);
            
               }





            }
        }
        ler.close();

    }


