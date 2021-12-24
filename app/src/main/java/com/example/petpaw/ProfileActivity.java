package com.example.petpaw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private TextView usernameTextView, addressTextView, phoneTextView;
    private Button editButton;
    private ImageButton logoutButton;
    private RecyclerView myPostRecyclerView;
    private PostAdapter postAdapter;
    private List<CreatePost> postList;
    DatabaseReference databaseReference;

    private String userFullname;
    private String userPassword;
    private String userAddress;
    private String userPhone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        postList = new ArrayList<>();
        //databaseReference = FirebaseDatabase.getInstance().getReference("users");

        usernameTextView = findViewById(R.id.usernameTextView);
        addressTextView = findViewById(R.id.addressTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        editButton = findViewById(R.id.editButton);
        logoutButton = findViewById(R.id.logoutButton);


        myPostRecyclerView = findViewById(R.id.myPostRecyclerView);
        myPostRecyclerView.setHasFixedSize(true);
        myPostRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        userPhone = intent.getStringExtra("phoneFromDB");
        userFullname = intent.getStringExtra("nameFromDB");
        userPassword = intent.getStringExtra("passwordFromDB");
        userAddress = intent.getStringExtra("addressFromDB");


//        databaseReference.orderByChild("phone").equalTo(userPhone).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.getValue() != null) {
//                    userFullname = snapshot.child(userPhone).child("fullname").getValue(String.class);
//                    userAddress = snapshot.child(userPhone).child("address").getValue(String.class);
//                    userPassword = snapshot.child(userPhone).child("password").getValue(String.class);
//
//                    usernameTextView.setText(userFullname);
//                    addressTextView.setText(userAddress);
//                    phoneTextView.setText(userPhone);
//                    //Toast.makeText(getApplicationContext(), userFullname + "\n" + userAddress + "\n" + userPassword, Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getApplicationContext(), "Oops! database error occurred", Toast.LENGTH_SHORT).show();
//            }
//        });


        databaseReference = FirebaseDatabase.getInstance().getReference("posts");

        usernameTextView.setText(userFullname);
        addressTextView.setText(userAddress);
        phoneTextView.setText(userPhone);



        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("nameFromDB", userFullname);
                intent.putExtra("addressFromDB", userAddress);
                intent.putExtra("passwordFromDB", userPassword);
                intent.putExtra("phoneFromDB", userPhone);

                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                finishAffinity();
                startActivity(intent);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    CreatePost createPost = dataSnapshot1.getValue(CreatePost.class);
                    if(createPost.getPhone().equals(userPhone)){
                        postList.add(0, createPost);
                    }
                }
                postAdapter = new PostAdapter(ProfileActivity.this, postList);
                myPostRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Oops! database error occurred", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
