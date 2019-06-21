package com.example.mapdemo;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mapdemo.Model.PlaceDetail;
import com.example.mapdemo.Remote.IGoogleAPIService;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class ViewPlaceActivity extends AppCompatActivity {
//
//    @OverrideNewPlaceActivity
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_place);
//    }
//}

public class ViewPlaceActivity extends AppCompatActivity {
    ImageView photo;
    RatingBar ratingBar;
    TextView place_address,place_name,place_phone,opening_hours,rating;
    Button btnViewOnMap;
    ImageView icon;

    IGoogleAPIService mService;
    PlaceDetail mPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onSaveInstanceState(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);

        mService=PCommon.getGoogleAPIService();

        photo =(ImageView)findViewById(R.id.photo);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);

        place_address=(TextView)findViewById(R.id.place_address);
        place_name=(TextView)findViewById(R.id.place_name);
        place_phone=(TextView) findViewById(R.id.place_phone);
        opening_hours=(TextView)findViewById(R.id.open_hour);
        rating=(TextView)findViewById(R.id.rating);
        icon=(ImageView)findViewById(R.id.myicon);
        btnViewOnMap=(Button)findViewById(R.id.btn_show_map);


        place_name.setText("");
        place_phone.setText("");
        place_address.setText("");
        opening_hours.setText("");
        rating.setText("");

        Double latitude=Double.parseDouble(PCommon.currentResult.getGeometry().getLocation().getLat());
        Double longtitude=Double.parseDouble(PCommon.currentResult.getGeometry().getLocation().getLng());
        String strAdd = "";
        String strPhone="";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> addresses1 = geocoder.getFromLocation(latitude, longtitude, 1);
            if(addresses1!=null)
            {
                Address returnAdd=addresses1.get(1);
                StringBuilder strReturnAdd = new StringBuilder("");
                strReturnAdd.append(returnAdd.getPhone());
                strPhone = strReturnAdd.toString();
                place_phone.setText(strPhone);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                }
                strAdd = strReturnedAddress.toString();
                place_address.setText(strAdd);

            } else {
                place_address.setText("None");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
//            mPlace.getResult().getUrl()
            public void onClick(View view) {
                Intent mapIntent= new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);

            }
        });


        //get photo
        if(PCommon.currentResult.getPhotos()!=null && PCommon.currentResult.getPhotos().length>0)
        {
            Picasso.with(this)
                    .load(getPhotoOfPlace(PCommon.currentResult.getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(photo);
//            lay mot hinh dau tien thoi
        }
        //get icon
        Picasso.with(this)
                .load(PCommon.currentResult.getIcon())
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(icon);
        // rating bar
        if(PCommon.currentResult.getRating()!=null&& !TextUtils.isEmpty(PCommon.currentResult.getRating()))
        {
            ratingBar.setRating(Float.parseFloat(PCommon.currentResult.getRating()));
        }
        else
        {
            ratingBar.setVisibility(View.GONE);
        }
        // opening hour
        if(PCommon.currentResult.getOpening_hours()!=null)
        {
            opening_hours.setText("Open Now: "+PCommon.currentResult.getOpening_hours().getOpen_now());
        }else
        {
            opening_hours.setVisibility(View.GONE);
        }
        //use service to fetch adress and name
//        Common.currentResult.getPlace_id()

        mService.getDetailPlace(getPlaceDetailUrl(PCommon.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                        mPlace=response.body();
                        place_name.setText(PCommon.currentResult.getName());
                        rating.setText("("+PCommon.currentResult.getRating()+")");
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {
                    }
                });
    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url=new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+place_id);
        url.append("&key="+getResources().getString(R.string.google_api_key));
        return url.toString();
    }

    private String getPhotoOfPlace(String photo_reference, int maxWidth) {
        StringBuilder url=new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_reference);
        url.append("&key="+getResources().getString(R.string.google_api_key));
        return url.toString();
    }
}
