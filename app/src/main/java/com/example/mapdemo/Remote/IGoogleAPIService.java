package com.example.mapdemo.Remote;


import com.example.mapdemo.Model.MyPlaces;
import com.example.mapdemo.Model.PlaceDetail;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleAPIService {
    @GET
    Call<MyPlaces> getNearByPlaces(@Url String url);
    // new ver
    @GET
    Call<PlaceDetail> getDetailPlace(@Url String url);
}
