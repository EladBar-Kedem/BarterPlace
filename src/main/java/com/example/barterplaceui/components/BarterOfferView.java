package com.example.barterplaceui.components;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.barterplaceui.ActivitiesInProgresPage;
import com.example.barterplaceui.DeleteBarterOfferPage;
import com.example.barterplaceui.DisplayBarterPage;
import com.example.barterplaceui.R;
import com.google.firebase.storage.FirebaseStorage;

import services.Barter;

//organize barter offer view
//contain 2 barters and image
//if sent on click the barter OFFER will be click able if not sent each barter will be clickable
public class BarterOfferView extends LinearLayout {

    Barter barterSent;
    Barter barterReceived;
    ImageView arrow;
    int arrowPic = R.drawable.ic_arrow;
    BarterView bv;
    BarterView bv2;

    //barter offer view
    public BarterOfferView(Context context, Barter barterSent, Barter barterReceived, FirebaseStorage storage, OnClickListener onClick) {
        super(context);
        this.barterReceived = barterReceived;
        this.barterSent = barterSent;

        setOrientation(HORIZONTAL);

        if (onClick != null) {
            //first barter - the one how made the offer
            bv = new BarterView(context, barterSent, storage, null);

            //second barter - the one how received the offer
            bv2 = new BarterView(context, barterReceived, storage, null);

        } else {
            //first barter - the one how made the offer
            bv = new BarterView(context, barterSent, storage,       new View.OnClickListener() {        //create barter offer view with on click listener how send to delete barter offer page
                @Override
                public void onClick(View v) {
                    displayBarter(context, barterSent.getId());
                }
            });

            //second barter - the one how received the offer
            bv2 = new BarterView(context, barterReceived, storage,       new View.OnClickListener() {        //create barter offer view with on click listener how send to delete barter offer page
                @Override
                public void onClick(View v) {
                    displayBarter(context, barterReceived.getId());
                }
            });
        }

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT

        );
        param.width = 380;
        param.setMargins(0, 0, 5, 0);
        bv.setLayoutParams(param);
        addView(bv);

        //arrow image
        this.arrow = new ImageView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.width = 200;
        layoutParams.gravity = Gravity.CENTER;
        arrow.setLayoutParams(layoutParams);
        arrow.setImageResource(arrowPic);
        addView(arrow);

        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT

        );
        param2.width = 380;
        bv2.setLayoutParams(param2);
        addView(bv2);

        if (onClick != null) {
            setOnClickListener(onClick);
        }
    }

    public void displayBarter(Context context, String barterIdToOpen){
        Intent intent = new Intent(context, DisplayBarterPage.class);
        intent.putExtra("barterIdToOpen", barterIdToOpen);     //barter id to open
        context.startActivity(intent);
    }

}
