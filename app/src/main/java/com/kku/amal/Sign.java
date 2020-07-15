package com.kku.amal;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Sign extends AppCompatActivity {

    public Integer REQUEST_EXIT = 9;
    public FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    Button signUpButton, signInButton;
    TextView errorView;
    EditText nameet , emailet, passwordet, repasset;
    LinearLayout repass ;
    public ProgressBar mProgressBar;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);
        ref = FirebaseDatabase.getInstance().getReference();
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
                            System.out.print("account clicccccccccc");

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

                }
                //This is for maintaining the behavior of the Navigation view
                //This is for closing the drawer after acting on it
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        signUpButton = findViewById(R.id.signup);
        signUpButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                signUp();
                                            }
                                        }
        );
        signInButton = findViewById(R.id.signin);
        signInButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                signIn();
                                            }
                                        }
        );
        emailet = findViewById(R.id.emailet);
        passwordet = findViewById(R.id.passwordet);
        repasset = findViewById(R.id.repasset);

        repass = findViewById(R.id.repassv);

        errorView = findViewById(R.id.errorview);

    }



    @Override
    public void onStart() {
        super.onStart();

        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    private void signIn() {
        Log.d("1", "signIn");
        if (!validateForm()) {
            return;
        }

        String email = emailet.getText().toString();
        String password = passwordet.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("1", "signIn:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(Sign.this, "Sign In Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void signUp() {
        Log.d("1", "signUp");
        if (!validateForm()) {
            return;
        }

        String email = emailet.getText().toString();
        String password = passwordet.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("1", "createUser:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            onAuthSuccess(task.getResult().getUser());
                        } else {
                            Toast.makeText(Sign.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        String username = usernameFromEmail(user.getEmail());
        // Write new user
        writeNewUser(user.getUid(), username, user.getEmail());
        // Go to MainActivity
        startActivity(new Intent(Sign.this, MainActivity.class));
        finish();
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty( emailet.getText().toString())) {
           emailet.setError("Required");
            result = false;
        } else {
           emailet.setError(null);
        }

        if (TextUtils.isEmpty(passwordet.getText().toString())) {
           passwordet.setError("Required");
            result = false;
        } else {
            passwordet.setError(null);
        }

        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email, null, null );

        ref.child("users").child(userId).setValue(user);
    }
    // [END basic_write]




}
