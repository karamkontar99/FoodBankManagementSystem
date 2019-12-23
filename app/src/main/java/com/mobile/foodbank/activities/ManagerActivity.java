package com.mobile.foodbank.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.mobile.foodbank.R;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        configureListingUsers();
        configureListingItems();
        configureApprovingDonations();
        configureAddingNewItem();
    }

    private void configureApprovingDonations() {
        Button ApproveDonations = findViewById(R.id.ApproveDonations);
        ApproveDonations.setOnClickListener(view -> {
            Intent in = new Intent(ManagerActivity.this, ApproveDonations.class);
            startActivity(in);
        });
    }


    private void configureListingUsers() {
        Button ListUsers = findViewById(R.id.ListUsers);
        ListUsers.setOnClickListener(view -> {
            Intent in = new Intent(ManagerActivity.this, ListUsers.class);
            startActivity(in);
        });
    }

    private void configureListingItems() {
        Button ListItems = findViewById(R.id.ListItems);
        ListItems.setOnClickListener(view -> {
            Intent in = new Intent(ManagerActivity.this, ListItems.class);
            startActivity(in);
        });
    }

    private void configureAddingNewItem() {
        Button AddItem = findViewById(R.id.AddItem);
        AddItem.setOnClickListener(view -> {
            Intent in = new Intent(ManagerActivity.this, com.mobile.foodbank.activities.AddItem.class);
            startActivity(in);
        });
    }
}
