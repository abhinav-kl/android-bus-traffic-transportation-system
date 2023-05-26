package com.example.androidtraffic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomViewStops extends BaseAdapter {


    private final Context context;
    String[] s_id, l_latitude, l_longitude, s_stops, s_route_start, s_route_end, s_arr_time, s_dep_time;

    public CustomViewStops(Context applicationContext, String[] s_id, String[] l_latitude, String[] l_longitude, String[] s_route_start, String[] s_route_end, String[] s_stops, String[] s_arr_time, String[] s_dep_time) {

        this.context = applicationContext;
        this.s_id = s_id;
//        this.rid = rid;
        this.l_latitude = l_latitude;
        this.l_longitude = l_longitude;
        this.s_route_start = s_route_start;
        this.s_route_end = s_route_end;
        this.s_stops = s_stops;
        this.s_arr_time = s_arr_time;
        this.s_dep_time = s_dep_time;
    }


    @Override
    public int getCount() {
        return s_id.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_stops, null);//same class name

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView14);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView15);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView12);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView17);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView19);
        Button button = gridView.findViewById(R.id.button10);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Tracking", Toast.LENGTH_SHORT).show();
                Intent location = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(("google.navigation:q= " + l_latitude[i] + ", " + l_longitude[i] + " ")));
                location.setPackage("com.google.android.apps.maps");
                location.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(location);
            }
        });

        tv1.setTextColor(Color.BLACK);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLUE);
        tv5.setTextColor(Color.GREEN);


        tv1.setText(s_route_start[i]);
        tv2.setText(s_route_end[i]);
        tv3.setText(s_stops[i]);
        tv4.setText(s_arr_time[i]);
        tv5.setText(s_dep_time[i]);
//
        return gridView;

    }
}