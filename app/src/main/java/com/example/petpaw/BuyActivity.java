package com.example.petpaw;

import android.os.Bundle;
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

public class BuyActivity extends AppCompatActivity {

    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;
    private List<CreatePost> postList;
    DatabaseReference databaseReference;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        postRecyclerView = findViewById(R.id.postRecyclerView);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    CreatePost createPost = dataSnapshot1.getValue(CreatePost.class);
                    assert createPost != null;
                    if(createPost.getType().equals("Sell")){
                        postList.add(0, createPost);
                    }
                }

                postAdapter = new PostAdapter(BuyActivity.this, postList);
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Oops! database error occurred", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
