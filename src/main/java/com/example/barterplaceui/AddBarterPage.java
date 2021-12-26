package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import services.DBBartersServices;
import services.DBUsersServices;

import static java.util.UUID.randomUUID;

//take barter details and create barter in db with service
public class AddBarterPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int PICK_IMAGE = 100;
    private FirebaseUser currentUser;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_barter_page);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

    }

    public void addBarterDetails(View view) {
        if (isAllFieldsFilled()) {
            String barterID = randomUUID().toString();
            EditText etBarterTitle = (EditText) findViewById(R.id.etBarterTitle);
            String stBarterTitle = etBarterTitle.getText().toString();              //input
            EditText etBarterArea = (EditText) findViewById(R.id.etBarterArea);
            String stBarterArea = etBarterArea.getText().toString();                //input
            EditText etBarterDetails = (EditText) findViewById(R.id.etBarterDetails);
            String stBarterDetails = etBarterDetails.getText().toString();          //input

            DBBartersServices service = new DBBartersServices();
            service.addBarterToDB(barterID, stBarterTitle, stBarterArea, stBarterDetails);//addBarter Service
            if (isImageLoad()) {
                uploadeBarterImageToStorage(imageUri, barterID);    //if there is an image save it
            } else {
                Intent intent = new Intent(getBaseContext(), MyBartersPage.class);
                //send to MyBartersPage
                startActivity(intent);
                //end sending and kill last process
                finish();
            }
        } else {
            Toast.makeText(getBaseContext(), "All Fields Must Be Filled!", Toast.LENGTH_LONG).show(); //message if not all fields are filled
        }
    }

    private boolean isImageLoad() {
        return imageUri != null;
    }

    //return if all fields are filled
    private boolean isAllFieldsFilled() {
        EditText field1 = (EditText) findViewById(R.id.etBarterTitle);
        EditText field2 = (EditText) findViewById(R.id.etBarterArea);
        EditText field3 = (EditText) findViewById(R.id.etBarterDetails);
        return field1.getText().toString().length() != 0 && field2.getText().toString().length() != 0 && field3.getText().toString().length() != 0;
    }

    //user pick picture from phone
    public void userInsertImage(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    //when user pick image this function will save the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();      //save image
        }
    }

    //store image in storage DB
    private void uploadeBarterImageToStorage(Uri imageUri, String barterId) {
        StorageReference bartersImagesRef = storageRef.child("barters_images");
        StorageReference barterIdForPicRef = bartersImagesRef.child(barterId);//create road to store image under barter id

        barterIdForPicRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override                                                           //upload image and create onSuccess listener
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                backToMyBartersPage(null);
            }
        });
    }

    public void backToMyBartersPage(View view) {
        Intent intent = new Intent(getBaseContext(), MyBartersPage.class);
        //send to MyBartersPage
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}