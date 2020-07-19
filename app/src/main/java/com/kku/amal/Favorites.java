package com.kku.amal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class Favorites extends AppCompatActivity {
LinearLayout area ;
TextView sentence, wholeText;
ImageView close;
    CheckBox fav; // زر الاعجاب
    ImageView share; // زر المشاركة
    TextView textView, a,b; // بنحط العبارة في هالتكست فيو
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);



        area = findViewById(R.id.area);
        area.setVisibility(View.INVISIBLE);

        sentence= findViewById(R.id.sentence);


        close= findViewById(R.id.close);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/" + user.getUid() + "/favorites");




        if (user != null) {

            //  ref.child("users").child(user.getUid()).child("favorites");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final ArrayList<Fav> list = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String sentence = ds.getValue(String.class);
                        list.add(new Fav(sentence));
                    }

                    final ListView listView = (ListView) findViewById(R.id.list_view);
                    FavAdapter mAdapter= new FavAdapter(Favorites.this,list);

                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            area.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);

                            Fav word = list.get(i);
                            String s=word.getSentence();
                            Object o=  adapterView.getSelectedItem();

                            TextView t = findViewById(R.id.wholetext);
                            // Display the selected item text on TextView
                            sentence.setText(s);



      }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            area.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.VISIBLE);

                        }
                    });
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


        fav = findViewById(R.id.fav);
        fav.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // أولا بنتأكد لو فيه يوزر مسجل أو لا
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                // تذكروا ان الاعجاب كان تشيك بوكس مو زر، بالتالي له أحد حالتين ، تشيكد أو مو تشيكد

                    if (user != null) {
                        // راح نقول له احذف العبارة هذي من المفضلة
                        // مرة أخرى، بأشرح لكم ليش سويت هالمسار
                        ref.child("users").child(user.getUid()).child("favorites")
                                .child(user.getUid()+sentence.getText()
                                        .subSequence(0,3)).removeValue();
                        Toast.makeText(getApplicationContext(), "Unliked",
                                Toast.LENGTH_SHORT).show();

                    }}

        } );


    }


}
