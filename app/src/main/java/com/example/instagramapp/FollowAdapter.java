package com.example.instagramapp;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.Parse;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    Context context;
    List<ParseUser> users;

    public FollowAdapter(Context context, List<ParseUser> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_follow, parent, false);
        return new FollowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data at position
        ParseUser user = users.get(position);
        // Bind the tweet with the view holder
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvScreenName;
        TextView tvName;

        Button follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfPic);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvName = itemView.findViewById(R.id.tvName);
            follow = itemView.findViewById(R.id.follow);
        }

        public void bind(ParseUser user) {
            tvScreenName.setText(user.getUsername());
            tvName.setText(user.getString("name"));
            ParseFile profile = user.getParseFile("Profile");
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfile);
            } else {
                Glide.with(context).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfile);
            }

            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (follow.getText().toString() == "Follow") {
                        follow.setText("Following");
                        follow.setBackgroundColor(Color.argb(255, 255, 255, 255));
                        follow.setTextColor(Color.argb(255, 29, 161, 242));
                    } else {
                        follow.setText("Follow");
                        follow.setBackgroundColor(Color.argb(255, 29, 161, 242));
                        follow.setTextColor(Color.argb(255, 255, 255, 255));
                    }
                }
            });

        }
    }


}
