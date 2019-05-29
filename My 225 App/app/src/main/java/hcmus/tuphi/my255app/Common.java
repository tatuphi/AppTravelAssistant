package hcmus.tuphi.my255app;

import hcmus.tuphi.my255app.Remote.IGoogleAPIService;
import hcmus.tuphi.my255app.Remote.RetrofitClient;

public class Common {
    private static final String GOOGLE_API_URL="https://maps.googleapis.com/";
    public static IGoogleAPIService getGoogleAPIService()
    {
        return RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService.class);
    }
}
