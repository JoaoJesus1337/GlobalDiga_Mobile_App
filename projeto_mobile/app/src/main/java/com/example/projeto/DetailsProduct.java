package com.example.projeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import Listeners.DetailsListener;
import Models.Produto;
import Models.Singleton;
import Utils.JsonParser;
import Utils.Public;

public class DetailsProduct extends AppCompatActivity implements DetailsListener {

    private ImageView imgCapa;
    private TextView tvDetails, tvStock, tvPreco, tvMarca, tvReferencia;
    private ImageButton btnMore, btnMinus;
    private EditText numQuant;
    private Button btnCart, btnLocation;
    ActionBar actionBar = null;
    double latitude = 0;
    double longitude = 0;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);

        if (!JsonParser.isConnected(this)) {
            Toast.makeText(this, "Sem ligação à internet", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnLocation = findViewById(R.id.btnLocation);
        imgCapa = findViewById(R.id.imageCapa);
        tvDetails = findViewById(R.id.tvDetalhes);
        tvStock = findViewById(R.id.tvStock);
        tvPreco = findViewById(R.id.tvPrecoTotal);
        btnMinus = findViewById(R.id.btnMinus);
        btnMore = findViewById(R.id.btnMore);
        btnCart = findViewById(R.id.btnAddCart);
        numQuant = findViewById(R.id.numQuantity);
        tvMarca = findViewById(R.id.tvMarca);
        tvReferencia = findViewById(R.id.tvReferencia);

        id = getIntent().getIntExtra("Produto", 0);
        Singleton.getInstance(this).setDetailsListener(this);
        Singleton.getInstance(this).getProdutoAPI(this, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        SharedPreferences sharedPreferences = getSharedPreferences(Public.SHARED_FILE, MODE_PRIVATE);
        Intent i = null;
        boolean isLogged = sharedPreferences.contains(Public.TOKEN);
        if(!isLogged) {
            i = new Intent(this, Login.class);
            startActivity(i);
            return super.onOptionsItemSelected(item);
        }

        switch (itemId){
            case R.id.app_bar_cart:
                i = new Intent(this, ShoppingCart.class);
                break;
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

    private void carregarProduto(Produto produto) {
        tvDetails.setText(produto.getDetalhes());
        tvMarca.setText(produto.getMarca());
        tvReferencia.setText(produto.getReferencia());
        if(produto.isEmStock()) {
            tvStock.setText(R.string.inStock);
            tvStock.setTextColor(Color.parseColor("#048000"));
        } else {
            tvStock.setText(R.string.outStock);
            btnCart.setEnabled(false);
            tvStock.setTextColor(Color.parseColor("#b00200"));
            btnLocation.setEnabled(false);
            btnMore.setEnabled(false);
            btnMinus.setEnabled(false);
            numQuant.setEnabled(false);
        }
        tvPreco.setText(String.format("%.2f€", produto.getPreco()));
        setTitle(produto.getNome());
        Glide.with(this)
                .load(Public.imgURL+produto.getCapa())
                .into(imgCapa);

    }

    @Override
    public void onRefreshDetails(Produto produto) {
        if (produto != null){
            carregarProduto(produto);
        }
    }


    //Buttons
    public void onCLickChangeQuantity(View view) {
        int quantity = Integer.parseInt(numQuant.getText().toString());
        int id = view.getId();
        switch (id){
            case R.id.btnMinus:
                if(quantity > 1)
                    quantity--;
                break;
            case R.id.btnMore:
                if (quantity < 20)
                    quantity++;
                break;
        }
        numQuant.setText(String.valueOf(quantity));
    }

    public void onClickAddCart(View view) {
        int quantity;
        SharedPreferences sharedPreferences = getSharedPreferences(Public.SHARED_FILE, MODE_PRIVATE);
        if(!sharedPreferences.contains(Public.TOKEN)){
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            return;
        }
        try {
            quantity = Integer.parseInt(numQuant.getText().toString());
        }catch (Exception e){
            Toast.makeText(this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
            return;
        }
        int id = getIntent().getIntExtra("Produto", 1);
        Singleton.getInstance(this).addCartAPI(this, id, quantity);
    }

    public void onClickbtnL(View view)
    {
        try
        {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Singleton.getInstance(this).getLojaMaisPerto(this,longitude,latitude,id);


    }
}