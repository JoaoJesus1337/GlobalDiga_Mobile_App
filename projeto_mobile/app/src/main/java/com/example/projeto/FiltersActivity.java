package com.example.projeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import Listeners.FilterListener;
import Models.Categoria;
import Models.Marca;
import Models.Singleton;
import Utils.JsonParser;
import Utils.Public;

public class FiltersActivity extends AppCompatActivity implements FilterListener{

    ActionBar actionBar = null;
    private Spinner spinnerCategorias, spinnerMarcas, spinnerOrdem;
    private EditText tbSearch;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        spinnerOrdem = findViewById(R.id.spinnerOrdem);
        tbSearch = findViewById(R.id.tbSearch);
        spinnerCategorias = findViewById(R.id.spinnerCategorias);
        spinnerMarcas = findViewById(R.id.spinnerMarcas);
        btnSearch = findViewById(R.id.btnSearch);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        if(!JsonParser.isConnected(this)){
            btnSearch.setEnabled(false);
        }

        Singleton.getInstance(this).setFilterListener(this);
        Singleton.getInstance(this).getALlFiltersDB(this);
    }

    @Override
    public void onRefreshFilters(ArrayList<Categoria> categorias, ArrayList<Marca> marcas) {
        //set spinner categorias
        ArrayList<String> categoriasString = new ArrayList<>();
        categoriasString.add("Todas as categorias");
        for (Categoria categoria : categorias) {
            categoriasString.add(categoria.getNome());
        }
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasString);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapterCategorias);

        //set spinner marcas
        ArrayList<String> marcasString = new ArrayList<>();
        marcasString.add("Todas as marcas");
        for (Marca marca : marcas) {
            marcasString.add(marca.getNome());
        }
        ArrayAdapter<String> adapterMarcas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, marcasString);
        adapterMarcas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMarcas.setAdapter(adapterMarcas);

        //set spinner ordem
        ArrayList<String> ordemString = new ArrayList<>();
        ordemString.add("Ordenar por");
        ordemString.add("Preço Crescente");
        ordemString.add("Preço Decrescente");
        ArrayAdapter<String> adapterOrdem = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ordemString);
        adapterOrdem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrdem.setAdapter(adapterOrdem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        menu.removeItem(R.id.app_bar_search);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent i = null;
        SharedPreferences sharedPreferences = getSharedPreferences(Public.SHARED_FILE, MODE_PRIVATE);
        boolean isLogged = sharedPreferences.contains(Public.TOKEN);
        if (!isLogged) {
            i = new Intent(this, Login.class);
            startActivity(i);
            return super.onOptionsItemSelected(item);
        }

        switch (itemId) {
            case R.id.app_bar_cart:
                i = new Intent(this, ShoppingCart.class);
                break;

            case R.id.app_bar_personalArea:
                i = new Intent(this, PersonalArea.class);
                break;
        }

        if (i != null) {
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCLickSearch(View view){
        String query = "?";
        String search = tbSearch.getText().toString();
        String categoria = spinnerCategorias.getSelectedItem().toString();
        String marca = spinnerMarcas.getSelectedItem().toString();
        String ordem = spinnerOrdem.getSelectedItem().toString();
        if(categoria != "Todas as categorias"){
            query += "categoria=" + categoria + "&";
        }
        if(marca != "Todas as marcas"){
            query += "marca=" + marca + "&";
        }
        if(ordem != "Ordenar por"){
            switch (ordem){
                case "Preço Crescente":
                    query += "order=asc&";
                    break;
                case "Preço Decrescente":
                    query += "order=desc&";
                    break;
            }
        }
        if(!search.equals("")){
            query += "search=" + search + "&";
        }
        query = query.substring(0, query.length() - 1);
        Singleton.getInstance(this).getQueryProdutosAPI(this, query);
        finish();
    }
}