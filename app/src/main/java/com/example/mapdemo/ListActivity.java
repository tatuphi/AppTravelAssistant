package com.example.mapdemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ListActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference dbRef;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHelper db = new DBHelper();
        db.ConnectToFirebase();
        setContentView(R.layout.activity_list);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dbRef = FirebaseDatabase.getInstance().getReference();
        if (user == null){
            Toast.makeText(this, "NGU", Toast.LENGTH_LONG).show();
            return;
        }
        dbRef.child("locations").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showData(dataSnapshot, "-LfTXKITAmMA6NPzwVUx");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // onCreate
    private  void showData(DataSnapshot data, String userID){
//        Place p;
////        for (c : data){
////            p.city = c.
////        }
//        p = data.child(userID).getValue(Place.class);
//        Toast.makeText(this, p.city.toString(), Toast.LENGTH_LONG).show();

    } // showData
}
