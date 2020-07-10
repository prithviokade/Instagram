package com.example.instagramapp;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;

import org.w3c.dom.Text;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<com.example.instagramapp.CommentsAdapter.ViewHolder>{

    Context context;
    List<Comment> comments;

    public CommentsAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvContent;
        ImageView ivProfPic;
        TextView tvLikes;
        ImageView ivLike;
        TextView tvReply;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivProfPic  = itemView.findViewById(R.id.ivProfPic);
            tvLikes  = itemView.findViewById(R.id.tvLikes);
            ivLike  = itemView.findViewById(R.id.ivLike);
            tvReply = itemView.findViewById(R.id.tvReply);
        }

        public void bind(Comment comment) {
            tvContent.setText(Html.fromHtml("<b>" + comment.getAuthor().getUsername() + "</b> " + comment.getContent()));

            tvLikes.setText(Integer.toString(comment.getLikes()) + "likes");
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TO DO
                }
            });

            ParseFile profile = comment.getAuthor().getParseFile("Profile");
            if (profile != null) {
                Glide.with(context).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfPic);
            } else {
                Glide.with(context).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfPic);
            }

        }
    }

}
