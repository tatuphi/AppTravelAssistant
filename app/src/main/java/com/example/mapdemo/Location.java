package com.example.mapdemo;

public class Location{
    int ID;
    public String name, city;
    double rate;
    public Location(int ID, String name, String city, Double rate){
        this.ID = ID;
        this.city = city;
        this.name = name;
        this.rate = rate;
    }
}