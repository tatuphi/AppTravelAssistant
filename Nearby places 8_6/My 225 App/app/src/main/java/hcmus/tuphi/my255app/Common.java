package hcmus.tuphi.my255app;

import hcmus.tuphi.my255app.Model.MyPlaces;
import hcmus.tuphi.my255app.Model.Result;
import hcmus.tuphi.my255app.Model.Results;
import hcmus.tuphi.my255app.Remote.IGoogleAPIService;
import hcmus.tuphi.my255app.Remote.RetrofitClient;

public class Common {
    //new ver

    public static Results currentResult;


    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
