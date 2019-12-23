package com.mobile.foodbank.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobile.foodbank.R;
import com.mobile.foodbank.models.User;
import com.mobile.foodbank.repositories.UserRepository;

import java.util.Calendar;
import java.util.regex.Pattern;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getName();

    private TextView mDisplayDate, mName, mUsername, mPassword, mEmail, mSec_ans;
    private RadioGroup mRole;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public String name, username, password, email, role, dob, security_answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mDisplayDate = findViewById(R.id.txt_dob);
        mDisplayDate.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    SignUpActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        mDateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            Log.d(TAG, "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

            dob = month + "/" + day + "/" + year;
            mDisplayDate.setText(dob);
        };

        Spinner spinner = findViewById(R.id.spinner_sec_quest);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.securityQues, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
        spinner.setAdapter(adapter);

        configureButton();
    }

    private String getRole() {
        mRole = findViewById(R.id.rg_roles);
        RadioButton radio = findViewById(mRole.getCheckedRadioButtonId());
        return radio.getText().toString();
    }

    private void configureButton() {
        Button btn = findViewById(R.id.btn_sign_up);
        btn.setOnClickListener(view -> {
            int ok = 0;

            mName = findViewById(R.id.txt_name);
            mUsername = findViewById(R.id.txt_username);
            mEmail = findViewById(R.id.txt_email);
            mPassword = findViewById(R.id.txt_password);
            mSec_ans = findViewById(R.id.txt_sec_quest_ans);

            name = mName.getText().toString();
            username = mUsername.getText().toString();
            email = mEmail.getText().toString();
            password = mPassword.getText().toString();
            dob = mDisplayDate.getText().toString();
            security_answer = mSec_ans.getText().toString();

            //checking for filling all the fields by the user
            String role = getRole();

            if (TextUtils.isEmpty(mName.getText())) {
                mName.setError("Name is required!");
                ok++;
            }
            if (TextUtils.isEmpty(mPassword.getText())) {
                mPassword.setError("Password is required!");
                ok++;
            }
            if (TextUtils.isEmpty(mUsername.getText())) {
                mUsername.setError("Username is required!");
                ok++;
            }
            if (TextUtils.isEmpty(mEmail.getText())) {
                mEmail.setError("Email is required!");
                ok++;
            }

            if (TextUtils.isEmpty(mDisplayDate.getText())) {
                mDisplayDate.setError("Date Of Birth is required!");
                ok++;
            }
            if (TextUtils.isEmpty(mSec_ans.getText())) {
                mSec_ans.setError("you have to answer the security question.");
            }

            if (password.length() < 8) {
                mPassword.setError("password should be at least 8 characters.");
                ok++;
            }

            boolean fl = Pattern.matches("[a-zA-Z]+(.*)(@)[a-zA-Z]+(.*)(\\.)[a-zA-Z]+(.*)", email);
            if (!fl) {
                mEmail.setError("please enter a valid email address.");
                ok++;
            }


            if (ok == 0) {
                User user = new User(name, dob, email, username, password, role, security_answer);
                UserRepository.addUser(user, u -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)),
                        message -> Snackbar.make(mName, message, Snackbar.LENGTH_LONG).setAction("OK", null).show());
            }
        });
    }
}