package com.example.mapdemo;

import java.util.HashMap;
import java.util.Map;

public class Review{
    public String id;
    public String content, userID, locationID, locationName;
    public double rate;
    public String userName;

    public Review() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("locationID", locationID);
        result.put("userID", userID);
        result.put("rate", rate);
        result.put("id", id);
        result.put("username", userName);
        result.put("locationName", locationName);
        return result;
    }
    public  Review(String id, String userID, String locationID, String content, Double rate){
        this.id = id;
        this.userID = userID;
        this.locationID = locationID;
        this.content = content;
        this.rate = rate;
    }
}