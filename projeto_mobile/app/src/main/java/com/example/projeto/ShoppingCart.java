package com.example.projeto;


import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Adapters.CartListAdapter;
import Listeners.CartListener;
import Models.Carrinho;
import Models.Singleton;
import Utils.JsonParser;
import Utils.Public;

public class ShoppingCart extends AppCompatActivity implements CartListener {

    private TextView tvPrecoTotal,tvNumeroArtigos ;
    private EditText tbCoupon;
    private ListView lvCart;
    private Button btnCheckout, btnClearCart;
    private double Total;
    private int TotalArtigos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        setTitle("Carrinho de Compras");
        tvPrecoTotal = findViewById(R.id.tvPrecoTotal);
        tvNumeroArtigos = findViewById(R.id.tvArtigos);
        tbCoupon = findViewById(R.id.tbCoupon);
        lvCart = findViewById(R.id.lvCart);
        btnCheckout = findViewById(R.id.btnComprar);
        btnClearCart = findViewById(R.id.btnClear);

        if(!JsonParser.isConnected(this)){
            btnCheckout.setEnabled(false);
            btnClearCart.setEnabled(false);
            Toast.makeText(this, R.string.sem_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        lvCart.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Carrinho carrinho = (Carrinho) adapterView.getItemAtPosition(position);
                int quantidade = carrinho.getQuantidade();
                Intent intent = new Intent(ShoppingCart.this, EditCart.class);
                intent.putExtra("Quantidade", quantidade);
                intent.putExtra("Produto", carrinho.getProduto().getId());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        Total = 0;
        TotalArtigos = 0;
        updateCarrinho();
        super.onResume();
    }

    private void updateCarrinho(){
        Singleton.getInstance(this).setCartListener(this);
        Singleton.getInstance(this).viewCartAPI(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        menu.removeItem(R.id.app_bar_cart);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent i = null;
        SharedPreferences sharedPreferences = getSharedPreferences(Public.SHARED_FILE, MODE_PRIVATE);
        boolean isLogged = sharedPreferences.contains(Public.TOKEN);
        if(!isLogged) {
            i = new Intent(this, Login.class);
            startActivity(i);
            return super.onOptionsItemSelected(item);
        }

        switch (itemId){
            case R.id.app_bar_search:
                i = new Intent(this, FiltersActivity.class);
                break;
            case R.id.app_bar_personalArea:
                i = new Intent(this, PersonalArea.class);
                break;
        }

        if(i != null){
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickClear(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Limpar Carrinho");
        builder.setMessage("Tem a certeza que pretende limpar o carrinho?");
        builder.setPositiveButton("Sim", (dialogInterface, i) -> {
            Singleton.getInstance(this).clearCartAPI(this);
            onResume();
        });
        builder.setNegativeButton("Não", (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    public void onClickBuy(View view){
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }


    @Override
    public void onRefreshCart(ArrayList<Carrinho> carrinhos) {
        if(carrinhos != null) {
            if (carrinhos.size() == 0){
                btnCheckout.setEnabled(false);
                btnClearCart.setEnabled(false);
            }
            for (Carrinho c : carrinhos) {
                Total += c.getProduto().getPreco() * c.getQuantidade();
                TotalArtigos += c.getQuantidade();
            }
            tvPrecoTotal.setText(String.format("%s€", String.format("%.2f", Total)));
            tvNumeroArtigos.setText(String.format("Artigos: %s", TotalArtigos));
            lvCart.setAdapter(new CartListAdapter(this, carrinhos));
        }
    }
}