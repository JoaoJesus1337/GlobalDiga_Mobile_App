package Models;

public class Dados {
    String nome, telefone,nif, morada,codPostal;

    public Dados(String nome, String telefone, String nif, String morada, String codPostal) {
        this.nome = nome;
        this.telefone = telefone;
        this.nif = nif;
        this.morada = morada;
        this.codPostal = codPostal;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }
}
