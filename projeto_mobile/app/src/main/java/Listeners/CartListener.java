package Listeners;

import java.util.ArrayList;

import Models.Carrinho;

public interface CartListener {
    void onRefreshCart(ArrayList<Carrinho> carrinhos);
}
