package com.example.barterplaceui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//take user name and password from user
public class SignUpPage extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        //set font to text view
        TextView clickTiltle = (TextView) findViewById(R.id.tv);
        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        clickTiltle.setTypeface(myFont);

        mAuth = FirebaseAuth.getInstance();
    }

    //signup
    public void signup(View view) {
        EditText etMail = (EditText) findViewById(R.id.userNameField);
        EditText etPass = (EditText) findViewById(R.id.passwordField);
        String newEmail = etMail.getText().toString();       //input
        String newPassword = etPass.getText().toString();    //input
        Task<AuthResult> t;

        //check inputs field and firebase require (atleast 6 chars)
        if (!newEmail.isEmpty() && newPassword.length() >= 6) {
            //get the reference
            FirebaseAuth fire = mAuth;
            //Sign up:
            t = fire.createUserWithEmailAndPassword(newEmail, newPassword);
            //add listener to dynamic activity
            t.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() { //add listener
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        //get reference to current user
                        FirebaseUser user = mAuth.getCurrentUser();
                        //clean fields
                        etMail.setText("");
                        etPass.setText("");
                        Toast.makeText(getBaseContext(), "Succes", Toast.LENGTH_SHORT).show();//dev
                        //send to SignUpDetailsPage
                        Intent intent = new Intent(getBaseContext(), SignUpDetailsPage.class);
                        startActivity(intent);
                        //end sending and kill last process
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), "Invalid Email/Password", Toast.LENGTH_LONG).show();   //process fail
                    }
                }
            });
        } else {
            Toast.makeText(getBaseContext(), "Invalid Email Or Password, Password must be more then 6 characters",
                    Toast.LENGTH_LONG).show();
        }
    }
}