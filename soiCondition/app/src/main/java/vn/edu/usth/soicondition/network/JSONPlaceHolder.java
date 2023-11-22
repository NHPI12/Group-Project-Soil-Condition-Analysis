package vn.edu.usth.soicondition.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.PlantResponse;

public interface JSONPlaceHolder {
    @GET("species-list")
    Call<PlantResponse> getData(@Query("key") String apiKey, @Query("page") int pageNumber);
}
