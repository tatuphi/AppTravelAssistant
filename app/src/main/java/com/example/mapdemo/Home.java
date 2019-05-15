package com.example.mapdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class Home extends AppCompatActivity {
    String userID= "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        LinearLayout checklist =(LinearLayout)findViewById(R.id.lnCheckList);
        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, ChecklistActivity.class);
                startActivity(intent);
//                finish();
            }
        });
        LinearLayout Mydiary =(LinearLayout)findViewById(R.id.lnDiary);
        Mydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, DiaryActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        LinearLayout weather =(LinearLayout)findViewById(R.id.lnWeather);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, WeatherActivity.class);
                startActivity(intent);
//                finish();
        // Get userID from Intent that invoke
                Bundle extras = getIntent().getExtras();
                if(extras != null){
                    userID = extras.getString("USER");
                }

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_arrowleft:
                PreferenceUtils.savePassword(null, this);
                PreferenceUtils.saveEmail(null, this);
                intent = new Intent(this, UsersActivity.class);
                intent.putExtra("USER", userID);
                startActivity(intent);
//                finish();
                break;
            case R.id.lnCheckList :
                intent = new Intent(this, ChecklistActivity.class);
                intent = new Intent(this, UsersActivity.class);
                intent.putExtra("USER", userID);
                startActivity(intent);
//                finish();
                break;
            case R.id.lnDiary :
                intent = new Intent(this, DiaryActivity.class);
                intent = new Intent(this, UsersActivity.class);
                intent.putExtra("USER", userID);
                startActivity(intent);
//                finish();
                break;
                case R.id.lnWeather :
                intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
//                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
