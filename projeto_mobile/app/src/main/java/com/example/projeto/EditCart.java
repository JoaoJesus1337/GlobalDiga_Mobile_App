package com.example.projeto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import Listeners.DetailsListener;
import Models.Produto;
import Models.Singleton;
import Utils.Public;

public class EditCart extends AppCompatActivity implements DetailsListener {

    private ImageView imgCapa;
    private TextView tvNome, tvStock, tvPreco, tvDescricao;
    private ImageButton btnMore, btnMinus;
    private EditText numQuant;
    private Button btnApply, btnRemove;
    private int quantidade;
    private int idProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cart);
        imgCapa = findViewById(R.id.imageCapa);
        tvNome = findViewById(R.id.tvNomeDisplay);
        tvStock = findViewById(R.id.tvStock);
        tvPreco = findViewById(R.id.tvPreco);
        tvDescricao = findViewById(R.id.tvDescricao);
        btnMinus = findViewById(R.id.btnMinus);
        btnMore = findViewById(R.id.btnMore);
        numQuant = findViewById(R.id.numQuantity);
        btnApply = findViewById(R.id.btnApply);
        btnRemove = findViewById(R.id.btnRemove);

        idProduto = getIntent().getIntExtra("Produto", 0);
        quantidade = getIntent().getIntExtra("Quantidade", 0);
        numQuant.setText(String.valueOf(quantidade));

        Singleton.getInstance(this).setDetailsListener(this);
        Singleton.getInstance(this).getProdutoAPI(this, idProduto);
    }

    @Override
    public void onRefreshDetails(Produto produto) {
        if (produto != null) {
            //load produto into activity
            tvNome.setText(produto.getNome());
            tvPreco.setText(String.format("%s €", String.valueOf(produto.getPreco())));
            tvDescricao.setText(produto.getDetalhes());
            if(produto.isEmStock()) {
                tvStock.setText(R.string.inStock);
                tvStock.setTextColor(Color.parseColor("#048000"));
            } else {
                tvStock.setText(R.string.outStock);
                btnApply.setEnabled(false);
                tvStock.setTextColor(Color.parseColor("#b00200"));
            }
            Glide.with(this).
                    load(Public.imgURL+produto.getCapa()).
                    into(imgCapa);
        }
    }

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

    public void onClickRemover(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Remover Produto");
        builder.setMessage("Tem a certeza que pretende remover este produto do carrinho?");
        builder.setPositiveButton("Sim", (dialog, which) -> {
            Singleton.getInstance(this).removeCartAPI(this, idProduto);
            finish();
        });
        builder.setNegativeButton("Não", (dialog, which) -> dialog.dismiss());
        builder.show();

    }

    public void onClickApply(View view){
        int quantity = Integer.parseInt(numQuant.getText().toString());
        Singleton.getInstance(this).updateCartAPI(this, idProduto, quantity);
        finish();
    }
}