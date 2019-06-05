package com.example.mapdemo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Diary {
    public String id;
    public String friends, cost, note, places, userID;
    public String    date;

    public Diary() {
        id =friends = cost = note = places =userID =date ="";
    }

    public Diary(String id, String userID, String friends, String cost, String note, String places, String date) {
        this.id = id;
        this.friends = friends;
        this.cost = cost;
        this.note = note;
        this.places = places;
        this.date = date;
        this.userID = userID;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("friends", friends);
        result.put("cost", cost);
        result.put("note", note);
        result.put("places", places);
        result.put("date", date);
        result.put("userID", userID);
        return result;
    }
}
