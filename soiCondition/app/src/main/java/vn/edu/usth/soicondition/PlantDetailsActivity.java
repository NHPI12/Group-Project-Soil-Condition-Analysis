package vn.edu.usth.soicondition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.soicondition.Plant_Details_Image_Recycle_Adapter;
import vn.edu.usth.soicondition.network.JSONPlaceHolder;
import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.PlantDetailsResponse;

public class PlantDetailsActivity extends AppCompatActivity {
    private static final String ARG_PLANT = "plant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        // Initialize and set up your RecyclerView and Adapter here
        RecyclerView recyclerView = findViewById(R.id.imageListRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        ArrayList<String> scientificNames = intent.getStringArrayListExtra("scientific_name");
        String commonName = intent.getStringExtra("common_name");
        String cycle = intent.getStringExtra("cycle");
        String watering = intent.getStringExtra("watering");
        String originalUrl = intent.getStringExtra("original_url");
        Log.d("Common",""+ commonName);
        int ID = intent.getIntExtra("id", 0);
        Log.d("Okabcde","Original URL: " + originalUrl);
        Log.d("Okabcde","Common Name: " + commonName);

        // Find TextView elements
        TextView scientificNameTextView = findViewById(R.id.Scientific_details_name);
        TextView commonNameTextView = findViewById(R.id.Common_details_name);
        TextView cycleTextView = findViewById(R.id.cycle_details);
        TextView wateringTextView = findViewById(R.id.humidity_details);
        ImageView BigImageDetails = findViewById(R.id.ImageBigDetails);
        TextView temperatureTextView = findViewById(R.id.temperatureDetails);

        // Set text in TextView elements
        scientificNameTextView.setText(joinStrings(scientificNames));
        commonNameTextView.setText(commonName);
        cycleTextView.setText(cycle);
        wateringTextView.setText(watering);
        if (originalUrl != null && !originalUrl.isEmpty()) {
            Picasso.get().load(originalUrl).into(BigImageDetails);
        } else {
            Picasso.get().load(R.drawable.ic_thumbnail).into(BigImageDetails);
        }

        List<Integer> imageList = Arrays.asList(
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail
        );

        Plant_Details_Image_Recycle_Adapter adapter = new Plant_Details_Image_Recycle_Adapter(this, imageList);
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String apiKey     = "sk-gAIS6560794454fbf2885";   // Quy's API key
        //String apiKey     = "sk-O0QK655e2575b0b303082";   // Nguyen Main
        //String apiKey     = "sk-JAdj65704f90038483358";   // Nguyen 2nd
        //String apiKey     = "sk-PEwA657057073ee313360";   // Quy 2nd
        fetchPlantDetails(ID,apiKey);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This is called when the up/back button is pressed
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private String joinStrings(List<String> strings) {
        StringBuilder result = new StringBuilder();
        for (String str : strings) {
            result.append(str).append(", ");
        }
        if (result.length() > 0) {
            // Remove the trailing comma and space
            result.setLength(result.length() - 2);
        }
        return result.toString();
    }
    //fetch details data
    private void fetchPlantDetails(int plantId, String apiKey) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://perenual.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        JSONPlaceHolder jsonPlaceHolder = retrofit.create(JSONPlaceHolder.class);
        Call<PlantDetailsResponse> call = jsonPlaceHolder.getPlantDetails(plantId, apiKey);
        call.enqueue(new Callback<PlantDetailsResponse>() {
            @Override
            public void onResponse(Call<PlantDetailsResponse> call, Response<PlantDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlantDetailsResponse plantDetails = response.body();
                    String wateringPeriod = plantDetails.getWatering_period();
                    String description = plantDetails.getDescription();
                    TextView flowering_season = findViewById(R.id.SeasonsDetails);
                    TextView description_details = findViewById(R.id.descriptionDetails);
                    if (plantDetails.getFlowering_season() != null) {
                        flowering_season.setText(plantDetails.getFlowering_season());
                    } else {
                        flowering_season.setText("Not specified");
                    }
                    description_details.setText(description);
                } else {
                    Log.e("PlantDetails", "Error: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<PlantDetailsResponse> call, Throwable t) {
                Log.e("PlantDetails", "Error: " + t.getMessage());
            }
        });
    }
}