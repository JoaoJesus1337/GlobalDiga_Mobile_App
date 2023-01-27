package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import Adapters.LinhaListAdapter;
import Listeners.LinhasListener;
import Models.Fatura;
import Models.LinhaFatura;
import Models.Singleton;

public class FaturaActivity extends AppCompatActivity implements LinhasListener {

    ListView lvLinhas;
    TextView tvDataFatura, tvPrecoTotal, tvIvaTotal, tvMorada, tvNif, tvDesconto, tvSubtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fatura);
        int idFatura = getIntent().getIntExtra("Fatura", 0);
        setTitle("Fatura Nº" + idFatura);
        lvLinhas = findViewById(R.id.lvLinhas);
        tvDesconto = findViewById(R.id.tvTotalDesconto);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvDataFatura = findViewById(R.id.tvDataFatura);
        tvPrecoTotal = findViewById(R.id.tvTotal);
        tvIvaTotal = findViewById(R.id.tvTotalIva);
        tvMorada = findViewById(R.id.tvMorada);
        tvNif = findViewById(R.id.tvNif);
        Singleton.getInstance(this).setLinhasListener(this);
        Singleton.getInstance(this).getFaturaDB(this, idFatura);

    }


    @Override
    public void onRefreshLinhasFatura(ArrayList<LinhaFatura> linhas, Fatura fatura) {
        if (linhas != null) {
            tvDesconto.setText(String.format("%.2f€", fatura.getValorDesconto()));
            tvSubtotal.setText(String.format("%.2f€", fatura.getSubtotal()));
            tvDataFatura.setText(String.format("Compra efetuada a %s", fatura.getData()));
            tvPrecoTotal.setText(String.format("%.2f€", fatura.getValorTotal()));
            tvIvaTotal.setText(String.format("%.2f€", fatura.getValorIva()));
            tvMorada.setText(fatura.getMorada());
            tvNif.setText(String.format("NIF: %s", fatura.getNif()));
            lvLinhas.setAdapter(new LinhaListAdapter(this, linhas));
        }
    }
}