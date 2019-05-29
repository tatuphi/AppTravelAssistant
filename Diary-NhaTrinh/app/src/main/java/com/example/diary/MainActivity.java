package com.example.diary;

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
import android.text.InputType;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Button btnC;
    Button btnInsert, btnRemove, btnEdit,btnItemInsert, btnItemDelete,btnItemEdit;
    TextView txtFriend, txtPlaces,txtTime;
    ImageView imgV;
    EditText txtCost, txtNote,txtFriendItem,txtPlaceItem;
    TextView lblDate;
    ArrayAdapter Friendadapter, Placeadapter;

    int REQUEST_CODE_CAMERA = 123;
    int REQUEST_CODE_FOLDER = 456;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ArrayList<String>friendList;
    ArrayList<String>placeList;
    int itemPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
//        txtItem = (EditText)findViewById(R.id.txtItem_detail);

        //lay ngay hien tai
        setCurDate();
        getData();
//        txtNote.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //them diary
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                friendList,placeList,txtCost.getText().toString(),txtTime.getText().toString(),txtNote.getText().toString();
                Toast.makeText(MainActivity.this, "Thêm Diary thành công!!", Toast.LENGTH_LONG).show();
            }
        });

        //xoa diary
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCost.getText().clear();
                txtNote.getText().clear();
                setCurDate();
                imgV.setImageResource(R.drawable.photo);
                Toast.makeText(MainActivity.this, "Xóa Diary thành công!!", Toast.LENGTH_LONG).show();
//                friendList,placeList,txtCost.getText().toString(),txtTime.getText().toString(),txtNote.getText().toString();

            }
        });
        //sua diary
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Cập Nhật Diary thành công!!", Toast.LENGTH_LONG).show();
//                friendList,placeList,txtCost.getText().toString(),txtTime.getText().toString(),txtNote.getText().toString();

            }
        });
        //chon ngay
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
        //sua dia diem
        txtPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("DANH SÁCH ĐỊA ĐIỂM");
                View row_place = getLayoutInflater().inflate(R.layout.row_items,null);
                final ListView lvPl = (ListView) row_place.findViewById(R.id.lvItems);
                txtPlaceItem= row_place.findViewById(R.id.txtItem_detail);
                btnItemInsert= row_place.findViewById(R.id.btnItemInsert);
                btnItemDelete = row_place.findViewById(R.id.btnItemRemove);
                btnItemEdit = row_place.findViewById(R.id.btnItemEdit);
                lvPl.setAdapter(Placeadapter);
                lvPl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(MainActivity.this, "Clicked "+placeList.get(position).toString(),
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
                        placeList.add(newitem);
                        Placeadapter.notifyDataSetChanged();
                    }
                });
                btnItemDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String removeitem = txtPlaceItem.getText().toString();
                        placeList.remove(removeitem);
                        Placeadapter.notifyDataSetChanged();
                        txtPlaceItem.setText("");
                    }
                });
                btnItemEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edititem = txtPlaceItem.getText().toString();
                        placeList.set(itemPos,edititem);
                        Placeadapter.notifyDataSetChanged();
                        txtPlaceItem.setText(edititem);
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

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("DANH SÁCH BẠN BÈ");
                View row_fr = getLayoutInflater().inflate(R.layout.row_items,null);
                ListView lvFr = (ListView) row_fr.findViewById(R.id.lvItems);
                txtPlaceItem= row_fr.findViewById(R.id.txtItem_detail);
                btnItemInsert= row_fr.findViewById(R.id.btnItemInsert);
                btnItemDelete = row_fr.findViewById(R.id.btnItemRemove);
                btnItemEdit = row_fr.findViewById(R.id.btnItemEdit);
                lvFr.setAdapter(Friendadapter);

                Friendadapter.notifyDataSetChanged();
                alertDialog.setView(row_fr);

                txtFriendItem= row_fr.findViewById(R.id.txtItem_detail);

                lvFr.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(MainActivity.this, "Clicked "+friendList.get(position).toString(),
                                Toast.LENGTH_SHORT).show();
                        String clickedItem = friendList.get(position).toString();
                        itemPos=position;
                        txtFriendItem.setText(clickedItem);
                    }
                });
                Placeadapter.notifyDataSetChanged();
                alertDialog.setView(row_fr);
                btnItemInsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newitem = txtFriendItem.getText().toString();
                        friendList.add(newitem);
                        Friendadapter.notifyDataSetChanged();
                    }
                });
                btnItemDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String removeitem = txtFriendItem.getText().toString();
                        friendList.remove(removeitem);
                        Friendadapter.notifyDataSetChanged();
                        txtFriendItem.setText("");
                    }
                });
                btnItemEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edititem = txtFriendItem.getText().toString();
                        friendList.set(itemPos,edititem);
                        Friendadapter.notifyDataSetChanged();
                        txtFriendItem.setText(edititem);
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
}

    private AdapterView.OnItemClickListener mFriendItemOnClickListener = new AdapterView.OnItemClickListener()
    {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(MainActivity.this, "Clicked " +friendList.get(i).toString(),
                    Toast.LENGTH_SHORT).show();
            txtFriendItem.setText(friendList.get(i).toString());
            //friendList.remove(i);
            //Friendadapter.notifyDataSetChanged();
            //txtFriendItem.setText("");

        }
    };
    private void getData()
    {
        friendList=new ArrayList<String>();
        friendList.add("Nhật Dinh");
        friendList.add("Tú Phi");
        friendList.add("Hồng Mơ");
        friendList.add("Thanh Nhã");
        friendList.add("Nhật Nguyên");

        placeList = new ArrayList<String>();
        placeList.add("Hà Nội");
        placeList.add("TP.HCM");
        placeList.add("Đà Nẵng");
        placeList.add("Huế");
        placeList.add("Đà Lạt");

        Friendadapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,friendList);
        Placeadapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,placeList);

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
