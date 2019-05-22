import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends Activity {

    private RatingBar ratingBar;
    private TextView txtRatingValue;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        addListenerOnRatingBar();
        List<review_comment> image_details = getListData();
        final ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new review_CustomListAdapter(this, image_details));

        // Khi người dùng click vào các ListItem
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                review_comment review_cmt = (review_comment) o;
                Toast.makeText(ReviewActivity.this, "Selected :" + " " + review_cmt, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.textview_RatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
    }
    //lấy dữ liệu (tạm thời)
    private  List<review_comment> getListData() {
        List<review_comment> list = new ArrayList<review_comment>();
        review_comment cmt1 = new review_comment("Person1", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần. Là một người thích đi du lịch, tại sao đến bây giờ mình mới được biết một nơi đẹp như thế này nhỉ. Trời đất dung hoa, vạn vật sinh sôi, người dân nơi đây thì cực kỳ thân thiện và hiếu khách.");
        review_comment cmt2 = new review_comment("Person2", "", "Đẹp dã man");
        review_comment cmt3 = new review_comment("Person3", "", "Đáng để bỏ tiền đi tham quan một lần");
        review_comment cmt4 = new review_comment("Person4", "", "Sao không ai chỉ mình đi sớm hơn nhỉ. Quá tuyệt vời");
        review_comment cmt5 = new review_comment("Person5", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần");
        review_comment cmt6 = new review_comment("Person6", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần");
        review_comment cmt7 = new review_comment("Person7", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần");
        review_comment cmt8 = new review_comment("Person8", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần");
        review_comment cmt9 = new review_comment("Person9", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần");
        review_comment cmt10 = new review_comment("Person10", "", "Đẹp kinh khủng khiếp, chắc có lẽ mình phải đi thử một lần");

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