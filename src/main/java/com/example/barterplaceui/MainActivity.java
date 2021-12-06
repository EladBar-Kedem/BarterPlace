package com.example.barterplaceui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import services.User;

//sign in page
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SignInButton signInButton;
    private GoogleSignInClient googleSignInClient;
    private String TAG = "MainActivity";
    private int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        //google sign in
        signInButton = findViewById(R.id.btnGoogleLogin);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });
        //load the font to the titles:
        TextView title1 = (TextView) findViewById(R.id.welcomeTitle);
        TextView title2 = (TextView) findViewById(R.id.welcomeTitle2);
        TextView title3 = (TextView) findViewById(R.id.tvNewUser);

        Typeface myFont = Typeface.createFromAsset(getAssets(), "font2.ttf");
        title1.setTypeface(myFont);
        title2.setTypeface(myFont);
        title3.setTypeface(myFont);
    }

    private void googleSignIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(getBaseContext(), "SignInGoogle Succes", Toast.LENGTH_LONG).show();//dev
                FireBaseGoogleAuth(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getBaseContext(), "SignInGoogle Fail", Toast.LENGTH_LONG).show();//dev
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(getBaseContext(), "SignInGoogle Succes", Toast.LENGTH_LONG).show();//dev
            FireBaseGoogleAuth(acc.getIdToken());
        } catch (ApiException e) {
            Toast.makeText(getBaseContext(), "SignInGoogle Fail", Toast.LENGTH_LONG).show();//dev
            FireBaseGoogleAuth(null);
        }
    }

    private void FireBaseGoogleAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //log in successfully send to HomePage
                    Intent intent = new Intent(getBaseContext(), HomePage.class);
                    startActivity(intent);
                    //end sending and kill last process
                    finish();
                } else {
                    Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();//log in fail
                }
            }
        });
    }

    //save user who already sign in for next entries
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) { //user already made login
            //send to HomePage
            Intent intent = new Intent(getBaseContext(), HomePage.class);
            startActivity(intent);
            //end sending and kill last process for avoid -> user in home page press back button and stack in loop because didnt log out.
            finish();
        } else {
            //welcome toast for visit login page
            Toast.makeText(getBaseContext(), "Welcome", Toast.LENGTH_SHORT).show();
        }
    }

    //Sign in:
    public void sign(View view) {
        EditText etMail = (EditText) findViewById(R.id.userNameField);
        EditText etPass = (EditText) findViewById(R.id.passwordField);
        String newEmail = etMail.getText().toString();       //input
        String newPassword = etPass.getText().toString();    //input
        Task<AuthResult> t;
        //check inputs field and firebase require (at least 6 chars)
        if (!newEmail.isEmpty() && newPassword.length() >= 6) {
            //get the reference
            FirebaseAuth fire = mAuth;
            //Sign in:
            t = fire.signInWithEmailAndPassword(newEmail, newPassword);
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
                        //send to HomePage
                        Intent intent = new Intent(getBaseContext(), HomePage.class);
                        startActivity(intent);
                        //end sending and kill last process
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), "Invalid Email/Password", Toast.LENGTH_LONG).show();   } }});
        } else {
            Toast.makeText(getBaseContext(), "Invalid Email Or Password, Password must be more then 6 characters", Toast.LENGTH_LONG).show(); }
    }

    public void moveToSignUpPage(View view) {
        //send to SignUp
        Intent intent = new Intent(getBaseContext(), SignUpPage.class);
        startActivity(intent);
        //end sending and kill last process
        finish();
    }
}