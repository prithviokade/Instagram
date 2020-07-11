package com.example.instagramapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileActivity";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 112;
    public String photoFileName = "photo.jpg";
    File photoFile;

    TextView tvChangeProfilePhoto;
    EditText etName;
    EditText etBio;
    EditText etUsername;
    ImageView ivProfilePic;
    TextView tvDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        tvChangeProfilePhoto = findViewById(R.id.tvChangeProfilePhoto);
        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        etUsername = findViewById(R.id.etUsername);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvDone = findViewById(R.id.tvDone);

        getSupportActionBar().hide();

        String fillName = ParseUser.getCurrentUser().getString("name");
        if (fillName != null) {
            etName.setText(fillName);
        }
        String fillBio = ParseUser.getCurrentUser().getString("bio");
        if (fillBio != null) {
            etBio.setText(fillBio);
        }


        etUsername.setText(ParseUser.getCurrentUser().getUsername());
        ParseFile profile = ParseUser.getCurrentUser().getParseFile("Profile");
        if (profile != null) {
            Glide.with(this).load(profile.getUrl()).placeholder(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfilePic);
        } else {
            Glide.with(this).load(R.drawable.instagram_user_filled_24).transform(new CircleCrop()).into(ivProfilePic);
        }

        tvChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCamera();
                savePicture(ParseUser.getCurrentUser(),photoFile);
                ParseFile profile = ParseUser.getCurrentUser().getParseFile("Profile");
                Glide.with(EditProfileActivity.this).load(profile.getUrl()).transform(new CircleCrop()).into(ivProfilePic);
            }
        });

        etUsername.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                ParseUser.getCurrentUser().setUsername(etUsername.getText().toString());
                return true;
            }
        });

        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                return true;
            }
        });

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                ParseUser.getCurrentUser().put("name", etName.getText().toString());
                ParseUser.getCurrentUser().put("bio", etBio.getText().toString());
                saveUser();
                startActivity(intent);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // Load the taken image into a preview
                ivProfilePic.setImageBitmap(takenImage);
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUser() {
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving post info", e);
                } else {
                    Log.i(TAG, "Success saving post info");
                }
            }
        });
    }

    private void savePicture(ParseUser user, File photoFile) {
        user.put("Profile", new ParseFile(photoFile));
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving new post", e);
                } else {
                    Log.i(TAG, "Success saving new post");
                }
            }
        });
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        Uri fileProvider = FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

}