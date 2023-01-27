package Models;

import java.util.ArrayList;
import java.util.Date;

public class Fatura {
    int id;
    String nif, morada, data;
    double valorTotal, valorIva, valorDesconto, subtotal;

    public Fatura(int id, String nif, String morada, String data, double valorTotal, double valorIva, double valorDesconto, double subtotal) {
        this.id = id;
        this.subtotal = subtotal;
        this.valorDesconto = valorDesconto;
        this.nif = nif;
        this.morada = morada;
        this.data = data;
        this.valorTotal = valorTotal;
        this.valorIva = valorIva;
    }

    public double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorIva() {
        return valorIva;
    }

    public void setValorIva(double valorIva) {
        this.valorIva = valorIva;
    }
}
