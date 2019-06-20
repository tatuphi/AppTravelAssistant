package com.example.mapdemo;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Convert_money extends AppCompatActivity {
    private ArrayList<CountryItem> mCountryList;
    private CountryAdapter mAdapter;
    private CountryAdapter mAdapterother;
    String clickedCountryName;
    String clickedCountryNameother;
    TextInputEditText money;
    EditText showmoney;
    String inputnumber;
    String donvi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_money);
        initList();
        money= findViewById(R.id.money);
        showmoney=findViewById(R.id.solve);
        Spinner spinnerCountries = findViewById(R.id.country);
        Spinner SpinnerCountriesother= findViewById(R.id.countryother);

        mAdapterother = new CountryAdapter(this, mCountryList);
        SpinnerCountriesother.setAdapter(mAdapterother);

        SpinnerCountriesother.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);
                clickedCountryNameother = clickedItem.getCountryName();

                Toast.makeText(Convert_money.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAdapter = new CountryAdapter(this, mCountryList);
        spinnerCountries.setAdapter(mAdapter);
        spinnerCountries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);
                clickedCountryName = clickedItem.getCountryName();
                Toast.makeText(Convert_money.this, clickedCountryName + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        AppCompatButton conver= findViewById(R.id.appCompatButtonconverter);
        conver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Convertmoney();
            }
        });
    }
    private  double Conertresult(double k,String money){
        double result= 0;

        double a=Integer.parseInt(money);
        result=a*k;
        return  result;

    }
    private  void Convertmoney()
    {

         inputnumber=  money.getText().toString().trim();

        double result = 0;
        if((clickedCountryName.equals("USA"))&&(clickedCountryNameother.equals("UK"))){

            result =Conertresult(0.79,inputnumber);

        }
        if((clickedCountryName.equals("USA"))&&(clickedCountryNameother.equals("EU"))){
            result =Conertresult(0.88,inputnumber);

        }
        if((clickedCountryName.equals("USA"))&&(clickedCountryNameother.equals("VIETNAM"))){
            result =Conertresult(23200,inputnumber);

        }
        if((clickedCountryName.equals("USA"))&&(clickedCountryNameother.equals("JAPAN"))){
            result =Conertresult(109,inputnumber);

        }
        if((clickedCountryName.equals("UK"))&&(clickedCountryNameother.equals("USA"))){
            result =Conertresult(1.27,inputnumber);

        }
        if((clickedCountryName.equals("UK"))&&(clickedCountryNameother.equals("EU"))){
            result =Conertresult(1.12,inputnumber);


        }
        if((clickedCountryName.equals("UK"))&&(clickedCountryNameother.equals("VIETNAM"))){
            result =Conertresult(29600,inputnumber);

        }
        if((clickedCountryName.equals("UK"))&&(clickedCountryNameother.equals("JAPAN"))){
            result =Conertresult(138,inputnumber);

        }
        if((clickedCountryName.equals("EU"))&&(clickedCountryNameother.equals("USA"))){
            result =Conertresult(1.13,inputnumber);

        }
        if((clickedCountryName.equals("EU"))&&(clickedCountryNameother.equals("UK"))){
            result =Conertresult(1.03,inputnumber);

        }
        if((clickedCountryName.equals("EU"))&&(clickedCountryNameother.equals("VIETNAM"))){
            result =Conertresult(26400,inputnumber);


        }
        if((clickedCountryName.equals("EU"))&&(clickedCountryNameother.equals("JAPAN"))){
            result =Conertresult(0.003,inputnumber);

        }
        if((clickedCountryName.equals("VIETNAM"))&&(clickedCountryNameother.equals("USA"))){
            result =Conertresult(0.05,inputnumber);

        }
        if((clickedCountryName.equals("VIETNAM"))&&(clickedCountryNameother.equals("UK"))){
            result =Conertresult(0.07,inputnumber);

        }
        if((clickedCountryName.equals("VIETNAM"))&&(clickedCountryNameother.equals("EU"))){
            result =Conertresult(0.06,inputnumber);

        }
        if((clickedCountryName.equals("VIETNAM"))&&(clickedCountryNameother.equals("JAPAN"))){
            result =Conertresult(0.0049,inputnumber);

        }
        if((clickedCountryName.equals("JAPAN"))&&(clickedCountryNameother.equals("USA"))){
            result =Conertresult(0.08,inputnumber);

        }
        if((clickedCountryName.equals("JAPAN"))&&(clickedCountryNameother.equals("UK"))){
            result =Conertresult(0.01,inputnumber);

        }
        if((clickedCountryName.equals("JAPAN"))&&(clickedCountryNameother.equals("EU"))){
            result =Conertresult(0.01,inputnumber);

        }
        if((clickedCountryName.equals("JAPAN"))&&(clickedCountryNameother.equals("VIETNAM"))){
            result =Conertresult(200,inputnumber);


        }
        String a= String.valueOf(result);

        showmoney.setText(a);
        Toast.makeText(Convert_money.this, a  , Toast.LENGTH_SHORT).show();



    }

    private void initList() {
        mCountryList = new ArrayList<>();
        mCountryList.add(new CountryItem("UK", R.drawable.uk));
        mCountryList.add(new CountryItem("EU", R.drawable.eu));
        mCountryList.add(new CountryItem("USA", R.drawable.us));
        mCountryList.add(new CountryItem("VIETNAM", R.drawable.vietnam));
        mCountryList.add(new CountryItem("JAPAN", R.drawable.japan));
    }
}




