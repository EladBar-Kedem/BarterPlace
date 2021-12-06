package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barterplaceui.components.BarterView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import services.Barter;
import services.BarterOfferModel;
import services.DBBarterOfferServices;
import services.DBBartersServices;
import services.IBarterCallback;
import services.IBarterOffersByUserId;

//display all current user barters and alert icon while the current user got an barter offer
public class MyBartersPage extends AppCompatActivity {
    Barter[] barters;
    private FirebaseStorage storage;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    ArrayList<BarterOfferModel> barterOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_barters_page);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        putBarters();
        lookForOffers();
    }

    //check if there is any barter offer that the current user has received
    private void lookForOffers() {
        DBBarterOfferServices service = new DBBarterOfferServices();
        service.getBarterReceivedByUserId(currentUser.getUid(), new IBarterOffersByUserId() {
            @Override
            public void sendBarterOffers(ArrayList<BarterOfferModel> barterOffers) {
                handleOffersBack(bringRelevantBarterOffers(barterOffers));
            }
        });
    }

    private ArrayList<BarterOfferModel> bringRelevantBarterOffers(ArrayList<BarterOfferModel> barterOffers) {
        ArrayList<BarterOfferModel> relevantBartersOffer = new ArrayList<>();
        for (int i = 0; i < barterOffers.size(); i++) {
            if (barterOffers.get(i).getStatus().equals("waiting_for_answer")) {
                relevantBartersOffer.add(barterOffers.get(i));
            }
        }

        return relevantBartersOffer;
    }

    //if there are offer set icon visible
    private void handleOffersBack(ArrayList<BarterOfferModel> barterOffers) {
        if (barterOffers.size() != 0) {
            this.barterOffers = barterOffers;
            ImageView alertIv = (ImageView) findViewById(R.id.offerAlertIv);
            alertIv.setVisibility(View.VISIBLE);
            TextView alertTv = (TextView) findViewById(R.id.alertTv);
            Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
            alertTv.setTypeface(myFont);
            alertTv.setVisibility(View.VISIBLE);
        }
    }

    //get all barters
    private void putBarters() {
        DBBartersServices service = new DBBartersServices();
        service.getUserBarters(new IBarterCallback() {
            @Override
            public void sendBarters(Barter[] barters) {
                insertToFields(barters);
            }
        });
    }

    //display relevant barters
    private void insertToFields(Barter[] barters) {
        LinearLayout bartersll = (LinearLayout) findViewById(R.id.bartersLayout);
        this.barters = barters;
        if (doesUserHaveBarters(barters)) {
            for (int i = 0; i < 5; i++) {
                if (barters[i] != null) {
                    Barter current = barters[i];        //current loop barter
                    BarterView barter = new BarterView(bartersll.getContext(), current, storage
                            , new View.OnClickListener() {      //create clickable barter view
                        @Override
                        public void onClick(View v) {
                            moveToDeleteBarterPage(v, MyBartersPage.this, current);
                        }
                    });
                    //styles
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(30, 20, 30, 10);

                    bartersll.addView(barter, layoutParams);    //add view
                }
            }
        } else {
            Toast.makeText(getBaseContext(), "There are no Barters yet", Toast.LENGTH_SHORT).show();
        }
    }


    //barter clicked
    public void moveToDeleteBarterPage(View view, Context context, Barter current) {
        //send to EditDeleteBarterPage
        Intent intent = new Intent(context, DeleteBarterPage.class);
        intent.putExtra("barterIdToOpen", current.getId());     //barter id to open
        context.startActivity(intent);
        finish();
    }


    public boolean doesUserHaveBarters(Barter[] barters) {
        return barters[0] != null;
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

    public void moveToHomePage(View view) {
        //send to HomePage
        Intent intent = new Intent(getBaseContext(), HomePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToActivitiesDone(View view) {
        //send to ActivitiesDone
        Intent intent = new Intent(getBaseContext(), ActivitiesDonePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToActivitiesInProgresPage(View view) {
        //send to ActivitiesInProgresPage
        Intent intent = new Intent(getBaseContext(), ActivitiesInProgresPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToAddBarterPage(View view) {
        if (doesUserGotMaxBarters()) {
            Toast.makeText(getBaseContext(), "User Allows To Create 5 Barters", Toast.LENGTH_LONG).show();
        } else {
            //send to AddBarterPage
            Intent intent = new Intent(getBaseContext(), AddBarterPage.class);
            startActivity(intent);
            //end sending and kill last process
            finish();
        }

    }

    private boolean doesUserGotMaxBarters() {
        return barters[4] != null;
    }


    public void showOffersToOwner(View view) {
        moveToOwnerAnswerPage();
    }

    public void moveToOwnerAnswerPage() {
        //send to OwnerAnswerPage
        Intent intent = new Intent(getBaseContext(), OwnerAnswerOfferPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}