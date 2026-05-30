package entities;

public class EntradaProduto {
    private ProdutoEntity produto;
    private int qtd;

    public EntradaProduto() {
        produto = new ProdutoEntity();
        qtd = 0;
    }

    public EntradaProduto(ProdutoEntity produto, int qtd) {
        this.produto = produto;
        this.qtd = qtd;
    }

    public ProdutoEntity getProduto() {
        return this.produto;
    }

    public int getQtd() {
        return this.qtd;
    }

    public void setProduto(ProdutoEntity produto) {
        this.produto = produto;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
}
