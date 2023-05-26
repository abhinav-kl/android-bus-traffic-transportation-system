package com.example.androidtraffic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PassengerRegistration extends AppCompatActivity {

    SharedPreferences sh;
    EditText passenger_name, passenger_email, passenger_phone, passenger_password;

    String url="";
    ProgressDialog process;
    Bitmap bitmap = null;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_registration);
        sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ip","");
        url=sh.getString("url","")+"passenger_reg";

        passenger_name = (EditText) findViewById(R.id.editTextTextPersonName3);
        passenger_phone = (EditText) findViewById(R.id.editTextPhone);
        passenger_email = (EditText) findViewById(R.id.editTextTextEmailAddress);
        passenger_password = (EditText) findViewById(R.id.editTextTextPassword);
        btn = (Button) findViewById(R.id.button3);
        url = sh.getString("url", "") + "passenger_reg";

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=passenger_name.getText().toString();
                String email=passenger_email.getText().toString();
                String phone=passenger_phone.getText().toString();
                String password=passenger_password.getText().toString();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String numberPattern = "\\d{10}$";
                String passwordPattern = "[a-zA-Z0-9]{6,}";

                int flag = 0;

                if (name.equalsIgnoreCase("")) {
                    passenger_name.setError("Name is Required!");
                    flag++;
                }
                if (phone.matches(numberPattern)) {
                    Toast.makeText(PassengerRegistration.this, "Valid", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PassengerRegistration.this, "Invalid", Toast.LENGTH_SHORT).show();
                    passenger_phone.setError("Phone is Required!");
                    flag++;
                }

                if (email.matches(emailPattern)) {
                    Toast.makeText(PassengerRegistration.this, "Email is Valid", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PassengerRegistration.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    passenger_email.setError("Email is Required!");
                    flag++;
                }

                if (password.matches(passwordPattern)) {
                    Toast.makeText(PassengerRegistration.this, "Valid", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PassengerRegistration.this, "Invalid Check Password", Toast.LENGTH_SHORT).show();
                    passenger_password.setError("Password is Required!");
                    flag++;
                }


                if (flag == 0) {
                    uploadBitmap(name, email, phone, password);
                }
            }
        });
    }
    private void uploadBitmap(final String name, final String email, final String phone, final String password) {


        process = new ProgressDialog(PassengerRegistration.this);
        process.setMessage("Uploading....");
        process.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            process.dismiss();


                            JSONObject obj = new JSONObject(new String(response.data));

                            if(obj.getString("status").equals("ok")){
                                Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), Login.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Registration failed" ,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("name", name);//passing to python
                params.put("phone", phone);
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

}