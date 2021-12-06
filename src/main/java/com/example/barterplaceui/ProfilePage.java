package com.example.barterplaceui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import services.DBUsersServices;
import services.IUserCallback;
import services.User;

//show the user details. can link to edit profile page
public class ProfilePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        storage = FirebaseStorage.getInstance();
        StorageReference imgRef = storage.getReference().child("users_images/" + currentUser.getUid());//refernce to user pic
        try {
            File localFile = File.createTempFile(currentUser.getUid(), "jpeg");
            imgRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {//take exist pic
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ImageView imageUser = (ImageView) findViewById(R.id.imageUser);
                            imageUser.setImageBitmap(bitmap);
                            imageUser.getLayoutParams().height = 600;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {                           //take default pic
                    ImageView imageUser = (ImageView) findViewById(R.id.imageUser);
                    imageUser.setImageResource(R.drawable.anonimi_man);
                    imageUser.getLayoutParams().height = 400;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        putData();
    }

    //bring user data
    private void putData() {
        DBUsersServices service = new DBUsersServices();
        service.getUser(new IUserCallback() {
            @Override
            public void sendUser(User user) {
                insertToFields(user);
            }

            ;
        });
    }

    //display user data
    private void insertToFields(User user) {
        TextView tvArea = (TextView) findViewById(R.id.etTextPersonArea); // get destenation field
        tvArea.setText(user.area);                                            // put in that field
        TextView tvFirst = (TextView) findViewById(R.id.etTextPersonName);
        tvFirst.setText(user.name);
        TextView tvLast = (TextView) findViewById(R.id.etTextPersonLastName);
        tvLast.setText(user.lastName);
        TextView tvPhone = (TextView) findViewById(R.id.etTextPersonPhone);
        tvPhone.setText(user.phone);
    }

    public void moveToHomePage(View view) {
        //send to HomePage
        Intent intent = new Intent(getBaseContext(), HomePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToEditProfilePage(View view) {
        //send to EditProfilePage
        Intent intent = new Intent(getBaseContext(), EditProfilePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();

    }

    public void moveToSettingsPage(View view) {
        //send to SettingsPage
        Intent intent = new Intent(getBaseContext(), SettingsPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToChatPage(View view) {
        //send to ChatPage
        Intent intent = new Intent(getBaseContext(), ChatPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToBartersPage(View view) {
        //send to BartersPage
        Intent intent = new Intent(getBaseContext(), BartersPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}