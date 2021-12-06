package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.barterplaceui.components.BarterOfferView;
import com.google.firebase.storage.FirebaseStorage;

import services.Barter;
import services.BarterOfferModel;
import services.DBBarterOfferServices;
import services.DBBartersServices;
import services.IbarterByIdCallBack;

//cancele barter offer by click on the barter offer in, in progress page
public class DeleteBarterOfferPage extends AppCompatActivity {
    BarterOfferModel bom;
    public FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_barter_offer_page);

        storage = FirebaseStorage.getInstance();

        //set text view font
        TextView tiltle = (TextView) findViewById(R.id.titleTextView);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        tiltle.setTypeface(myFont);

        Intent intent = getIntent();
        bom = new BarterOfferModel((String) intent.getSerializableExtra("barterOfferId"),
                (String) intent.getSerializableExtra("ownerId"),
                (String) intent.getSerializableExtra("offerId"),
                (String) intent.getSerializableExtra("ownerBarterId"),
                (String) intent.getSerializableExtra("offerBarterId"),
                (String) intent.getSerializableExtra("status")); //create barter offer model
        insertBarterOfferFirst(bom);
    }

    //get first barter
    private void insertBarterOfferFirst(BarterOfferModel barterOfferModel) {
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterOfferModel.getOwnerBarterId(), new IbarterByIdCallBack() {
            @Override
            public void sendBarter(Barter barter) {
                insertBarterOfferSecond(barter, barterOfferModel);
            }
        });
    }

    //get second barter
    public void insertBarterOfferSecond(Barter barterLeft, BarterOfferModel barterOfferModel) {
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterOfferModel.getOfferBarterId(), new IbarterByIdCallBack() {
            @Override
            public void sendBarter(Barter barterRight) {
                insertBarterOfferThird(barterLeft, barterRight, barterOfferModel);
            }
        });
    }

    //display unclickable barter offer view
    public void insertBarterOfferThird(Barter barterLeft, Barter barterRight, BarterOfferModel barterOfferModel) {
        LinearLayout bartersOfferll = (LinearLayout) findViewById(R.id.barterOfferLayout);
        BarterOfferView bov = new BarterOfferView(DeleteBarterOfferPage.this, barterLeft, barterRight, storage,null);
        //styles
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(60, 300, 0, 25);
        bov.setLayoutParams(lp);

        bartersOfferll.addView(bov);        //add view

    }

    public void handleNoPressed(View view) {
        moveToActivitiesInProgresPage();
    }

    //change barter offer status to canceled
    public void handleYesPressed(View view) {
        DBBarterOfferServices service = new DBBarterOfferServices();
        service.EditBarterOfferStatusToDB(bom.getBarterOfferId(), bom.getOfferId(), bom.getOwnerBarterId(),
                bom.getOfferBarterId(), bom.getOwnerId(), "canceled");
        moveToActivitiesInProgresPage();
    }

    public void moveToActivitiesInProgresPage() {
        //send to ActivitiesInProgresPage
        Intent intent = new Intent(getBaseContext(), ActivitiesInProgresPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}