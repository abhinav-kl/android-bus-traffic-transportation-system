package com.example.androidtraffic;

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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewStops extends AppCompatActivity {

    ListView list;
    SharedPreferences sh;
    String[] s_id, l_latitude, l_longitude, s_stops, s_route_start, s_route_end, s_arr_time, s_dep_time;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_stops);
        list = (ListView) findViewById(R.id.viewstops);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url = sh.getString("url", "") + "view_bus_stops";

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
                                s_id = new String[js.length()];
//                                rid = new String[js.length()];
                                l_latitude = new String[js.length()];
                                l_longitude = new String[js.length()];
                                s_route_start = new String[js.length()];
                                s_route_end = new String[js.length()];
                                s_stops = new String[js.length()];
                                s_arr_time = new String[js.length()];
                                s_dep_time = new String[js.length()];


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    s_id[i] = u.getString("id");//dbcolumn name in double quotes
//                                    rid[i] = u.getString("rid");//dbcolumn name in double quotes
                                    l_latitude[i] = u.getString("lati");//dbcolumn name in double quotes
                                    l_longitude[i] = u.getString("longi");//dbcolumn name in double quotes
                                    s_route_start[i] = u.getString("route_start");
                                    s_route_end[i] = u.getString("route_end");
                                    s_stops[i] = u.getString("stop");
                                    s_arr_time[i] = u.getString("arrival");
                                    s_dep_time[i] = u.getString("departure");


                                }
                                list.setAdapter(new CustomViewStops(getApplicationContext(), s_id, l_latitude, l_longitude, s_route_start, s_route_end, s_stops, s_arr_time, s_dep_time));//custom_view_service.xml and li is the listview object


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
                params.put("rid", sh.getString("rid", ""));//passing to python
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