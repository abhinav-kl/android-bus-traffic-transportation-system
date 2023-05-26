package com.example.androidtraffic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomViewComplaintReply extends BaseAdapter {


    private final Context context;
    String[] c_id, b_id, complaint, complaint_date, reply, reply_date;

    public CustomViewComplaintReply(Context applicationContext, String[] c_id, String[] b_id, String[] complaint, String[] complaint_date, String[] reply, String[] reply_date) {

        this.context = applicationContext;
        this.c_id = c_id;
        this.b_id = b_id;
        this.complaint = complaint;
        this.complaint_date = complaint_date;
        this.reply = reply;
        this.reply_date = reply_date;
    }

    @Override
    public int getCount() {
        return c_id.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_view_complaint_reply, null);//same class name

        } else {
            gridView = (View) view;

        }

        TextView tv1 = (TextView) gridView.findViewById(R.id.textView29);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView30);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView31);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView32);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView33);


        tv1.setTextColor(Color.BLACK);//color setting
        tv2.setTextColor(Color.BLACK);//color setting
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);


        tv1.setText(b_id[i]);
        tv2.setText(complaint[i]);
        tv3.setText(complaint_date[i]);
        tv4.setText(reply[i]);
        tv5.setText(reply_date[i]);
//
        return gridView;
    }
}