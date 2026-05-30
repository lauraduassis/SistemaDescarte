package entities;

public class CargoEntity {

    private int codigo;
    private String descricao;

    public CargoEntity(){
        codigo = 0;
        descricao = "";
    }

    
    public CargoEntity(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }


    public int getCodigo() {
        return this.codigo;
    }


    public String getDescricao() {
        return this.descricao;
    }


    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    

    
}