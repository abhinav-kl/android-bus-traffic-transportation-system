package com.example.androidtraffic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomTrafficBlock extends BaseAdapter {

    private final Context context;
    String[] b_id, block_date, block_route_s, block_route_e, block_stop;

    public CustomTrafficBlock(Context applicationContext, String[] b_id, String[] block_date, String[] block_route_s, String[] block_route_e, String[] block_stop) {

        this.context = applicationContext;
        this.b_id = b_id;
        this.block_date = block_date;
        this.block_route_s = block_route_s;
        this.block_route_e = block_route_e;
        this.block_stop = block_stop;
    }

    @Override
    public int getCount() { return  b_id.length; }

    @Override
    public Object getItem(int i) { return null; }

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
//            gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_traffic_block, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView68);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView66);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView50);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView67);


        tv1.setTextColor(Color.BLACK);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);


        tv1.setText(block_date[i]);
        tv2.setText(block_route_s[i]);
        tv3.setText(block_route_e[i]);
        tv4.setText(block_stop[i]);

        return gridView;
    }
}