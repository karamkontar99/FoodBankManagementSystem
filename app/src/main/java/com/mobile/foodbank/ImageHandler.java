package com.mobile.foodbank;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class ImageHandler {

    public static Bitmap decodeImage(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedBitamp = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedBitamp;
    }

    public static String encodeImage(Bitmap decodedBitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        decodedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String encodedBitmap = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return encodedBitmap;
    }
}
