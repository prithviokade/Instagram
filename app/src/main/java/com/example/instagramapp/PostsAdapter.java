package com.example.instagramapp;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    Context context;
    List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvScreenName;
        TextView tvCaption;
        ImageView ivPost;
        ImageView ivProfile;
        TextView tvCreated;
        RelativeLayout container;
        ImageView ivLike;
        TextView tvLikes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvCaption = itemView.findViewById(R.id.tvCaption);
            ivPost = itemView.findViewById(R.id.ivPost);
            ivProfile = itemView.findViewById(R.id.ivProfPic);
            tvCreated = itemView.findViewById(R.id.tvCreatedAt);
            container = itemView.findViewById(R.id.container);
            ivLike = itemView.findViewById(R.id.ivLike);
            tvLikes = itemView.findViewById(R.id.tvLikes);
        }

        public void bind(final Post post) {
            tvScreenName.setText(post.getUser().getUsername());
            tvCaption.setText(Html.fromHtml("<b>" + post.getUser().getUsername() + "</b> " + post.getCaption()));
            tvCreated.setText(post.getCreatedAt().toString());
            tvLikes.setText(Integer.toString(post.getLikes()) + " likes");
            ParseFile profile = post.getUser().getParseFile("Profile");
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfile);
            } else {
                Glide.with(context).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfile);
            }
            if (post.getImage() != null) {
                Glide.with(context).load(post.getImage().getUrl()).into(ivPost);
            }

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("POST", Parcels.wrap(post));
                    context.startActivity(intent);
                }
            });

            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    post.setLikes(post.getLikes() + 1);
                    tvLikes.setText(Integer.toString(post.getLikes()) + " Likes");
                    ivLike.setImageResource(R.drawable.ufi_heart_active);
                    savePost(post);
                }
            });
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    private void savePost(Post post) {
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("PostsAdapter", "Error while saving post info", e);
                } else {
                    Log.i("PostsAdapter", "Success saving post info");
                }
            }
        });
    }
}
