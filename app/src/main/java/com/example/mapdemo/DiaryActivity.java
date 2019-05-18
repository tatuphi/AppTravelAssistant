package com.example.mapdemo;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DiaryActivity extends AppCompatActivity {

    //Button btnC;
    Button btnInsert, btnRemove, btnEdit;
    TextView txtFriend, txtPlaces,txtTime;
    ImageView imgV;
    EditText txtCost, txtNote;
    TextView lblDate;
    ArrayAdapter adapter;
    Integer userID;
    //ArrayList<String>placeList;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get userID from Activity that invoke this one
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            userID = extras.getInt("USER");
        }

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
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiaryActivity.this, "Thêm Diary thành công!!", Toast.LENGTH_LONG).show();
            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCost.getText().clear();
                txtNote.getText().clear();
                setCurDate();
                imgV.setImageResource(R.drawable.photo);
                Toast.makeText(DiaryActivity.this, "Xóa Diary thành công!!", Toast.LENGTH_LONG).show();

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DiaryActivity.this, "Cập Nhật Diary thành công!!", Toast.LENGTH_LONG).show();

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

                String[]placeList ={"Nhật Dinh","Tú Phi", "Hồng Mơ","Thanh Nhã", "Nhật Nguyên"};


                //placeList.add("Hải Phòng");

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DiaryActivity.this);
                alertDialog.setTitle("DANH SÁCH ĐỊA ĐIỂM");
                View row_place = getLayoutInflater().inflate(R.layout.row_items,null);
                final ListView lvPl = (ListView) row_place.findViewById(R.id.lvItems);

                adapter = new ArrayAdapter(DiaryActivity.this, android.R.layout.simple_list_item_1,placeList);
                lvPl.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                alertDialog.setView(row_place);
                //alertDialog.setMessage(arrayDes.get(position));
                alertDialog.setNegativeButton("XÓA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
        txtFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[]friendlist ={"Nhật Dinh","Tú Phi", "Hồng Mơ","Thanh Nhã", "Nhật Nguyên"};


                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(DiaryActivity.this);
                alertDialog.setTitle("YOUR FRIENDS");
                View row_fr = getLayoutInflater().inflate(R.layout.row_items,null);
                ListView lvFr = (ListView) row_fr.findViewById(R.id.lvItems);
                adapter = new ArrayAdapter(DiaryActivity.this, android.R.layout.simple_list_item_1,friendlist);
                lvFr.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                alertDialog.setView(row_fr);
                //alertDialog.setMessage(arrayDes.get(position));
                alertDialog.setNegativeButton("XÓA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

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
    private void setCurDate()
    {
        Calendar c= Calendar.getInstance();
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
        txtTime.setText(simpleDateFormat.format(c.getTime()));
    }

    private void pickDate()
    {
        final Calendar calendar = Calendar.getInstance();
        int curDate = calendar.get(Calendar.DATE);
        int curMonth = calendar.get(Calendar.MONTH);
        int curYear = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat= new SimpleDateFormat("dd/MM/yyyy");
                txtTime.setText(simpleDateFormat.format(calendar.getTime()));
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
}
