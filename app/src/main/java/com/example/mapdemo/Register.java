package com.example.mapdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;

public  class Register extends AppCompatActivity {

    private final AppCompatActivity activity = Register.this;

    private NestedScrollView nestedScrollView;


    private TextInputLayout textInputLayoutUser;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;


    private TextInputEditText textInputEditTextUser;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatTextView appCompatTextViewLoginLink;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);


        textInputLayoutUser = (TextInputLayout) findViewById(R.id.textInputLayoutUser);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);


        textInputEditTextUser = (TextInputEditText) findViewById(R.id.textInputEditTextUser);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatTextView) findViewById(R.id.appCompatTextViewLoginLink);
        appCompatTextViewLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new  Intent(activity,LoginActivity.class);

                startActivity(intent);
                finish();
                }


        });
        appCompatButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataToSQLite();
            }
        });
        // Firebase
        auth = FirebaseAuth.getInstance();

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
//        db.CloseDB();
    }
    private boolean validateUsername() {

        String usernameInput = textInputEditTextUser.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            textInputLayoutUser.setError("Field can't be empty");
            return false;

        } else {
            textInputEditTextUser.setError(null);
            return true;
        }
    }
    // kiểm tra password
    private boolean validatePassword() {
        String passwordInput = textInputEditTextPassword.getText().toString().trim();


        if (passwordInput.isEmpty()) {
            textInputLayoutPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputEditTextPassword.setError(null);
            return true;
        }
    }
    private boolean validateConfirmPassword() {
        String passwordInput = textInputEditTextConfirmPassword.getText().toString().trim();


        if (passwordInput.isEmpty()) {
            textInputLayoutConfirmPassword.setError("Field can't be empty");
            return false;
        }
        else {
            textInputEditTextConfirmPassword.setError(null);
            return true;
        }
    }
    private Boolean isInputEditTextMatches(){
        String passwordInput = textInputEditTextConfirmPassword.getText().toString().trim();
        String cpasswordInput = textInputEditTextPassword.getText().toString().trim();
        if(!passwordInput.contentEquals(cpasswordInput)){
            textInputLayoutConfirmPassword.setError("Doesn't mach password");
            return false;
        }

        textInputEditTextConfirmPassword.setError(null);
        return true;
    }


    private void postDataToSQLite(){
        if ( !validateUsername() | !validatePassword()|!validateConfirmPassword()|!isInputEditTextMatches()) {
            return;
        }
        auth.createUserWithEmailAndPassword(textInputEditTextUser.getText().toString(), textInputEditTextPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(Register.this, "Authentication Successful.",
                            Toast.LENGTH_SHORT).show();
                    emptyInputEditText();
                    Intent intent = new Intent(Register.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(Register.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    emptyInputEditText();
                }
            }
        });
// chỗ load dữ liệ
//        if (0 >= db.insertUser(textInputEditTextUser.getText().toString().trim(), textInputEditTextPassword.getText().toString().trim(), null, null, null
//        )){
//
//            String input="User:  "+textInputEditTextUser.getText().toString();
//            input+="\n";
//            input+="Password:  "+textInputEditTextPassword.getText().toString();
//
//
//
//            Toast.makeText(getApplicationContext(),input , Toast.LENGTH_SHORT).show();
//
//            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
//            emptyInputEditText();
//            Intent intent = new Intent(Register.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//
//
//        } else {
//            // Snack Bar to show error message that record already exists
//            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
//        }


    }

    private void emptyInputEditText(){

        textInputEditTextUser.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}

