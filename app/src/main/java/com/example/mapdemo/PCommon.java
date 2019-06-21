package com.example.mapdemo;

import com.example.mapdemo.Model.Results;
import com.example.mapdemo.Remote.IGoogleAPIService;
import com.example.mapdemo.Remote.RetrofitClient;

public class PCommon { public static Results currentResult;

    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }


}
