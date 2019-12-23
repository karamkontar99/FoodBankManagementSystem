package com.mobile.foodbank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.mobile.foodbank.R;

public class VolunteerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        configureReceiveDonation();
        configureUpdateStockLevel();
    }

    public void configureReceiveDonation()
    {
        Button acc_don = findViewById( R.id.ReceiveDonation );
        acc_don.setOnClickListener(v -> {
            Intent in = new Intent( VolunteerActivity.this , ReceiveDonations.class );
            startActivity(in);
        });
    }

    public void configureUpdateStockLevel()
    {
        Button upd = findViewById( R.id.UpdateStock );
        upd.setOnClickListener(view -> {
            Intent in = new Intent( VolunteerActivity.this , ListItems.class );
            startActivity(in);
        });

    }
}
