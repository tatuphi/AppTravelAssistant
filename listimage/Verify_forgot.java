package com.example.mapdemo;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Verify_forgot extends AppCompatActivity {
    private TextInputLayout textInputLayoutEmail ;
    private TextInputEditText textInputEditTextEmail;
    private Button btnReset;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    AppCompatTextView btnBack;
    Button resetpassword;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_forgot);
        auth = FirebaseAuth.getInstance();
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyEmail();
            }
        });

    }
    private boolean validateEmail() {
        String emailInput = textInputEditTextEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            textInputLayoutEmail.setError("Field can't be empty");
            return false;
        } else {
            textInputEditTextEmail.setError(null);
            return true;
        }
    }
    void verifyEmail(){
        String emailInput = textInputEditTextEmail.getText().toString().trim();
        if(!validateEmail())
        {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(emailInput)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Verify_forgot.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Verify_forgot.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}













