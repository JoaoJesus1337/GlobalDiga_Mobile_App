package com.example.projeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import Models.DBHelper;
import Utils.JsonParser;
import Utils.Public;

public class PersonalArea extends AppCompatActivity {

    ActionBar actionBar = null;
    DBHelper dbHelper = null;
    private Button btnOrder, btnPersonal;
    private FragmentContainerView fragmentPersonal, fragmentOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_personal_area);
        setTitle("Bem Vindo");
        btnOrder = findViewById(R.id.btnOrders);
        btnPersonal = findViewById(R.id.btnPersonal);
        fragmentPersonal = findViewById(R.id.fragmentPersonal);
        fragmentOrders = findViewById(R.id.fragmentOrders);
        actionBar = getSupportActionBar();
        isConnected();
        actionBar.setDisplayShowHomeEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        menu.removeItem(R.id.app_bar_personalArea);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Intent i = null;

        switch (itemId){
            case R.id.app_bar_cart:
                i = new Intent(this, ShoppingCart.class);
                break;
            case R.id.app_bar_search:
                i = new Intent(this, FiltersActivity.class);
                break;
        }
        if(i != null){
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCLickLogout(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Terminar Sessão");
        //caso o utilizador efetue o logout os seus dados são apagados da aplicação
        builder.setMessage("Tem a certeza que pretende terminar sessão?");
        builder.setPositiveButton("Sim", (dialog, which) -> {
            dbHelper.removeAllFaturas();
            SharedPreferences sharedPreferences = getSharedPreferences(Public.SHARED_FILE, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove(Public.TOKEN);
            editor.apply();
            Toast.makeText(this, "Logout efetuado com sucesso", Toast.LENGTH_SHORT).show();
            finish();
        });
        builder.setNegativeButton("Não", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    public void onClickSwitch(View view){
        int id = view.getId();
        switch (id){
            case R.id.btnOrders:
                fragmentOrders.setVisibility(View.VISIBLE);
                fragmentPersonal.setVisibility(View.GONE);
                btnOrder.setEnabled(false);
                btnPersonal.setEnabled(true);
                break;
            case R.id.btnPersonal:
                fragmentOrders.setVisibility(View.GONE);
                fragmentPersonal.setVisibility(View.VISIBLE);
                btnPersonal.setEnabled(false);
                btnOrder.setEnabled(true);
                break;
        }
    }

    public void isConnected(){
        if (JsonParser.isConnected(this)){
            btnPersonal.setEnabled(true);
        }else {
            btnPersonal.setEnabled(false);
        }
        btnOrder.setEnabled(false);
        fragmentOrders.setVisibility(View.VISIBLE);
        fragmentPersonal.setVisibility(View.GONE);
    }
}