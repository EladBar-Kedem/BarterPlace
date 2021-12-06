package com.example.barterplaceui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

import services.DBUsersServices;
import services.IUserCallback;
import services.User;

//home page with hello message for user and go to my activities link
public class HomePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FirebaseUser currentUser;
    private CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        //get current user:
        currentUser = mAuth.getCurrentUser();

        getUserName();
        //set text view font
        TextView clickTiltle = (TextView) findViewById(R.id.tv);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        clickTiltle.setTypeface(myFont);
    }

    //create welcome message + name
    private void getUserName() {
        DBUsersServices service = new DBUsersServices();
        service.getUser(new IUserCallback() {
            @Override
            public void sendUser(User user) {
                sayHello(user);
            }

            ;
        });
    }

    //check for google user. and display message hello
    private void sayHello(User user) {
        //google user
        if (user == null) {
            //send to SignUpDetailsPage
            Intent intent = new Intent(getBaseContext(), SignUpDetailsPage.class);
            startActivity(intent);
            //end sending and kill last process
            finish();
        }
        // put in that field
        TextView welcomeTitle = (TextView) findViewById(R.id.welcome_user);
        welcomeTitle.setText("Welcome " + user.name);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        welcomeTitle.setTypeface(myFont);
    }

    public void moveToProfilePage(View view) {
        //send to ProfilePage
        Intent intent = new Intent(getBaseContext(), ProfilePage.class);
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

    public void moveToMyBartersPage(View view) {
        //send to MyBartersPage
        Intent intent = new Intent(getBaseContext(), MyBartersPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}