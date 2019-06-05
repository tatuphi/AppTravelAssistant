package com.example.mapdemo;

import java.util.HashMap;
import java.util.Map;

public class CheckItem {
    String userID, content;

    public CheckItem() {
    }

    public CheckItem(String userID, String content) {
        this.userID = userID;
        this.content = content;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("content", content);
        return result;
    }
}
