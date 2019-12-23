package com.mobile.foodbank.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mobile.foodbank.R;
import com.mobile.foodbank.models.User;
import com.mobile.foodbank.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;


public class PasswordResetActivity extends AppCompatActivity {
    private List<User> mUsers = new ArrayList<>();

    private EditText mUserName, mAnswer, mNewPassword;
    public String username, security_ans,new_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        mUserName = findViewById(R.id.txt_username_ans);
        mAnswer = findViewById(R.id.txt_sec_quest_ans);
        mNewPassword = findViewById(R.id.new_password_txt);

        Spinner spinner = findViewById(R.id.spinner_sec_quest);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.securityQues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner.setAdapter(adapter);

        configureButton();

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRepository.getAllUsers(users -> {
            mUsers = users;
        }, message -> {
            Snackbar.make(mUserName, message, Snackbar.LENGTH_LONG).setAction("OK", null).show();
        });
    }

    private void configureButton() {
        Button btn = findViewById(R.id.btn_password_reset);
        btn.setOnClickListener(view -> {
                    new_password = mNewPassword.getText().toString();
                    username = mUserName.getText().toString();
                    security_ans = mAnswer.getText().toString();

                    if (new_password.length() < 8) {
                        mNewPassword.setError("password should be at least 8 characters.");
                        return;
                    }

                    User user = validate(username, security_ans);
                    if (user!=null) {
                        user.setPassword(new_password);
                        UserRepository.putUser(user, u -> finish(),
                                message -> Snackbar.make(mUserName, message, Snackbar.LENGTH_LONG).setAction("OK", null).show());
                    }else{
                        Toast.makeText(PasswordResetActivity.this, "This of username and security answer combination does not exist!",
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private User validate(String username, String asnwer) {
        User user = null;
        for (int i = 0; i < mUsers.size(); i++) {
            if (username.equals(mUsers.get(i).getUsername())) {
                user = mUsers.get(i);
            }
        }
        if (user != null) {
            if (asnwer.equals(user.getSecurityAns())) {
                return user;
            }
        }
        return null;
    }

}