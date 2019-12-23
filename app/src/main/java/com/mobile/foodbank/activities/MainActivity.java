package com.mobile.foodbank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.google.firebase.FirebaseApp;
import com.mobile.foodbank.R;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(MainActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureSignup();
        configureLogin();
    }

    private void configureSignup() {
        Button btn_sign = findViewById(R.id.btn_sign_up);
        btn_sign.setOnClickListener(view -> {
            Intent in = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(in);
        });
    }

    private void configureLogin() {
        Button btn_sign = findViewById(R.id.btn_login);
        btn_sign.setOnClickListener(view -> {
            Intent in = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(in);
        });
    }

}
