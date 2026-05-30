
//public class Validacoes { //onde vai realizar as validações

//public static boolean validarCEP(String cep) {
//public(para funcionar e visibilidade) ou private(somente dentro dessa classe é visivel) ou protected(proteje o metodo e o atributo,pode usar dentro do pacote ou dentro da herança), são modificadores de acesso
// o staic executa na memoria e sobe, todo metodo main é static
//boolean é um tipo de retorno que dá um retorno(return), faz a validação e retorna, e isso fala para o metodo se vai dar um retornar não
//validarCEP é o nome do metodo , sempre vai ser seguido por (), sempre vai ter alguma coisa ou as vezes pode estar vazio
// a parte dentro dos paranteses (String cep) --> parametros e argumentos 
//  depois dos parametros e argumentos tem o corpo do codigo 

//  validação com 9,9 e cep
/* 
        if (!validarTamanho(9,9,cep)) {
            return false;
        } 

    //validar o hipen
        if (cep.charAt(5) != '-'){
            return false;
        } 
        return true;
    }
    //verificação se está enquadrado dentro do tamanho
        public static boolean validarTamanho(int minimo, int maximo, String valor) { //os métodos os indicam ações, e sempre noinfinitivo ex: validar, a primeira letra é minuscula e na segunda palavra do nome a primeira é maiscula
            if (valor.length() < minimo || valor.length()>maximo) {
                return false;
            }
            return true;
*/

/* ------verificar nota-------

    //verificar nota  ---> public static ver se vai usar o boolean
            double nota ;
            public static boolean validarNota(double nota){
             
                if (nota < 0 || nota > 10){
                    return false;
                }else{
                    return true;
                }

            }

*/

//verficar positivo  ---> public static ver se vai usar o boolean se um numero é acima de 0
/* 
public class Validacoes {// onde vai realizar as validacoes

    public static boolean validarNumero(double numero) {
        
        if (numero < 0) {
            return false;
        } else {
            return true;
        }
    }
    

}*/

// verificar ficar par ou impar ---> public static ver se vai usar o boolean

public class Validacoes {

     public static boolean validarNumero (int numero){

        if (numero %2 == 0){
            return true; //par porque da resto 0
        }else{
            return false;//impar porque quando nao dá resto 0 é impar
        }


     }
}