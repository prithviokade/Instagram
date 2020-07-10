package com.example.instagramapp.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.instagramapp.Comment;
import com.example.instagramapp.R;
import com.parse.ParseFile;
import com.parse.ParseUser;


public class ReplyFragment extends DialogFragment {

    EditText etComment;
    ImageView ivProf;
    TextView tvPost;

    public ReplyFragment() {
        // Empty constructor is required for DialogFragment
    }

    public interface ReplyFragmentListener {
        void onActivityResult(String content);
    }

    ReplyFragmentListener listener;

    public static ReplyFragment newInstance() {
        ReplyFragment frag = new ReplyFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reply, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        etComment = view.findViewById(R.id.etComment);
        // Show soft keyboard automatically and request focus to field
        etComment.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        ivProf = view.findViewById(R.id.ivProf);
        ParseFile profile = ParseUser.getCurrentUser().getParseFile("Profile");
        if (profile != null) {
            Glide.with(getContext()).load(profile.getUrl()).placeholder(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProf);
        } else {
            Glide.with(getContext()).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProf);
        }

        tvPost = view.findViewById(R.id.tvPost);
        tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etComment.getText().toString();
                listener = (ReplyFragmentListener) getActivity();
                listener.onActivityResult(content);
                dismiss();
            }
        });


    }


}
