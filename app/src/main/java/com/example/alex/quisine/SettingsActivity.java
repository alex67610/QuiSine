package com.example.alex.quisine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.sql.Ref;

public class SettingsActivity extends Activity implements Button.OnClickListener{

    private TextView textViewFirstName, textViewLastName, textViewAge, textViewAddress, textViewBiography, textViewEmail;
    private Button buttonUpdateProfile, buttonUploadProfile;
    private ImageView ProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textViewFirstName = (TextView) findViewById(R.id.textViewFirstName);
        textViewLastName = (TextView) findViewById(R.id.textViewLastName);
        textViewAge = (TextView) findViewById(R.id.textViewAge);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        textViewBiography = (TextView) findViewById(R.id.textViewBiography);
        buttonUpdateProfile = (Button) findViewById(R.id.buttonUpdateProfile);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        buttonUploadProfile = (Button) findViewById(R.id.buttonUploadPicture);
        ProfilePic = (ImageView) findViewById(R.id.ProfilePic);

        buttonUpdateProfile.setOnClickListener(this);
        buttonUploadProfile.setOnClickListener(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://quisine-c8d9c.appspot.com");
        StorageReference RefPic = storageRef.child(uid);

        final long ONE_MEGABYTE = 1024*1024;

        RefPic.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ProfilePic.setImageBitmap(bitmap);
            }
        });

        FirebaseDatabase db1 = FirebaseDatabase.getInstance();
        DatabaseReference myLookupRef = db1.getReference("User");
        DatabaseReference myFName = myLookupRef.child(uid).child("User Info").child("First Name");
        DatabaseReference myLName = myLookupRef.child(uid).child("User Info").child("Last Name");
        DatabaseReference myAge = myLookupRef.child(uid).child("User Info").child("Age");
        DatabaseReference myAddress = myLookupRef.child(uid).child("User Info").child("Address");
        DatabaseReference myBiography = myLookupRef.child(uid).child("User Info").child("Biography");

        String email = user.getEmail();
        textViewEmail.setText(email);

        myFName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String FName = dataSnapshot.getValue(String.class);
                textViewFirstName.setText(FName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myLName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String LName = dataSnapshot.getValue(String.class);
                textViewLastName.setText(LName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myAge.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Age = dataSnapshot.getValue(String.class);
                textViewAge.setText(Age);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myAddress.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Address = dataSnapshot.getValue(String.class);
                textViewAddress.setText(Address);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myBiography.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String Biography = dataSnapshot.getValue(String.class);
                textViewBiography.setText(Biography);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        createBottomBar();

    }

    private void createBottomBar() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item0 = new AHBottomNavigationItem(R.string.Home, R.drawable.ic_home_black_24dp, R.color.colorRed);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.Settings, R.drawable.ic_settings_black_24dp, R.color.colorRed);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.Match, R.drawable.ic_group_black_24dp, R.color.colorRed);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.Discussion, R.drawable.ic_chat_black_24dp, R.color.colorRed);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.Recipe, R.drawable.ic_local_dining_black_24dp, R.color.colorRed);

// Add items
        bottomNavigation.addItem(item0);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(false);

// Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#F63D2B"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Display color under navigation bar (API 21+)
// Don't forget these lines in your style-v21
// <item name="android:windowTranslucentNavigation">true</item>
// <item name="android:fitsSystemWindows">true</item>
        bottomNavigation.setTranslucentNavigationEnabled(true);

// Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(4);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Enable / disable item & set disable color
        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                if (position == 1) {
                    Intent intentSettings = new Intent(SettingsActivity.this, MatchActivity.class);
                    startActivity(intentSettings);
                } else if (position == 0) {
                    Intent intentMain = new Intent(SettingsActivity.this, MainActivity.class);
                    startActivity(intentMain);
                } else if (position == 3) {
                    Intent intentChat = new Intent(SettingsActivity.this, DiscussionReviewActivity.class);
                    startActivity(intentChat);
                } else if (position == 2) {
                    Intent intentMatch2 = new Intent(SettingsActivity.this, MatchActivity2.class);
                    startActivity(intentMatch2);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonUpdateProfile) {
            Intent intentUpdateProfile = new Intent(SettingsActivity.this, UpdateProfile.class);
            startActivity(intentUpdateProfile);
        } else if (view.getId() == R.id.buttonUploadPicture) {
            Intent intentUploadPicture = new Intent(SettingsActivity.this, UploadProfilePicture.class);
            startActivity(intentUploadPicture);
        }
    }
}