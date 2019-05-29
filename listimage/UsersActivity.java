package com.example.mapdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by delaroy on 3/27/17.
 */
public class    UsersActivity extends AppCompatActivity {

    private TextView textViewName;
    String[] listviewTitle = new String[]{
            "Hồ Chí Minh", "Hà Nội", "Đã Nẵng", "Nha Trang",
            "Cần Thơ", "Phan Thiết", "Hội An", "Đà Lạt",
    };


    int[] listviewImage = new int[]{
            R.drawable.dalat, R.drawable.hanoi, R.drawable.danang, R.drawable.nhatrang,
            R.drawable.cantho, R.drawable.phanthiet, R.drawable.hoian, R.drawable.dalat,
    };

    String[] listviewShortDescription = new String[]{
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",
            "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description", "Android ListView Short Description",

    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 8; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);


        }


        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.spacecraftImageView, R.id.nameTextView, R.id.propellantTextView};
        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.model, from,to);
        ListView androidListView = (ListView) findViewById(R.id.myListView);
        androidListView.setAdapter(simpleAdapter);

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
