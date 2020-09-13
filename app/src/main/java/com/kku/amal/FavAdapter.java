package com.kku.amal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
public class FavAdapter extends ArrayAdapter<Fav> {

    private Context mContext;
    private List<Fav> list = new ArrayList<>();

    public FavAdapter(@NonNull Context context, ArrayList<Fav> list) {
        super(context, 0, list);
        context = mContext;
        this.list = list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Fav sentence = getItem(position);


        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fav_list, parent, false);


        CardView card = convertView.findViewById(R.id.cardbg);
        LinearLayout itembg = convertView.findViewById(R.id.itembg);

        TextView whole = (TextView) convertView.findViewById(R.id.wholetext);

        whole.setText(sentence.getSentence());
        whole.setVisibility(View.GONE);

        int length = sentence.getSentence().length();
        String substring="";
        if(length<20)
            substring=sentence.getSentence()+ " ... ";
        else
        substring=sentence.getSentence().substring(0,20)+ " ... ";


        TextView sentenceTV = (TextView) convertView.findViewById(R.id.favsentence);
        sentenceTV.setText(substring);


        int[] androidColors = getContext().getResources().getIntArray(R.array.rainbow);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];



     card.setRadius(40);
     card.setCardBackgroundColor(randomAndroidColor);
       // itembg.setBackgroundColor(randomAndroidColor);
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}