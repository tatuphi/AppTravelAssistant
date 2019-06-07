package hcmus.tuphi.my255app.Remote;

import hcmus.tuphi.my255app.Model.MyPlaces;
import hcmus.tuphi.my255app.Model.PlaceDetail;
import hcmus.tuphi.my255app.Model.Result;
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
