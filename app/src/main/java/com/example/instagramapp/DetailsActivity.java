package com.example.instagramapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramapp.fragments.ProfileFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;

public class DetailsActivity extends AppCompatActivity {

    public static final String TAG = "DetailsActivity";

    TextView tvScreenName;
    TextView tvCaption;
    ImageView ivPost;
    ImageView ivProfile;
    TextView tvCreated;
    TextView tvLikes;
    ImageView ivLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_post);

        tvScreenName = findViewById(R.id.tvScreenName);
        tvCaption = findViewById(R.id.tvCaption);
        ivPost = findViewById(R.id.ivPost);
        ivProfile = findViewById(R.id.ivProfPic);
        tvCreated = findViewById(R.id.tvCreatedAt);
        tvLikes = findViewById(R.id.tvLikes);
        ivLike = findViewById(R.id.ivLike);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        final Post post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("POST"));
        final int likes = post.getLikes();
        tvLikes.setText(Integer.toString(likes) + " likes");
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post.setLikes(likes + 1);
                savePost(post);
                tvLikes.setText(Integer.toString(post.getLikes()) + " Likes");
                ivLike.setImageResource(R.drawable.ufi_heart_active);
            }
        });
        tvScreenName.setText(post.getUser().getUsername());
        tvScreenName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailsActivity.this, ProfileActivity.class);
                intent.putExtra("POST", Parcels.wrap(post));
                startActivity(intent);
            }
        });
        tvCaption.setText(Html.fromHtml("<b>" + post.getUser().getUsername() + "</b> " + post.getCaption()));
        tvCreated.setText(post.getCreatedAt().toString());
        ParseFile profile = post.getUser().getParseFile("Profile");
        if (profile != null) {
            Glide.with(this).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfile);
        } else {
            Glide.with(this).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfile);
        }
        if (post.getImage() != null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivPost);
        }

    }

    private void savePost(Post post) {
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving post info", e);
                } else {
                    Log.i(TAG, "Success saving post info");
                }
            }
        });
    }
}