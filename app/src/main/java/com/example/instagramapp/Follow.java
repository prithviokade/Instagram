package com.example.instagramapp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.util.ArrayList;

@ParseClassName("Follow")
public class Follow extends ParseObject {



    public static final String KEY_USER = "user";
    public static final String KEY_FOLLOWING = "Following";
    public static final String KEY_FOLLOWERS = "Followers";

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }

    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }


    public ArrayList<ParseUser> getFollowers() {
        return (ArrayList<ParseUser>) get(KEY_FOLLOWERS);
    }

    public void addFollower(ParseUser user) {
        add(KEY_FOLLOWERS, user);
    }
    public void setFollowers(ArrayList<ParseUser> users) {
        put(KEY_FOLLOWERS, users);
    }


    public ArrayList<ParseUser> getFollowing() {
        return (ArrayList<ParseUser>) get(KEY_FOLLOWING);
    }

    public void addFollowing(ParseUser user) {
        add(KEY_FOLLOWING, user);
    }

    public void setFollowing(ArrayList<ParseUser> users) {
        put(KEY_FOLLOWING, users);
    }

}


