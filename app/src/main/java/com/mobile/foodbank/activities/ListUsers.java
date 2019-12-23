package com.mobile.foodbank.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.mobile.foodbank.R;
import com.mobile.foodbank.models.User;
import com.mobile.foodbank.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class ListUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        SearchView searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.recycleview);

        UserRepository.getAllUsers(users -> setConfigUsers(recyclerView, searchView, users), message ->
                Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).setAction("OK", null).show()
        );
    }

    private void setConfigUsers(RecyclerView recyclerView, SearchView searchView, List<User> users) {
        UsersAdapter usersAdapter = new UsersAdapter(users);
        recyclerView.setLayoutManager(new LinearLayoutManager(ListUsers.this));
        recyclerView.setAdapter(usersAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                usersAdapter.mUserList = new ArrayList<>();
                for (User user : usersAdapter.mAllUsers)
                    if (query.isEmpty() || user.getName().toLowerCase().contains(query.toLowerCase()))
                        usersAdapter.mUserList.add(user);
                usersAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }


    private class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView mUsername;
        private TextView mName;
        private TextView mRole;
        private TextView mEmail;

        public UserViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(ListUsers.this).inflate(R.layout.user_list_item, parent, false));

            mUsername = itemView.findViewById(R.id.txt_username);
            mName = itemView.findViewById(R.id.txt_name);
            mRole = itemView.findViewById(R.id.role);
            mEmail = itemView.findViewById(R.id.email);
        }

        public void bind(User user) {
            mUsername.setText(user.getUsername());
            mName.setText(user.getName());
            mRole.setText(user.getRole());
            mEmail.setText(user.getEmail());
        }
    }

    private class UsersAdapter extends RecyclerView.Adapter<UserViewHolder> {
        private List<User> mUserList;
        private List<User> mAllUsers;

        public UsersAdapter(List<User> mUserList) {
            this.mAllUsers = this.mUserList = mUserList;
        }

        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            return new UserViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int i) {
            holder.bind(mUserList.get(i));
        }

        @Override
        public int getItemCount() {
            return mUserList.size();
        }
    }
}
