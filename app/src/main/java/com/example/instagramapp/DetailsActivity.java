package com.example.instagramapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class DetailsActivity extends AppCompatActivity {

    TextView tvScreenName;
    TextView tvCaption;
    ImageView ivPost;
    ImageView ivProfile;
    TextView tvCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvScreenName = findViewById(R.id.tvScreenName);
        tvCaption = findViewById(R.id.tvCaption);
        ivPost = findViewById(R.id.ivPost);
        ivProfile = findViewById(R.id.ivProfPic);
        tvCreated = findViewById(R.id.tvCreatedAt);

        Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("POST"));

        tvScreenName.setText(post.getUser().getUsername());
        tvCaption.setText(post.getCaption());
        tvCreated.setText(post.getCreatedAt().toString());
        ParseFile profile = post.getUser().getParseFile("Profile");
        if (profile != null) {
            Glide.with(this).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfile);
        }
        if (post.getImage() != null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivPost);
        }

    }
}