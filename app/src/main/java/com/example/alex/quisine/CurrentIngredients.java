package com.example.alex.quisine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CurrentIngredients extends Activity implements Button.OnClickListener{

    private Button buttonBack;
    private TextView textViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_ingredients);

        buttonBack = (Button) findViewById(R.id.buttonBack);
        textViewList = (TextView) findViewById(R.id.textViewList);

        createSearchBar();
        buttonBack.setOnClickListener(this);

    }

    private void createSearchBar() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference().child("User").child(uid).child("User Ingredients");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> list = new ArrayList<String>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String ingredient = ds.getValue(String.class);
                    list.add(ingredient);
                }
                Iterator<String> iterator = list.iterator();
                while (iterator.hasNext()) {
                    textViewList.setText(textViewList.getText() + System.lineSeparator() + iterator.next());
                }
                if (!list.isEmpty()) {
                    String[] INGREDIENTS = new String[list.size()];
                    INGREDIENTS = list.toArray(INGREDIENTS);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(CurrentIngredients.this,
                            android.R.layout.simple_dropdown_item_1line, INGREDIENTS);
                    final AutoCompleteTextView textView = (AutoCompleteTextView)
                            findViewById(R.id.search_bar3);
                    textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            final String selectedIngredient = (String) adapterView.getItemAtPosition(i);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            String uid = user.getUid();
                            final DatabaseReference myRef = database.getReference("User");
                            myRef.child(uid).child("User Ingredients").child(selectedIngredient).removeValue();
                            Toast.makeText(CurrentIngredients.this, "Item deleted",
                                    Toast.LENGTH_SHORT).show();
                            textView.setText(null);
                            textViewList.setText(null);
                        }
                    });
                    textView.setAdapter(adapter);



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonBack) {
            Intent intentBack = new Intent(CurrentIngredients.this, MainActivity.class);
            startActivity(intentBack);
        }
    }
}
