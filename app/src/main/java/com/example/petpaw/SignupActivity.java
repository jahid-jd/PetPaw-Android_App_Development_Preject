package com.example.petpaw;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignupActivity extends AppCompatActivity {
    private EditText signup_name_edit;
    private EditText signup_phone_edit;
    private EditText signup_address_edit;
    private EditText signup_password_edit;
    private Button signupButton;
    private TextView have_account_signin;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        signup_name_edit = findViewById(R.id.signup_name_edit);
        signup_phone_edit = findViewById(R.id.signup_phone_edit);
        signup_address_edit = findViewById(R.id.signup_address_edit);
        signup_password_edit = findViewById(R.id.signup_password_edit);
        signupButton = findViewById(R.id.signupButton);
        have_account_signin = findViewById(R.id.have_account_signin);

        signup_name_edit.requestFocus();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        have_account_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    void signup(){
        String fullname = signup_name_edit.getText().toString().trim();
        String phone = signup_phone_edit.getText().toString().trim();
        String address = signup_address_edit.getText().toString().trim();
        String password = signup_password_edit.getText().toString().trim();

        databaseReference.orderByChild("phone").equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(fullname.equals("")){
                    signup_name_edit.setError("Enter your Full Name");
                }
                else if(phone.equals("")){
                    signup_phone_edit.setError("Enter your Phone Number");
                }
                else if(address.equals("")){
                    signup_address_edit.setError("Enter your Address");
                }
                else if(password.equals("")){
                    signup_password_edit.setError("Enter your Password");
                }
                else if(snapshot.getValue() != null){
                    signup_phone_edit.setError("This number is already registered");
                }
                else{
                    SignupHandler one_user = new SignupHandler(fullname, phone, address, password);
                    databaseReference.child(phone).setValue(one_user);
                    Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Oops! Database error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
