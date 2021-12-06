package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.barterplaceui.components.BarterOfferView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import services.Barter;
import services.BarterOfferModel;
import services.DBBarterOfferServices;
import services.DBBartersServices;
import services.DBUsersServices;
import services.IBarterOffersById;
import services.IUserCallback;
import services.IbarterByIdCallBack;
import services.User;

//page that open when message click and shows the barter offer that get to the clicked answer
public class AnswerBarterOfferPage extends AppCompatActivity {

    public FirebaseStorage storage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    FirebaseUser currentUser;
    private CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_barter_offer_page);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        //get current user:
        currentUser = mAuth.getCurrentUser();

        storage = FirebaseStorage.getInstance();
        Intent intent = getIntent();
        String barterOfferId = (String) intent.getSerializableExtra("barter_offer_id_to_open"); //get the id to open
        getBomFromId(barterOfferId);
    }

    //get barter offer model to open
    private void getBomFromId(String barterOfferId) {
        DBBarterOfferServices service = new DBBarterOfferServices();
        service.getBarterOfferById(barterOfferId, new IBarterOffersById() {
            @Override
            public void sendBarterOfferModelById(BarterOfferModel bom) {
                insertBarterOfferFirst(bom);    //when finish do this
            }
        });
    }

    //create from id first barter
    private void insertBarterOfferFirst(BarterOfferModel barterOfferModel) {
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterOfferModel.getOwnerBarterId(), new IbarterByIdCallBack() {
            @Override
            public void sendBarter(Barter barter) {
                insertBarterOfferSecond(barter, barterOfferModel);
            }
        });
    }

    //create from id second barter
    public void insertBarterOfferSecond(Barter barterLeft, BarterOfferModel barterOfferModel) {
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterOfferModel.getOfferBarterId(), new IbarterByIdCallBack() {
            @Override
            public void sendBarter(Barter barterRight) {
                insertBarterOfferThird(barterLeft, barterRight, barterOfferModel);
            }
        });
    }

    //create barter offer view
    public void insertBarterOfferThird(Barter barterLeft, Barter barterRight, BarterOfferModel barterOfferModel) {
        LinearLayout bartersOfferll = (LinearLayout) findViewById(R.id.barterOfferLayout);
        BarterOfferView bov = new BarterOfferView(this, barterLeft, barterRight, storage,null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(60, 300, 0, 25);
        bov.setLayoutParams(lp);
        bartersOfferll.addView(bov);
        //show user details for accepted barter offer
        if(barterOfferModel.getStatus().equals("accepted")){
            if(!barterOfferModel.getOwnerId().equals(currentUser.getUid())){
                putUserDetails(barterOfferModel.getOwnerId());
            }else{
                putUserDetails(barterOfferModel.getOfferId());
            }

        }
    }

    private void putUserDetails(String userId) {
        DBUsersServices service = new DBUsersServices();
        service.getUserByID(userId, new IUserCallback() {
            @Override
            public void sendUser(User user) {
                TextView tv = (TextView)findViewById(R.id.userDetailsTV);
                tv.setText("You can contact " +user.name +" " +user.lastName+ " in phone number:"+user.phone);
            }
        });
    }

    //close that activity while user press X and back to last activity
    public void backToChatPage(View view) {
        moveToChatPage();
    }

    public void moveToChatPage() {
        //send to ChatPage
        Intent intent = new Intent(getBaseContext(), ChatPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}