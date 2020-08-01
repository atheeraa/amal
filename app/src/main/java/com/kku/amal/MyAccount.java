package com.kku.amal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyAccount extends Activity {

    Button logout;
    EditText  emailet;
    TextView favtv, collecttv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        logout = findViewById(R.id.logout);
         emailet = findViewById(R.id.email);

         favtv = findViewById(R.id.favtv);
         collecttv= findViewById(R.id.collecttv);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        int colorprefs = prefs.getInt("color", 1);
        drawer.setBackgroundColor(colorprefs);


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
                            intent = new Intent(getApplicationContext(), CollectionsActivity.class);
                            startActivity(intent);


                        } else {
                            // No user is signed in
                            intent = new Intent(getApplicationContext(), Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.account: // Change this as your menuitem in menu.xml.
                        if (user != null) {
                            // User is signed in
                            intent = new Intent(getApplicationContext(), MyAccount.class);
                            startActivity(intent);

                        } else {
                            // No user is signed in
                            intent = new Intent(getApplicationContext(), Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.favnav: // Change this as your menuitem in menu.xml.
                        if (user != null) {
                            // User is signed in
                            intent = new Intent(getApplicationContext(), Favorites.class);
                            startActivity(intent);


                        } else {
                            // No user is signed in
                            intent = new Intent(getApplicationContext(), Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.home: // Change this as your menuitem in menu.xml.

                        intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);


                        break;
                    case R.id.settings:
                        intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);

                }
                //This is for maintaining the behavior of the Navigation view
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
             String email = user.getEmail();

            emailet.setText(email);
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();


        }

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MyAccount.this, "Logged-out",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MyAccount.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        favtv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccount.this, Favorites.class);
                startActivity(intent);

            }}
        );

        collecttv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccount.this, CollectionsActivity.class);
                startActivity(intent);

            }}
        );


        }
}
