package entities;

public class StatusEntity {
    private int id;
    private String descricao;


    // Construtores, getters e setters

    public StatusEntity(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
    //Inicialização dos atributos
    public StatusEntity() {
        id = 0;
        descricao = new String();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }



}
