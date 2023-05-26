package com.example.androidtraffic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomSearchBus extends BaseAdapter {

    String[] b_id, rid, bus_name, route_start, route_end;
    SharedPreferences sh;
    private final Context context;

    public CustomSearchBus(Context applicationContext, String[] b_id, String[] rid, String[] bus_name, String[] route_start, String[] route_end) {

        this.context = applicationContext;
        this.b_id = b_id;
        this.rid = rid;
        this.bus_name = bus_name;
        this.route_start = route_start;
        this.route_end = route_end;
    }


    @Override
    public int getCount() {
        return b_id.length;
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
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.activity_custom_search_bus,null);//same class name

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView20);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView21);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView22);
        Button buttonstop = gridView.findViewById(R.id.button3);
        buttonstop.setTag(i);
        Button button1 = gridView.findViewById(R.id.feedback_rating);
        button1.setTag(i);
        Button button2 = gridView.findViewById(R.id.complaint);
        button2.setTag(i);


        buttonstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ik= (int) v.getTag();

                Toast.makeText(context, "hiiiiiiiii", Toast.LENGTH_SHORT).show();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("rid", rid[ik]);
                ed.apply();
                Intent ij=new Intent(context,ViewStops.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ij);

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ik= (int) v.getTag();
                sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor=sh.edit();
                editor.putString("b_id",b_id[ik]);
                editor.apply();
//                    Toast.makeText(context, ""+d[ik], Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, Feedback.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ik= (int) v.getTag();
                sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor=sh.edit();
                editor.putString("b_id",b_id[ik]);
                editor.apply();
//                    Toast.makeText(context, ""+d[ik], Toast.LENGTH_SHORT).show();

                Intent i = new Intent(context, RegisterComplaint.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        tv1.setTextColor(Color.BLACK);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);


        tv1.setText(bus_name[i]);
        tv2.setText(route_start[i]);
        tv3.setText(route_end[i]);

        return gridView;


    }

}