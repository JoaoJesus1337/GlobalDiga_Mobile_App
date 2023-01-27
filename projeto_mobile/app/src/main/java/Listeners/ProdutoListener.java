package Listeners;

import java.util.ArrayList;

import Models.Produto;

public interface ProdutoListener {
    void onRefreshProdutos(ArrayList<Produto> listaProdutos);
}
