package com.furkandemir.onlineyoklama;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String> {

    private String[] text;
    private String[] number;
    private Activity context;

    public CustomListAdapter(Activity context, String[] text, String[] number) {
        super(context, R.layout.simple_list_item, text);

        this.context = context;
        this.text = text;
        this.number = number;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewHolder = null;

        if(r == null){
            LayoutInflater layoutInflater  = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.simple_list_item, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.tv_text.setText(text[position]);
        viewHolder.tv_number.setText(number[position]);

        return r;
    }

    class ViewHolder{
        TextView tv_text;
        TextView tv_number;

        ViewHolder(View v){
            tv_text = v.findViewById(R.id.txt_list);
            tv_number = v.findViewById(R.id.txt_lan);
        }
    }
}
