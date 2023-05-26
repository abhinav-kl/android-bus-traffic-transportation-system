package com.example.androidtraffic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.androidtraffic.databinding.ActivityPassengerHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

public class PassengerHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPassengerHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPassengerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarPassengerHome.toolbar);
        binding.appBarPassengerHome.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ij = new Intent(getApplicationContext(), SearchBus.class);
                startActivity(ij);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
//         Passing each menu ID as a set of Ids because each
//         menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_complaints, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_passenger_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        View hd=navigationView.getHeaderView(0);

        TextView name=(TextView)hd.findViewById(R.id.username);
        TextView email=(TextView)hd.findViewById(R.id.email);

        name.setText(sh.getString("name",""));
        email.setText(sh.getString("email",""));
        String ip = sh.getString("ip", "");
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.passenger_home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_passenger_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
//
        if(id == R.id.nav_home) {
            Intent i = new Intent(getApplicationContext(), SearchBus.class);
            startActivity(i);
        }
        else if (id == R.id.nav_complaints) {
            Intent ij = new Intent(getApplicationContext(), ViewComplaintReply.class);
            startActivity(ij);
        }
        else if (id == R.id.nav_logout) {
            Intent ij = new Intent(getApplicationContext(), IpPage.class);
            ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ij.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(ij);
        }

        return false;
    }
}