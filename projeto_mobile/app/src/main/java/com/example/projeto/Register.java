package com.example.projeto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import Models.Signup;
import Models.Singleton;

public class Register extends AppCompatActivity {
    private EditText tbEmail, tbPassword, tbUsername, tbNif, tbTelefone, tbMorada, tbCodPostal, tbNome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tbEmail = findViewById(R.id.tbEmail);
        tbPassword = findViewById(R.id.tbPass);
        tbUsername = findViewById(R.id.tbUsername);
        tbNif = findViewById(R.id.tbNif);
        tbTelefone = findViewById(R.id.tbTelefone);
        tbMorada = findViewById(R.id.tbMorada);
        tbCodPostal = findViewById(R.id.tbCodPostal);
        tbNome = findViewById(R.id.tbNome);

    }

    public void onClickRegister(View view){
        Signup signup = null;
        String email = tbEmail.getText().toString();
        String pass = tbPassword.getText().toString();
        String username = tbUsername.getText().toString();
        String nif = tbNif.getText().toString();
        String telefone = tbTelefone.getText().toString();
        String morada = tbMorada.getText().toString();
        String codPostal = tbCodPostal.getText().toString();
        String nome = tbNome.getText().toString();

        //verficar se os campos estão bem preenchidos
        if(!isUsernameValid(username)){
            showToast("Username inválido");
            return;
        }
        if(!isEmailValid(email)){
            showToast("Email inválido");
            return;
        }
        if(!isPasswordValid(pass)){
            showToast("Password inválida");
            return;
        }
        if(!isNomeValid(nome)){
            showToast("Nome inválido");
            return;
        }
        if(!isNifValid(nif)){
            showToast("NIF inválido");
            return;
        }
        if(!isCodPostalValid(codPostal)){
            showToast("Código Postal inválido");
            return;
        }
        if(!isTelefoneValid(telefone)){
            showToast("Telefone inválido");
            return;
        }
        if(!isMoradaValid(morada)){
            showToast("Morada inválida");
            return;
        }

        signup = new Signup(username,email,pass,nome,nif,codPostal,telefone,morada);

        Singleton.getInstance(this).createUserAPI(this,signup);
        finish();
    }

    //verificações
    public boolean isNomeValid(String nome){
        if(nome.length() <= 4){ return false;}
        return true;
    }

    public boolean isEmailValid(String email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public boolean isPasswordValid(String pass){
        if(pass.length() <= 8){ return false;}
        return true;
    }

    public boolean isUsernameValid(String username){
        if(username.length() <= 2){ return false;}
        return true;
    }

    public boolean isNifValid(String nif){
        return nif.matches("[0-9]{9}");
    }

    public boolean isTelefoneValid(String telefone){
        return telefone.matches("[0-9]{9}");
    }

    public boolean isMoradaValid(String morada){
        if(morada.length() <= 2){ return false;}
        return true;
    }

    public boolean isCodPostalValid(String codPostal){
        return codPostal.matches("[0-9]{4}-[0-9]{3}");
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}