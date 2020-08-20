package com.kku.amal;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

    RelativeLayout error;
    RelativeLayout area2;
    TextView sentence, errorText;
    ImageView close;
    CheckBox unfav; // زر الاعجاب
    ImageView share; // زر المشاركة
    TextView textView, a, b, title; // بنحط العبارة في هالتكست فيو
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        final ListView listView = (ListView) view.findViewById(R.id.favlist);

        error = view.findViewById(R.id.error);
        errorText = view.findViewById(R.id.errortext);
        area2 = view.findViewById(R.id.area2);
        area2.setVisibility(View.INVISIBLE);

        sentence = view.findViewById(R.id.fullsentence);

        close = view.findViewById(R.id.close);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            error.setVisibility(View.VISIBLE);
            errorText.setText("الرجاء تسجيل الدخول");
        } else {

            listView.setVisibility(View.VISIBLE);
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("users/" + user.getUid() + "/favorites");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final ArrayList<Fav> list = new ArrayList<>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String sentence = ds.getValue(String.class);

                        list.add(new Fav(sentence));
                    }
                    if (getActivity() != null) {
                        FavAdapter mAdapter = new FavAdapter(getActivity(), list);

                        listView.setAdapter(mAdapter);
                    }
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            listView.setVisibility(View.INVISIBLE);
                            area2.setVisibility(View.VISIBLE);
                            Fav f = list.get(i);
                            String s = f.getSentence();
                            sentence.setText(s);
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            area2.setVisibility(View.INVISIBLE);
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


        unfav = view.findViewById(R.id.unfav);
        unfav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
// أولا بنتأكد لو فيه يوزر مسجل أو لا
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference();
                // تذكروا ان الاعجاب كان تشيك بوكس مو زر، بالتالي له أحد حالتين ، تشيكد أو مو تشيكد
                if (!unfav.isChecked()) {
                    if (user != null) {
                        // راح نقول له احذف العبارة هذي من المفضلة
                        // مرة أخرى، بأشرح لكم ليش سويت هالمسار
                        ref.child("users").child(user.getUid()).child("favorites")
                                .child(user.getUid() + sentence.getText()
                                        .subSequence(0, 3)).removeValue();
                        Toast.makeText(getContext(), "أُزيلت من المفضلة!",
                                Toast.LENGTH_SHORT).show();

                    }
                } else {
                    ref.child("users").child(user.getUid()).child("favorites")
                            //بأشرح لكم في فيديو اش يعني السطرين التاليين
                            .child(user.getUid() + sentence.getText().subSequence(0, 3))
                            .setValue(sentence.getText());
                    Toast.makeText(getContext(), "أُعيدت للمفضلة!",
                            Toast.LENGTH_SHORT).show();

                }

            }

        });

        share = view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // تتذكرون الانتنتس؟ هذا استعمال ثاني لها، اعتبروها طريقة تنقل في الأبلكيشن أو من الابلكيشن لابلكيشنز ثانين
                Intent sendIntent = new Intent();
                // نوع الانتنت ارسال
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, sentence.getText());
                // نوع البيانات اللي بتنرسل نص
                sendIntent.setType("text/plain");

                // بنسوي لها شير
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);

    }
}