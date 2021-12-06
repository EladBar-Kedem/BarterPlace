package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import services.Barter;
import services.DBBarterOfferServices;
import services.DBBartersServices;
import services.IBarterCallback;
import services.IBarterPicByIdCallBack;
import services.IbarterByIdCallBack;

//when click on another user barter in barters page,
//this page will display and the user will have to choose barter to offer the other user
public class OfferBarterFirst extends AppCompatActivity {

    Barter currentBarter;
    String currentBarterId;
    Barter[] bartersInexchange;
    private FirebaseStorage storage;
    TextView title, area, details;
    ImageView pic;
    Spinner openBartersMenu;
    int noBarterImage = R.drawable.ic_help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_barter_first);

        openBartersMenu = (Spinner) findViewById(R.id.chooseBarterSpinner);
        Intent intent = getIntent();
        storage = FirebaseStorage.getInstance();
        currentBarterId = (String) intent.getSerializableExtra("barterIdToOpen");       //get barter id to open

        DBBartersServices service = new DBBartersServices();
        service.getBarterById(currentBarterId, new IbarterByIdCallBack() {       //get barter by id
            @Override
            public void sendBarter(Barter barter) {
                insertToFields(barter);
            }

        });
        service.getBarterPicById(currentBarterId, storage, new IBarterPicByIdCallBack() {       //get picure by barter id
            @Override
            public void sendBarterPic(Bitmap barterpic) {
                insertImage(barterpic);
            }
        });
        putBartersInSpinner();
    }

    //insert image. if user upload image he will see it if no he will get default picture
    private void insertImage(Bitmap barterpic) {
        pic = findViewById(R.id.barterImage);
        if (barterpic == null)
            pic.setImageResource(noBarterImage);
        else {
            pic.setImageBitmap(barterpic);
        }
    }

    //display barter
    private void insertToFields(Barter barter) {
        currentBarter = barter;
        title = findViewById(R.id.title);
        area = findViewById(R.id.area);
        details = findViewById(R.id.details);

        title.setText(currentBarter.getTitle());
        area.setText(currentBarter.getArea());
        details.setText(currentBarter.getDetails());
    }

    //put barter titles in the spinner for user to choose one barter from his barters to offer
    private void putBartersInSpinner() {
        DBBartersServices service = new DBBartersServices();
        service.getUserBarters(new IBarterCallback() {
            @Override
            public void sendBarters(Barter[] barters) {
                insertToSpinner(barters);
            }
        });
    }

    private void insertToSpinner(Barter[] barters) {
        this.bartersInexchange = barters;
        ArrayList<String> bartersTitels = new ArrayList<>();
        if (doesUserHaveBarters(barters)) {
            for (int i = 0; i < 5; i++) {
                if (barters[i] != null) {
                    Barter current = barters[i];
                    bartersTitels.add(current.getTitle());
                }
            }
            activeSpinner(bartersTitels);
        } else {
            Toast.makeText(getBaseContext(), "There are no Barters yet", Toast.LENGTH_LONG).show();
        }
    }

    public boolean doesUserHaveBarters(Barter[] barters) {
        return barters[0] != null;
    }

    private void activeSpinner(ArrayList<String> bartersTitles) {
        Spinner dropdownSpinner = findViewById(R.id.chooseBarterSpinner);
        bartersTitles.add(0, "Choose Barter To Offer");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, bartersTitles);
        dropdownSpinner.setAdapter(adapter);
    }

    public void backToBarterPage(View view) {
        finish();
    }

    //create barter offer
    public void makeAnBarterOffer(View view) {
        Spinner dropdownSpinner = findViewById(R.id.chooseBarterSpinner);
        int barterPos = dropdownSpinner.getSelectedItemPosition() - 1;        //the pos -1 is equal now to the index in Barter[] bartersInexchange
        if (validChoice(barterPos)) { //if user made barter choice
            Barter spinnerChosenBarter = (Barter) bartersInexchange[barterPos];
            if (isSameUser(currentBarter.getUserId(), spinnerChosenBarter.getUserId())) {//check if user try to made barter offer with himself
                Toast.makeText(getBaseContext(), "You Cannot Offer Barter To Yourself!", Toast.LENGTH_SHORT).show(); //show message
            } else {
                DBBarterOfferServices service = new DBBarterOfferServices();
                service.addBarterOfferToDB(currentBarter.getUserId(), currentBarter.getId(),
                        spinnerChosenBarter.getId(), spinnerChosenBarter.getUserId());       //add barter offer
                Toast.makeText(getBaseContext(), "Barter Offer Send", Toast.LENGTH_SHORT).show();        //update ui
                finish();       //close activity
            }
        } else {
            Toast.makeText(getBaseContext(), "Please Choose Barter", Toast.LENGTH_SHORT).show();    //user didn't choose barter in exchange
        }
    }

    private boolean isSameUser(String userId, String userId1) {
        return userId.equals(userId1);
    }

    //return if user pic barter in exchange
    private boolean validChoice(int pos) {
        return pos != -1;
    }


}