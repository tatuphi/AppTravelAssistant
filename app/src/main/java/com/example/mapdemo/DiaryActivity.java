package com.example.mapdemo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class DiaryActivity extends AppCompatActivity {

    //Button btnC;
    Button btnInsert, btnRemove, btnEdit,btnItemInsert, btnItemDelete,btnItemEdit;
    TextView txtFriend, txtPlaces,txtTime;
    ImageView imgV;
    EditText txtCost, txtNote,txtFriendItem,txtPlaceItem;
    TextView lblDate;
    ArrayAdapter Friendadapter, Placeadapter;
    //ArrayList<String>placeList;
    DBHelper db;
    Diary currentDiary;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference  dbRef;
    String datee;


    ArrayList<String> placeList;
    ArrayList<String> friendList;
    int itemPos;


    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeList = new ArrayList<String>();
        friendList = new ArrayList<String>();

        // init DB
        db = new DBHelper();
        db.ConnectToFirebase();
        // init currentDiay
        currentDiary = new Diary();
        // get userID from Activity that invoke this one
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
        Query query = dbRef.child("diaries").orderByChild("userID").equalTo(user.getUid()).limitToFirst(1);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // init View
        setContentView(R.layout.diary);

        imgV =(ImageView)findViewById(R.id.imgV);

        txtFriend= (TextView) findViewById(R.id.lbFriend);
        txtPlaces = (TextView) findViewById(R.id.lbPlaces);
        txtCost = (EditText) findViewById(R.id.txtCost);
        txtTime =(TextView) findViewById(R.id.lbTime);
        txtCost =(EditText) findViewById(R.id.txtCost);
        txtNote = (EditText)findViewById(R.id.txtNote);

        btnInsert =(Button)findViewById(R.id.btnInsert);
        btnRemove =(Button)findViewById(R.id.btnRemove);
        btnEdit =(Button) findViewById(R.id.btnEdit);

        setCurDate();
        // Insert New Diary or Update the existing one
        btnInsert.setOnClickListener(new View.OnClickListener() {
            Diary d;
            @Override
            public void onClick(View v) {
//                String t = db.F_insertDiary(user.getUid(), txtFriend.getText().toString(), txtCost.getText().toString(), txtNote.getText().toString(), txtPlaces.getText().toString(), txtTime.getText().toString());
//                String id, String userID, String friends, String cost, String note, String places, String date
                d = new Diary("1", user.getUid(),txtFriend.getText().toString(), txtCost.getText().toString(), txtNote.getText().toString(), txtPlaces.getText().toString(),txtTime.getText().toString());
                Query q = dbRef.child("diaries").orderByChild("userID").equalTo(user.getUid()).limitToFirst(1);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                issue.getRef().setValue(d.toMap());
                            }
                        } else {
                            dbRef.child("diaries").push().setValue(d.toMap());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Toast.makeText(DiaryActivity.this, "Thêm com.example.mapdemo.MapsActivity.Diary thành công!!", Toast.LENGTH_LONG).show();
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCost.getText().clear();
                txtNote.getText().clear();
                setCurDate();
                imgV.setImageResource(R.drawable.photo);
                Toast.makeText(DiaryActivity.this, "Xóa com.example.mapdemo.MapsActivity.Diary thành công!!", Toast.LENGTH_LONG).show();

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiaryActivity.this, "Cập Nhật com.example.mapdemo.MapsActivity.Diary thành công!!", Toast.LENGTH_LONG).show();

            }
        });
        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });
        imgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_FOLDER);

            }
        });

        txtPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeList.clear();
                String[] temp = currentDiary.places.split(",");
                for( int i=0;i <temp.length;i++){
                    placeList.add(temp[i]);
                }
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DiaryActivity.this);
                alertDialog.setTitle("DANH SÁCH ĐỊA ĐIỂM");
                View row_place = getLayoutInflater().inflate(R.layout.row_items,null);
                final ListView lvPl = (ListView) row_place.findViewById(R.id.lvItems);
//                TextView t = R.layout.row_items
                txtPlaceItem= row_place.findViewById(R.id.txtItem_detail);
                btnItemInsert= row_place.findViewById(R.id.btnItemInsert);
                btnItemDelete = row_place.findViewById(R.id.btnItemRemove);
                btnItemEdit = row_place.findViewById(R.id.btnItemEdit);

                Placeadapter = new ArrayAdapter(DiaryActivity.this, android.R.layout.simple_list_item_1,placeList);
                lvPl.setAdapter(Placeadapter);
                lvPl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(DiaryActivity.this, "Clicked "+placeList.get(position).toString(),
                                Toast.LENGTH_SHORT).show();
                        String clickedItem = placeList.get(position).toString();
                        itemPos=position;
                        txtPlaceItem.setText(clickedItem);
                    }
                });
                Placeadapter.notifyDataSetChanged();
                alertDialog.setView(row_place);
//                alertDialog.setMessage("Chọn vào để xóa!");
                btnItemInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newitem = txtPlaceItem.getText().toString();
                        if (newitem != null){
                        placeList.add(newitem);}
                        Placeadapter.notifyDataSetChanged();
                        currentDiary.places = "";
                        for (int i = 0; i< placeList.size(); i++){
                            currentDiary.places += placeList.get(i) + ",";
                        }
                        if (currentDiary.places.charAt(currentDiary.places.length()-1) == ','){
                            currentDiary.places = currentDiary.places.substring(0, currentDiary.places.length()-1);
                        }
                        txtPlaces.setText(currentDiary.places);
                    }
                });
                btnItemDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String removeitem = txtPlaceItem.getText().toString();
                        placeList.remove(removeitem);
                        Placeadapter.notifyDataSetChanged();
                        txtPlaceItem.setText("");
                        currentDiary.places = "";
                        for (int i = 0; i< placeList.size(); i++){
                            currentDiary.places += placeList.get(i) + ",";
                        }
                        if (currentDiary.places.charAt(currentDiary.places.length()-1) == ','){
                            currentDiary.places = currentDiary.places.substring(0, currentDiary.places.length()-1);
                        }
                        txtPlaces.setText(currentDiary.places);
                    }
                });
                btnItemEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edititem = txtPlaceItem.getText().toString();
                        placeList.set(itemPos,edititem);
                        Placeadapter.notifyDataSetChanged();
                        txtPlaceItem.setText(edititem);
                        currentDiary.places = "";
                        for (int i = 0; i< placeList.size(); i++){
                            currentDiary.places += placeList.get(i) + ",";
                        }
                        if (currentDiary.places.charAt(currentDiary.places.length()-1) == ','){
                            currentDiary.places = currentDiary.places.substring(0, currentDiary.places.length()-1);
                        }
                        txtPlaces.setText(currentDiary.places);
                    }
                });
                alertDialog.setNeutralButton("THOÁT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                alertDialog.show();


            }
        });

        txtFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendList.clear();
                String[] tempForFriends = currentDiary.friends.split(",");
                for( int i=0;i <tempForFriends.length;i++){
                    friendList.add(tempForFriends[i]);
                }
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DiaryActivity.this);
                alertDialog.setTitle("DANH SÁCH BẠN BÈ");
                View row_place = getLayoutInflater().inflate(R.layout.row_items,null);
                final ListView lvPl = (ListView) row_place.findViewById(R.id.lvItems);
                txtFriendItem= row_place.findViewById(R.id.txtItem_detail);
                btnItemInsert= row_place.findViewById(R.id.btnItemInsert);
                btnItemDelete = row_place.findViewById(R.id.btnItemRemove);
                btnItemEdit = row_place.findViewById(R.id.btnItemEdit);
                Friendadapter = new ArrayAdapter(DiaryActivity.this, android.R.layout.simple_list_item_1,friendList);
                lvPl.setAdapter(Friendadapter);
                lvPl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(DiaryActivity.this, "Clicked "+friendList.get(position).toString(),
                                Toast.LENGTH_SHORT).show();
                        String clickedItem = friendList.get(position).toString();
                        itemPos=position;
                        txtFriendItem.setText(clickedItem);
                    }
                });
                Friendadapter.notifyDataSetChanged();
                alertDialog.setView(row_place);
//                alertDialog.setMessage("Chọn vào để xóa!");
                btnItemInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newitem = txtFriendItem.getText().toString();
                        if (newitem != null){
                        friendList.add(newitem);}
                        Friendadapter.notifyDataSetChanged();
                        currentDiary.friends = "";
                        for (int i = 0; i< friendList.size(); i++){
                            currentDiary.friends += friendList.get(i) + ",";
                        }
                        if (currentDiary.friends.charAt(currentDiary.friends.length()-1) == ','){
                            currentDiary.friends = currentDiary.friends.substring(0, currentDiary.friends.length()-1);
                        }
                        txtFriend.setText(currentDiary.friends);
                    }
                });
                btnItemDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String removeitem = txtFriendItem.getText().toString();
                        friendList.remove(removeitem);
                        Friendadapter.notifyDataSetChanged();
                        txtFriendItem.setText("");

                        currentDiary.friends = "";
                        for (int i = 0; i< friendList.size(); i++){
                            currentDiary.friends += friendList.get(i) + ",";
                        }
                        if (currentDiary.friends.charAt(currentDiary.friends.length()-1) == ','){
                            currentDiary.friends = currentDiary.friends.substring(0, currentDiary.friends.length()-1);
                        }
                        txtFriend.setText(currentDiary.friends);
                    }
                });
                btnItemEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edititem = txtFriendItem.getText().toString();
                        friendList.set(itemPos,edititem);
                        Friendadapter.notifyDataSetChanged();
                        txtFriendItem.setText(edititem);

                        currentDiary.friends = "";
                        for (int i = 0; i< friendList.size(); i++){
                            currentDiary.friends += friendList.get(i) + ",";
                        }
                        if (currentDiary.friends.charAt(currentDiary.friends.length()-1) == ','){
                            currentDiary.friends = currentDiary.friends.substring(0, currentDiary.friends.length()-1);
                        }
                        txtFriend.setText(currentDiary.friends);
                    }
                });
                alertDialog.setPositiveButton("THÊM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Intent intent= new Intent(MainActivity.this,webview.class);
                        //intent.putExtra("Link Item",arrayLink.get(position));
                        //startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }
    private void getData(DataSnapshot data){
//        Diary d = new Diary();
        if (data.exists()) {
            for (DataSnapshot issue : data.getChildren()) {
                // do with your result
                currentDiary.userID = issue.child("userID").getValue(String.class);
                currentDiary.cost = issue.child("cost").getValue(String.class);
                currentDiary.date= issue.child("date").getValue(String.class);
                currentDiary.id= issue.child("id").getValue(String.class);
                currentDiary.friends= issue.child("friends").getValue(String.class);
                currentDiary.note= issue.child("note").getValue(String.class);
                currentDiary.places= issue.child("places").getValue(String.class);
                txtCost.setText(currentDiary.cost);
                txtFriend.setText(currentDiary.friends);
                txtNote.setText(currentDiary.note);
                txtPlaces.setText(currentDiary.places);
                txtTime.setText(currentDiary.date);
            }
        }
    }
    private void setCurDate()
    {
        Calendar c= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        txtTime.setText(simpleDateFormat.format(c.getTime()));
    }

    private void pickDate()
    {
        final Calendar calendar = Calendar.getInstance();
        final int curDate = calendar.get(Calendar.DATE);
        final int curMonth = calendar.get(Calendar.MONTH);
        final int curYear = calendar.get(Calendar.YEAR);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
//                Date t = simpleDateFormat.format(Date);
                txtTime.setText(simpleDateFormat.format(calendar.getTime()));
                datee = simpleDateFormat.format(calendar.getTime());
                Toast.makeText(getBaseContext(), datee.toString(), Toast.LENGTH_LONG).show();
            }
        },curYear,curMonth,curDate);
        datePickerDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_CODE_FOLDER && resultCode==RESULT_OK && data != null)
        {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgV.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Query q = dbRef.child("diaries").orderByChild("userID").equalTo(user.getUid()).limitToFirst(1);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        issue.getRef().setValue(currentDiary.toMap());
                    }
                } else {
                    dbRef.child("diaries").push().setValue(currentDiary.toMap());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
