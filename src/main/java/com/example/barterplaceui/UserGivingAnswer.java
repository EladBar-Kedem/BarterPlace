package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.barterplaceui.components.BarterOfferView;
import com.google.firebase.storage.FirebaseStorage;

import services.Barter;
import services.BarterOfferModel;
import services.DBBarterOfferServices;
import services.DBBartersServices;
import services.DBMessagesService;
import services.IbarterByIdCallBack;

//display one barter offer and waiting for user to give answer
public class UserGivingAnswer extends AppCompatActivity {
    public FirebaseStorage storage;
    BarterOfferModel bom;
    Barter owner;
    Barter offer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_giving_answer);

        storage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        bom = (BarterOfferModel) intent.getSerializableExtra("barterOffer"); //get barter offer model to open
        insertBarterOfferFirst(bom);
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

    //display barter offer view
    public void insertBarterOfferThird(Barter barterLeft, Barter barterRight, BarterOfferModel barterOfferModel) {
        offer = barterRight;
        owner = barterLeft;
        if (barterOfferModel.getStatus().equals("waiting_for_answer")) {
            LinearLayout bartersOfferll = (LinearLayout) findViewById(R.id.barterOffer);
            BarterOfferView bov = new BarterOfferView(UserGivingAnswer.this, barterLeft, barterRight, storage, null);
            //styles
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(50, 150, 0, 25);
            bov.setLayoutParams(lp);
            bartersOfferll.addView(bov);
        }
    }

    //change barter offer status in DB to rejected
    public void handleNoPressed(View view) {
        DBBarterOfferServices service = new DBBarterOfferServices();
        service.EditBarterOfferStatusToDB(bom.getBarterOfferId(), bom.getOfferId(), bom.getOwnerBarterId(), bom.getOfferBarterId(), bom.getOwnerId(), "rejected");
        DBMessagesService service2 = new DBMessagesService();
        service2.addMessageToDB(bom.getBarterOfferId(), "rejected", bom.getOfferId(),bom.getOwnerId());
        moveToMyBartersPage();
    }

    //change barter offer status in DB to accepted
    public void handleYesPressed(View view) {
        DBBartersServices service = new DBBartersServices();
        service.closeBarterInDB(owner.getId(), owner.getTitle(), owner.getArea(), owner.getDetails());
        service.closeBarterInDB(offer.getId(), offer.getTitle(), offer.getArea(), offer.getDetails());
        DBMessagesService service2 = new DBMessagesService();
        service2.addMessageToDB(bom.getBarterOfferId(), "accepted", bom.getOfferId(),bom.getOwnerId());

        DBBarterOfferServices service3 = new DBBarterOfferServices();
        service3.EditBarterOfferStatusToDB(bom.getBarterOfferId(), bom.getOfferId(), bom.getOwnerBarterId(), bom.getOfferBarterId(), bom.getOwnerId(), "accepted");
        moveToMyBartersPage();
    }

    public void moveToMyBartersPage() {
        //send to MyBartersPage
        Intent intent = new Intent(getBaseContext(), MyBartersPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }

    public void answerLaterClicked(View view) {
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