package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import Adapters.FaturaListAdapter;
import Listeners.FaturasListener;
import Models.Fatura;
import Models.Singleton;

public class OrdersList extends Fragment implements FaturasListener {

    private ListView lvOrders;

    public OrdersList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        lvOrders = view.findViewById(R.id.lvOrders);
        Singleton.getInstance(getContext()).setFaturasListener(this);
        Singleton.getInstance(getContext()).getFaturasAPI(getContext());

        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), FaturaActivity.class);
                intent.putExtra("Fatura", (int) l);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onRefreshFaturas(ArrayList<Fatura> faturas) {
        if (faturas != null) {
            lvOrders.setAdapter(new FaturaListAdapter(getContext(), faturas));
        }
    }

}