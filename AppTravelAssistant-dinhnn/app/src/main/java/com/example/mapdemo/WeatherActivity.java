package com.example.mapdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
public class WeatherActivity extends AppCompatActivity {

    EditText editSearch;
    Button btnSearch;
    TextView txtName,txtTemp,txtStatus,txtMinTemp,txtMaxTemp, txtDay;
    ImageView imgIcon;
    String City = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        Anhxa();
        GetCurrentWeatherData("Thanh Pho Ho Chi Minh");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editSearch.getText().toString();
                if (city.equals("")){
                    City = "Thanh Pho Ho Chi Minh";
                    GetCurrentWeatherData(City);
                }else{
                    City = city;
                    GetCurrentWeatherData(City);
                }
            }
        });

    }
    public void GetCurrentWeatherData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(WeatherActivity.this);
        String url ="https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=89046cf2d2b4a042c06bff449185dbc7";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("ketqua",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtName.setText("Tên thành phố: "+name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l*1000L);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE yyyy-MM-dd");
                            String Day = simpleDateFormat.format(date);

                            txtDay.setText(Day);
                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");

                            Picasso.with(WeatherActivity.this).load("http://openweathermap.org/img/w/"+icon+".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String nhietdomin = jsonObjectMain.getString("temp_min");
                            String nhietdomax = jsonObjectMain.getString("temp_max");

                            Double a = Double.valueOf(nhietdo);
                            String Nhietdo = String.valueOf(a.intValue());
                            Double amin = Double.valueOf(nhietdomin);
                            String Nhietdomin = String.valueOf(amin.intValue());
                            Double amax = Double.valueOf(nhietdomax);
                            String Nhietdomax = String.valueOf(amax.intValue());

                            txtTemp.setText(Nhietdo+"°C");
                            txtMinTemp.setText(Nhietdomin+"°C");
                            txtMaxTemp.setText(Nhietdomax+"°C");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }


    private void Anhxa() {
        editSearch=(EditText) findViewById(R.id.edittextSearch);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        txtStatus = (TextView) findViewById(R.id.textviewStatus);
        txtTemp = (TextView) findViewById(R.id.textviewTemp);
        txtName = (TextView) findViewById(R.id.textviewName);
        txtMinTemp = (TextView) findViewById(R.id.textviewMinTemp);
        txtMaxTemp = (TextView) findViewById(R.id.textviewMaxTemp);
        txtDay = (TextView) findViewById(R.id.textviewDay);
        imgIcon = (ImageView) findViewById(R.id.imageIcon);
    }
}
