package com.example.mapdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public  class ForgotPassword extends AppCompatActivity  {

    private TextInputEditText textInputEditTextEmail;
    private TextInputLayout textInputLayoutEmail;

    private AppCompatButton appCompatButtonConfirm;

    FirebaseAuth auth;
    TextView txtEmail;
    Button btnConfirm;
    private NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        btnConfirm = findViewById(R.id.btnConfirm);
        txtEmail = findViewById(R.id.txtEmail);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.sendPasswordResetEmail(txtEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPassword.this,"Check your mail inbox to reset password", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        } else{
                            txtEmail.setText("");
                            Toast.makeText(ForgotPassword.this,"Your Email may wrong, plz re-enter", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
}