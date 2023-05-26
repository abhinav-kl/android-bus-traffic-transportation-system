package com.example.androidtraffic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterComplaint extends AppCompatActivity {

    EditText complaint;
    Button btn;

    SharedPreferences sh;

    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Toast.makeText(this, "" + sh.getString("ip", ""), Toast.LENGTH_SHORT).show();

        complaint = findViewById(R.id.editTextTextMultiLine);
        btn = findViewById(R.id.button4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String send_complaint = complaint.getText().toString();
                Toast.makeText(RegisterComplaint.this, "Message Send" , Toast.LENGTH_SHORT).show();
                url=sh.getString("url","")+"reg_complaint";

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok"))

                                    {
                                        Toast.makeText(getApplicationContext(), "Message Send", Toast.LENGTH_LONG).show();
                                        Intent ij = new Intent(getApplicationContext(), SearchBus.class);
                                        ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(ij);

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

                        params.put("complaints", send_complaint);//passing to python
                        params.put("id", sh.getString("lid",""));//passing to python
                        params.put("b_id", sh.getString("b_id",""));//passing to python
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
    }
}