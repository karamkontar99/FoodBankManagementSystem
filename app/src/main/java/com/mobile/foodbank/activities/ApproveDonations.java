package com.mobile.foodbank.activities;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.foodbank.ImageHandler;
import com.mobile.foodbank.R;
import com.mobile.foodbank.fcm.FirebaseMessaging;
import com.mobile.foodbank.models.Donation;
import com.mobile.foodbank.repositories.DonationRepository;

import java.util.ArrayList;
import java.util.List;

public class ApproveDonations extends AppCompatActivity {

    private DonationsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.recycleview);

        DonationRepository.getPendingDonations(donations -> setConfigDonations(recyclerView, searchView, donations), message ->
                Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).setAction("OK", null).show()
        );
    }

    private void setConfigDonations(RecyclerView recyclerView, SearchView searchView, List<Donation> donations) {
        DonationsAdapter donationsAdapter = new DonationsAdapter(donations);
        mAdapter = donationsAdapter;
        recyclerView.setLayoutManager(new LinearLayoutManager(ApproveDonations.this));
        recyclerView.setAdapter(donationsAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                donationsAdapter.mDonationsList = new ArrayList<>();
                for (Donation donation : donationsAdapter.mAllDonations)
                    if (query.isEmpty() || donation.getItemName().toLowerCase().contains(query.toLowerCase()))
                        donationsAdapter.mDonationsList.add(donation);
                donationsAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private class DonationViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemName;
        private TextView mDate;
        private TextView mStatus;
        private TextView mQuantity;
        private ImageView mImage;

        public DonationViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(ApproveDonations.this).inflate(R.layout.donation_list_item, parent, false));

            mItemName = itemView.findViewById(R.id.itemName);
            mStatus = itemView.findViewById(R.id.status);
            mQuantity = itemView.findViewById(R.id.quantity);
            mDate = itemView.findViewById(R.id.date);
            mImage = itemView.findViewById(R.id.itemImage);
        }

        public void bind(Donation donation) {
            mItemName.setText(donation.getItemName());
            mDate.setText(donation.getDate());
            mQuantity.setText(donation.getQuantity() + " " + donation.getQuantityUnit());
            mStatus.setText(donation.getStatus());
            mImage.setImageBitmap(ImageHandler.decodeImage(donation.getImage()));

            itemView.setOnClickListener(view -> {
                new AlertDialog.Builder(ApproveDonations.this)
                        .setTitle("Approve donation?")
                        .setMessage(donation.getKey() + "   " + donation.getItemName() + "\n" + donation.getQuantity() + " " + donation.getQuantityUnit() + "\n" + donation.getDate() + "\n" + donation.getDonorUsername())
                        .setIcon(new BitmapDrawable(getResources(), ImageHandler.decodeImage(donation.getImage())))
                        .setPositiveButton("Accept", (dialog, which) -> {
                            dialog.cancel();
                            donation.setStatus("Accepted");
                            DonationRepository.putDonation(donation, d -> {
                                        Toast.makeText(ApproveDonations.this, "status updated", Toast.LENGTH_SHORT).show();
                                        mAdapter.mAllDonations.remove(donation);
                                        mAdapter.notifyDataSetChanged();
                                        FirebaseMessaging.updateDonorOnDonation(d);
                                    },
                                    message ->
                                            Snackbar.make(itemView, message, Snackbar.LENGTH_LONG).setAction("OK", null).show());
                        })
                        .setNegativeButton("Reject", (dialog, which) -> {
                            dialog.cancel();
                            donation.setStatus("Rejected");
                            DonationRepository.putDonation(donation, d -> {
                                        Toast.makeText(ApproveDonations.this, "status updated", Toast.LENGTH_SHORT).show();
                                        mAdapter.mAllDonations.remove(donation);
                                        mAdapter.notifyDataSetChanged();
                                        FirebaseMessaging.updateDonorOnDonation(d);
                                    },
                                    message ->
                                            Snackbar.make(itemView, message, Snackbar.LENGTH_LONG).setAction("OK", null).show());
                        })
                        .show();
            });
        }
    }

    private class DonationsAdapter extends RecyclerView.Adapter<DonationViewHolder> {
        private List<Donation> mDonationsList;
        private List<Donation> mAllDonations;


        public DonationsAdapter(List<Donation> mDonationsList) {
            this.mAllDonations = this.mDonationsList = mDonationsList;
        }

        @Override
        public DonationViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            return new DonationViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(DonationViewHolder holder, int i) {
            holder.bind(mDonationsList.get(i));
        }

        @Override
        public int getItemCount() {
            return mDonationsList.size();
        }
    }

}
