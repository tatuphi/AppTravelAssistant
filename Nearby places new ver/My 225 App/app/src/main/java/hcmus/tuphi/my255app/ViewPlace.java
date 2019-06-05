package hcmus.tuphi.my255app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.squareup.picasso.Picasso;

import hcmus.tuphi.my255app.Model.PlaceDetail;
import hcmus.tuphi.my255app.Remote.IGoogleAPIService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewPlace extends AppCompatActivity {
    ImageView photo;
    RatingBar ratingBar;
    TextView place_address,place_name,place_phone,opening_hours;
    Button btnViewOnMap;
    ImageView icon;

    IGoogleAPIService mService;
    PlaceDetail mPlace;
    Place newplace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onSaveInstanceState(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_place);

        mService=Common.getGoogleAPIService();

        photo =(ImageView)findViewById(R.id.photo);
        ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        place_address=(TextView)findViewById(R.id.place_address);
        place_name=(TextView)findViewById(R.id.place_name);
        place_phone=(TextView) findViewById(R.id.place_phone);
        opening_hours=(TextView)findViewById(R.id.open_hour);
        icon=(ImageView)findViewById(R.id.myicon) ;
        btnViewOnMap=(Button)findViewById(R.id.btn_show_map);


        place_name.setText("");
        place_phone.setText("");
        place_address.setText("");
        opening_hours.setText("");

        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
//            mPlace.getResult().getUrl()
            public void onClick(View view) {
                Intent mapIntent= new Intent(Intent.ACTION_VIEW, Uri.parse(mPlace.getResult().getUrl()));
                startActivity(mapIntent);
            }
        });

        //get photo
        if(Common.currentResult.getPhotos()!=null && Common.currentResult.getPhotos().length>0)
        {
            Picasso.with(this)
                    .load(getPhotoOfPlace(Common.currentResult.getPhotos()[0].getPhoto_reference(),1000))
                    .placeholder(R.drawable.ic_image_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .into(photo);
//            lay mot hinh dau tien thoi
        }
        Picasso.with(this)
                .load(Common.currentResult.getIcon())
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(icon);
        // rating bar
        if(Common.currentResult.getRating()!=null&& !TextUtils.isEmpty(Common.currentResult.getRating()))
        {
            ratingBar.setRating(Float.parseFloat(Common.currentResult.getRating()));
        }
        else
        {
            ratingBar.setVisibility(View.GONE);
        }
        // opening hour
        if(Common.currentResult.getOpening_hours()!=null)
        {
            opening_hours.setText("Open Now: "+Common.currentResult.getOpening_hours().getOpen_now());
        }else
        {
            opening_hours.setVisibility(View.GONE);
        }
//        website
//        if(Common.currentResult.getWebsite()!=null)
//        {
//            place_phone.setText("Website: "+Common.currentResult.getWebsite().toString());
//        }
//        else
//        {
//            place_phone.setVisibility(View.GONE);
//        }
//        address
//        if(Common.currentResult.getFormatted_address()!=null)
//        {
//            place_address.setText(Common.currentResult.getFormatted_address());
//        }
//        else
//        {
//            place_address.setVisibility(View.GONE);
//        }

        //use service to fetch adress and name
//        Common.currentResult.getPlace_id()
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult.getPlace_id()))
                .enqueue(new Callback<PlaceDetail>() {
                    @Override
                    public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                    mPlace=response.body();

                    place_name.setText(Common.currentResult.getName());
//                    place_address.setText(Common.currentResult.getFormatted_address());

//                    place_address.setText(Common.currentResult.getFormatted_address());
//                    place_phone.setText(Common.currentResult.getWebsite());
                    //place_address.setText(mPlace.getResult().getFormatted_address());
//                    place_name.setText(mPlace.getResult().getName());
//                    place_phone.setText(mPlace.getResult().getWebsite());
                    }

                    @Override
                    public void onFailure(Call<PlaceDetail> call, Throwable t) {
                    }
                });
    }

    private String getPlaceDetailUrl(String place_id) {
        StringBuilder url=new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        url.append("?placeid="+place_id);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }

    private String getPhotoOfPlace(String photo_reference, int maxWidth) {
        StringBuilder url=new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        url.append("?maxwidth="+maxWidth);
        url.append("&photoreference="+photo_reference);
        url.append("&key="+getResources().getString(R.string.browser_key));
        return url.toString();
    }
}
