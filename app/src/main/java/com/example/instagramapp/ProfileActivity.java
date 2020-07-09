package com.example.instagramapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    RecyclerView rvPosts;
    Post post;
    ProfilePostsAdapter adapter;
    public static final String TAG = "ProfileActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 112;
    public String photoFileName = "photo.jpg";
    File photoFile;
    List<Post> posts;
    ImageView ivProfPic;
    Button btnChangeProf;
    TextView tvPostsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_profile_other);
        TextView tvUsername = findViewById(R.id.tvUsername);
        Button btnLogout = findViewById(R.id.btnLogout);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));


        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("POST"));
        tvUsername.setText(post.getUser().getUsername());

        rvPosts = findViewById(R.id.rvPosts);
        ivProfPic = findViewById(R.id.ivProfPic);
        btnChangeProf = findViewById(R.id.btnChangeProf);
        tvPostsCount = findViewById(R.id.tvPostsCount);
        posts = new ArrayList<>();
        adapter = new ProfilePostsAdapter(this, posts);

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new GridLayoutManager(this, 3));

        queryPosts();

        ParseFile profile = post.getUser().getParseFile("Profile");
        if (profile != null){
            Glide.with(this).load(profile.getUrl()).placeholder(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfPic);
        } else {
            Glide.with(this).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfPic);
        }
        tvPostsCount.setText(Integer.toString(posts.size()));
        }

    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, post.getUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> retreivedPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while getting posts");
                    return;
                }
                for (Post postt : retreivedPosts) {
                    Log.i(TAG, "Post: " + postt.getCaption() + " User: " + postt.getUser().getUsername());
                }
                posts.addAll(retreivedPosts);
                adapter.notifyDataSetChanged();
                tvPostsCount.setText(Integer.toString(posts.size()));
            }
        });
    }
}