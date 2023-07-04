package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Sign_In extends AppCompatActivity {

   ArrayList<health_info_model> arrHealth_info = new ArrayList<>();

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    RecyclerView recyclerView;

    DatabaseReference databaseReference;
    private FirebaseUser firebaseuser;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    health_info_Adapter show_adapter;
    ArrayList<health_info_model> list;

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(firebaseuser.getUid()).child("Health Information");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        show_adapter = new health_info_Adapter(this,list);
        recyclerView.setAdapter(show_adapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    health_info_model user = dataSnapshot.getValue(health_info_model.class);
                    user.setKey(dataSnapshot.getKey());
                    list.add(user);
                    progressBar.setVisibility(View.GONE);
                }

                show_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        show_adapter.setOnItemClickListener(new health_info_Adapter.LongClickListener() {
            @Override
            public View.OnLongClickListener onItemClick(int position, View view) {
                health_info_model show = show_adapter.getItemAt(position);
                if (show == null) return null;

                key = show.getKey();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Sign_In.this);

                alertDialog.setTitle("Operation!");
                alertDialog.setIcon(R.drawable.ic_baseline_info_24);
                alertDialog.setMessage("Delete or Update Your Health Information.");

                alertDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        databaseReference.child(key).removeValue();

                        Toast.makeText(Sign_In.this, "Health Information Deleted!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Sign_In.this, Sign_In.class);
                        startActivity(intent);
                        finish();
                    }
                });

                alertDialog.setNegativeButton("Upadte", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Dialog Udialog = new Dialog(Sign_In.this);
                        Udialog.setContentView(R.layout.update_layout);

                        systolic_pressure=(EditText) Udialog.findViewById(R.id.usp);
                        diastolic_pressure=(EditText)Udialog.findViewById(R.id.udp);
                        heart_rate=(EditText)Udialog.findViewById(R.id.uhr);

                        Button button_up = Udialog.findViewById(R.id.button_update);

                        button_up.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int y=1;
                                String Systolic_Pressure = systolic_pressure.getText().toString().trim();
                                String Diastolic_Pressure =  diastolic_pressure.getText().toString().trim();
                                String Heart_Rate = heart_rate.getText().toString().trim();


                                int S_P =0, D_P = 0, H_R = 0;

                                try {
                                    S_P = Integer.parseInt(Systolic_Pressure);
                                    D_P = Integer.parseInt(Diastolic_Pressure);
                                    H_R = Integer.parseInt(Heart_Rate);
                                }catch (Exception ignored){}


                                if(Systolic_Pressure.isEmpty()){
                                    systolic_pressure.setError("Required!");
                                    systolic_pressure.requestFocus();
                                    y=0;
                                    return;
                                }
                                if(S_P>200 || S_P<80 ){
                                    systolic_pressure.setError("Invalid!");
                                    systolic_pressure.requestFocus();
                                    y=0;
                                    return;
                                }

                                if(Diastolic_Pressure.isEmpty()){
                                    diastolic_pressure.setError("Required!");
                                    diastolic_pressure.requestFocus();
                                    y=0;
                                    return;
                                }

                                if(D_P>120|| D_P<40){
                                    diastolic_pressure.setError("Invalid!");
                                    diastolic_pressure.requestFocus();
                                    y=0;
                                    return;
                                }

                                if(Heart_Rate.isEmpty()){
                                    heart_rate.setError("Required!");
                                    heart_rate.requestFocus();
                                    y=0;
                                    return;
                                }

                                if(H_R>200 || H_R<40){
                                    heart_rate.setError("Invalid!");
                                    heart_rate.requestFocus();
                                    y=0;
                                    return;
                                }



                                if(S_P<90 && D_P<60) {
                                    Status="Low Blood Pressure.";
                                }
                                else if(S_P<120 && D_P<80) {
                                    Status="Normal.";
                                }

                                else if((S_P<130 || S_P>119) && (D_P<80)) {
                                    Status="ELEVATED.";
                                }
                                else if((S_P<140 || S_P>129) && (D_P<90 && D_P>=80)) {
                                    Status="Stage 1 Hypertension.";
                                }
                                else if(S_P>=140 && D_P>=90){
                                    Status="Stage 2 Hypertension.";
                                }else if(S_P>=180 && D_P>120){
                                    Status="Hypertension Crisis.";
                                }
                                else {
                                    Status="Undefine.";
                                }

                                if(y==1){

                                    firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");

                                    reference.child(firebaseuser.getUid()).child("Health Information").child(key).child("diastolic_pressure").setValue(Diastolic_Pressure);
                                    reference.child(firebaseuser.getUid()).child("Health Information").child(key).child("systolic_pressure").setValue(Systolic_Pressure);
                                    reference.child(firebaseuser.getUid()).child("Health Information").child(key).child("heart_rate").setValue(Heart_Rate);
                                    reference.child(firebaseuser.getUid()).child("Health Information").child(key).child("Status").setValue(Status);

                                    Toast.makeText(Sign_In.this, "Information Update Successfully Done.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Sign_In.this, Sign_In.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });
                        Udialog.setCancelable(true);
                        Udialog.show();

                    }
                });


                alertDialog.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                alertDialog.show();
                return null;
            }
        });




        button1 = findViewById(R.id.insert);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(Sign_In.this);
                dialog.setContentView(R.layout.insert_layout);

                date =(TextView) dialog.findViewById(R.id.date);
                time = (TextView) dialog.findViewById(R.id.time);
                systolic_pressure=(EditText) dialog.findViewById(R.id.sp);
                diastolic_pressure=(EditText) dialog.findViewById(R.id.dp);
                heart_rate=(EditText)dialog.findViewById(R.id.hr);


                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Calendar calendar = Calendar.getInstance();
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int month = calendar.get(Calendar.MONTH);
                        int year = calendar.get(Calendar.YEAR);

                        picker = new DatePickerDialog(Sign_In.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                date.setText(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);

                        picker.show();
                    }
                });

                time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        calendar = Calendar.getInstance();
                        currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                        curretMinute = calendar.get(Calendar.MINUTE);

                        Tpicker = new TimePickerDialog(Sign_In.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                if (i >= 12) {
                                    ampm = "PM";
                                } else {
                                    ampm = "AM";
                                }
                                if (i > 12 || (i == 12 && i1 > 0)) {
                                    time.setText(String.format("%02d:%02d", i - 12, i1) + ampm);
                                } else {
                                    time.setText(String.format("%02d:%02d", i, i1) + ampm);
                                }
                            }
                        }, currentHour, curretMinute, false);
                        Tpicker.show();
                    }
                });


                Button button_in = dialog.findViewById(R.id.button_insert);
                button_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int x=1;

                        String Date = date.getText().toString().trim();
                        String Time = time.getText().toString().trim();
                        String Systolic_pressure = systolic_pressure.getText().toString().trim();
                        String Diastolic_pressure =  diastolic_pressure.getText().toString().trim();
                        String Heart_rate = heart_rate.getText().toString().trim();

                        int S_P = 0, D_P = 0, H_R = 0;

                        try {
                            S_P = Integer.parseInt(Systolic_pressure);
                            D_P = Integer.parseInt(Diastolic_pressure);
                            H_R = Integer.parseInt(Heart_rate);
                        }catch (Exception ignored){}


                        if(Date.isEmpty()){
                            date.setError("Required!");
                            date.requestFocus();
                            x=0;
                            return;
                        }

                        if(Time.isEmpty()){
                            time.setError("Required!");
                            time.requestFocus();
                            x=0;
                            return;
                        }

                        if(Systolic_pressure.isEmpty()){
                            systolic_pressure.setError("Required!");
                            systolic_pressure.requestFocus();
                            x=0;
                            return;
                        }
                        if(S_P>200 || S_P<80 ){
                            systolic_pressure.setError("Invalid!");
                            systolic_pressure.requestFocus();
                            x=0;
                            return;
                        }

                        if(Diastolic_pressure.isEmpty()){
                            diastolic_pressure.setError("Required!");
                            diastolic_pressure.requestFocus();
                            x=0;
                            return;
                        }

                        if(D_P>120 || D_P<40){
                            diastolic_pressure.setError("Invalid!");
                            diastolic_pressure.requestFocus();
                            x=0;
                            return;
                        }

                        if((S_P -D_P<30)|| S_P<D_P){
                            systolic_pressure.setError("Invalid!");
                            systolic_pressure.requestFocus();
                            diastolic_pressure.setError("Invalid!");
                            diastolic_pressure.requestFocus();
                            x=0;
                        }

                        if(Heart_rate.isEmpty()){
                            heart_rate.setError("Required!");
                            heart_rate.requestFocus();
                            x=0;
                            return;
                        }

                        if(H_R>200 || H_R<40){
                            heart_rate.setError("Invalid!");
                            heart_rate.requestFocus();
                            x=0;
                            return;
                        }



                        if(S_P<90 && D_P<60) {
                            Status="Low Blood Pressure.";
                        }
                        else if(S_P<120 && D_P<80) {
                            Status="Normal.";
                        }

                        else if((S_P<130 || S_P>119) && (D_P<80)) {
                            Status="ELEVATED.";
                        }
                        else if((S_P<140 || S_P>129) && (D_P<90 || D_P>=80)) {
                            Status="Stage 1 Hypertension.";
                        }
                        else if(S_P>=140 && D_P>=90){
                            Status="Stage 2 Hypertension.";
                        }else if(S_P>=180 && D_P>120){
                            Status="Hypertension Crisis.";
                        }
                        else {
                            Status="Undefine.";
                        }


                        if(x==1){
                            Health_Info();
                        }
                    }
                });
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    private void Health_Info(){

        HashMap<String, Object> map = new HashMap<>();

        map.put("date",date.getText().toString().trim());
        map.put("time",time.getText().toString().trim());
        map.put("systolic_pressure",systolic_pressure.getText().toString().trim());
        map.put("diastolic_pressure", diastolic_pressure.getText().toString().trim());
        map.put("heart_rate",heart_rate.getText().toString().trim());
        map.put("status",Status.trim());

        firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
        reference.child(firebaseuser.getUid()).child("Health Information").push()
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(Sign_In.this, "Health Information Update Successfully Done.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Sign_In.this, Sign_In.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Sign_In.this, "Health Information Update failed Try again!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}