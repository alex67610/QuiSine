package com.example.alex.quisine;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.media.audiofx.BassBoost;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.name;


public class MainActivity extends Activity {

    private ArrayList<String> selectedIngredients = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createBottomBar();
        createSearchBar();
        createSearchBar2();
    }

    private void createSearchBar2() {
        final String[] CITIES = new String[]{
                "Ann Arbor, MI", "Chicago, IL", "New York City, NY", "Detroit, MI", "Boston, MA"
                , "Los Angeles, CA", "San Francisco, CA", "Miami, FL"
        };
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, CITIES);
        final AutoCompleteTextView textView2 = (AutoCompleteTextView)
                findViewById(R.id.search_bar2);
        textView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedCity = (String) adapterView.getItemAtPosition(i);

            }
        });
        textView2.setAdapter(adapter2);
        }

    private void createSearchBar() {
        final String[] INGREDIENTS = new String[]{
                "Tarragon", "Almonds", "Grouper", "Buckwheat", "Soybeans", "Pea Beans"
                , "Turnips", "Curry Leaves", "Creme Fraiche", "Hoisin Sauce", "Fennel Seeds"
                , "Watermelons", "Green Onions", "Salt", "Tuna", "Tomato Paste", "Baguette"
                , "Cornmeal", "Black Beans", "Passion Fruit", "Sweet Chili Sauce", "Capers"
                , "Plums", "Gelatin", "Spearmint", "Red Snapper", "Tomatoes", "Walnuts"
                , "Broccoli", "Curry Powder", "Molasses", "Mushrooms", "Apple Pie Spice"
                , "Unsweetened Chocolate", "Macaroni", "Corned Beef", "Paprika", "Pasta"
                , "Cider", "Shrimp", "Tabasco Sauce", "Radishes", "Pumpkin Seeds", "Raspberries"
                , "Flax Seed", "Rosemary", "Catfish", "Swordfish", "Berries", "Corn Syrup"
                , "Sour Cream", "Maple Syrup", "Coffee", "Adobo", "Lemon Juice", "Lettuce"
                , "Tartar Sauce", "Eggs", "Baking Soda", "Curry Paste", "Baking Powder"
                , "Grits", "Pink Beans", "Succotash", "Sweet Peppers", "Quail", "Peanuts"
                , "Barbecue Sauce", "Romano Cheese", "Ale", "Jelly Beans", "Pineapples"
                , "Onion Powder", "Cashew Nuts", "Crabs", "Couscous", "Navy Beans"
                , "Red Chile Powder", "Broth", "Focaccia", "Portabella Mushrooms", "Herring"
                , "Provolone", "Chicory", "Kumquats", "Squid", "Aquavit", "Olives", "Prawns"
                , "Mesclun Greens", "Shitakes", "Pesto", "Octopus", "Ham", "Papayas", "Cranberries"
                , "Squash", "Prunes", "Lemon Peel", "Oatmeal", "Pork", "White Beans", "Lima Beans"
                , "Liver", "Sausages", "Bean Threads", "Remoulade", "Ricotta Cheese", "Kidney Beans"
                , "Fennel", "Pancetta", "Anchovies", "Bruschetta", "Basil", "Bard", "Allspice"
                , "Cayenne Pepper", "Cooking Wine", "Cream of Tartar", "Cannellini Beans"
                , "Plum Tomatoes", "Red Pepper Flakes", "Brown Rice", "Sunflower Seeds", "Veal"
                , "Rhubarb", "Margarine", "Vanilla Bean", "Corn", "Potatoes", "Graham Crackers"
                , "Dried Leeks", "Mayonnaise", "Zest", "Snow Peas", "Beans", "Poultry Seasoning"
                , "Rice Paper", "Cauliflower", "Red Cabbage", "Irish Cream Liqueur", "Green Beans"
                , "Beets", "Chard", "Chutney", "Aioli", "Custard", "Croutons", "Chaurice Sausage"
                , "Bourbon", "Bananas", "Soymilk", "Brussels Sprouts", "Salmon", "Mozzarella"
                , "Broccoli Raab", "Hash Browns", "Guavas", "Bok Choy", "Pig's Feet", "Mustard"
                , "Fish Sauce", "Split Peas", "Barley", "Mascarpone", "Cheddar Cheese", "Dumpling"
                , "Kiwi", "Melons", "Beer", "Plantains", "Steak", "Cappuccino Latte", "Canola Oil"
                , "Bacon", "Coconut Oil", "Bean Sprouts", "Oranges", "Bay Leaves", "Sesame Seeds"
                , "Water Chestnuts", "Cucumbers", "Parsnips", "Snap Peas", "Dill", "Blueberries"
                , "Lentils", "Andouille Sausage", "Tomato Sauce", "Cod", "Flour", "Cornstarch"
                , "Pecans", "Acorn Squash", "Venison", "Grapes"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, INGREDIENTS);
        final AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.search_bar);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedIngredient = (String) adapterView.getItemAtPosition(i);
                if (!selectedIngredients.contains(selectedIngredient)) {
                    //addIngredient(selectedIngredient);








                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    DatabaseReference myRef = database.getReference(selectedIngredient);
                    myRef.setValue(1);
                    Toast.makeText(MainActivity.this, "Item added",
                            Toast.LENGTH_SHORT).show();
                    textView.setText(null);
                } else if (selectedIngredients.contains(selectedIngredient)) {
                    Toast.makeText(MainActivity.this, "Item already added",
                            Toast.LENGTH_SHORT).show();
                    textView.setText(null);
                }
            }
        });
        textView.setAdapter(adapter);
    }

    private void addIngredient(String newIngredient) {
        //selectedIngredients.add(newIngredient);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(newIngredient);

        myRef.setValue(1);
    }

    private void createBottomBar() {
        AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

// Create items
        AHBottomNavigationItem item0 = new AHBottomNavigationItem(R.string.Home, R.drawable.ic_home_black_24dp, R.color.colorRed);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.Settings, R.drawable.ic_settings_black_24dp, R.color.colorRed);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.Match, R.drawable.ic_group_black_24dp, R.color.colorRed);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.Discussion, R.drawable.ic_chat_black_24dp, R.color.colorRed);

// Add items
        bottomNavigation.addItem(item0);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);

// Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        bottomNavigation.setBehaviorTranslationEnabled(true);

// Enable the translation of the FloatingActionButton
        //bottomNavigation.manageFloatingActionButtonBehavior(floatingActionButton);

// Change colors
        bottomNavigation.setAccentColor(Color.parseColor("#FEFEFE"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(false);

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
        bottomNavigation.setCurrentItem(0);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

// Enable / disable item & set disable color
        bottomNavigation.setItemDisableColor(Color.parseColor("#3A000000"));

// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // Do something cool here...
                if (position == 3) {
                    Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intentSettings);
                } else if (position == 1) {
                    Intent intentMatch = new Intent(MainActivity.this, MatchActivity.class);
                    startActivity(intentMatch);
                } else if (position == 2) {
                    Intent intentChat = new Intent(MainActivity.this, DiscussionReviewActivity.class);
                    startActivity(intentChat);
                }
                return true;
            }
        });
    }
}