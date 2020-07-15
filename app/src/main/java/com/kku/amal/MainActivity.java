package com.kku.amal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity  {
    LinearLayout area;
    RelativeLayout holder;
    CheckBox fav;
    ImageView share;
    String Select;
    List<String> list;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //واجهة الصفحة هذي راح تكون من ملف activity_main
        setContentView(R.layout.activity_main);
        // التكستفيو اللي راح نحط فيه العبارة
        textView = findViewById(R.id.sentence);


        // the drawer
        final  DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
                            intent = new Intent(MainActivity.this, CollectionsActivity.class);
                            startActivity(intent);


                        } else {
                            // No user is signed in
                            intent = new Intent(MainActivity.this, Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.account: // Change this as your menuitem in menu.xml.
                         if (user != null) {
                            // User is signed in
                            intent = new Intent(MainActivity.this, MyAccount.class);
                            startActivity(intent);
System.out.print("account clicccccccccc");

                        } else {
                            // No user is signed in
                            intent = new Intent(MainActivity.this, Sign.class);
                            startActivity(intent);

                        }
                        break;
                    case R.id.favnav: // Change this as your menuitem in menu.xml.
                        if (user != null) {
                            // User is signed in
                            intent = new Intent(MainActivity.this, Favorites.class);
                            startActivity(intent);


                        } else {
                            // No user is signed in
                            intent = new Intent(MainActivity.this, Sign.class);
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
        area = findViewById(R.id.area);
        area.setVisibility(View.INVISIBLE);

        holder = findViewById(R.id.holder);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.setVisibility(View.INVISIBLE);
                area.setVisibility(View.VISIBLE);

            }
        });


        /// Ref to access sentences

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sentences");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String sentence = ds.getValue(String.class);
                    list.add(sentence);
                }
                Random r = new Random();
                int n = list.size();
                if (!list.isEmpty()) {
                    String Select = list.get((r.nextInt(n)));
                    textView.setText(Select);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });
        fav = findViewById(R.id.fav);
        fav.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();

                if(fav.isChecked()){
                if (user != null) {

                    ref.child("users").child(user.getUid()).child("favorites").child(user.getUid()+textView.getText().subSequence(0,3)).setValue(textView.getText());
                    Toast.makeText(MainActivity.this, "added"+textView.getText(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "You're not signed in",
                            Toast.LENGTH_SHORT).show();
                }

            }
            else{
                    if (user != null) {

                    ref.child("users").child(user.getUid()).child("favorites").child(user.getUid()+textView.getText().subSequence(0,3)).removeValue();
            }}
            }
        } );



                /*setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                if (user != null) {

                                ref.child("users").child(user.getUid()).child("favorites").push().setValue(textView.getText());
                            Toast.makeText(MainActivity.this, "added"+textView.getText(),
                                    Toast.LENGTH_SHORT).show();




                } else {
                    Toast.makeText(MainActivity.this, "You're not signed in",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
*/
        share = findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Select);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });

        /*
        collection = findViewById(R.id.fav);
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


            }
        });*/


    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.fav:
                if (checked){
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                if (user != null) {

                    ref.child("users").child(user.getUid()).child("favorites").child(user.getUid()+"1").setValue(textView.getText());
                    Toast.makeText(MainActivity.this, "added"+textView.getText(),
                            Toast.LENGTH_SHORT).show();




                } else {
                    Toast.makeText(MainActivity.this, "You're not signed in",
                            Toast.LENGTH_SHORT).show();
                }

        }
                else{

                }
    }
    }

}