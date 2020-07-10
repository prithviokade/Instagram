package com.example.instagramapp;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_CONTENT = "Content";
    public static final String KEY_AUTHOR = "Author";

    public String getContent() {
        try {
            return fetchIfNeeded().getString(KEY_CONTENT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    public ParseUser getAuthor() {
        try {
            return fetchIfNeeded().getParseUser(KEY_AUTHOR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setAuthor(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

    public int getLikes() {
        try {
            return fetchIfNeeded().getInt("Likes");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

        public void setLikes(int likes) { put("Likes", likes); }

}

