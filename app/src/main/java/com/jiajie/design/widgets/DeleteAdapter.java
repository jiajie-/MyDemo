package com.jiajie.design.widgets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jiajie.design.R;

/**
 * Created by jiajie on 16/8/7.
 */
public class DeleteAdapter extends ArrayAdapter<String> {

    public DeleteAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_delete_view, null);
        } else {
            view = convertView;
        }
        TextView textView = (TextView) view.findViewById(R.id.text);
        textView.setText(getItem(position));
        return view;
    }
}
