package com.mobile.foodbank.repositories;

import com.mobile.foodbank.models.Donation;
import com.mobile.foodbank.models.Item;
import com.mobile.foodbank.models.User;

import java.util.List;

public class DbListeners {

    public interface OnItemsLoadedListener {
        void onItemsLoaded(List<Item> items);
    }

    public interface OnUsersLoadedListener {
        void onUsersLoaded(List<User> users);
    }

    public interface OnDonationsLoadedListener {
        void onDonationsLoaded(List<Donation> donations);
    }

    public interface OnUserAddedListener {
        void onUserAdded(User user);
    }

    public interface OnItemAddedListener {
        void onItemAdded(Item item);
    }

    public interface OnDonationAddedListener {
        void onDonationAdded(Donation donation);
    }

    public interface OnDonationAcceptedListener {
        void onDonationAccepted(Donation donation);
    }

    public interface OnErrorListener {
        void onError(String message);
    }
}
