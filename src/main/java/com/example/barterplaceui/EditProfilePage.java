package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

//user edit profile page
public class EditProfilePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;
    private StorageReference storageRef;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        //set text view font
        TextView changePicTV = (TextView) findViewById(R.id.changePic);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        changePicTV.setTypeface(myFont);

        //put default picture
        ImageView imageUser = (ImageView) findViewById(R.id.imageUser);
        imageUser.setImageResource(R.drawable.anonimi_man);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //get current user:
        currentUser = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    //move page after ending edit profile process
    public void moveToProfilePage(View view) {
        if (isAllFieldsFilled()) {
            CollectionReference usersDoc = db.collection("users"); //receive collection reference
            Map<String, Object> data1 = new HashMap<>(); //make document object to fill
            EditText etName = (EditText) findViewById(R.id.etTextPersonName);
            data1.put("first", etName.getText().toString());                //input
            EditText etLastName = (EditText) findViewById(R.id.etTextPersonLastName);
            data1.put("last", etLastName.getText().toString());             //input
            EditText etArea = (EditText) findViewById(R.id.etTextPersonArea);
            data1.put("area", etArea.getText().toString());                 //input
            EditText etPhone = (EditText) findViewById(R.id.etTextPersonPhone);
            data1.put("phone", etPhone.getText().toString());                 //input
            usersDoc.document(currentUser.getUid()).set(data1);//set data

            Intent intent = new Intent(getBaseContext(), ProfilePage.class);
            //send to ProfilePage
            startActivity(intent);
            //end sending and kill last process
            finish();
        } else {
            Toast.makeText(getBaseContext(), "All Fields Must Be Filled!", Toast.LENGTH_LONG).show();   //not all field are filled case
        }
    }

    //return if all field are filled
    private boolean isAllFieldsFilled() {
        EditText field1 = (EditText) findViewById(R.id.etTextPersonName);
        EditText field2 = (EditText) findViewById(R.id.etTextPersonLastName);
        EditText field3 = (EditText) findViewById(R.id.etTextPersonArea);
        EditText field4 = (EditText) findViewById(R.id.etTextPersonPhone);
        return field1.getText().toString().length() != 0 && field2.getText().toString().length() != 0
                && field3.getText().toString().length() != 0 && field4.getText().toString().length() != 0;
    }

    //user pick picture from phone
    public void userInsertImage(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    //store image in storage DB
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            StorageReference usersImagesRef = storageRef.child("users_images");
            StorageReference userIdForPicRef = usersImagesRef.child(currentUser.getUid());
            imageUri = data.getData();
            userIdForPicRef.putFile(imageUri);
        }
    }
}