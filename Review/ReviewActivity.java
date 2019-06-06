package com.example.mapdemo;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends Activity {

    private Dialog AddCmtDialog;
    private RatingBar ratingBarOld;
    private TextView txtRatingAvg;
    private Button btnAddYourCmt;
    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private EditText txtComment;
    private Button btnAddComment;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ratingBarOld = (RatingBar) findViewById(R.id.ratingBarOld);
        txtRatingAvg = (TextView)findViewById(R.id.textview_RateAvg) ;
        //giá trị trung bình các lượt đánh giá trước
        Float oldrating = 4.3f;
        ratingBarOld.setRating( oldrating);
        txtRatingAvg.setText(String.valueOf(oldrating));
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
                Float getrating = ratingBar.getRating();
                txtRatingValue = (TextView) AddCmtDialog.findViewById(R.id.textview_RatingValue);
                txtComment = (EditText)AddCmtDialog.findViewById(R.id.edit_Comment);
                btnAddComment = (Button)AddCmtDialog.findViewById(R.id.button_AddComment) ;
                //show dialog
                AddCmtDialog.show();
                // getrating: lấy số sao
                Review rev = new Review("UserName","IconUser",txtComment.getText().toString(),getrating);
            }
        });

        List<Review> image_details = getListData();
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ReviewListCustom(this, image_details));


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

    //lấy dữ liệu listview(tạm thời)
    private List<Review> getListData() {
        List<Review> list = new ArrayList<Review>();
        Review cmt1 = new Review("Person1", "ps1", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần. Là một người thích đi du lịch, tại sao đến bây giờ mình mới được biết một nơi đẹp như thế này nhỉ. Trời đất dung hoa, vạn vật sinh sôi, người dân nơi đây thì cực kỳ thân thiện và hiếu khách.",4.1f);
        Review cmt2 = new Review("Person2", "ps1", "Đẹp dã man",1.0f);
        Review cmt3 = new Review("Person3", "ps1", "Đáng để bỏ tiền đi tham quan một lần",2.0f);
        Review cmt4 = new Review("Person4", "", "Sao không ai chỉ mình đi sớm hơn nhỉ. Quá tuyệt vời",3.0f);
        Review cmt5 = new Review("Person5", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần",4.0f);
        Review cmt6 = new Review("Person6", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần",2.0f);
        Review cmt7 = new Review("Person7", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần",5.0f);
        Review cmt8 = new Review("Person8", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần",3.0f);
        Review cmt9 = new Review("Person9", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần",4.0f);
        Review cmt10 = new Review("Person10", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần",2.0f);

        list.add(cmt1);
        list.add(cmt2);
        list.add(cmt3);
        list.add(cmt4);
        list.add(cmt5);
        list.add(cmt6);
        list.add(cmt7);
        list.add(cmt8);
        list.add(cmt9);
        list.add(cmt10);
        return list;
    }

}