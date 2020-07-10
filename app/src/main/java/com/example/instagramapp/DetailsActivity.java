package com.example.instagramapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.instagramapp.fragments.ReplyFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity implements ReplyFragment.ReplyFragmentListener {

    public static final String TAG = "DetailsActivity";

    TextView tvScreenName;
    TextView tvCaption;
    ImageView ivPost;
    ImageView ivProfile;
    TextView tvCreated;
    TextView tvLikes;
    ImageView ivLike;
    RecyclerView rvComments;
    CommentsAdapter adapter;
    ArrayList<Comment> comments;
    ImageView ivComment;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        comments = new ArrayList<>();
        tvScreenName = findViewById(R.id.tvScreenName);
        tvCaption = findViewById(R.id.tvCaption);
        ivPost = findViewById(R.id.ivPost);
        ivProfile = findViewById(R.id.ivProfPic);
        tvCreated = findViewById(R.id.tvCreatedAt);
        tvLikes = findViewById(R.id.tvLikes);
        ivLike = findViewById(R.id.ivLike);
        rvComments = findViewById(R.id.rvComments);
        ivComment = findViewById(R.id.ivComment);
        adapter = new CommentsAdapter(this, comments);

        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("POST"));

        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        queryComments();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        final int likes = post.getLikes();
        tvLikes.setText(Integer.toString(likes) + " likes");
        if (userContained(post)) {
            ivLike.setImageResource(R.drawable.ufi_heart_active);
        }

        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userContained(post)) {
                    post.setLikes(post.getLikes() + 1);
                    post.addLikedBy(ParseUser.getCurrentUser());
                    tvLikes.setText(Integer.toString(post.getLikes()) + " likes");
                    ivLike.setImageResource(R.drawable.ufi_heart_active);
                    savePost(post);
                }
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

        ivComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReplyDialog();
            }
        });

    }

    public boolean userContained(Post post) {
        for (ParseUser user: post.getLikedBy()) {
            if (ParseUser.getCurrentUser().getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private void showReplyDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ReplyFragment replyFragment = ReplyFragment.newInstance();
        replyFragment.show(fm, "fragment_reply");
    }

    private void queryComments() {
        ArrayList<Comment> commentsFromDatabase = (ArrayList<Comment>) post.getComments();
        comments.clear();
        comments.addAll(commentsFromDatabase);
        adapter.notifyDataSetChanged();
    }

    public void onActivityResult(String content) {
        Comment finComment = new Comment();
        finComment.setContent(content);
        finComment.setAuthor(ParseUser.getCurrentUser());
        finComment.setLikes(0);
        saveComment(finComment);
        comments.add(finComment);
        adapter.notifyItemInserted(comments.size() - 1);
        post.setComments(comments);
        savePost(post);
    }

    private void saveComment(Comment comment) {
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving comment info", e);
                } else {
                    Log.i(TAG, "Success saving comment info");
                }
            }
        });
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