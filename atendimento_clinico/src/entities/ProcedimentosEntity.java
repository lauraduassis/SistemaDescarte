package entities;

public class ProcedimentosEntity {
    private int id;
    private String descricao;
    private String observacoes;

    public ProcedimentosEntity() {
        id = 0;
        descricao = new String();
        observacoes = new String();
    }

    public ProcedimentosEntity(int id, String descricao, String observacoes) {
        this.id = id;
        this.descricao = descricao;
        this.observacoes = observacoes;

    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}