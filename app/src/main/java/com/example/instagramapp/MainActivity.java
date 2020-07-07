package com.example.instagramapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    EditText etCaption;
    Button btnCamera;
    Button btnPost;
    ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCaption = findViewById(R.id.etCaption);
        btnCamera = findViewById(R.id.btnCamera);
        btnPost = findViewById(R.id.btnPost);
        ivPhoto = findViewById(R.id.ivPhoto);

        queryPosts();
    }

    private void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while getting posts");
                    return;
                }
                 for (Post post : posts) {
                     Log.i(TAG, "Post" + post.getCaption());
                 }
            }
        });

    }
}