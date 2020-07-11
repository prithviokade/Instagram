package com.example.instagramapp.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramapp.EditProfileActivity;
import com.example.instagramapp.Post;
import com.example.instagramapp.PostsAdapter;
import com.example.instagramapp.ProfilePostsAdapter;
import com.example.instagramapp.R;
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

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    public static final String TAG = "ProfileFragment";

    public ProfileFragment() {
        // Required empty public constructor
    }

    RecyclerView rvPosts;
    ProfilePostsAdapter adapter;
    List<Post> posts;
    ImageView ivProfPic;
    Button btnEditProf;
    TextView tvPostsCount;
    TextView tvBio;
    TextView tvName;
    TextView tvFollowers;
    TextView tvFollowing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);
        ivProfPic = view.findViewById(R.id.ivProfPic);
        btnEditProf = view.findViewById(R.id.btnChangeProf);
        tvPostsCount = view.findViewById(R.id.tvPostsCount);
        tvBio = view.findViewById(R.id.tvBio);
        tvName = view.findViewById(R.id.tvName);
        tvFollowers = view.findViewById(R.id.tvFollowersCount);
        tvFollowing = view.findViewById(R.id.tvFollowingCount);
        posts = new ArrayList<>();
        adapter = new ProfilePostsAdapter(getContext(), posts);

        rvPosts.setAdapter(adapter);
        rvPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));

        queryPosts();

        ParseFile profile = ParseUser.getCurrentUser().getParseFile("Profile");
        if (profile != null) {
            Glide.with(getContext()).load(profile.getUrl()).placeholder(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfPic);
        } else {
            Glide.with(getContext()).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfPic);
        }
        tvPostsCount.setText(Integer.toString(posts.size()));

        btnEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        String name = ParseUser.getCurrentUser().getString("name");
        if (name != null) {
            tvName.setText(name);
        } else {
            tvName.setText("");
        }

        String bio = ParseUser.getCurrentUser().getString("bio");
        if (bio != null) {
            tvBio.setText(bio);
        } else {
            tvBio.setText("");
        }

    }


    protected void queryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> retreivedPosts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while getting posts");
                    return;
                }
                for (Post post : retreivedPosts) {
                    Log.i(TAG, "Post: " + post.getCaption() + " User: " + post.getUser().getUsername());
                }
                posts.addAll(retreivedPosts);
                adapter.notifyDataSetChanged();
                tvPostsCount.setText(Integer.toString(posts.size()));
            }
        });
    }

}
