package com.example.appassistent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



/**
 * Created by delaroy on 3/27/17.
 */
public class UsersActivity extends AppCompatActivity {

    private TextView textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.log_out:
                PreferenceUtils.savePassword(null, this);
                PreferenceUtils.saveEmail(null, this);
                 intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;


        case R.id.action_hom:
//        PreferenceUtils.savePassword(null, this);
//        PreferenceUtils.saveEmail(null, this);
        Intent i = new Intent(this, Home.class);
        startActivity(i);
        finish();
        return true;

    }

        return super.onOptionsItemSelected(item);
    }

}
