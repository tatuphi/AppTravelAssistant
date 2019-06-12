package com.example.mapdemo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class ChecklistActivity extends AppCompatActivity {
    EditText et;
    ListView lv;
    ListView list_load;
    Button btn;
    Button btn2;
    Button btn3;
    ArrayList<String> arrayList;
    ArrayList<String> selectedItems;
    ArrayAdapter<String> adapter;

    private DBHelper db;
    private DatabaseReference dbRef;
    FirebaseUser user;

    String data = "";
//    Integer userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checklist);

        // get userID from Activity that invoke this one
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else{
            DBHelper db = new DBHelper();
            db.ConnectToFirebase();
            dbRef = FirebaseDatabase.getInstance().getReference();
        }

        // add event listner for Checkitems
        dbRef.child("checkitems").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // gang cac controls
        et = (EditText) findViewById(R.id.edit_list);
        lv = (ListView) findViewById(R.id.list_check);
        btn = (Button) findViewById(R.id.btn_add);
        btn2 = (Button) findViewById(R.id.btn_delete);
        btn3 = (Button) findViewById(R.id.btn_edit);

        // Khoi tao ListArray
        selectedItems = new ArrayList<String>();
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(ChecklistActivity.this, android.R.layout.simple_list_item_checked,
                arrayList);

    }
    private void getData(DataSnapshot snapshot){
        try {
            data = snapshot.child(user.getUid()).getValue(String.class);
        }catch (Exception e){
            data = "";
        }
        Toast.makeText(this, data, Toast.LENGTH_LONG).show();
//        if (data == null){
//            data = "0_";
//        }
        //        load hiển thị các item dã dược lưu
        String[] data1 = data.split(",");
        ListView ch=(ListView) findViewById(R.id.list_check);
        if (data.length() > 0 && data != null){
            for(int j=0;j<data1.length;j++)
            {
                arrayList.add(data1[j].substring(2));
                selectedItems.add(data1[j].substring(0,1));
            }
        }
        //xóa từng item duoc chon
        lv.setOnItemLongClickListener(new onRemoveItems());
        onBtnClick();
        onBtnDelClick();
        lv.setAdapter(adapter);

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {

            dbRef.child("checkitems").child(user.getUid()).setValue(data);
        }catch (Exception e){}

        // Save Intances to DB First

    }
    public void onStart(){
        super.onStart();
        //create an instance of ListView
        ListView chl=(ListView) findViewById(R.id.list_check);
        //set multiple selection mode
        chl.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // xu li
        chl.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // selected item
                String selectedItem = ((TextView) view).getText().toString();
                if(selectedItems.contains(selectedItem))
                    selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
                else
                    selectedItems.add(selectedItem); //add selected item to the list of selected items

            }

        });
    }
    //    xử lý button Add
    public void onBtnClick(){
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String result=et.getText().toString();
                if (result.length() <= 0 || result == null){
                    return;
                }
                arrayList.add(result);
                adapter.notifyDataSetChanged();
                if (data.length() <= 0){
                    data += "0_"+ result;
                } else {
                    data += ",0_" + result;
                }
                et.setText("");
            }}
        );
    }
//    Xử lý button delete

    public void onBtnDelClick(){
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrayList.clear();
                selectedItems.clear();
                data="";
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void onBtnEditClick(){
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView item_each=(ListView) findViewById(R.id.list_check);
                item_each.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // selected item
                        String selectedItem = ((TextView) view).getText().toString();
                        String result=et.getText().toString();
                        selectedItem=result;
                        adapter.notifyDataSetChanged();

                    }

                });
            }
        });
    }
    //    xóa từng item được chọn khi chạm vào item đó lâu
    private class onRemoveItems implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView parent, View view, final int position, long id) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChecklistActivity.this);
            alertDialogBuilder.setMessage("Bạn có muốn xóa item này!");
            alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // xóa items đang nhấn giữ
                    arrayList.remove(position);
                    selectedItems.remove(position);
                    //cập nhật lại listview
                    adapter.notifyDataSetChanged();
                    //Cap nhat lai data bien toan cuc DATA
                    Iterator<String> it = arrayList.iterator();
                    Iterator<String> selected = selectedItems.iterator();
                    data = "";
                    while(it.hasNext() && selected.hasNext()) {
                        data += selected+"_"+it.next()+",";
                    }
                    String temp = data;

                }
            });
            alertDialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialogBuilder.show();
            return true;
        }
    }

}