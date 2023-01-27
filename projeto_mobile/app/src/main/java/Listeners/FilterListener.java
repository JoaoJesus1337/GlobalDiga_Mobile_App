package Listeners;

import java.util.ArrayList;

import Models.Categoria;
import Models.Marca;

public interface FilterListener {
    void onRefreshFilters(ArrayList<Categoria> categorias, ArrayList<Marca> marcas);
}
