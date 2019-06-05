package com.example.mapdemo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by delaroy on 3/27/17.
 */
public class User{
    String ID;
    public String friends;
    public User(String ID){
        this.ID = ID;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("friends", friends);
        return result;
    }

}
