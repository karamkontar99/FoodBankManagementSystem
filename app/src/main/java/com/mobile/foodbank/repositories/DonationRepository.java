package com.mobile.foodbank.repositories;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.foodbank.models.Donation;
import com.mobile.foodbank.repositories.DbListeners.OnDonationsLoadedListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DonationRepository {

    public static void getAllDonations(final OnDonationsLoadedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Donation> donations = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Donation donation = keyNode.getValue(Donation.class);
                    donation.setKey(keyNode.getKey());
                    donations.add(donation);
                }
                listener.onDonationsLoaded(donations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorListener.onError(databaseError.getMessage());
            }
        });
    }

    public static void getDonations(final String username, final OnDonationsLoadedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Donation> donations = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Donation donation = keyNode.getValue(Donation.class);
                    donation.setKey(keyNode.getKey());
                    if (donation.getDonorUsername().equals(username))
                        donations.add(donation);
                }
                // put the pending ones first
                Collections.sort(donations, (a, b) -> {
                    if (a.getStatus().equals(b.getStatus()))
                        return 0;
                    if (a.getStatus().equals("Pending"))
                        return -1;
                    if (b.getStatus().equals("Pending"))
                        return 1;
                    if (a.getStatus().equals("Accepted"))
                        return -1;
                    if (b.getStatus().equals("Accepted"))
                        return 1;
                    return 0;
                });
                listener.onDonationsLoaded(donations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorListener.onError(databaseError.getMessage());
            }
        });
    }

    public static void getPendingDonations(final OnDonationsLoadedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Donation> donations = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Donation donation = keyNode.getValue(Donation.class);
                    donation.setKey(keyNode.getKey());
                    if (donation.getStatus().equals("Pending"))
                        donations.add(donation);
                }
                listener.onDonationsLoaded(donations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorListener.onError(databaseError.getMessage());
            }
        });
    }

    public static void getAcceptedDonations(final OnDonationsLoadedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Donation> donations = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Donation donation = keyNode.getValue(Donation.class);
                    donation.setKey(keyNode.getKey());
                    if (donation.getStatus().equals("Accepted"))
                        donations.add(donation);
                }
                listener.onDonationsLoaded(donations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorListener.onError(databaseError.getMessage());
            }
        });
    }

    public static void addDonation(final Donation donation, final DbListeners.OnDonationAddedListener listener, final DbListeners.OnErrorListener errorListener) {
        DatabaseReference reference = getDatabase().push();
        donation.setKey(reference.getKey());
        reference.setValue(donation)
                .addOnCompleteListener(task -> listener.onDonationAdded(donation))
                .addOnFailureListener(e -> errorListener.onError(e.getMessage()));
    }

    public static void putDonation(final Donation donation, final DbListeners.OnDonationAddedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().child(donation.getKey()).setValue(donation)
                .addOnCompleteListener(task -> listener.onDonationAdded(donation))
                .addOnFailureListener(e -> errorListener.onError(e.getMessage()));
    }

    private static DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference("donations");
    }
}
