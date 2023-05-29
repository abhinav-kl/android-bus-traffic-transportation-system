package com.example.androidtraffic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

public class SearchBus extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView li;
    EditText search_start, search_end;
    Button button;
    SharedPreferences sh;
    String[] b_id, rid, bus_name, route_start, route_end;
    String url="",url1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus);
        search_start = findViewById(R.id.editTextText);
        search_end = findViewById(R.id.editTextText2);
        button = findViewById(R.id.button9);
        li = (ListView) findViewById(R.id.findbus);
        li.setOnItemClickListener(this);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        url = sh.getString("url", "") + "search_results";

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.passHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.passHome) {
                return true;
            } else if (itemId == R.id.passComplaint) {
                startActivity(new Intent(getApplicationContext(), ViewComplaintReply.class));
                return true;
            } else if (itemId == R.id.passLogout) {
                Intent ij = new Intent(getApplicationContext(), IpPage.class);
                ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(ij);
            }
            return false;
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String start = search_start.getText().toString();
                final String end = search_end.getText().toString();

                url1=sh.getString("url","")+"routes";

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url1,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {

                                        JSONArray js = jsonObj.getJSONArray("data");//from python
                                        b_id = new String[js.length()];
                                        rid = new String[js.length()];
                                        bus_name= new String[js.length()];
                                        route_start= new String[js.length()];
                                        route_end= new String[js.length()];



                                        for (int i = 0; i < js.length(); i++) {
                                            JSONObject u = js.getJSONObject(i);
                                            b_id[i] = u.getString("id");//dbcolumn name in double quotes
                                            rid[i] = u.getString("rid");//dbcolumn name in double quotes
                                            bus_name[i] = u.getString("bus");
                                            route_start[i] = u.getString("route_start");
                                            route_end[i] = u.getString("route_end");
                                            Toast.makeText(SearchBus.this, ""+rid[i], Toast.LENGTH_SHORT).show();


                                        }
                                        li.setAdapter(new CustomSearchBus(getApplicationContext(), b_id, rid, bus_name, route_start, route_end));//custom_view_service.xml and li is the listview object


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
                        params.put("id", sh.getString("lid",""));//passing to python
                        params.put("start",start);
                        params.put("end",end);

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
                                b_id = new String[js.length()];
                                rid= new String[js.length()];
                                bus_name= new String[js.length()];
                                route_start= new String[js.length()];
                                route_end= new String[js.length()];



                                for (int i = 0; i < js.length(); i++) {
                                    JSONObject u = js.getJSONObject(i);
                                    b_id[i] = u.getString("id");//dbcolumn name in double quotes
                                    rid[i] = u.getString("rid");
                                    bus_name[i] = u.getString("bus");
                                    route_start[i] = u.getString("route_start");
                                    route_end[i] = u.getString("route_end");


                                }
                                li.setAdapter(new CustomSearchBus(getApplicationContext(), b_id, rid, bus_name, route_start, route_end));//custom_view_service.xml and li is the listview object


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
                params.put("id", sh.getString("lid",""));//passing to python
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
