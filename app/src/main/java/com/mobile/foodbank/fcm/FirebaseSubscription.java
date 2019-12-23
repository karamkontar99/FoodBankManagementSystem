package com.mobile.foodbank.fcm;

import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseSubscription {

    public static void subscribeDonor(String username) {
        FirebaseMessaging.getInstance().subscribeToTopic("donor_" + username);
    }

    public static void subscribeManager() {
        FirebaseMessaging.getInstance().subscribeToTopic("manager");
    }
}
