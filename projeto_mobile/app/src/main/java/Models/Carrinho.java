package Models;

public class Carrinho {
    int id;
    Produto produto;
    int quantidade;

    public Carrinho(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.id = produto.getId();
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getId() {
        return id;
    }
}