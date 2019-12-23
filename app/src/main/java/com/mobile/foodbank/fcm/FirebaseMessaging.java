package com.mobile.foodbank.fcm;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;
import com.mobile.foodbank.models.Donation;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessaging extends FirebaseMessagingService {
	
	// TODO replace with actual API key from Firebase project
	private final String API_KEY = "your api key";

    public static void updateManagerOnDonation(final Donation donation) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body = RequestBody.create(mediaType, new Gson().toJson(getManagerMessage(donation)));

                Request request = new Request.Builder()
                        .url("https://fcm.googleapis.com/fcm/send")
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + API_KEY)
                        .addHeader("cache-control", "no-cache")
                        .build();

                client.newCall(request).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void updateDonorOnDonation(final Donation donation) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");

                RequestBody body = RequestBody.create(mediaType, new Gson().toJson(getDonorMessage(donation)));

                Request request = new Request.Builder()
                        .url("https://fcm.googleapis.com/fcm/send")
                        .post(body)
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + API_KEY)
                        .addHeader("cache-control", "no-cache")
                        .build();

                client.newCall(request).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static Map<String,Object> getDonorMessage(final Donation donation) {
        Map<String,Object> payload = new HashMap<>();
        payload.put("title", "Donation status: " + donation.getStatus());
        payload.put("body", donation.getItemName() + " - " + donation.getQuantity() + " " + donation.getQuantityUnit());

        Map<String,Object> message = new HashMap<>();
        message.put("to", "/topics/donor_" + donation.getDonorUsername());
        message.put("notification", payload);

        return message;
    }

    private static Map<String,Object> getManagerMessage(final Donation donation) {
        Map<String,Object> payload = new HashMap<>();
        payload.put("title", "Donation from " + donation.getDonorUsername());
        payload.put("body", donation.getItemName() + " - " + donation.getQuantity() + " " + donation.getQuantityUnit());

        Map<String,Object> message = new HashMap<>();
        message.put("to", "/topics/manager");
        message.put("notification", payload);

        return message;
    }
}
