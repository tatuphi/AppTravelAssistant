package com.example.mapdemo;

/**
 * Created by delaroy on 3/27/17.
 */
public class User{
    int ID;
    public String username, password, name, checklist, diary;
    public User(int ID, String username,String password,String name,String checklist,String diary){
        this.ID = ID;
        this.checklist = checklist;
        this.diary = diary;
        this.name = name;
        this.password = password;
        this.username = username;
    }
}
