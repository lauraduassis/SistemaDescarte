package entities;

import java.time.LocalDate;

public interface impressao {
    public static final String CABECALHO = "==== EXERCÍCIOS DO DIA ====\n";
    public void imprimirTreino();
    public void imprimirTreino(LocalDate data);//filter for  date, why the people  want  to see the exercises of the day, the want to see to day exercises,so the filter is for the date, to show only the exercises of the day, if the date is null, show all exercises, if the date is not null, show only the exercises of the day. 
    


}
