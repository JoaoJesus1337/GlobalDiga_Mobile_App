package Models;

public class LinhaFatura {
    String produto_nome, produto_referencia;
    int quantidade, id, id_Fatura;
    double valor, valorIva;


    public LinhaFatura(int id, int id_Fatura, String produto_nome, String produto_referencia, int quantidade, double valor, double valorIva) {
        this.produto_nome = produto_nome;
        this.produto_referencia = produto_referencia;
        this.quantidade = quantidade;
        this.valor = valor;
        this.id = id;
        this.id_Fatura = id_Fatura;
        this.valorIva = valorIva;
    }

    public int getId_Fatura() {
        return id_Fatura;
    }

    public void setId_Fatura(int id_Fatura) {
        this.id_Fatura = id_Fatura;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getProduto_nome() {
        return produto_nome;
    }

    public void setProduto_nome(String produto_nome) {
        this.produto_nome = produto_nome;
    }

    public String getProduto_referencia() {
        return produto_referencia;
    }

    public void setProduto_referencia(String produto_referencia) {
        this.produto_referencia = produto_referencia;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValorIva() {
        return valorIva;
    }

    public void setValorIva(double valorIva) {
        this.valorIva = valorIva;
    }

}
