package com.example.alex.quisine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

import static com.example.alex.quisine.R.id.uploadCancel;

public class MatchActivity extends Activity {

    private Button buttonLike, buttonDislike;
    private TextView textViewTest;
    private ImageView imageViewTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        buttonLike = findViewById(R.id.buttonLike);
        buttonDislike = findViewById(R.id.buttonDislike);
        textViewTest = findViewById(R.id.textViewTest);
        imageViewTest = findViewById(R.id.imageViewTest);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myReft = db.getReference().child("User").child(uid).child("Potential Recipes");
        final DatabaseReference myRef = db.getReference().child("User");
        final DatabaseReference myRef2 = myRef.child(uid).child("User Recipes");

        myReft.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> list = new ArrayList<String>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getValue(String.class);
                    list.add(name);
                }
                if (!list.isEmpty()) {
                    textViewTest.setText(list.get(0));
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://quisine-c8d9c.appspot.com");
                    StorageReference RefPic = storageRef.child(list.get(0) + ".jpg");

                    final long ONE_MEGABYTE = 1024 * 1024;

                    RefPic.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            imageViewTest.setImageBitmap(bitmap);
                        }
                    });
                } else if (list.isEmpty()) {
                    textViewTest.setText(null);
                    imageViewTest.setImageBitmap(null);
                }

                buttonLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!list.isEmpty()) {
                            myReft.child("Recipe " + list.get(0)).removeValue();
                            DatabaseReference myRef2t = db.getReference().child("User").child(uid).child("User Recipes");
                            myRef2t.child("Recipe " + list.get(0)).setValue(list.get(0));
                            Toast.makeText(MatchActivity.this, "You liked it!", Toast.LENGTH_SHORT).show();

                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (final DataSnapshot ds : dataSnapshot.getChildren()) {
                                        if (!ds.getKey().toString().matches(uid)) {
                                            final String userKey = ds.getKey().toString();
                                            myRef.child(userKey).child("User Recipes").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                                                        final String recipe = ds1.getValue(String.class);
                                                        myRef2.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot ds2 : dataSnapshot.getChildren()) {
                                                                    String myRecipe = ds2.getValue(String.class);
                                                                    if (recipe.matches(myRecipe)) {
                                                                        myRef.child(uid).child("Potential Match").child(userKey + " " + recipe).child("Recipe").setValue(recipe);
                                                                        myRef.child(uid).child("Potential Match").child(userKey + " " + recipe).child("UserKey").setValue(userKey);
                                                                    }
                                                                }
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
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        } else {
                            Toast.makeText(MatchActivity.this, "No other potential recipe",
                                    Toast.LENGTH_SHORT).show();
                            imageViewTest.setImageBitmap(null);
                        }
                    }
                });

                buttonDislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!list.isEmpty()) {
                            myReft.child("Recipe " + list.get(0)).removeValue();
                            Toast.makeText(MatchActivity.this, "Sorry to hear that..", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MatchActivity.this, "No other potential recipe",
                                    Toast.LENGTH_SHORT).show();
                            imageViewTest.setImageBitmap(null);
                            textViewTest.setText(null);
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
        bottomNavigation.setCurrentItem(1);

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
                    Intent intentSettings = new Intent(MatchActivity.this, SettingsActivity.class);
                    startActivity(intentSettings);
                } else if (position == 0) {
                    Intent intentMain = new Intent(MatchActivity.this, MainActivity.class);
                    startActivity(intentMain);
                } else if (position == 3) {
                    Intent intentChat = new Intent(MatchActivity.this, DiscussionReviewActivity.class);
                    startActivity(intentChat);
                } else if (position == 2) {
                    Intent intentMatch2 = new Intent(MatchActivity.this, MatchActivity2.class);
                    startActivity(intentMatch2);
                }
                return true;
            }
        });
    }

}