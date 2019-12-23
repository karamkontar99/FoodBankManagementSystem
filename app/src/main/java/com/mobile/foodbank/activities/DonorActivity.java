package com.mobile.foodbank.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobile.foodbank.ImageHandler;
import com.mobile.foodbank.R;
import com.mobile.foodbank.fcm.FirebaseMessaging;
import com.mobile.foodbank.models.Donation;
import com.mobile.foodbank.repositories.DonationRepository;

import java.util.Date;

public class DonorActivity extends AppCompatActivity {

    private final int CAMERA_PERMISSION_REQUEST = 100;
    private final int CAMERA_REQUEST = 200;

    private EditText itemName;
    private EditText itemQuantity;
    private EditText itemQuantityUnit;
    private ImageView itemImage;

    private boolean imageLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemName = findViewById(R.id.txt_item_name);
        itemQuantity = findViewById(R.id.txt_item_quantity);
        itemQuantityUnit = findViewById(R.id.txt_item_quantityUnit);
        itemImage = findViewById(R.id.img_item_photo);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if (validateInput())
                Snackbar.make(itemName, "Are you sure?", Snackbar.LENGTH_INDEFINITE)
                        .setAction("submit", v -> submitDonation()).show();
        });

        itemImage.setOnClickListener(view ->
                ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                CAMERA_PERMISSION_REQUEST));
    }

    private boolean validateInput() {
        if (itemName.getText().toString().isEmpty()) {
            itemName.setError("cannot be empty");
            return false;
        }
        if (itemQuantity.getText().toString().isEmpty()) {
            itemQuantity.setError("cannot be empty");
            return false;
        }
        if (itemQuantityUnit.getText().toString().isEmpty()) {
            itemQuantityUnit.setError("cannot be empty");
            return false;
        }
        if (!imageLoaded) {
            Toast.makeText(this, "add an image", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void submitDonation() {
        String username = getUsername();

        BitmapDrawable drawable = (BitmapDrawable) itemImage.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        String image = ImageHandler.encodeImage(bitmap);

        String date = new Date().toLocaleString();

        Donation donation = new Donation(itemName.getText().toString(), Integer.parseInt(itemQuantity.getText().toString()), itemQuantityUnit.getText().toString(), username, image, date, "Pending");
        DonationRepository.addDonation(donation,
                d -> {
                    Toast.makeText(this, "donation added successfully", Toast.LENGTH_LONG).show();
                    FirebaseMessaging.updateManagerOnDonation(d);
                },
                message -> Snackbar.make(itemName, message, Snackbar.LENGTH_LONG).setAction("OK", null).show());
    }

    private String getUsername() {
        SharedPreferences prefs = getSharedPreferences(getApplication().getPackageName(), MODE_PRIVATE);
        String username = prefs.getString("username", "");
        return username;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            itemImage.setImageBitmap(photo);
            imageLoaded = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_donor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_location:
                startActivity(new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("geo:0,0?q=" + Uri.encode("Lebanese American University, Beirut Campus"))));
                return true;
            case R.id.action_list:
                startActivity(new Intent(this, ListDonations.class));
                return true;
            default:
                    return super.onContextItemSelected(item);
        }
    }
}
