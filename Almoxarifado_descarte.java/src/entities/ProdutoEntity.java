package entities;

import java.time.LocalDate;

public class ProdutoEntity {

    private int codigo;
    private String descricao;
    private String nrPatrimonio;
    private LocalDate dataRegistro;


    public ProdutoEntity() {
        codigo = 0;
        descricao = "";
        nrPatrimonio = "";
        dataRegistro = LocalDate.now();
    }


    public ProdutoEntity(int codigo, String descricao, String nrPatrimonio, LocalDate dataRegistro) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.nrPatrimonio = nrPatrimonio;
        this.dataRegistro = dataRegistro;
    }


    public int getCodigo() {
        return codigo;
    }


    public String getDescricao() {
        return descricao;
    }


    public String getNrPatrimonio() {
        return nrPatrimonio;
    }


    public LocalDate getDataRegistro() {
        return dataRegistro;
    }


    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }


    public void setNrPatrimonio(String nrPatrimonio) {
        this.nrPatrimonio = nrPatrimonio;
    }


    public void setDataRegistro(LocalDate dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    

}
