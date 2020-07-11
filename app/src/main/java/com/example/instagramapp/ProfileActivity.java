package com.example.instagramapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    TextView tvFollowers;
    TextView tvFollowing;
    Button btnFollow;
    List<Follow> follow;
    Follow currUserFollow;
    Follow profileUserFollow;
    TextView tvName;
    TextView tvBio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_profile_other);
        TextView tvUsername = findViewById(R.id.tvUsername);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));


        post = (Post) Parcels.unwrap(getIntent().getParcelableExtra("POST"));
        tvUsername.setText(post.getUser().getUsername());

        rvPosts = findViewById(R.id.rvPosts);
        ivProfPic = findViewById(R.id.ivProfPic);
        btnChangeProf = findViewById(R.id.btnChangeProf);
        tvPostsCount = findViewById(R.id.tvPostsCount);
        tvFollowers = findViewById(R.id.tvFollowersCount);
        tvFollowing = findViewById(R.id.tvFollowingCount);
        btnFollow = findViewById(R.id.btnFollow);
        tvBio = findViewById(R.id.tvBio);
        tvName = findViewById(R.id.tvName);
        posts = new ArrayList<>();
        follow = new ArrayList<>();
        adapter = new ProfilePostsAdapter(this, posts);

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new GridLayoutManager(this, 3));
        // queryFollows();
        // profileUserFollow = findUserFollow(follow, post.getUser().getUsername());
        // ArrayList<ParseUser> profileUserFollowFollowers = profileUserFollow.getFollowers();
        // tvFollowers.setText(Integer.toString(profileUserFollowFollowers.size()));
        tvFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FollowersActivity.class);
                intent.putExtra("POST", Parcels.wrap(post));
                startActivity(intent);
            }
        });

        // ArrayList<ParseUser> profileUserFollowFollowing = profileUserFollow.getFollowing();
        // tvFollowing.setText(Integer.toString(profileUserFollowFollowing.size()));
        tvFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FollowingActivity.class);
                intent.putExtra("POST", Parcels.wrap(post));
                startActivity(intent);
            }
        });
        // currUserFollow = findUserFollow(follow, ParseUser.getCurrentUser().getUsername());
        // ArrayList<ParseUser> currUserFollowing = (ArrayList<ParseUser>) currUserFollow.get("Following");
        // if (userContained(currUserFollowing, post.getUser().getUsername())) {
            btnFollow.setText("Following");
            btnFollow.setBackgroundColor(0xffffffff);
            btnFollow.setTextColor(0xff000000);
            /*
        } else {
            btnFollow.setText("Follow");
            btnFollow.setBackgroundColor(0xFF00A6FF);
            btnFollow.setTextColor(0xffffffff);
        }

         */

        String name = post.getUser().getString("name");
        if (name != null) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        String bio = post.getUser().getString("bio");
        if (bio != null) {
            tvBio.setText(bio);
        } else {
            tvBio.setText("");
        }

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnFollow.getText().equals("Following")) {
                    // unfollowed
                    btnFollow.setText("Follow");
                    btnFollow.setBackgroundColor(0xFF00A6FF);
                    btnFollow.setTextColor(0xffffffff);

                    /*
                    ArrayList<ParseUser> currUserFollowing = currUserFollow.getFollowing();
                    currUserFollowing.remove(post.getUser());
                    currUserFollow.setFollowing(currUserFollowing);
                    currUserFollow.saveInBackground();

                    ArrayList<ParseUser> profileUserFollowFollowers = profileUserFollow.getFollowers();
                    profileUserFollowFollowers.remove(ParseUser.getCurrentUser());
                    profileUserFollow.setFollowers(profileUserFollowFollowers);
                    profileUserFollow.saveInBackground();

                     */

                } else {
                    // followed
                    btnFollow.setText("Following");
                    btnFollow.setBackgroundColor(0xFFFFFFFF);
                    btnFollow.setTextColor(0xff000000);
                    /*
                    currUserFollow.addFollowing(post.getUser());
                    profileUserFollow.addFollower(ParseUser.getCurrentUser());
                    profileUserFollow.saveInBackground();
                    currUserFollow.saveInBackground();

                     */
                    }

            }
        });

        queryPosts();

        ParseFile profile = post.getUser().getParseFile("Profile");
        if (profile != null){
            Glide.with(this).load(profile.getUrl()).placeholder(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfPic);
        } else {
            Glide.with(this).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfPic);
        }
        tvPostsCount.setText(Integer.toString(posts.size()));
        }

    private Follow findUserFollow(List<Follow> follow, String username) {
        for (Follow f : follow) {
            Log.d("ffff", f.getUser().getUsername());
            if (f.getUser().getUsername().equals(username)) {
                return f;
            }
        }
        return null;
    }

    private void saveUser(ParseUser user) {
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("PostsAdapter", "Error while saving user ingo", e);
                } else {
                    Log.i("PostsAdapter", "Success saving user info");
                }
            }
        });
    }

    public boolean userContained(ArrayList<ParseUser> allUsers, String username) {
        for (ParseUser user : allUsers) {
            if (username.equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }
/*
    protected void queryFollows() {
        // Specify which class to query
        ParseQuery<Follow> query = ParseQuery.getQuery(Follow.class);
        query.include(Follow.KEY_USER);
        query.include(Follow.KEY_USER + "." + Follow.KEY_USER);
        query.findInBackground(new FindCallback<Follow>() {
            @Override
            public void done(List<Follow> retreivedFollowInfo, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while getting foll info");
                    return;
                }
                Log.d("whyyy", Integer.toString(retreivedFollowInfo.size()));
                for (Follow follow : retreivedFollowInfo) {
                    Log.i(TAG, "User: " + follow.getUser().getUsername());
                }
                follow.clear();
                follow.addAll(retreivedFollowInfo);
            }
        });
    }

 */

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