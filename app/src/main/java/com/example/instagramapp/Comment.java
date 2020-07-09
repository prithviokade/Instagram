package com.example.instagramapp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze={Post.class})
@ParseClassName("Comment")
public class Comment extends ParseObject {

    // empty constructor needed by the Parceler library
    public Comment() { }

    public static final String KEY_CONTENT = "Content";
    public static final String KEY_AUTHOR = "Author";

    public String getContent() {
        return getString(KEY_CONTENT);
    }

    public void setContent(String content) {
        put(KEY_CONTENT, content);
    }

    public ParseUser getAuthor() {
        return getParseUser(KEY_AUTHOR);
    }

    public void setAuthor(ParseUser user) {
        put(KEY_AUTHOR, user);
    }

}

