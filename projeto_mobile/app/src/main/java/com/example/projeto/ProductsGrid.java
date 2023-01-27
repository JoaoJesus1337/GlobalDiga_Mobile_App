package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import Adapters.ProductGridAdapter;
import Listeners.ProdutoListener;
import Models.Produto;
import Models.Singleton;

public class ProductsGrid extends Fragment implements ProdutoListener {

    private GridView gvProdutos;

    public ProductsGrid() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products_grid, container, false);
        gvProdutos = view.findViewById(R.id.gvProdutosFiltrados);
        Singleton.getInstance(getContext()).setProdutoListener(this);
        Singleton.getInstance(getContext()).getAllProdutosAPI(getContext());

        gvProdutos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position , long id) {
                Intent intent = new Intent(getContext(), DetailsProduct.class);
                intent.putExtra("Produto", (int) id);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onRefreshProdutos(ArrayList<Produto> listaProdutos) {
        if (listaProdutos != null) {
            gvProdutos.setAdapter(new ProductGridAdapter(getContext(), listaProdutos));
        }
    }

}