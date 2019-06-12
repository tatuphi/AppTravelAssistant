package com.example.mapdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    Integer userID;
    Review lc;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        DBHelper db = new DBHelper();
        db.ConnectToFirebase();

        // init Authenticator
        auth =FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null){
            Intent intent= new Intent(Home.this, LoginActivity.class);
            startActivity(intent);
//            Toast.makeText(this, "user no null", Toast.LENGTH_LONG).show();
            finish();
        }



       //====================================

        setContentView(R.layout.activity_home);
        LinearLayout checklist =(LinearLayout)findViewById(R.id.lnCheckList);
        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, ChecklistActivity.class);
                intent.putExtra("USER", userID);
                startActivity(intent);
            }
        });

        LinearLayout Mydiary =(LinearLayout)findViewById(R.id.lnDiary);
        Mydiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, DiaryActivity.class);
                intent.putExtra("USER", "dinhnn");
                startActivity(intent);
            }
        });

        LinearLayout weather =(LinearLayout)findViewById(R.id.lnWeather);
        weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, WeatherActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout convert =(LinearLayout)findViewById(R.id.convert_money);
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, Convert_money.class);
                startActivity(intent);
            }
        });
        LinearLayout logOut =(LinearLayout)findViewById(R.id.lnLogOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(Home.this, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(Home.this,"Signed OUT", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        LinearLayout nearBy =(LinearLayout)findViewById(R.id.lnNearbyServices);
        nearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, NewPlaceActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout addNew =(LinearLayout)findViewById(R.id.lnAddNewPlace);
        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Home.this, NewPlaceActivity.class);
                startActivity(intent);
            }
        });
        if (lc != null){
        Toast.makeText(this, lc.content, Toast.LENGTH_LONG).show();}


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (lc != null){
            Toast.makeText(this, lc.content, Toast.LENGTH_LONG).show();}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (lc != null){
            Toast.makeText(this, lc.content, Toast.LENGTH_LONG).show();}
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
                startActivity(intent);
//                finish();
                break;
            case R.id.lnCheckList :
                intent = new Intent(this, ChecklistActivity.class);
                intent.putExtra("USER", userID);
                startActivity(intent);
//                finish();
                break;
            case R.id.lnDiary :
                intent = new Intent(this, DiaryActivity.class);
                intent.putExtra("USER", userID);
                startActivity(intent);
//                finish();
                break;
                case R.id.lnWeather :
                intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
//                finish();
                break;
            case R.id.lnNearbyServices :
//                intent = new Intent(this, WeatherActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(this, "Nearby Services", Toast.LENGTH_LONG).show();
                break;
            case R.id.lnAddNewPlace :
//                intent = new Intent(this, WeatherActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(this, "New Place", Toast.LENGTH_LONG).show();
                break;
            case R.id.lnDestinationList :
//                intent = new Intent(this, WeatherActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(this, "Destination List", Toast.LENGTH_LONG).show();
                break;
            case R.id.lnLogOut :
//                intent = new Intent(this, WeatherActivity.class);
//                startActivity(intent);
//                finish();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
