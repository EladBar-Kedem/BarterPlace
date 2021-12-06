package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

//this page display when click on the icon who says if current user received barter offer in my barter page,
//and display all the offer that the user received
public class OwnerAnswerOfferPage extends AppCompatActivity {
    ArrayList<BarterOfferModel> barterOffers;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public FirebaseStorage storage;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_answer_offer_page);

        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        putBartersOffers();
    }

    //get all relevant barter offers
    private void putBartersOffers() {
        DBBarterOfferServices service = new DBBarterOfferServices();
        service.getBarterReceivedByUserId(currentUser.getUid(), new IBarterOffersByUserId() {
            @Override
            public void sendBarterOffers(ArrayList<BarterOfferModel> barterOffers) {
                insertToFields(barterOffers);
            }
        });
    }

    //create each barter offer
    private void insertToFields(ArrayList<BarterOfferModel> barterOffers) {
        this.barterOffers = barterOffers;
        if (barterOffers != null) {
            for (int i = 0; i < barterOffers.size(); i++) {
                insertBarterOfferFirst(barterOffers.get(i));
            }
        } else {
            Toast.makeText(getBaseContext(), "There are no Barters Offers yet", Toast.LENGTH_LONG).show();
        }
    }

    //create first barter
    private void insertBarterOfferFirst(BarterOfferModel barterOfferModel) {
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterOfferModel.getOwnerBarterId(), new IbarterByIdCallBack() {
            @Override
            public void sendBarter(Barter barter) {
                insertBarterOfferSecond(barter, barterOfferModel);
            }
        });
    }

    //create second barter
    public void insertBarterOfferSecond(Barter barterLeft, BarterOfferModel barterOfferModel) {
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterOfferModel.getOfferBarterId(), new IbarterByIdCallBack() {
            @Override
            public void sendBarter(Barter barterRight) {
                insertBarterOfferThird(barterLeft, barterRight, barterOfferModel);
            }
        });
    }

    //display each barter offer view, set on click listener
    public void insertBarterOfferThird(Barter barterLeft, Barter barterRight, BarterOfferModel barterOfferModel) {
        if (barterOfferModel.getStatus().equals("waiting_for_answer")) {
            LinearLayout bartersOfferll = (LinearLayout) findViewById(R.id.bartersOfferLayout);
            BarterOfferView bov = new BarterOfferView(OwnerAnswerOfferPage.this, barterLeft, barterRight, storage, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveToUserGivingAnswer(v, OwnerAnswerOfferPage.this, barterOfferModel);
                }
            });
            //styles
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(50, 25, 0, 25);
            bov.setLayoutParams(lp);
            bartersOfferll.addView(bov);    //add view
        }
    }

    private void moveToUserGivingAnswer(View v, Context context, BarterOfferModel barterOfferModel) {
        Intent intent = new Intent(context, UserGivingAnswer.class);
        intent.putExtra("barterOffer", barterOfferModel); //barter offer model to open
        context.startActivity(intent);
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