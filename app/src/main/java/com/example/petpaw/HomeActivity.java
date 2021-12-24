package com.example.petpaw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {
    private ImageView create_post_img;
    private ImageView profile_img;
    private ImageView recent_img;
    private ImageView adoption_img;
    private ImageView buy_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        create_post_img = findViewById(R.id.create_post_img);
        profile_img = findViewById(R.id.profile_img);
        recent_img = findViewById(R.id.recent_img);
        adoption_img = findViewById(R.id.adoption_img);
        buy_img = findViewById(R.id.buy_img);

        Intent intent = getIntent();
        String userFullname = intent.getStringExtra("nameFromDB");
        String userPassword = intent.getStringExtra("passwordFromDB");
        String userAddress = intent.getStringExtra("addressFromDB");
        String userPhone = intent.getStringExtra("phoneFromDB");

        // Toast.makeText(getApplicationContext(), userFullname + "\n" + userPassword + "\n" + userAddress + "\n" + userPhone, Toast.LENGTH_SHORT).show();

        create_post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                intent.putExtra("nameFromDB", userFullname);
                intent.putExtra("phoneFromDB", userPhone);
                startActivity(intent);
            }
        });

        profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("nameFromDB", userFullname);
                intent.putExtra("passwordFromDB", userPassword);
                intent.putExtra("addressFromDB", userAddress);
                intent.putExtra("phoneFromDB", userPhone);
                startActivity(intent);
            }
        });

        recent_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, RecentActivity.class);
                startActivity(intent);
            }
        });

        adoption_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AdoptionActivity.class);
                startActivity(intent);
            }
        });

        buy_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });
    }
}
