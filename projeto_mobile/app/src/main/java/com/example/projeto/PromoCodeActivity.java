package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import Utils.Public;

public class PromoCodeActivity extends AppCompatActivity {

    private TextView tvPromoCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code);
        tvPromoCode = findViewById(R.id.tvPromoCode);
        tvPromoCode.setText(getIntent().getStringExtra(Public.PROMOCODE));
    }
}