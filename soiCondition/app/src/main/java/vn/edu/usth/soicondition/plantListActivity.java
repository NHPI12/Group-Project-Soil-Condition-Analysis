package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.soicondition.network.JSONPlaceHolder;
import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.PlantResponse;
import vn.edu.usth.soicondition.network.model.default_Image;

public class plantListActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private List<PlantData> plantList;
    private default_Image defaultImage;
    private Plant_List_Recycle_Adapter plantListRecycleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        //Navigation menu
        drawerLayout = findViewById(R.id.plant_list_nav_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // RecycleView
        recyclerView = findViewById(R.id.plant_list_recycle_View);
        plantList = new ArrayList<>();
        plantListRecycleAdapter = new Plant_List_Recycle_Adapter(this, plantList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(plantListRecycleAdapter);
        fetchData();
    }
        private void fetchData() {
            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://perenual.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();

            JSONPlaceHolder jsonPlaceHolder = retrofit.create(JSONPlaceHolder.class);
            String apiKey = "sk-tizW655dda2fd073d2885";
            fetchDatafromMultiplePages(jsonPlaceHolder, apiKey, 1);
        }
        private void fetchDatafromMultiplePages(JSONPlaceHolder jsonPlaceHolder, String apiKey, int pageNumber){
            Call<PlantResponse> call = jsonPlaceHolder.getData(apiKey, pageNumber);
            call.enqueue(new Callback<PlantResponse>() {
                @Override
                public void onResponse(Call<PlantResponse> call, Response<PlantResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        PlantResponse plantResponse = response.body();
                        List<PlantData> postList = plantResponse.getPlantDataList();
                        plantList.addAll(postList);
                        plantListRecycleAdapter.notifyDataSetChanged();
                        if (pageNumber <= 30) {
                            fetchDatafromMultiplePages(jsonPlaceHolder, apiKey, pageNumber + 1);
                        } else {
                            Log.d("PlantList", "DONE");
                        }
                    } else {
                        Log.e("PlantList", "Error" + response.code());
                    }
                }
                @Override
                public void onFailure(Call<PlantResponse> call, Throwable t) {
                    Log.d("error", t.getMessage());
                }
            });
        }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_bar_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search_action_bar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return  super.onCreateOptionsMenu(menu);
    }
}