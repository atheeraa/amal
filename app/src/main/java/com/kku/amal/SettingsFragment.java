package com.kku.amal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    RelativeLayout title;
    CheckBox ar, en;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        SharedPreferences prefs = getActivity().getSharedPreferences("lang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        title= view.findViewById(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int reqCode = 1;
                 NotificationUtil. showNotification(getContext(), "Title", "This is the message to display", reqCode);

            }
        });
        Spinner spinner = view.findViewById(R.id.reps);
         int  nums []={1,2,3};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
               R.array.reps, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        SharedPreferences prefs = getActivity().getSharedPreferences("lang", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int arpref = prefs.getInt("ar", 1);
        int enpref = prefs.getInt("en", 1);

        ar = view.findViewById(R.id.favarabic);
        en = view.findViewById(R.id.favenglish);
        ar.setChecked(true);
        en.setChecked(true);

        if (arpref==0){
            ar.setChecked(false);
        }
        if (enpref==0){
            en.setChecked(false);
        }

        ar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                if (ar.isChecked()) {
                    editor.putInt("ar", 1);
                } else if(!ar.isChecked() && !en.isChecked()) {

                    Toast.makeText(getContext(), "يجب اختيار لغة واحدة على الأقل",
                            Toast.LENGTH_SHORT).show();
                    ar.setChecked(true);
                    en.setChecked(true);

                }
                else {
                    editor.putInt("ar", 0);
                }
                editor.apply();

            }
        });

        en.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (en.isChecked()) {
                    editor.putInt("en", 1);
                } else if(!ar.isChecked() && !en.isChecked()) {

                    Toast.makeText(getContext(), "يجب اختيار لغة واحدة على الأقل",
                            Toast.LENGTH_SHORT).show();
                    ar.setChecked(true);
                    en.setChecked(true);

                }
                else {
                    editor.putInt("en", 0);
                }
                editor.apply();

            }
        });
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceType) {
        super.onActivityCreated(savedInstanceType);


    }

}