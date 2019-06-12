package com.example.mapdemo;


import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBHelper{
    SQLiteDatabase db;
    String myDBPath;
    DatabaseReference dbRef;
    FirebaseDatabase fire;
    private FirebaseAuth auth;
    // Default Operators:
    public int OpenDB(File storagePath, String dbName){

        try{
            myDBPath = storagePath + "/" + dbName;
            db = SQLiteDatabase.openDatabase(myDBPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        } catch (SQLException e){
            // should log e.String
            Log.e(new String("abc"), e.toString());
            return -1;
        }
        return 0;
    }
    public int ConnectToFirebase(){
        fire = FirebaseDatabase.getInstance();
        dbRef = fire.getReference();
        return 0;
    }
    public void Login(){
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        auth.createUserWithEmailAndPassword("email","password");

    }
//    public String F_insertLocation(String name, String city, String rate){
//        String t = dbRef.child("locations").push().getKey();
//        dbRef.child("locations").child(t).setValue(new Place(t, name, city, rate));
//        return t;
//    }
    public String F_insertReview(String userID, String locationID, String content, Double rate){
        String t = dbRef.child("reviews").push().getKey();
        dbRef.child("reviews").child(t).setValue(new Review(t, userID, locationID, content, rate));
        return t;
    }
    public String F_insertDiary(String userID, String friends, String cost, String note, String places, String date){
        String t = dbRef.child("diaries").push().getKey();
        dbRef.child("diaries").push().setValue(new Diary(t, userID,friends,cost,note,places, date));
        return t;
    }
    Review rv;

    // get
//    public Location F_getLocation(String id){
//        Location lc = new Location();
//        Query query = dbRef.child("locations").equalTo(id);
//        query.addListenerForSingleValueEvent(listener);
//        return  lc;
//    }
    public Review F_getReview(String id){
        Review rv = new Review();
        dbRef.child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                rv = dataSnapshot.getValue(Review.class);
//                getReview(dataSnapshot, rv);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return rv;
    }
    private void getReview(DataSnapshot data, Review rv){
        rv = data.getValue(Review.class);
    }
//    public Diary F_getDiary(String id){
//        Diary dia = new Diary();
//        Query query = dbRef.child("locations").equalTo(id);
//        query.addListenerForSingleValueEvent(listener);
//        return dia;
//    }
    // update

    public void F_updateLocation(Place r){
        dbRef.child("locations").child(r.id).updateChildren(r.toMap());
    }
    public void F_updateReview(Review r){
        dbRef.child("reviews").child(r.id).updateChildren(r.toMap());
    }
    public void F_updateDiary(Diary r){
        dbRef.child("diaries").child(r.id).updateChildren(r.toMap());
    }
    // delete

    public void CloseDB(){
        db.close();
    }
    public void createTable(){
        db.execSQL("CREATE TABLE if not exists `Location` ( `ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` TEXT, `city` TEXT, `rate` REAL)");
        db.execSQL("CREATE TABLE if not exists `Review` ( `userID` INTEGER NOT NULL, `locationID` INTEGER NOT NULL, `content` TEXT, PRIMARY KEY(`userID`,`locationID`) )");
        db.execSQL("CREATE TABLE if not exists `User` ( `ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `name` TEXT, `username` TEXT, `password` TEXT, `checklist` TEXT, `diary` TEXT, `Field7` INTEGER )");
        db.execSQL("CREATE TABLE if not exists `Users_Locations` ( `userID` INTEGER NOT NULL, `locationID` INTEGER NOT NULL, PRIMARY KEY(`userID`,`locationID`) )");
        db.execSQL("CREATE TABLE if not exists `Users_Locations` ( `userID` INTEGER NOT NULL, cost, date, note, friends )");
    }

    // index for name of user
    // CREATE
    public int insertLocation(String name, String city, Double rate){
        // Create a review first
        String queryString = "select count(*) from Location where name = ?";
        String[] usrname = {name};
        Cursor c1 = db.rawQuery(queryString, usrname);
        if (c1.getCount()>0){ // duplicated username
            return -1;
        }
        // Query creating location
        queryString = "insert into User(name, city, rate) values (?, ?, ?);";
        String[] args = {name, city, rate.toString()};
        db.execSQL(queryString, args);
        return 0;
    }
    public int insertUser(String username, String password, String name, String checklist, String diary) {
        // Check if username is used
        String queryString = "select count(*) from User where username = ?";
        String[] usrname = {username};
        Cursor c1 = db.rawQuery(queryString, usrname);
        if (c1.isFirst()){ // duplicated username
            return -1;
        }
        // Creating
        queryString = "insert into User(username, password, name, checklist, diary) values (?, ?, ?, ?, ?);";
        String[] args = {username, password, name, checklist, diary};
        db.execSQL(queryString, args);
        return 0;
    }
    //insertUsers_Locations create history when a User visited a location
    public int insertUsers_Locations(String username, String locationName){
        int userID, locationID;
        // check if User existed
        String queryString = "select ID from User where username = ?";
        Cursor c1 = db.rawQuery(queryString, new String[]{username});
        if (c1.getCount() <= 0 ){
            return -1;
        } else {
            c1.moveToFirst();
            userID = c1.getInt(c1.getColumnIndex("userID"));
        }
        // check if Location existed
        queryString = "select ID from Location where name = ?";
        c1 = db.rawQuery(queryString, new String[]{locationName});
        if (c1.getCount() <= 0 ){
            return -1;
        } else{
            c1.moveToFirst();
            locationID = c1.getInt(c1.getColumnIndex("locationID"));
        }
        // insert
        queryString = "insert into Users_Locations(userID, locationID) values (?, ?);";
        db.execSQL(queryString, new String[] {Integer.toString(userID), Integer.toString(locationID)});
        return 0;
    }
    public int insertReview() {
        // check if User
        // check if Location
        return 0;
    }
    // READ
//    public Place getLocation(int locationID){
//        String[] args = {Integer.toString(locationID)};
//        Cursor c1 = db.rawQuery("select * from Location where ID = ?", args);
//        c1.moveToFirst();
//        int ID = c1.getInt(c1.getColumnIndex("ID"));
//        String name = c1.getString(c1.getColumnIndex("name"));
//        String city = c1.getString(c1.getColumnIndex("city"));
//        Double rate = c1.getDouble(c1.getColumnIndex("rate"));
//        String review = c1.getString(c1.getColumnIndex("review"));
//        return new Place("1",name, city, rate);
//    }
//    public User getUser(int ID){
//        String[] args = {Integer.toString(ID)};
//        Cursor c1 = db.rawQuery("select * from User where ID = ?", args);
//        c1.moveToFirst();
//        String username = c1.getString(c1.getColumnIndex("username"));
//        String password = c1.getString(c1.getColumnIndex("password"));
//        String name = c1.getString(c1.getColumnIndex("name"));
//        String checklist = c1.getString(c1.getColumnIndex("checklist"));
//        String diary = c1.getString(c1.getColumnIndex("diary"));
//        return new User(ID, username, password, name, checklist, diary);
//    }
//    public User getUser(String username){
//        String[] args = {username};
//        Cursor c1 = db.rawQuery("select * from User where username = ?", args);
//        if (c1.getCount() <=0 ){
//            return null;
//        }
//        c1.moveToPosition(-1);
//        try {
//            while (c1.moveToNext()) {
//                int ID = c1.getInt(c1.getColumnIndex("ID"));
//                String password = c1.getString(c1.getColumnIndex("password"));
//                String name = c1.getString(c1.getColumnIndex("name"));
//                String checklist = c1.getString(c1.getColumnIndex("checklist"));
//                String diary = c1.getString(c1.getColumnIndex("diary"));
//                return new User(ID, username, password, name, checklist, diary);
//            }
//        } catch (SQLException e){
//            Log.e(new String("ngu"), e.toString());
//        }
//        return null;
//    }
    // get all review from locationID
    public Review[] getReviewsFromLocation(int locationID){
        String[] agrs = {Integer.toString(locationID)};
        Cursor c1 = db.rawQuery("select * from Review where ID = ?", agrs);
        c1.moveToPosition(-1);
        Review[] rs = new Review[c1.getCount()];
        int i = 0;
        while(c1.moveToNext()){
            int userID = c1.getInt(c1.getColumnIndex("userID"));
            String cnt = c1.getString(c1.getColumnIndex("content"));
            rs[i++] = new Review("1","1", "1", cnt, 1.0);
        }
        return rs;
    }
    // get all location ID array from user
    public int[] getLocationsFromUser(int userID){
        String[] agrs = {Integer.toString(userID)};
        Cursor c1 = db.rawQuery("select * from Users_Locations where userID = ?", agrs);
        c1.moveToPosition(-1);
        int[] rs = new int[c1.getCount()];
        int i = 0;
        while(c1.moveToNext()){
            int locationID = c1.getInt(c1.getColumnIndex("userID"));
            rs[i++] = locationID;
        }
        return rs;
    }
    // UPDATE
    public int updateUser() {

        return 0;
    }
    public int updateLocation() {
        return 0;
    }
    public int updateReview() {
        return 0;
    }

    // DELETE


}