package com.example.diary;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    //Button btnC;
    Button btnInsert, btnRemove, btnEdit;
    ImageView btnFriend, btnCost, btnMap,btnTimer;
    ImageView imgV;
    EditText txtCost, txtNote;
    TextView lblDate;
    ArrayAdapter adapter;
    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgV =(ImageView)findViewById(R.id.imgV);
        btnFriend = (ImageView)findViewById(R.id.imgFriend);
        btnMap = (ImageView) findViewById(R.id.imgMap);
        btnCost = (ImageView)findViewById(R.id.imgCost);
        btnTimer =(ImageView)findViewById(R.id.imgTimer);
        txtCost =(EditText) findViewById(R.id.txtCost);
        txtNote = (EditText)findViewById(R.id.txtNote);
        lblDate =(TextView)findViewById(R.id.lbTimer);
        btnInsert =(Button)findViewById(R.id.btnInsert);
        btnRemove =(Button)findViewById(R.id.btnRemove);
        btnEdit =(Button) findViewById(R.id.btnEdit);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnTimer.setOnClickListener(new View.OnClickListener() {
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
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, Maps.class);
                startActivity(intent);
            }
        });
        btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[]friendlist ={"Nhật Dinh","Tú Phi", "Hồng Mơ","Thanh Nhã", "Nhật Nguyên"};
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("YOUR FRIENDS");
                View row_fr = getLayoutInflater().inflate(R.layout.row_friends,null);
                ListView lvFr = (ListView) row_fr.findViewById(R.id.lvFriends);
                adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,friendlist);
                lvFr.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                alertDialog.setView(row_fr);
                //alertDialog.setMessage(arrayDes.get(position));
                alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.setPositiveButton("MORE", new DialogInterface.OnClickListener() {
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
                    lblDate.setText(simpleDateFormat.format(calendar.getTime()));
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
