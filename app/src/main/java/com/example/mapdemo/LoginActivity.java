package com.example.mapdemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

        private final AppCompatActivity activity = LoginActivity.this;
        private Context context;
        FirebaseAuth auth;
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
            auth =FirebaseAuth.getInstance();

            initViews();
            initListeners();
//        initObjects();
        }
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }
        private void initViews(){
            nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

            textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
            textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);

            textInputEditTextEmail = (TextInputEditText) findViewById(R.id.txtEmail);
            textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

            appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

            textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
            textViewLinkForgotPassword = (AppCompatTextView) findViewById(R.id.forgotPassword);

            // AUTO LOGIN USER
//            PreferenceUtils utils = new PreferenceUtils();
//
//            if (utils.getName(this) != null ){
//                Intent intent = new Intent(LoginActivity.this, UsersActivity.class);
//                User u = db.getUser(utils.getName(this));
//                intent.putExtra("USER", u.ID);
//                startActivity(intent);
//            }else{
//
//            }
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
            String email = textInputEditTextEmail.getText().toString().trim();
            String password = textInputEditTextPassword.getText().toString().trim();
//            User user = db.getUser(email);
//            if (user == null){
//                emptyInputEditText();
//                return;
//            }
//            if (user.password.equals(password)) {
//                PreferenceUtils.saveEmail(email, this);
//                PreferenceUtils.savePassword(password, this);
//                Intent accountsIntent = new Intent(activity, UsersActivity.class);
//                // get userID from username
//                User u = db.getUser( textInputEditTextEmail.getText().toString().trim());
//                accountsIntent.putExtra("USER", u.ID);
//                emptyInputEditText();
//                startActivity(accountsIntent);
//                finish();
//            } else {
//                Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
//            }
            // Alternative
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = auth.getCurrentUser();
                                Intent accountsIntent = new Intent(activity, UsersActivity.class);
                                Toast.makeText(LoginActivity.this, user.getUid(), Toast.LENGTH_LONG).show();
                                emptyInputEditText();
                                startActivity(accountsIntent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Wrong pass or email!!!!!!", Toast.LENGTH_LONG).show();
                                emptyInputEditText();
                            }
                        }
                    });
        }

        private void emptyInputEditText(){
            textInputEditTextEmail.setText("");
            textInputEditTextPassword.setText("");
        }

    }




