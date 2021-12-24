package com.example.petpaw;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private static final int IMAGE_REQUEST = 1;
    private Button postButton, addPhotoButton;
    private EditText descriptionEditText;
    private ImageView uploadImageView;
    private Uri postImageUri;
    private String userFullname;
    private String userPhone;
    private String type = "";
    private String category = "";

    Calendar calendar;

    String[] typeItems = {"Adoption", "Sell"};
    String[] categoryItems = {"Dog", "Cat", "Bird", "Fish", "Rabbit", "Mouse", "Others"};
    AutoCompleteTextView autoCompleteTextView1, autoCompleteTextView2;
    ArrayAdapter<String> typeArrayAdapter, categoryArrayAdapter;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageTask uploadTask;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Intent intent = getIntent();
        userFullname = intent.getStringExtra("nameFromDB");
        userPhone = intent.getStringExtra("phoneFromDB");

        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        storageReference = FirebaseStorage.getInstance().getReference("posts");

        postButton = findViewById(R.id.postButton);
        addPhotoButton = findViewById(R.id.addPhotoButton);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        descriptionEditText.requestFocus();
        uploadImageView = findViewById(R.id.uploadImageView);

        autoCompleteTextView1 = findViewById(R.id.autoCompleteTextView1);
        autoCompleteTextView2 = findViewById(R.id.autoCompleteTextView2);

        typeArrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, typeItems);
        categoryArrayAdapter = new ArrayAdapter<String>(this, R.layout.dropdown_item, categoryItems);

        autoCompleteTextView1.setAdapter(typeArrayAdapter);
        autoCompleteTextView2.setAdapter(categoryArrayAdapter);

        autoCompleteTextView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                type = item;
            }
        });
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                category = item;
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uploadTask != null && uploadTask.isInProgress()){
                    Toast.makeText(getApplicationContext(), "Uploading in progress", Toast.LENGTH_SHORT).show();
                }
                else{
                    create_post();
                }
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
    }
    public void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            postImageUri = data.getData();
            Picasso.with(this).load(postImageUri).into(uploadImageView);
        }
    }

    public String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @SuppressLint("SimpleDateFormat")
    public void create_post(){

        descriptionEditText.setError(null);
        autoCompleteTextView1.setError(null);
        autoCompleteTextView2.setError(null);

        if(descriptionEditText.getText().toString().trim().equals("")){
            descriptionEditText.setError("Write a description");
            descriptionEditText.requestFocus();
        }
        else if(postImageUri == null){
            Toast.makeText(getApplicationContext(), "Please select a Photo", Toast.LENGTH_SHORT).show();
            uploadImageView.requestFocus();
        }
        else if(type.equals("")){
            autoCompleteTextView1.setError("Please select a Type");
            autoCompleteTextView1.requestFocus();
        }
        else if(category.equals("")){
            autoCompleteTextView2.setError("Please select a Category");
            autoCompleteTextView2.requestFocus();
        }
        else{
            calendar = Calendar.getInstance();

            String username = userFullname;
            String phone = userPhone;
            String date = DateFormat.getDateInstance(DateFormat.DEFAULT).format(calendar.getTime());
            String description = "#" + type + " " + category + "\n" +  descriptionEditText.getText().toString().trim() + "\nContact no: " + userPhone;
            //String imageUri = postImageUri.toString();
            postButton.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Posting...", Toast.LENGTH_SHORT).show();
            StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(postImageUri));

            reference.putFile(postImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());
                    Uri downloadUrl = uriTask.getResult();

                    assert downloadUrl != null;
                    CreatePost createPost = new CreatePost(username, phone, date, description, type, category, downloadUrl.toString());
                    String uniqueKey = databaseReference.push().getKey();
                    assert uniqueKey != null;
                    databaseReference.child(uniqueKey).setValue(createPost);

                    Intent intent = new Intent(PostActivity.this, RecentActivity.class);
                    startActivity(intent);
                    finish();

                    Toast.makeText(getApplicationContext(), "Post has been created successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Oops! Database error occurred", Toast.LENGTH_SHORT).show();
                }
            });
            // Toast.makeText(getApplicationContext(), username + "\n" + date + "\n" + description + "\n" + imageUri, Toast.LENGTH_SHORT).show();
        }
    }
}
