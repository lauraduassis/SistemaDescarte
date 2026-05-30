public class Validacoes {
    public int calcularPontuacao(int idade, int tempo, int area) {

        // vamos realizar o processamento
        // variavel para calcular os pontos das areas
        int areaPontos = 0;
        int pontuacao = 0;
        if (area == 1) {// para calcular a area e ver quantos pontos
            areaPontos = 5;

        } else if (area == 2) {
            areaPontos = 10;

        } else if (area == 3) {
            areaPontos = 15;
        }
        pontuacao = idade / 2 + tempo * 3 + areaPontos;
        return pontuacao;

    }

}
