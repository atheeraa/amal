package com.kku.amal;

import android.content.Context;
import android.content.Intent;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class FavAdapter extends ArrayAdapter<Fav> {

    private Context mContext;
    private List<Fav> list = new ArrayList<>();

    public FavAdapter(@NonNull Context context, ArrayList<Fav> list) {
        super(context, 0, list);
        mContext = context;
        this.list = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.fav_list, parent, false);

        Fav sentence = list.get(position);


        TextView whole = (TextView) listItem.findViewById(R.id.wholetext);
        whole.setText(sentence.getSentence());
        whole.setVisibility(View.GONE);

        String substring = sentence.getSentence().substring(0,25)+" ... ";

        TextView sentenceTV = (TextView) listItem.findViewById(R.id.textView);
        sentenceTV.setText(substring);



        String ItemName = sentence.getSentence();
        Intent intent = new Intent("custom-message");
         intent.putExtra("item",ItemName);
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);


        return listItem;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}