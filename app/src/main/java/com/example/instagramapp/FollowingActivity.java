package com.example.instagramapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class FollowingActivity extends AppCompatActivity {

    Post post;

    RecyclerView rvFollowing;
    FollowAdapter adapter;
    List<ParseUser> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("POST"));
        rvFollowing = findViewById(R.id.rvFollowing);
        users = new ArrayList<>();
        adapter = new FollowAdapter(this, users);
        rvFollowing.setLayoutManager(new LinearLayoutManager(this));
        rvFollowing.setAdapter(adapter);
        populateFollowing();


    }

    private void populateFollowing() {
        ArrayList<ParseUser> followingToAdd = (ArrayList<ParseUser>) post.getUser().get("Following");
        users.clear();
        users.addAll(followingToAdd);
        adapter.notifyDataSetChanged();
    }
}