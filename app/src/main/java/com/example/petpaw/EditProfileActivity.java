package com.example.petpaw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputLayout  nameEdit, addressEdit, passEdit;
    private ImageButton saveButton;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        nameEdit = findViewById(R.id.nameEdit);
        addressEdit = findViewById(R.id.addressEdit);
        passEdit = findViewById(R.id.passEdit);
        saveButton = findViewById(R.id.saveButton);

        Intent intent = getIntent();
        String userFullname = intent.getStringExtra("nameFromDB");
        String userPassword = intent.getStringExtra("passwordFromDB");
        String userAddress = intent.getStringExtra("addressFromDB");
        String userPhone = intent.getStringExtra("phoneFromDB");

        nameEdit.getEditText().setText(userFullname);
        addressEdit.getEditText().setText(userAddress);
        passEdit.getEditText().setText(userPassword);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = nameEdit.getEditText().getText().toString().trim();
                String updatedAddress = addressEdit.getEditText().getText().toString().trim();
                String updatedPassword = passEdit.getEditText().getText().toString().trim();

                if(updatedName.equals("")){
                    nameEdit.setError("Please type a Name");
                }
                else if(updatedAddress.equals("")){
                    addressEdit.setError("Please type an Address");
                }
                else if(updatedPassword.equals("")){
                    passEdit.setError("Please type a Password");
                }
                else{
                    if(updatedName.equals(userFullname) && updatedAddress.equals(userAddress) && updatedPassword.equals(userPassword)){
                        finish();
                    }
                    else{
                        databaseReference.child(userPhone).child("fullname").setValue(updatedName);
                        databaseReference.child(userPhone).child("address").setValue(updatedAddress);
                        databaseReference.child(userPhone).child("password").setValue(updatedPassword);

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("nameFromDB", updatedName);
                        intent.putExtra("passwordFromDB", updatedPassword);
                        intent.putExtra("addressFromDB", updatedAddress);
                        intent.putExtra("phoneFromDB", userPhone);
                        finishAffinity();
                        startActivity(intent);
                    }
                }
            }
        });
    }
}