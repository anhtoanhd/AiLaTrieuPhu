package com.toanpt.ailatrieuphu.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.toanpt.ailatrieuphu.R;

import java.util.ArrayList;

public class LeftAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> list = new ArrayList<>();

    public LeftAdapter(Context context) {
        this.context = context;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.row_left, viewGroup, false);
            holder.tvLeft = view.findViewById(R.id.tvLeft);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if(position == 0 || position == 5 || position == 10){
            holder.tvLeft.setText(list.get(position));
            holder.tvLeft.setTextSize(30);
            holder.tvLeft.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.tvLeft.setText(list.get(position));
        }

        return view;
    }

    public class ViewHolder{
        TextView tvLeft;
    }
}
