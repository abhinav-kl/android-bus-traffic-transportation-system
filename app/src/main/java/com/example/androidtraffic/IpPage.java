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

public class IpPage extends AppCompatActivity {
    EditText ip;
    Button b;
    SharedPreferences sh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip_page);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ip = findViewById(R.id.editTextTextPersonName6);
        b = findViewById(R.id.button7);

        // retrieve last data
        String ed = sh.getString("ip", "");
        ip.setText(ed);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipaddress = ip.getText().toString();

                int flag = 0;
                if (ipaddress.equalsIgnoreCase("")) {
                    ip.setError("This Field is Required");
                    flag++;
                }
                if (flag == 0) {
                SharedPreferences.Editor editor = sh.edit();
                editor.putString("ip", ipaddress);
                editor.putString("url", "http://" + ipaddress + ":8000/");
                editor.apply();
                Toast.makeText(IpPage.this, " Welcome " + ipaddress, Toast.LENGTH_SHORT).show();

                Intent ij = new Intent(getApplicationContext(), Login.class);
                startActivity(ij);
                }
            }
        });
    }

}