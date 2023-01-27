package Listeners;

import java.util.ArrayList;

import Models.Fatura;

public interface FaturasListener {
    void onRefreshFaturas(ArrayList<Fatura> faturas);
}
