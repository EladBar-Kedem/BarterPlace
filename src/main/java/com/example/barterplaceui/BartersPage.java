package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barterplaceui.components.BarterView;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import services.Barter;
import services.DBBartersServices;
import services.IbartersCallBack;

//the main page that the user can look for barters to offer his barters for
public class BartersPage extends AppCompatActivity {

    ArrayList<Barter> barters;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barters_page);

        storage = FirebaseStorage.getInstance();
        putBarters();
    }

    //bring all barters
    private void putBarters() {
        DBBartersServices service = new DBBartersServices();
        service.getAllBarters(new IbartersCallBack() {
            @Override
            public void sendAllBarters(ArrayList<Barter> barters) {
                insertToFields(barters);
            }
        });
    }

    //bring all relevant barters by what the user has been write on the plain text
    private void putBartersByUserChosenTitle(String userInput) {
        DBBartersServices service = new DBBartersServices();
        service.getAllRelevantBarters(userInput,new IbartersCallBack() {
            @Override
            public void sendAllBarters(ArrayList<Barter> barters) {
                insertToFields(barters);
            }
        });
    }

    //display relevant barters
    private void insertToFields(ArrayList<Barter> barters) {
        LinearLayout bartersll = (LinearLayout) findViewById(R.id.bartersLayout);
        this.barters = barters;     //store barters
        if (doesHaveBarters(barters)) {
            for (int i = 0; i < barters.size(); i++) {
                Barter current = barters.get(i);        //get loop current barter
                BarterView barter = new BarterView(bartersll.getContext(), current, storage
                        , new View.OnClickListener() { //create clickable barter view
                    @Override
                    public void onClick(View v) {
                        moveToOfferBarterFirstPage(v, BartersPage.this, current);
                    }
                });
                //styles
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30, 20, 30, 10);
                bartersll.addView(barter, layoutParams);    //add ech barter view
            }
        } else {
            Toast.makeText(getBaseContext(), "No Barters Found", Toast.LENGTH_LONG).show();     //no barters in app case
        }
    }

    //while user click on search this function will run if there is no text write, all barters will display
    //if user write product or category it will show all barters with the title like the user input
    public void userWriteProduct(View view) {
        EditText writeProductET = (EditText) findViewById(R.id.writeProductET);
        String userWriteStr = writeProductET.getText().toString();
        if (userWriteStr.length() == 0) {
            LinearLayout bartersLayout = (LinearLayout) findViewById(R.id.bartersLayout);
            bartersLayout.removeAllViews();
            putBarters();
        } else {
            LinearLayout bartersLayout = (LinearLayout) findViewById(R.id.bartersLayout);
            bartersLayout.removeAllViews();
            putBartersByUserChosenTitle(userWriteStr);
        }
    }

    private void moveToOfferBarterFirstPage(View view, Context context, Barter current) {
        //send to OfferBarterFirstPage
        Intent intent = new Intent(context, OfferBarterFirst.class);
        intent.putExtra("barterIdToOpen", current.getId());     //send barter id to open
        context.startActivity(intent);
    }

    //return if user have barters or no
    public boolean doesHaveBarters(ArrayList<Barter> barters) {
        return !barters.isEmpty();
    }

    public void moveToProfilePage(View view) {
        //send to ProfilePage
        Intent intent = new Intent(getBaseContext(), ProfilePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToHomePage(View view) {
        //send to HomePage
        Intent intent = new Intent(getBaseContext(), HomePage.class);
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

    public void moveToSettingsPage(View view) {
        //send to SettingsPage
        Intent intent = new Intent(getBaseContext(), SettingsPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}