package com.example.alex.quisine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.tasks.OnFailureListener;
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

import java.sql.Ref;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MatchActivity2 extends Activity {

    private Button buttonLike, buttonDislike;
    private TextView textViewTest, textViewTest2;
    private ImageView imageViewTest, imageViewTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match2);

        buttonLike = findViewById(R.id.buttonLike);
        buttonDislike = findViewById(R.id.buttonDislike);
        textViewTest = findViewById(R.id.textViewTest);
        textViewTest2 = findViewById(R.id.textViewTest2);
        imageViewTest = findViewById(R.id.imageViewTest);
        imageViewTest2 = findViewById(R.id.imageViewTest2);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference().child("User").child(uid).child("Potential Match");
        final DatabaseReference myRef2 = db.getReference().child("User").child(uid).child("Child for Matching System");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String Key = ds.getKey();
                    final String Recipe = ds.child("Recipe").getValue(String.class);
                    final String UserKey = ds.child("UserKey").getValue(String.class);
                    myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myRef2.child(Key).child("Recipe").setValue(Recipe);
                            myRef2.child(Key).child("UserKey").setValue(UserKey);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> listUser = new ArrayList<String>();
                final List<String> listRecipe = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String UserKey = ds.child("UserKey").getValue(String.class);
                    String Recipe = ds.child("Recipe").getValue(String.class);

                    listUser.add(UserKey);
                    listRecipe.add(Recipe);
                }
                    if (!listUser.isEmpty()) {
                        textViewTest.setText(listRecipe.get(0));
                        DatabaseReference RefName = db.getReference().child("User").child(listUser.get(0)).child("User Info");
                        RefName.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String FName = dataSnapshot.child("First Name").getValue(String.class);
                                String LName = dataSnapshot.child("Last Name").getValue(String.class);
                                textViewTest2.setText(FName + " " + LName);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://quisine-c8d9c.appspot.com");
                        StorageReference RefPic = storageRef.child(listRecipe.get(0) + ".jpg");
                        StorageReference RefPic2 = storageRef.child(listUser.get(0));

                        final long ONE_MEGABYTE = 1024 * 1024;

                        RefPic.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageViewTest2.setImageBitmap(bitmap);
                            }
                        });
                        RefPic2.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageViewTest.setImageBitmap(bitmap);
                            }
                        });
                        RefPic2.getBytes(ONE_MEGABYTE).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                imageViewTest.setImageBitmap(null);
                            }
                        });
                    } else if (listUser.isEmpty()) {
                        textViewTest.setText(null);
                        textViewTest2.setText(null);
                        imageViewTest.setImageBitmap(null);
                        imageViewTest2.setImageBitmap(null);
                    }

                    buttonLike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!listUser.isEmpty()) {
                                final String Key = listUser.get(0) + " " + listRecipe.get(0);
                                myRef2.child(Key).removeValue();
                                Toast.makeText(MatchActivity2.this, "Nice!", Toast.LENGTH_SHORT).show();
                            } else if (listUser.isEmpty()) {
                                Toast.makeText(MatchActivity2.this, "No other potential match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    buttonDislike.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!listUser.isEmpty()) {
                                final String Key = listUser.get(0) + " " + listRecipe.get(0);
                                myRef2.child(Key).removeValue();
                                Toast.makeText(MatchActivity2.this, "Sorry..", Toast.LENGTH_SHORT).show();
                            } else if (listUser.isEmpty()) {
                                Toast.makeText(MatchActivity2.this, "No other potential match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


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
        bottomNavigation.setTranslucentNavigationEnabled(false);

// Manage titles
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);

// Use colored navigation with circle reveal effect
        bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(2);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Enable / disable item & set disable color
        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                if (position == 4) {
                    Intent intentSettings = new Intent(MatchActivity2.this, SettingsActivity.class);
                    startActivity(intentSettings);
                } else if (position == 0) {
                    Intent intentMain = new Intent(MatchActivity2.this, MainActivity.class);
                    startActivity(intentMain);
                } else if (position == 3) {
                    Intent intentChat = new Intent(MatchActivity2.this, DiscussionReviewActivity.class);
                    startActivity(intentChat);
                } else if (position == 1) {
                    Intent intentMatch = new Intent(MatchActivity2.this, MatchActivity.class);
                    startActivity(intentMatch);
                }
                return true;
            }
        });
    }
}
