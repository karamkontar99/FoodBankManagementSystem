package com.mobile.foodbank.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.mobile.foodbank.R;
import com.mobile.foodbank.models.Item;
import com.mobile.foodbank.repositories.ItemRepository;

public class AddItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Button AddItem = findViewById(R.id.AddingNewItem);
        AddItem.setOnClickListener(view -> {
            EditText ItemName = findViewById(R.id.ItemName);
            EditText Shelf_Id = findViewById(R.id.Shelf_Id);
            EditText Quantity = findViewById(R.id.Quantity);
            EditText Threshold = findViewById(R.id.Threshold);
            EditText QuantityUnit = findViewById(R.id.QuantityUnit);

            if (ItemName.getText().toString().isEmpty()) {
                ItemName.setError("cannot be empty");
                return;
            }
            if (Shelf_Id.getText().toString().isEmpty()) {
                Shelf_Id.setError("cannot be empty");
                return;
            }
            if (Quantity.getText().toString().isEmpty()) {
                Quantity.setError("cannot be empty");
                return;
            }
            if (QuantityUnit.getText().toString().isEmpty()) {
                QuantityUnit.setError("cannot be empty");
                return;
            }
            if (Threshold.getText().toString().isEmpty()) {
                Threshold.setError("cannot be empty");
                return;
            }

            String itemName = ItemName.getText().toString();
            String shelfLocation = Shelf_Id.getText().toString();
            int quantity = Integer.parseInt(Quantity.getText().toString());
            int threshold = Integer.parseInt(Threshold.getText().toString());
            String quantityUnit = QuantityUnit.getText().toString();
            Item item = new Item(itemName, shelfLocation, threshold, quantity, quantityUnit);
            ItemRepository.addItem(item,
                    i -> finish(),
                    message ->
                            Snackbar.make(ItemName, message, Snackbar.LENGTH_LONG).setAction("OK", null).show()
            );
        });

    }
}
