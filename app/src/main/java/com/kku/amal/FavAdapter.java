package com.kku.amal;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FavAdapter  extends ArrayAdapter<Fav> {

        private Context mContext;
        private List<Fav> list = new ArrayList<>();

    public FavAdapter(@NonNull Context context,  ArrayList<Fav> list) {
            super(context, 0 , list);
            mContext = context;
            this.list = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.fav_list,parent,false);

            Fav sentence = list.get(position);

            TextView sentenceTV = (TextView) listItem.findViewById(R.id.textView);
            sentenceTV.setText(sentence.getSentence());

            return listItem;
        }
    }