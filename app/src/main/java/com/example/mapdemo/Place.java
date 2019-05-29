package com.example.mapdemo;

import java.util.HashMap;
import java.util.Map;

public class Location{
    public String id;
    public String name, city;
    public double rate;
    public Location() {
    }
    public Location(String ID, String name, String city, Double rate){
        this.id = ID;
        this.city = city;
        this.name = name;
        this.rate = rate;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("city", city);
        result.put("rate", rate);

        return result;
    }
}