package com.mobile.foodbank.repositories;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.foodbank.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemRepository {

    public static void getAllItems(final DbListeners.OnItemsLoadedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Item> items = new ArrayList<>();
                for (DataSnapshot keyNode : dataSnapshot.getChildren()) {
                    Item item = keyNode.getValue(Item.class);
                    items.add(item);
                }
                listener.onItemsLoaded(items);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorListener.onError(databaseError.getMessage());
            }
        });
    }

    public static void addItem(final Item item, final DbListeners.OnItemAddedListener listener, final DbListeners.OnErrorListener errorListener) {
        DatabaseReference reference = getDatabase().child(item.getItemName());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    errorListener.onError("item exists");
                else {
                    reference.setValue(item);
                    listener.onItemAdded(item);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                errorListener.onError(databaseError.getMessage());
            }
        });
    }

    public static void putItem(final Item item, final DbListeners.OnItemAddedListener listener, final DbListeners.OnErrorListener errorListener) {
        getDatabase().child(item.getItemName()).setValue(item)
                .addOnCompleteListener(task -> listener.onItemAdded(item))
                .addOnFailureListener(e -> errorListener.onError(e.getMessage()));
    }

    private static DatabaseReference getDatabase() {
        return FirebaseDatabase.getInstance().getReference("items");
    }
}
