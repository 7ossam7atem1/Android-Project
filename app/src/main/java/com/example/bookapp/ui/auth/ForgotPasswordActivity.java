package com.example.bookapp.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.bookapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    //declaring the firebase
    private FirebaseAuth firebaseAuth;

     //adding the progress dialog
    private ProgressDialog progressDialog;

    private ImageButton backBtn;

    private Button submitBtn;
    private EditText emailEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
       //init the firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //initialize and setup the progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait");
        progressDialog.setCanceledOnTouchOutside(false);


        //handle the back button
        backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        //handle begin recovery password click
        submitBtn = findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });




    }
    private String email = "";
    private void validateData() {
        emailEt = findViewById(R.id.emailEt);
        email = emailEt.getText().toString().trim();
            //Edit text shouldn't be empty and contains a valid email format
        if(email.isEmpty()) {
            Toast.makeText(this, "Enter Your Email,please", Toast.LENGTH_SHORT).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid Email Pattern", Toast.LENGTH_SHORT).show();

        }else{
            recoverPassword();
        }


    }

    private void recoverPassword() {
        //Show the progress Dialog
        progressDialog.setMessage("Recovering your Password:) , Email sent to " + email);
        progressDialog.show();

        //sending the recovery email
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                // email sent
                progressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this,"Email sent Successfully to"+email+"  Check your Inbox", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // failed to send the email
                progressDialog.dismiss();
                Toast.makeText(ForgotPasswordActivity.this , "Email doesn't Exist , Failure due to"+e.getMessage(), Toast.LENGTH_SHORT ).show();



            }
        });



    }
}