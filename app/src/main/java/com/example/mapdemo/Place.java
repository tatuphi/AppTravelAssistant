package com.example.mapdemo;

import java.util.HashMap;
import java.util.Map;

public class Place{
    public String id;
    public String name, des;
    public String latt;
    public String longtt;
    public Place() {
    }
    public Place(String ID, String name, String des, String latt, String longtt){
        this.id = ID;
        this.des = des;
        this.name = name;
        this.latt = latt;
        this.longtt = longtt;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("des", des);
        result.put("latt", latt);
        result.put("longtt", longtt);

        return result;
    }
}