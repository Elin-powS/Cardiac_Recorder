package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class Sign_Up extends AppCompatActivity {

    private EditText fullname, Email, Date_of_Birth, contact_number,username, password, confirm_password;
    private Button B3;
    private DatePickerDialog picker;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private TextView Exit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        firebaseAuth = FirebaseAuth.getInstance();

        fullname = (EditText) findViewById(R.id.Fullname);
        Email = (EditText) findViewById(R.id.Email);
        Date_of_Birth = (EditText) findViewById(R.id.Date_of_Birth);
        contact_number = (EditText) findViewById(R.id.contact_number);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        Exit=(TextView) findViewById(R.id.exit);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Sign_Up.this, "Exit", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(Sign_Up.this,MainActivity.class);
                startActivity(intent);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Date_of_Birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(Sign_Up.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        Date_of_Birth.setText(i2 + "/" + (i1 + 1) + "/" + i);
                    }
                }, year, month, day);

                picker.show();
            }
        });

        B3 = (Button) findViewById(R.id.B3);
        B3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = fullname.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String birthday = Date_of_Birth.getText().toString().trim();
                String phone_number = contact_number.getText().toString().trim();
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Confirm_password = confirm_password.getText().toString().trim();

                if (name.isEmpty()) {
                    fullname.setError("Required");
                    fullname.requestFocus();
                    return;
                }
                if (email.isEmpty()) {
                    Email.setError("Required");
                    Email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Email.setError("Please Provide valid email!");
                    Email.requestFocus();
                    return;
                }
                if (birthday.isEmpty()) {
                    Date_of_Birth.setError("Required");
                    Date_of_Birth.requestFocus();
                    return;
                }
                if (phone_number.isEmpty()) {
                    contact_number.setError("Required");
                    contact_number.requestFocus();
                    return;
                }
                if (!Patterns.PHONE.matcher(phone_number    ).matches()) {
                    contact_number.setError("Please Provide valid Phone Number!");
                    contact_number.requestFocus();
                    return;
                }
                if (Username.isEmpty()) {
                    username.setError("Required");
                    username.requestFocus();
                    return;
                }
                if (Password.isEmpty()) {
                    password.setError("Required");
                    password.requestFocus();
                    return;
                }
                if (Password.length() < 6) {
                    password.setError("Minimum password length should be 6 character!");
                    password.requestFocus();
                    return;
                }
                if (Confirm_password.isEmpty() || !Password.equals((Confirm_password))) {
                    confirm_password.setError("Not Matched");
                    confirm_password.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                Sign_In(email,Password);
            }
        });
    }

    private void Sign_In(String Email, String Password){
        firebaseAuth.createUserWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();
                            updateUi(uid, Email);
                        } else {
                            Toast.makeText(Sign_Up.this, "Sign In Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void updateUi(String uid, String email) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("name", fullname.getText().toString().trim());
        map.put("email", Email.getText().toString().trim());
        map.put("birthday", Date_of_Birth.getText().toString().trim());
        map.put("phone_number", contact_number.getText().toString().trim());
        map.put("Username", username.getText().toString().trim());


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("user");
        reference.child(uid).child("user information")
                .setValue(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Sign_Up.this, "Sign Up Successfully Done", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Sign_Up.this, Sign_In.class));
                            finish();
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Exception e = task.getException();
                            if(e == null) {
                                Toast.makeText(Sign_Up.this, "Sign Up Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(Sign_Up.this, "Sign Up Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}