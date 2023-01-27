package Listeners;

import java.util.ArrayList;

import Models.Fatura;
import Models.LinhaFatura;

public interface LinhasListener {
    void onRefreshLinhasFatura(ArrayList<LinhaFatura> linhas , Fatura fatura);
}
