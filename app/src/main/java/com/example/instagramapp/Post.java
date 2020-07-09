package com.example.instagramapp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel(analyze={Post.class})
@ParseClassName("Post")
public class Post extends ParseObject {

    // empty constructor needed by the Parceler library
    public Post() { }

    public static final String KEY_CAPTION = "caption";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_LIKES = "Likes";
    public static final String KEY_LIKEDBY = "LikedBy";
    public static final String KEY_COMMENTS = "Comments";

    public String getCaption() {
        return getString(KEY_CAPTION);
    }

    public void setCaption(String caption) {
        put(KEY_CAPTION, caption);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile image) {
        put(KEY_IMAGE, image);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

    public int getLikes() {return getInt(KEY_LIKES); }

    public void setLikes(int likes) { put(KEY_LIKES, likes); }

    public ArrayList<ParseUser> getLikedBy() {
        return (ArrayList<ParseUser>) get(KEY_LIKEDBY);
    }

    public void setLikedBy(ArrayList<ParseUser> users) {
        put(KEY_LIKEDBY, users);
    }

    public void addComments(ParseUser user) {
        add(KEY_LIKEDBY, user);
    }
    
    public ArrayList<Comment> getComments() { return (ArrayList<Comment>) get(KEY_COMMENTS); }

    public void setComments(ArrayList<Comment> comments) {
        put(KEY_COMMENTS, comments);
    }

    public void addComments(Comment comment) {
        add(KEY_COMMENTS, comment);
    }
}

