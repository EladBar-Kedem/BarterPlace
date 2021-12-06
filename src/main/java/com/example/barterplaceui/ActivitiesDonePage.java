package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

//this page show all the barter offers that have been ended successfully
public class ActivitiesDonePage extends AppCompatActivity {

    ArrayList<BarterOfferModel> barterOffers;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public FirebaseStorage storage;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities_done_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();

        putBartersOffers();
    }


    //put the relevant barters offers
    private void putBartersOffers() {
        barterOffers = new ArrayList<>();
        DBBarterOfferServices service = new DBBarterOfferServices();    //create service obj
        service.getBarterOffersByUserId(currentUser.getUid(), new IBarterOffersByUserId() {      //service function
            @Override
            public void sendBarterOffers(ArrayList<BarterOfferModel> barterOffers) {        //while finish, get the barter offers model
                insertToFields(barterOffers);
            }
        });

        service.getBarterOffersDoneByUserId(currentUser.getUid(), new IBarterOffersByUserId() {      //service function
            @Override
            public void sendBarterOffers(ArrayList<BarterOfferModel> barterOffers) {        //while finish, get the barter offers model
                insertToFields(barterOffers);
            }
        });
    }

    //create from each barter offer model 2 barters
    private void insertToFields(ArrayList<BarterOfferModel> barterOffers) {
        if (barterOffers != null && barterOffers.size() != 0) {
            for (int i = 0; i < barterOffers.size(); i++) {
                insertBarterOfferFirst(barterOffers.get(i));        //for each barter offer model make this
            }
        }
        else{
            Toast.makeText(getBaseContext(), "There are no Done Barters Offers yet", Toast.LENGTH_SHORT).show();      //no barter offers case
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
        if (barterOfferModel.getStatus().equals("accepted")) {        //check status
            LinearLayout bartersOfferll = (LinearLayout) findViewById(R.id.bartersOfferLayout);
            BarterOfferView bov = new BarterOfferView(ActivitiesDonePage.this, barterLeft, barterRight, storage,null);
            //styles
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(50, 25, 0, 25);
            bov.setLayoutParams(lp);

            bartersOfferll.addView(bov);        //add view
        }
    }

    public void moveToProfilePage(View view) {
        //send to ProfilePage
        Intent intent = new Intent(getBaseContext(),ProfilePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
    public void moveToSettingsPage(View view) {
        //send to SettingsPage
        Intent intent = new Intent(getBaseContext(),SettingsPage.class);
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

    public void moveToHomePage(View view) {
        //send to HomePage
        Intent intent = new Intent(getBaseContext(),HomePage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void moveToMyBartersPage(View view) {
        //send to MyBartersPage
        Intent intent = new Intent(getBaseContext(),MyBartersPage.class);
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
}