package com.example.barterplaceui;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import services.Message;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Message> data;

    public CustomAdapter(Context context, ArrayList<Message> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null);
            holder.txtMessage = (TextView) view.findViewById(R.id.message);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.txtMessage.setText("Barter " + data.get(position).getAnswer());
        holder.txtMessage.setTextSize(22);
        holder.status = data.get(position).getStatus();

        if (holder.status.equals("created")) {              //mark message that doesn't readed as bold
            holder.txtMessage.setTypeface(null, Typeface.BOLD);
        } else {
            holder.txtMessage.setTypeface(null, Typeface.NORMAL);
        }
        return view;
    }

    class ViewHolder {
        TextView txtMessage;
        String status;
    }
}
