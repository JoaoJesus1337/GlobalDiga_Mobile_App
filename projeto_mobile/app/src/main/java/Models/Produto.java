package Models;

public class Produto {
    String nome, detalhes, marca, referencia;
    int id;
    String capa;
    double preco;
    boolean emStock;

    public String getMarca() {
        return marca;
    }

    public String getReferencia() {
        return referencia;
    }

    public Produto(int id, String nome, String detalhes, String capa, double preco, String marca, String referencia, boolean emStock) {
        this.nome = nome;
        this.marca = marca;
        this.referencia = referencia;
        this.detalhes = detalhes;
        this.id = id;
        this.capa = capa;
        this.preco = preco;
        this.emStock = emStock;
    }

    public boolean isEmStock() {
        return emStock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public String getNome() {
        return nome;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public String getCapa() {
        return capa;
    }

}
