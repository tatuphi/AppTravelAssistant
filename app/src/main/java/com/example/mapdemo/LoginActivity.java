package com.example.mapdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

        private final AppCompatActivity activity = LoginActivity.this;
    private Context context;

        private NestedScrollView nestedScrollView;

        private TextInputLayout textInputLayoutEmail;
        private TextInputLayout textInputLayoutPassword;

        private TextInputEditText textInputEditTextEmail;
        private TextInputEditText textInputEditTextPassword;

        private AppCompatButton appCompatButtonLogin;

        private AppCompatTextView textViewLinkRegister;
        private AppCompatTextView textViewLinkForgotPassword;
        private DBHelper db;


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            getSupportActionBar().hide();
            db = new DBHelper();
            File path = getApplication().getFilesDir();
            db.OpenDB(path, "mapServiceDB");
            db.createTable();
            db.insertUser("admin", "admin", null, "0_Mua do an  ,1_Di tam",null);
            db.insertUser("admin1", "admin1", null, null,null);
            db.insertUser("admin2", "admin2", null, null,null);
            initViews();
            initListeners();
//        initObjects();
        }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.CloseDB();
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

            if (utils.getName(this) != null ){
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
                    Intent intentRegister = new Intent(activity, Register.class);
                    startActivity(intentRegister);
                    break;
                case R.id.forgotPassword:
                    Intent intent = new Intent(activity, ForgotPassword.class);
                    startActivity(intent);
                    break;
            }
        }
        //kiểm tra username
    private boolean validateUsername() {

        String usernameInput = textInputEditTextEmail.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            textInputLayoutEmail.setError("Field can't be empty");
            return false;

        } else {
            textInputEditTextEmail.setError(null);
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



// chỗ kiểm  tra và load dữ liệu
        private void verifyFromSQLite(){
            if ( !validateUsername() | !validatePassword()) {
                return;
            }
            String username = textInputEditTextEmail.getText().toString().trim();
            String password = textInputEditTextPassword.getText().toString().trim();
            User user = db.getUser(username);
            if (user == null){
                emptyInputEditText();
                return;
            }
            if (user.password.equals(password)) {
                PreferenceUtils.saveEmail(username, this);
                PreferenceUtils.savePassword(password, this);
                Intent accountsIntent = new Intent(activity, UsersActivity.class);
                // get userID from username
                User u = db.getUser( textInputEditTextEmail.getText().toString().trim());
                accountsIntent.putExtra("USER", u.ID);
                emptyInputEditText();
                startActivity(accountsIntent);
                finish();
            } else {
                Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
            }
        }

        private void emptyInputEditText(){
            textInputEditTextEmail.setText("");
            textInputEditTextPassword.setText("");
        }

    }




