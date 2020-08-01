package com.kku.amal;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static android.content.Context.MODE_PRIVATE;

public class CollectionsDetailsAdapter extends ArrayAdapter<Collections> {

    private Context mContext;
    private List<Collections> list = new ArrayList<>();
    int colorprefs;
    int txtprefs;

    public CollectionsDetailsAdapter(@NonNull Context context, ArrayList<Collections> list) {
        super(context, 0, list);
        mContext = context;
        this.list = list;
        SharedPreferences prefs = context.getSharedPreferences("bgColour", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

         txtprefs = prefs.getInt("txt", 1);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.col_list, parent, false);

        Collections c = list.get(position);


        TextView whole = (TextView) listItem.findViewById(R.id.textView);
        whole.setText(c.getCollection());
        whole.setTextColor(txtprefs);

        return listItem;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

}