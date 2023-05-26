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

public class TrafficBlock extends AppCompatActivity {

    ListView list;
    SharedPreferences sh;
    String[] b_id, block_date, block_route_s, block_route_e, block_stop;
    String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_block);
        list = (ListView) findViewById(R.id.trafficblocks);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url = sh.getString("url", "") + "view_traffic_block";

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
                                b_id = new String[js.length()];
                                block_date = new String[js.length()];
                                block_route_s = new String[js.length()];
                                block_route_e = new String[js.length()];
                                block_stop = new String[js.length()];


                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    b_id[i] = u.getString("id");//dbcolumn name in double quotes
                                    block_date[i] = u.getString("date");
                                    block_route_s[i] = u.getString("route_id_id");
                                    block_route_e[i] = u.getString("route_id");
                                    block_stop[i] = u.getString("stop_id_id");

                                }
                                list.setAdapter(new CustomTrafficBlock(getApplicationContext(), b_id, block_date, block_route_s, block_route_e, block_stop));//custom_view_service.xml and li is the listview object


                            } else {
                                Toast.makeText(getApplicationContext(), "Not found ", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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