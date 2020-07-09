package com.example.instagramapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.instagramapp.fragments.CreateFragment;
import com.example.instagramapp.fragments.HomeFragment;
import com.example.instagramapp.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    BottomNavigationView bottomNavigation;
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);
        getSupportActionBar().setTitle("");

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.actionCreate:
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(R.layout.actionbar);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                        getSupportActionBar().setTitle("");
                        getSupportActionBar().setDisplayShowHomeEnabled(false);
                        fragment =  new CreateFragment();
                        break;
                    case R.id.actionProfile:
                        getSupportActionBar().setDisplayShowHomeEnabled(false);
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(R.layout.actionbar_profile);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                        getSupportActionBar().setTitle("");
                        TextView tvUsername = findViewById(R.id.tvUsername);
                        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
                        Button btnLogout = findViewById(R.id.btnLogout);
                        btnLogout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ParseUser.logOut();
                                goLoginActivity();
                            }
                        });
                        // Log.d(TAG, ParseUser.getCurrentUser().getUsername());
                        fragment = new ProfileFragment();
                        break;
                    case R.id.actionHome:
                    default:
                        menuItem.setIcon(R.drawable.instagram_home_filled_24);
                        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
                        getSupportActionBar().setCustomView(R.layout.actionbar);
                        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
                        getSupportActionBar().setTitle("");
                        getSupportActionBar().setDisplayShowHomeEnabled(true);
                        fragment =  new HomeFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        // Set default selection
        bottomNavigation.setSelectedItemId(R.id.actionHome);
    }

    private void goLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}