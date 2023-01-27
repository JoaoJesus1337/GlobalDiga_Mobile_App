package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import Listeners.DadosListener;
import Models.Dados;
import Models.Singleton;

public class CheckoutActivity extends AppCompatActivity implements DadosListener {

    private RadioGroup deliveryMethod;
    private EditText tbAddress, tbCoupon, tbNome, tbCard, tbMM, tbYY, tbCVV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        deliveryMethod = findViewById(R.id.radioGroup);
        tbAddress = findViewById(R.id.tbAdress);
        tbCoupon = findViewById(R.id.tbCoupon);
        tbNome = findViewById(R.id.tbNome);
        tbCard = findViewById(R.id.tbCard);
        tbMM = findViewById(R.id.tbMM);
        tbYY = findViewById(R.id.tbYY);
        tbCVV = findViewById(R.id.tbCVV);
        Singleton.getInstance(this).setDadosListener(this);
        Singleton.getInstance(this).getDadosAPI(this);

    }

    public void onClickFinish(View view) {
        String coupon = tbCoupon.getText().toString();
        int selectedId = deliveryMethod.getCheckedRadioButtonId();
        String address;
        switch (selectedId){
            case R.id.rbPickup:
                address = "Loja";
                break;
            case R.id.rbDelivery:
                address = tbAddress.getText().toString();
                if (address.isEmpty()){
                    Toast.makeText(this, "Insira um endereço de entrega", Toast.LENGTH_LONG).show();
                    return;
                }
                break;
            default:
                Toast.makeText(this, "Selecione um método de entrega", Toast.LENGTH_LONG).show();
                return;
        }

        if (isCardValid() == false){
            return;
        }
        Singleton.getInstance(this).buyCartAPI(this, address);
        finish();
    }

    public void onClickCheckCoupon(View view){
        String coupon = tbCoupon.getText().toString();
        if(coupon.isEmpty()){
            Toast.makeText(this, "Insira um cupão", Toast.LENGTH_SHORT).show();
            return;
        }
        Singleton.getInstance(this).checkCouponApi(this, coupon);
    }

    public boolean isCardValid(){
        String nome = tbNome.getText().toString();
        String card = tbCard.getText().toString();
        String mm = tbMM.getText().toString();
        String yy = tbYY.getText().toString();
        int mes;
        try {
            mes = Integer.parseInt(mm);
        }catch (Exception e){
            Toast.makeText(this, "Cartão Inválido: Data de validade inválida", Toast.LENGTH_SHORT).show();
            return false;
        }
        String cvv = tbCVV.getText().toString();
        if(nome.isEmpty() || card.isEmpty() || mm.isEmpty() || yy.isEmpty() || cvv.isEmpty()){
            Toast.makeText(this, "Cartão Inválido: Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(card.length() != 16){
            Toast.makeText(this, "Cartão Inválido: Número de cartão inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mes > 12 || mes < 1){
            Toast.makeText(this, "Cartão Inválido: Data de validade inválida", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(cvv.length() != 3){
            Toast.makeText(this, "Cartão Inválido: CVV inválido", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onClickCancel(View view){
        finish();
    }

    @Override
    public void onRefreshDados(Dados dado) {
        tbAddress.setText(dado.getMorada());
    }
}