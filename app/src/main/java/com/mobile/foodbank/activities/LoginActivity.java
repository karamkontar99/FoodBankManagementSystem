package com.mobile.foodbank.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.foodbank.R;
import com.mobile.foodbank.fcm.FirebaseSubscription;
import com.mobile.foodbank.models.User;
import com.mobile.foodbank.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText name;
    private EditText password;
    private Button login;
    private CheckBox rememberMe;
    private List<User> mUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = findViewById(R.id.txt_username_ans);
        password = findViewById(R.id.txt_password);
        login = findViewById(R.id.btn_login);
        rememberMe = findViewById(R.id.check_remember_me);

        configureForgetUsernameButton();

        loadFromSharedPrefs();

        login.setOnClickListener(view -> {
            if (validate(name.getText().toString(), password.getText().toString())) {
                saveToSharedPrefs(rememberMe.isChecked());

                switch (routing(name.getText().toString())) {
                    case "Donor":
                        FirebaseSubscription.subscribeDonor(name.getText().toString());
                        startActivity(new Intent(LoginActivity.this, DonorActivity.class));
                        break;
                    case "Manager":
                        FirebaseSubscription.subscribeManager();
                       startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
                        break;
                    case "Volunteer":
                        startActivity(new Intent(LoginActivity.this, VolunteerActivity.class));
                        break;
                }
            }
            else {
                Toast.makeText(this, "invalid login", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        login.setEnabled(false);

        UserRepository.getAllUsers(users -> {
            mUsers = users;
            login.setEnabled(true);
        }, message -> {
            Snackbar.make(name, message, Snackbar.LENGTH_LONG).setAction("OK", null).show();
        });
    }

    private void saveToSharedPrefs(boolean saveLogin) {
        SharedPreferences prefs = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", name.getText().toString());
        if (saveLogin)
            editor.putString("password", password.getText().toString());
        editor.apply();
    }

    private void loadFromSharedPrefs() {
        SharedPreferences prefs = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE);
        name.setText(prefs.getString("username", ""));
        password.setText(prefs.getString("password", ""));
        if (password.getText().toString().length() > 0)
            rememberMe.setChecked(true);
    }


    public void configureForgetUsernameButton() {
        Button ForgetUsername = findViewById(R.id.btn_password_reset);
        ForgetUsername.setOnClickListener(view -> {
            Intent in = new Intent( LoginActivity.this , PasswordResetActivity.class );
            startActivity(in);
        });
    }

    private boolean validate(String username, String userPassword) {
        User user = null;
        for (int i = 0; i < mUsers.size(); i++) {
            if (username.equals(mUsers.get(i).getUsername())) {
                user = mUsers.get(i);
            }
        }
        if (user != null) {
            return userPassword.equals(user.getPassword());
        }
        return false;
    }

    private String routing(String username) {
        User user = null;
        for (int i = 0; i < mUsers.size(); i++) {
            if (username.equals(mUsers.get(i).getUsername())) {
                user = mUsers.get(i);
            }
        }
        return user.getRole();
    }



}