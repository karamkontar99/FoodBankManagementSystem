package com.mobile.foodbank.repositories;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.foodbank.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    public static void getAllUsers(final DbListeners.OnUsersLoadedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    User user = keyNode.getValue(User.class);
                    users.add(user);
                }
                listener.onUsersLoaded(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorListener.onError(databaseError.getMessage());
            }
        });
    }

    public static void addUser(User user, DbListeners.OnUserAddedListener listener, DbListeners.OnErrorListener errorListener) {
        DatabaseReference reference = getDatabase().child(user.getUsername());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    errorListener.onError("username exists");
                else {
                    reference.setValue(user);
                    listener.onUserAdded(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorListener.onError(databaseError.getMessage());
            }
        });
    }

    public static void putUser(final User user, final DbListeners.OnUserAddedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().child(user.getUsername()).setValue(user)
                .addOnCompleteListener(task -> listener.onUserAdded(user))
                .addOnFailureListener(e -> errorListener.onError(e.getMessage()));
    }

    private static DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference("users");
    }

}
