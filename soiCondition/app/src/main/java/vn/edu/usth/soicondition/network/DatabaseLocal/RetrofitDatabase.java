package vn.edu.usth.soicondition.network.DatabaseLocal;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.soicondition.network.ApiServiceDatabase;

public class RetrofitDatabase {
    // private static final String BASE_URL = "http://172.31.99.32/codes/fetchdata.php/"; // USTH FLoor 4
    //private static final String BASE_URL = "http://192.168.1.150/codes/fetchdata.php/"; // 40th floor Tan Hoang Minh 4016
    //private static final String BASE_URL = "http://192.168.1.149/codes/fetchdata.php/"; // 42th floor Tan Hoang Minh 4217
    //private static final String BASE_URL = "http://192.168.50.149/codes/fetchdata.php/"; // Home floor 4
    private static final String BASE_URL = "http://172.20.10.3/codes/fetchdata.php/"; // Nhat Anh Hotspot
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Create a service for your API
    public static ApiServiceDatabase getApiService() {
        return getRetrofitInstance().create(ApiServiceDatabase.class);
    }
}
