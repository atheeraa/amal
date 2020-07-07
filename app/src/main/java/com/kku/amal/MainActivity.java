package com.kku.amal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    LinearLayout area ;
    RelativeLayout holder;
    CheckBox fav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        area= findViewById(R.id.area);
        area.setVisibility(View.INVISIBLE);

        holder= findViewById(R.id.holder);
        holder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                holder.setVisibility(View.INVISIBLE);
                area.setVisibility(View.VISIBLE);

            }
        });
        fav = findViewById(R.id.fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {}
        });
/*
        share = findViewById(R.id.fav);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


            }
        });
        collection = findViewById(R.id.fav);
        collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {


            }
        });*/
    }
    protected void onStart(){
        super.onStart();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("sentences");
        System.out.println(ref);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String sentence = ds.getValue(String.class);
                    list.add(sentence);
                }
                    Random r = new Random();
                    int n = list.size();
                    if(!list.isEmpty()) {
                        String Select = list.get((r.nextInt(n)));
                        TextView textView= findViewById(R.id.sentence);
                        textView.setText(Select);}
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

        }});
}

}
