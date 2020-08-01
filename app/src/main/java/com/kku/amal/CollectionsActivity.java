package com.kku.amal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class CollectionsActivity extends Activity {

    FloatingActionButton fab;
    LinearLayout area, buttons;
    TextView sentence, title, t;
    ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users/" + user.getUid() + "/collections");
        t = findViewById(R.id.hint);
        fab = findViewById(R.id.fab);
        area = findViewById(R.id.area);
        buttons = findViewById(R.id.buttons);

        area.setVisibility(View.INVISIBLE);

        sentence = findViewById(R.id.sentence);
        title = findViewById(R.id.title);
        title.setText("Collections");

        close = findViewById(R.id.close);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        SharedPreferences prefs = getSharedPreferences("bgColour", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        final int colorprefs = prefs.getInt("color", 1);
        final int txtprefs = prefs.getInt("txt", 1);
        title.setTextColor(txtprefs);
        drawer.setBackgroundColor(colorprefs);


        final ListView listView = (ListView) findViewById(R.id.collections_list);
        final ListView insideList = (ListView) findViewById(R.id.sentences_list);

        insideList.setVisibility(View.INVISIBLE);


        if (user != null) {
            ref.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final ArrayList<Collections> list = new ArrayList<>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String collection = (String) ds.getKey();
                        list.add(new Collections(collection));
                    }

                    CollectionsAdapter mAdapter = new CollectionsAdapter(CollectionsActivity.this, list);

                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Collections c = list.get(i);
                            final String s = c.getCollection();
                            title.setText(s);

                            final DatabaseReference ref = database.getReference("users/" + user.getUid() + "/collections/" + s);
                            ref.child("sentences").addValueEventListener(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    final ArrayList<Collections> list2 = new ArrayList<>();

                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        String collection = (String) ds.getValue();
                                        list2.add(new Collections(collection));

                                    }
                                    CollectionsDetailsAdapter mAdapter = new CollectionsDetailsAdapter(CollectionsActivity.this, list2);
                                    insideList.setAdapter(mAdapter);
                                    insideList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            title.setVisibility(View.INVISIBLE);

                                            area.setVisibility(View.VISIBLE);
                                            fab.setVisibility(View.INVISIBLE);
                                            insideList.setVisibility(View.INVISIBLE);

                                            Collections c = list2.get(i);
                                            final String s = c.getCollection();
                                            TextView t = findViewById(R.id.wholetext);
                                            // Display the selected item text on TextView
                                            sentence.setText(s);
                                            sentence.setTextColor(txtprefs);
                                            buttons.setOnClickListener(new View.OnClickListener() {
                                                                           @Override
                                                                           public void onClick(View view) {
                                                                               DatabaseReference ref = database.getReference();

                                                                               ref.child("UsersSentences").child(user.getUid() + sentence.getText().subSequence(0, 3)).setValue(sentence.getText());

                                                                           }
                                                                       }
                                            );

                                        }
                                    });
                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            area.setVisibility(View.INVISIBLE);
                                            insideList.setVisibility(View.VISIBLE);
                                            title.setVisibility(View.VISIBLE);


                                        }
                                    });

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            insideList.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.INVISIBLE);
                            fab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final AlertDialog dialogBuilder = new AlertDialog.Builder(CollectionsActivity.this).create();
                                    LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                                    final View dialogView = inflater.inflate(R.layout.addcollectiondialog, null);

                                    dialogBuilder.setTitle("Add a sentence to (" + s + ")");

                                    final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
                                    Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
                                    Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);

                                    button2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialogBuilder.dismiss();
                                        }
                                    });
                                    button1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            String sentence = editText.getText().toString();

                                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            if (user != null) {
                                                ref.child("sentences").child(user.getUid() + sentence.substring(0, 3)).setValue(sentence);
                                                Toast.makeText(getApplicationContext(), "Added",
                                                        Toast.LENGTH_SHORT).show();
                                                dialogBuilder.dismiss();
                                            }


                                        }
                                    });
                                    dialogBuilder.setView(dialogView);
                                    dialogBuilder.show();


                                }
                            });


                        }

                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog dialogBuilder = new AlertDialog.Builder(CollectionsActivity.this).create();
                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                final View dialogView = inflater.inflate(R.layout.addcollectiondialog, null);

                final EditText editText = (EditText) dialogView.findViewById(R.id.edt_comment);
                Button button1 = (Button) dialogView.findViewById(R.id.buttonSubmit);
                Button button2 = (Button) dialogView.findViewById(R.id.buttonCancel);
                dialogBuilder.setTitle("Name of the collection?");

                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogBuilder.dismiss();
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String collectionName = editText.getText().toString();

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference ref = database.getReference();
                        if (user != null) {
                            ref.child("users").child(user.getUid()).child("collections")
                                    .child(collectionName).child("name").setValue(collectionName);


                            Toast.makeText(getApplicationContext(), "Added",
                                    Toast.LENGTH_SHORT).show();
                            dialogBuilder.dismiss();
                        }

                    }
                });
                dialogBuilder.setView(dialogView);
                dialogBuilder.show();


            }
        });


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


    }
}