package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;

public class Sign_In extends AppCompatActivity {

   // ArrayList<health_info_model> arrHealth_info = new ArrayList<>();

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    private FirebaseUser firebaseuser;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    //health_info_Adapter show_adapter;
    //ArrayList<health_info_model> list;

    ImageView imageMenu;

    private DatePickerDialog picker;
    private TimePickerDialog Tpicker;
    private Calendar calendar;
    private int currentHour;
    private int curretMinute;
    private String ampm;

    private FloatingActionButton button1;

    public String key;


    private TextView date ,time ;
    private EditText systolic_pressure, diastolic_pressure,heart_rate;
    private  String Status ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager((this)));

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_View);
        imageMenu = findViewById(R.id.imageMenu);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        if (user == null) {
            startActivity(new Intent(Sign_In.this, MainActivity.class));
            finish();
        }


        toggle = new ActionBarDrawerToggle(Sign_In.this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent;
                switch (item.getItemId()) {

                    case R.id.mHome:
                        Toast.makeText(Sign_In.this, "Clicked to Home", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.mProfile:
                        Toast.makeText(Sign_In.this, "Personal Information", Toast.LENGTH_SHORT).show();
                        intent = new Intent(Sign_In.this,Profile.class) ;
                        startActivity(intent);
                        break;

                    case R.id.mAbout_App:
                        Toast.makeText(Sign_In.this, "About App", Toast.LENGTH_SHORT).show();
                        intent = new Intent(Sign_In.this,About.class) ;
                        startActivity(intent);
                        break;

                    case R.id.mlog_out:
                        firebaseAuth.signOut();
                        Toast.makeText(Sign_In.this, "Log Out Successful.", Toast.LENGTH_SHORT).show();
                        intent = new Intent(Sign_In.this,MainActivity.class) ;
                        startActivity(intent);
                        finish();
                        break;
                }

                return false;
            }
        });


        imageMenu = findViewById(R.id.imageMenu);

        imageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
}