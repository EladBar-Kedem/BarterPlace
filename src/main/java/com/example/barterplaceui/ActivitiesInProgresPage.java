package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barterplaceui.components.BarterOfferView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import services.Barter;
import services.BarterOfferModel;

import services.DBBarterOfferServices;
import services.DBBartersServices;
import services.IBarterOffersByUserId;
import services.IbarterByIdCallBack;

//shows all the barter offers the current user sent to other users
public class ActivitiesInProgresPage extends AppCompatActivity {

    ArrayList<BarterOfferModel> barterOffers;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public FirebaseStorage storage;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_in_progres_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        //set text view font
        TextView tiltle = (TextView) findViewById(R.id.tv);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        tiltle.setTypeface(myFont);

        putBartersOffers();
    }

    //put the relevant barters offers
    private void putBartersOffers() {
        DBBarterOfferServices service = new DBBarterOfferServices();    //create service obj
        service.getBarterOffersByUserId(currentUser.getUid(), new IBarterOffersByUserId() {      //service function
            @Override
            public void sendBarterOffers(ArrayList<BarterOfferModel> barterOffers) {        //while finish, get the barter offers model
                insertToFields(barterOffers);
            }
        });
    }

    //create from each barter offer model 2 barters
    private void insertToFields(ArrayList<BarterOfferModel> barterOffers) {
        this.barterOffers = barterOffers;       //store barter offer models
        if (barterOffers != null && barterOffers.size() != 0) {
            for (int i = 0; i < barterOffers.size(); i++) {
                insertBarterOfferFirst(barterOffers.get(i));        //for each barter offer model make this
            }
        } else {
            Toast.makeText(getBaseContext(), "There are no Barters Offers yet", Toast.LENGTH_SHORT).show();      //no barter offers case
        }
    }

    //create first barter and then create another
    private void insertBarterOfferFirst(BarterOfferModel barterOfferModel) {
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterOfferModel.getOwnerBarterId(), new IbarterByIdCallBack() {
            @Override
            public void sendBarter(Barter barter) {         //first barter
                insertBarterOfferSecond(barter, barterOfferModel);      //send barter to next function with the second barter offer model
            }
        });
    }

    //create second barter
    public void insertBarterOfferSecond(Barter barterLeft, BarterOfferModel barterOfferModel) {
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterOfferModel.getOfferBarterId(), new IbarterByIdCallBack() {
            @Override
            public void sendBarter(Barter barterRight) {            //second barter
                insertBarterOfferThird(barterLeft, barterRight, barterOfferModel);
            }
        });
    }

    //insert to fields
    public void insertBarterOfferThird(Barter barterLeft, Barter barterRight, BarterOfferModel barterOfferModel) {
        if (barterOfferModel.getStatus().equals("waiting_for_answer")) {        //check status
            LinearLayout bartersOfferll = (LinearLayout) findViewById(R.id.bartersOfferLayout);
            BarterOfferView bov = new BarterOfferView(ActivitiesInProgresPage.this, barterLeft, barterRight, storage,
                    new View.OnClickListener() {        //create barter offer view with on click listener how send to delete barter offer page
                @Override
                public void onClick(View v) {
                    moveToDeleteBarterOfferPage(v, ActivitiesInProgresPage.this, barterOfferModel);
                }
            });
            //styles
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(50, 25, 0, 25);
            bov.setLayoutParams(lp);

            bartersOfferll.addView(bov);        //add view
        }
    }

    private void moveToDeleteBarterOfferPage(View v, Context context, BarterOfferModel bov) {
        //send to DeleteBarterOfferPage
        Intent intent = new Intent(context, DeleteBarterOfferPage.class);
        intent.putExtra("barterOfferId", bov.getBarterOfferId());
        intent.putExtra("ownerBarterId", bov.getOwnerBarterId());
        intent.putExtra("offerBarterId", bov.getOfferBarterId());
        intent.putExtra("ownerId", bov.getOwnerId());
        intent.putExtra("offerId", bov.getOfferId());
        intent.putExtra("status", bov.getStatus());
        context.startActivity(intent);
        finish();
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

    public void moveToMyBartersPage(View view) {
        //send to MyBartersPage
        Intent intent = new Intent(getBaseContext(), MyBartersPage.class);
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
}