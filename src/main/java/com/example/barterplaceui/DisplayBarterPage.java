package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

import services.Barter;
import services.DBBartersServices;
import services.IBarterPicByIdCallBack;
import services.IbarterByIdCallBack;

public class DisplayBarterPage extends AppCompatActivity {

    Barter currentBarter;
    String barterId;
    TextView title, area, details;
    private FirebaseStorage storage;
    int noBarterImage = R.drawable.ic_help;
    ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_barter_page);

        Intent intent = getIntent();
        storage = FirebaseStorage.getInstance();

        barterId = (String) intent.getSerializableExtra("barterIdToOpen");    //get barter id to open
        DBBartersServices service = new DBBartersServices();
        service.getBarterById(barterId, new IbarterByIdCallBack() {      //get barter
            @Override
            public void sendBarter(Barter barter) {
                insertToFields(barter);
            }
        });

        service.getBarterPicById(barterId, storage, new IBarterPicByIdCallBack() {  //get barter pic
            @Override
            public void sendBarterPic(Bitmap barterpic) {
                insertImage(barterpic);
            }
        });
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
        pic = findViewById(R.id.barterImage);

        title.setText(currentBarter.getTitle());
        area.setText(currentBarter.getArea());
        details.setText(currentBarter.getDetails());
    }

    public void backToLastPage(View view) {
        finish();
    }

}