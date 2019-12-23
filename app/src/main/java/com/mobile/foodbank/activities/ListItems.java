package com.mobile.foodbank.activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.foodbank.R;
import com.mobile.foodbank.models.Item;
import com.mobile.foodbank.repositories.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class ListItems extends AppCompatActivity {

    private ItemsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.recycleview);

        ItemRepository.getAllItems(items -> setConfigItems(recyclerView, searchView, items),
                message ->
                        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).setAction("OK", null).show()
        );
    }


    private void setConfigItems (RecyclerView recyclerView, SearchView searchView, List < Item > items){
        ItemsAdapter itemsAdapter = new ItemsAdapter(items);
        mAdapter = itemsAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(ListItems.this));
        recyclerView.setAdapter(itemsAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                itemsAdapter.mItemsList = new ArrayList<>();
                for (Item item : itemsAdapter.mAllItems)
                    if (query.isEmpty() || item.getItemName().toLowerCase().contains(query.toLowerCase()))
                        itemsAdapter.mItemsList.add(item);
                itemsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemName;
        private TextView mThreshold;
        private TextView mQuantity;
        private TextView mShelf;

        public ItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(ListItems.this).inflate(R.layout.item_list_item, parent, false));
            mItemName = itemView.findViewById(R.id.itemName);
            mThreshold = itemView.findViewById(R.id.threshold);
            mQuantity = itemView.findViewById(R.id.quantity);
            mShelf = itemView.findViewById(R.id.shelf);
        }

        public void bind(Item item) {
            mItemName.setText(item.getItemName());
            mThreshold.setText(String.valueOf(item.getThreshold()));
            mQuantity.setText(item.getQuantity() + " " + item.getQuantityUnit());
            mShelf.setText(item.getShelfNumber());

            if (item.getQuantity() < item.getThreshold()) {
                mQuantity.setTextColor(Color.RED);
                mQuantity.setTypeface(null, Typeface.BOLD);
            }
            else {
                mQuantity.setTextColor(Color.BLACK);
                mQuantity.setTypeface(null, Typeface.NORMAL);
            }


            itemView.setOnClickListener(view -> {

                AlertDialog.Builder builder = new AlertDialog.Builder(ListItems.this);
                builder.setTitle("Update Stock");
                builder.setMessage(item.getItemName() + " (" + item.getQuantityUnit() + ")");

                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setText(String.valueOf(item.getQuantity()));
                builder.setView(input);

                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                    if (input.getText().toString().isEmpty()) {
                        input.setError("cannot be empty");
                        return;
                    }
                    item.setQuantity(Integer.parseInt(input.getText().toString()));
                    ItemRepository.putItem(item, ii -> {
                                mAdapter.notifyDataSetChanged();
                                Toast.makeText(ListItems.this, "stock updated successfully", Toast.LENGTH_SHORT).show();
                            }, message ->
                                    Snackbar.make(itemView, message, Snackbar.LENGTH_LONG).setAction("OK", null).show()
                    );
                });
                builder.show();
            });
        }
    }

    private class ItemsAdapter extends RecyclerView.Adapter<ItemViewHolder> {
        private List<Item> mItemsList;
        private List<Item> mAllItems;


        public ItemsAdapter(List<Item> mItemsList) {
            this.mAllItems = this.mItemsList = mItemsList;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            return new ItemViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, int i) {
            holder.bind(mItemsList.get(i));
        }

        @Override
        public int getItemCount() {
            return mItemsList.size();
        }
    }
}