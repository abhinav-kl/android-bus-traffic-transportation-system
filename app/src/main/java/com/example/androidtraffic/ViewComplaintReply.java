package com.example.androidtraffic;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewComplaintReply extends AppCompatActivity {

    ListView list;

    String[] c_id, b_id, complaint, complaint_date, reply, reply_date;
    SharedPreferences sh;
    String url = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint_reply);

        list = findViewById(R.id.replylist);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url = sh.getString("url", "") + "view_reply";

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.passComplaint);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.passHome) {
                startActivity(new Intent(getApplicationContext(), SearchBus.class));
                return true;
            } else if (itemId == R.id.passComplaint) {
                return true;
            } else if (itemId == R.id.passLogout) {
                Intent ij = new Intent(getApplicationContext(), IpPage.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ij);
            }
            return false;
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                JSONArray js = jsonObj.getJSONArray("data");//from python
                                c_id = new String[js.length()];
                                b_id = new String[js.length()];
                                complaint = new String[js.length()];
                                complaint_date = new String[js.length()];
                                reply = new String[js.length()];
                                reply_date = new String[js.length()];


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    c_id[i] = u.getString("id");//dbcolumn name in double quotes
                                    b_id[i] = u.getString("b_id");//dbcolumn name in double quotes
                                    complaint[i] = u.getString("complaints");
                                    complaint_date[i] = u.getString("complaint_date");
                                    reply[i] = u.getString("reply");
                                    reply_date[i] = u.getString("reply_date");


                                }
                                list.setAdapter(new CustomViewComplaintReply(getApplicationContext(), c_id, b_id, complaint, complaint_date, reply, reply_date));//custom_view_service.xml and li is the listview object


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
                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", sh.getString("lid", ""));//passing to python
                return params;
            }
        };


        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);

    }
}