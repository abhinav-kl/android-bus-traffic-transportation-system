package com.example.androidtraffic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DriverHome extends AppCompatActivity {

    TextView driver_name, driver_email, driver_phone;
    ImageView imageView;
    Button btn;
    SharedPreferences sh;
    FloatingActionButton fab;
    String url = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        driver_name = findViewById(R.id.textView3);
        driver_phone = findViewById(R.id.textView10);
        driver_email = findViewById(R.id.textView11);
        imageView = findViewById(R.id.imageView3);
        btn = findViewById(R.id.button5);
        fab = findViewById(R.id.floatingActionButton);

        url = sh.getString("url", "") + "driver_card";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                /// passing data from pycharm
                                driver_name.setText(jsonObj.getString("name"));
                                driver_phone.setText(jsonObj.getString("phone"));
                                driver_email.setText(jsonObj.getString("email"));

                                String image = jsonObj.getString("image");
                                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String ip = sh.getString("ip", "");
                                String url = "http://" + ip + ":8000" + image;
                                Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).into(imageView);//circle


                            } else {
                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {

            //                value Passing android to python
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();

                params.put("lid", sh.getString("lid", ""));//passing to python
//                params.put("p", password);


                return params;
            }
        };


        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ij = new Intent(getApplicationContext(), TrafficBlock.class);
                startActivity(ij);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent location = new Intent(getApplicationContext(), Locationservice.class);
                stopService(location);
                Intent ij = new Intent(getApplicationContext(), IpPage.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ij);
            }
        });

    }
}