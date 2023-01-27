package com.example.projeto;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import Models.Singleton;

public class Login extends AppCompatActivity {
    private EditText tbUsername, tbPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tbUsername = findViewById(R.id.tbUsername);
        tbPassword = findViewById(R.id.tbPass);

    }

    public boolean isUsernameValid(String username){
        if(username.length() <= 2){ return false;}
        return true;
    }

    public void onClickLogin(View view){
        String username = tbUsername.getText().toString();
        if(!isUsernameValid(username)){
            Toast.makeText(this, "Username invalid", Toast.LENGTH_LONG).show();
            return;
        }
        //converte as crencais para base64
        String password = tbPassword.getText().toString();
        String credentials = username + ":" + password;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Singleton.getInstance(this).loginUserAPI(this,base64EncodedCredentials);
        finish();
    }

    public void onClickRegister(View view){
        Intent i = new Intent(Login.this, Register.class);
        startActivity(i);
        finish();
    }

}