package com.example.mapdemo;

public class Review{
    public String content;
    public int userID, locationID;
    public  Review(int userID, int locationID, String content){
        this.userID = userID;
        this.locationID = locationID;
        this.content = content;
    }
}