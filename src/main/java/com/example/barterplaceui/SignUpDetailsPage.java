package com.example.barterplaceui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import services.DBUsersServices;
//take user details
public class SignUpDetailsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_details);

        //load the font to the titles:
        TextView tiltle = (TextView)findViewById(R.id.tv);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        tiltle.setTypeface(myFont);
    }

    //add user details to DB
    public void addUserDetails(View view) {
        if(isAllFieldsFilled()){
            EditText etName = (EditText)findViewById(R.id.etTextPersonName);
            String stName = etName.getText().toString();                //input
            EditText etLastName = (EditText)findViewById(R.id.etTextPersonLastName);
            String stLastName =  etLastName.getText().toString();       //input
            EditText etArea = (EditText)findViewById(R.id.etTextPersonArea);
            String stArea = etArea.getText().toString();                //input
            EditText etPhone = (EditText)findViewById(R.id.etTextPersonPhone);
            String stPhone = etPhone.getText().toString();                //input

            DBUsersServices service = new DBUsersServices();
            service.addUserDetailsToDB(stName,stLastName,stArea,stPhone);//addUserDetails Service

            Intent intent = new Intent(getBaseContext(),HomePage.class);
            //send to ProfilePage
            startActivity(intent);
            //end sending and kill last process
            finish();
        }
        else{
            Toast.makeText(getBaseContext(), "All Fields Must Be Filled!", Toast.LENGTH_LONG).show();
        }
    }

    //return if all fields are filled
    private boolean isAllFieldsFilled() {
        EditText field1 = (EditText)findViewById(R.id.etTextPersonName);
        EditText field2 = (EditText)findViewById(R.id.etTextPersonLastName);
        EditText field3 = (EditText)findViewById(R.id.etTextPersonArea);
        EditText field4 = (EditText)findViewById(R.id.etTextPersonPhone);
        return field1.getText().toString().length() != 0 && field2.getText().toString().length() != 0 && field3.getText().toString().length() != 0 && field4.getText().toString().length() != 0;
    }
}