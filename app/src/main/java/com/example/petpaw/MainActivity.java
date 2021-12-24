package com.example.petpaw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private EditText login_phone_edit;
    private EditText login_password_edit;
    private TextView no_account_signup;
    private Button loginButton;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        login_phone_edit = findViewById(R.id.login_phone_edit);
        login_password_edit = findViewById(R.id.login_password_edit);
        loginButton = findViewById(R.id.loginButton);
        no_account_signup = findViewById(R.id.no_account_signup);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        no_account_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    void signIn(){
        String phone = login_phone_edit.getText().toString().trim();
        String password = login_password_edit.getText().toString().trim();

        databaseReference.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(phone.equals("")){
                    login_phone_edit.setError("Enter a Phone Number");
                }
                else if(password.equals("")){
                    login_password_edit.setError("Enter a Password");
                }
                else if(snapshot.getValue() != null){
                    String passwordFromDB = snapshot.child(phone).child("password").getValue(String.class);
                    if (passwordFromDB == null) throw new AssertionError();
                    if(passwordFromDB.equals(password)){
                        String userFullname = snapshot.child(phone).child("fullname").getValue(String.class);
                        String userPassword = snapshot.child(phone).child("password").getValue(String.class);
                        String userAddress = snapshot.child(phone).child("address").getValue(String.class);
                        String userPhone = snapshot.child(phone).child("phone").getValue(String.class);

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("nameFromDB", userFullname);
                        intent.putExtra("passwordFromDB", userPassword);
                        intent.putExtra("addressFromDB", userAddress);
                        intent.putExtra("phoneFromDB", userPhone);

                        finishAffinity();
                        startActivity(intent);

                    }
                    else{
                        login_password_edit.setError("The password is incorrect");
                    }
                }
                else{
                    login_phone_edit.setError("The phone number does not exists");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Oops! Database error occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }
}