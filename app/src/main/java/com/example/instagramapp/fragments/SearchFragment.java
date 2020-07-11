package com.example.instagramapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.instagramapp.Comment;
import com.example.instagramapp.MainActivity;
import com.example.instagramapp.Post;
import com.example.instagramapp.ProfileActivity;
import com.example.instagramapp.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    public static final String TAG = "SearchFragment";

    public SearchFragment() {
        // Required empty public constructor
    }

    EditText etSearch;
    TextView tvCancel;
    List<Post> posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        posts = new ArrayList<>();

        tvCancel = view.findViewById(R.id.tvCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        queryPosts();
        etSearch = view.findViewById(R.id.etSearch);
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    String searched = etSearch.getText().toString();
                    Log.d(TAG, "searched: " + searched);
                    for (Post post : posts) {
                        Log.d(TAG, "comparing with: " + post.getUser().getUsername());
                        if (post.getUser().getUsername().equals(searched)) {
                            Intent intent = new Intent(getContext(), ProfileActivity.class);
                            intent.putExtra("POST", Parcels.wrap(post));
                            startActivity(intent);
                            return true;
                        }

                }
                return false;
            }
        });
    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.include(Post.KEY_COMMENTS + "." + Comment.KEY_AUTHOR);
        query.include(Post.KEY_LIKEDBY);
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
                posts.clear();
                posts.addAll(retreivedPosts);
            }
        });
    }
}