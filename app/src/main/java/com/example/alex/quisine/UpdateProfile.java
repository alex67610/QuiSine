package com.example.alex.quisine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdateProfile extends Activity implements Button.OnClickListener{

    private Button buttonCancel, buttonSaveChanges;
    private EditText editTextFName, editTextLName, editTextAge, editTextAddress, editTextBiography;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonSaveChanges = (Button) findViewById(R.id.buttonSaveChanges);
        editTextFName = (EditText) findViewById(R.id.editTextFName);
        editTextLName = (EditText) findViewById(R.id.editTextLName);
        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        editTextBiography = (EditText) findViewById(R.id.editTextBiography);

        buttonCancel.setOnClickListener(this);
        buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(editTextFName) == true || isEmpty(editTextLName) == true || isEmpty(editTextAge) == true || isEmpty(editTextAddress) == true || isEmpty(editTextBiography) == true) {
                    Toast.makeText(UpdateProfile.this, "You did not complete your profile",
                            Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();

                    FirebaseDatabase db2 = FirebaseDatabase.getInstance();
                    final DatabaseReference myUpdateRef = db2.getReference("User");
                    final DatabaseReference myFName = myUpdateRef.child(uid).child("User Info").child("First Name");
                    final DatabaseReference myLName = myUpdateRef.child(uid).child("User Info").child("Last Name");
                    final DatabaseReference myAge = myUpdateRef.child(uid).child("User Info").child("Age");
                    final DatabaseReference myAddress = myUpdateRef.child(uid).child("User Info").child("Address");
                    final DatabaseReference myBiography = myUpdateRef.child(uid).child("User Info").child("Biography");

                    myFName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myFName.setValue(editTextFName.getText().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    myLName.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myLName.setValue(editTextLName.getText().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    myAge.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myAge.setValue(editTextAge.getText().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    myAddress.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myAddress.setValue(editTextAddress.getText().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    myBiography.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            myBiography.setValue(editTextBiography.getText().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    Toast.makeText(UpdateProfile.this, "Profile saved",
                            Toast.LENGTH_LONG).show();
                    Intent intentOK = new Intent(UpdateProfile.this, MainActivity.class);
                    startActivity(intentOK);
                }
            }
        });


    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonCancel) {
            Intent intentBack = new Intent(UpdateProfile.this, SettingsActivity.class);
            startActivity(intentBack);
        }
    }
}
