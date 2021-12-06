package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

//display management phone and mail. allows to log out the app
public class SettingsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        //load the font to the title:
        TextView title1 = (TextView)findViewById(R.id.tv1);
        TextView title2 = (TextView)findViewById(R.id.tv2);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        title1.setTypeface(myFont);
        title2.setTypeface(myFont);
    }

    public void phoneToast(View view) {
        Toast.makeText(getBaseContext(), "Contact Us: 0502223334", Toast.LENGTH_LONG).show();
    }

    public void mailToast(View view) {
        Toast.makeText(getBaseContext(), "Contact Us: Eladbk8@gmail.com", Toast.LENGTH_LONG).show();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        //return to login page
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
        //kill process
        finish();
    }

    public void moveToProfilePage(View view) {
        //send to ProfilePage
        Intent intent = new Intent(getBaseContext(),ProfilePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToHomePage(View view) {
        //send to HomePage
        Intent intent = new Intent(getBaseContext(),HomePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToChatPage(View view) {
        //send to ChatPage
        Intent intent = new Intent(getBaseContext(),ChatPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToBartersPage(View view) {
        //send to BartersPage
        Intent intent = new Intent(getBaseContext(),BartersPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();

    }
}