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

public class Login extends AppCompatActivity {
    EditText email, password;
    SharedPreferences sh;
    Button btn1, btn2;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Toast.makeText(this, "" + sh.getString("ip", ""), Toast.LENGTH_SHORT).show();
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword5);
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = email.getText().toString();
                final String user_password = password.getText().toString();
                url = sh.getString("url", "") + "login";// url name from pycharm fun android_login

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                                    int flag = 0;
                                    if (username.equalsIgnoreCase("")) {
                                        email.setError("Email is Required");
                                        flag++;
                                    }
                                    if (user_password.equalsIgnoreCase("")) {
                                        password.setError("Password is Required");
                                        flag++;
                                    }
                                    if (flag == 0) {

                                    try {
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) { // status
//                                        Toast.makeText(MainActivity.this, "welcome", Toast.LENGTH_SHORT).show();
                                            String typ = jsonObj.getString("type"); // user type from pycharm
                                            String id = jsonObj.getString("lid"); // login id from pycharm

                                            if (typ.equalsIgnoreCase("Driver")) {
                                                Toast.makeText(getApplicationContext(), "Driver", Toast.LENGTH_LONG).show();
                                                Intent location = new Intent(getApplicationContext(), Locationservice.class);
                                                startService(location);
                                                Intent i = new Intent(getApplicationContext(), DriverHome.class);
                                                startActivity(i);
                                            } else if (typ.equalsIgnoreCase("Passenger")) {
                                                Toast.makeText(getApplicationContext(), "Passenger", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(getApplicationContext(), SearchBus.class);
                                                startActivity(i);
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
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

                        params.put("username", username);//passing to python
                        params.put("password", user_password);


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
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PassengerRegistration.class);
                startActivity(i);
            }
        });
    }
}