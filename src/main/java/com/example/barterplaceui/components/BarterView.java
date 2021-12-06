package com.example.barterplaceui.components;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.barterplaceui.DeleteBarterPage;
import com.example.barterplaceui.MyBartersPage;
import com.example.barterplaceui.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import services.Barter;

//organize barter view
//contain title and image
public class BarterView extends LinearLayout {

    TextView tv,tv2;
    ImageView iv;
    int noBarterImage = R.drawable.ic_help;
    Barter currentBarter;
    private final int PIC_ID = 100;
    int id = 200;

    //barter view. if sent on click the view will be click able if sent null will not be
    public BarterView(Context context, Barter barter, FirebaseStorage storage,OnClickListener onClick){
        super(context);
        currentBarter = barter;

        //title
        this.tv = new TextView(context);
        this.tv.setText(barter.getTitle());
        tv.setTextSize(25);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(tv.getTypeface(), Typeface.BOLD_ITALIC);

        //image
        this.iv = new ImageView(context);
        this.iv.setId(PIC_ID);
        //style
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.LEFT;
        iv.setLayoutParams(layoutParams);
        iv.setPadding(0,0,0,0);

        //get image from db
        StorageReference imgRef = storage.getReference().child("barters_images/"+barter.getId());
        try {
            File localFile = File.createTempFile(barter.getId(),"jpeg");
            imgRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ImageView imageUser = (ImageView)findViewById(PIC_ID);
                            imageUser.setImageBitmap(bitmap);
                            imageUser.getLayoutParams().height = 300;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    ImageView imageUser = (ImageView)findViewById(PIC_ID);
                    imageUser.setImageResource(noBarterImage);
                    imageUser.getLayoutParams().height = 300;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOrientation(VERTICAL);
        //adding views
        addView(tv);
        addView(iv);
        //background
        this.setBackgroundResource(R.drawable.grey_rounded_background);
        setPadding(30,30,30,30);
        //check for click or not click able view
        if(onClick != null){
            setOnClickListener(onClick);
        }
    }
}
