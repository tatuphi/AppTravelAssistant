package com.example.appassistent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.Toast;






public  class ForgotPassword extends AppCompatActivity  {

    private TextInputEditText textInputEditTextEmail;
    private TextInputLayout textInputLayoutEmail;

    private AppCompatButton appCompatButtonConfirm;




    private NestedScrollView nestedScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

    }
}