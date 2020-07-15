package com.kku.amal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Favorites extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/" + user.getUid() + "/favorites");




        if (user != null) {

            //  ref.child("users").child(user.getUid()).child("favorites");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    ArrayList<Fav> list = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String sentence = ds.getValue(String.class);
                        String subsentence= sentence.substring(0,20)+"... ";
                        list.add(new Fav(subsentence));
                    }

                    ListView listView = (ListView) findViewById(R.id.list_view);
                    FavAdapter mAdapter= new FavAdapter(Favorites.this,list);

                    listView.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());

                }
            });
        }

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent;
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                //it's possible to do more actions on several items, if there is a large amount of items I prefer switch(){case} instead of if()
                switch (id) {
                    case R.id.collectionsnav: // Change this as your menuitem in menu.xml.

                        if (user != null) {
                            // User is signed in
                            intent = new Intent(Favorites.this, CollectionsActivity.class);
                            startActivity(intent);


                        } else {
                            // No user is signed in
                            intent = new Intent(Favorites.this, Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.account: // Change this as your menuitem in menu.xml.
                        if (user != null) {
                            // User is signed in
                            intent = new Intent(Favorites.this, MyAccount.class);
                            startActivity(intent);

                        } else {
                            // No user is signed in
                            intent = new Intent(Favorites.this, Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.favnav: // Change this as your menuitem in menu.xml.
                        if (user != null) {
                            // User is signed in
                            intent = new Intent(Favorites.this, Favorites.class);
                            startActivity(intent);


                        } else {
                            // No user is signed in
                            intent = new Intent(Favorites.this, Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.home: // Change this as your menuitem in menu.xml.

                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);


                        break;

                }
                //This is for maintaining the behavior of the Navigation view
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }}