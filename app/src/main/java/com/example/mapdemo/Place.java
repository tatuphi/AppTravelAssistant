package com.example.mapdemo;

import java.util.HashMap;
import java.util.Map;

public class Place{
    public String id;
    public String name, des;
    public Double latt;
    public Double longtt;
    public Place() {
    }
    public Place(String ID, String name, String des, Double latt, Double longtt){
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
        result.put("latt", latt.toString());
        result.put("longtt", longtt.toString());

        return result;
    }
}