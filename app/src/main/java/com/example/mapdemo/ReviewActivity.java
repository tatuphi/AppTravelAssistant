package com.example.mapdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends Activity {

    private TextView LocationName;
    private Dialog AddCmtDialog;
    private RatingBar ratingBarOld;
    private TextView txtRatingAvg;
    private Button btnAddYourCmt;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private EditText txtComment;
    private Button btnAddComment;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private FirebaseAuth auth;
    List<Review> image_details;
    String locationID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        final ListView listView = (ListView) findViewById(R.id.listView);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        // checkif user loggin
        if (user == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        // Init Database;
        dbRef = FirebaseDatabase.getInstance().getReference();

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            locationID =(String) b.get("LOCATION_ID");
//            Toast.makeText(this, j, Toast.LENGTH_LONG).show();
        }

        LocationName = (TextView) findViewById(R.id.textview_PlaceName);
        ratingBarOld = (RatingBar) findViewById(R.id.ratingBarOld);
        txtRatingAvg = (TextView)findViewById(R.id.textview_RateAvg) ;
        //giá trị trung bình các lượt đánh giá trước
        //Float oldrating = 4.3f;
//        Float oldrating = 4.3f;
        ratingBarOld.setRating(0f);

        btnAddYourCmt = (Button)findViewById(R.id.button_AddYourCmt);
        btnAddYourCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCmtDialog = new Dialog(ReviewActivity.this);
                AddCmtDialog.setTitle("Đánh giá của bạn");
                AddCmtDialog.setContentView(R.layout.add_review);
                AddCmtDialog.setCancelable(true);
                ratingBar = (RatingBar)AddCmtDialog.findViewById(R.id.ratingBar);
                addListenerOnRatingBar();
                txtRatingValue = (TextView) AddCmtDialog.findViewById(R.id.textview_RatingValue);
                txtComment = (EditText)AddCmtDialog.findViewById(R.id.edit_Comment);
                btnAddComment = (Button)AddCmtDialog.findViewById(R.id.button_AddComment);
                btnAddComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Float getrating = ratingBar.getRating();
                        Toast.makeText(ReviewActivity.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
//                        String id, String userID, String locationID, String content, Double rate
//                        Review rev = new Review(,"IconUser",txtComment.getText().toString(),getrating);
                        String key = dbRef.child("reviews").push().getKey();
                        Review rev = new Review();
                        rev.id = key;
                        rev.content = txtComment.getText().toString();
                        rev.locationID = locationID;
                        rev.locationName = "Locatoin Name";
                        rev.userID = user.getUid();
                        rev.userName = user.getEmail();//user.getDisplayName().isEmpty() ? "default username" : user.getDisplayName();
                        rev.rate = getrating;
                        dbRef.child("reviews").child(key).setValue(rev.toMap());
                AddCmtDialog.cancel();

                    }
                });
                //show dialog
                AddCmtDialog.show();
            }
        });


        //PASS Location ID down here
        Query q = dbRef.child("reviews").orderByChild("locationID").equalTo(locationID);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getData(dataSnapshot) ) {
                    LocationName.setText(image_details.get(0).locationName);
                    float rating = calculateRating();
                    ratingBarOld.setRating(rating);
                    DecimalFormat df = new DecimalFormat("#.#");
                    df.setRoundingMode(RoundingMode.CEILING);
                    txtRatingAvg.setText(df.format(rating));
                    listView.setAdapter(new ReviewListCustom(ReviewActivity.this, image_details));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }
    private float calculateRating() {
        float total = 0;
        for (Review r : image_details){
            total += r.rate;
        }

        float res = total / image_details.size();

        return res;
    }
    public void addListenerOnRatingBar() {


        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                txtRatingValue.setText(String.valueOf(rating));
            }
        });
    }

    private boolean getData(DataSnapshot data) {
        List<Review> list = new ArrayList<Review>();
        if (data.exists()) {
            for (DataSnapshot issue : data.getChildren()) {
                Review v =new Review();
//                v.id = issue.child("id").getValue(String.class);
//                v.userID = issue.child("userID").getValue(String.class);
                v.locationName = issue.child("locationName").getValue(String.class);
                v.rate = issue.child("rate").getValue(Float.class);
                v.content = issue.child("content").getValue(String.class);
                v.userName = issue.child("username").getValue(String.class);
                list.add(v);
            }
            image_details = list;
            return true;
        }
        return false;
    }


}