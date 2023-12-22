package vn.edu.usth.soicondition.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import vn.edu.usth.soicondition.network.DatabaseLocal.Measurements;

public interface ApiServiceDatabase {
    @GET("fetchdata.php")
    Call<List<Measurements>> fetchData();
}
