package com.example.appassistent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

        private final AppCompatActivity activity = LoginActivity.this;

        private NestedScrollView nestedScrollView;

        private TextInputLayout textInputLayoutEmail;
        private TextInputLayout textInputLayoutPassword;

        private TextInputEditText textInputEditTextEmail;
        private TextInputEditText textInputEditTextPassword;

        private AppCompatButton appCompatButtonLogin;

        private AppCompatTextView textViewLinkRegister;
        private AppCompatTextView textViewLinkForgotPassword;



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            getSupportActionBar().hide();

            initViews();
            initListeners();
//        initObjects();
        }
        private void initViews(){
            nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

            textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
            textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

            textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
            textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

            appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

            textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
            textViewLinkForgotPassword = (AppCompatTextView) findViewById(R.id.forgotPassword);
            PreferenceUtils utils = new PreferenceUtils();

            if (utils.getEmail(this) != null ){
                Intent intent = new Intent(LoginActivity.this, UsersActivity.class);
                startActivity(intent);
            }else{

            }
        }

        private void initListeners(){
            appCompatButtonLogin.setOnClickListener(this);
            textViewLinkRegister.setOnClickListener(this);
            textViewLinkForgotPassword.setOnClickListener(this);
        }



        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.appCompatButtonLogin:
                    verifyFromSQLite();
                    break;
                case R.id.textViewLinkRegister:
                    Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                    startActivity(intentRegister);
                    break;
                case R.id.forgotPassword:
                    Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
                    startActivity(intent);
                    break;
            }
        }
    private boolean validateUsername() {

        String usernameInput = textInputEditTextEmail.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            textInputEditTextEmail.setError("Field can't be empty");
            return false;
        } else if (usernameInput.length() > 15) {
            textInputEditTextEmail.setError("Username too long");
            return false;
        } else {
            textInputEditTextEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = textInputEditTextPassword.getText().toString().trim();


        if (passwordInput.isEmpty()) {
            textInputEditTextPassword.setError("Field can't be empty");
            return false;
        } else {
            textInputEditTextPassword.setError(null);
            return true;
        }
    }


        private void verifyFromSQLite(){
            if ( !validateUsername() | !validatePassword()) {
                return;
            }
            String email = textInputEditTextEmail.getText().toString().trim();
            String password = textInputEditTextPassword.getText().toString().trim();

            if (email.equals("test")&&password.equals("test")) {

                PreferenceUtils.saveEmail(email, this);
                PreferenceUtils.savePassword(password, this);
                Intent accountsIntent = new Intent(activity, UsersActivity.class);
                accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
                emptyInputEditText();
                startActivity(accountsIntent);
                finish();
            } else {
                Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
            }
        }

        private void emptyInputEditText(){
            textInputEditTextEmail.setText(null);
            textInputEditTextPassword.setText(null);
        }
    }




