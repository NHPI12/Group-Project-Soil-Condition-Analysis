package vn.edu.usth.soicondition;


import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import vn.edu.usth.soicondition.network.JSONPlaceHolder;
import vn.edu.usth.soicondition.network.model.PlantDetailsResponse;

import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;



public class PlantDetailsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    boolean  tempMode;
    private static final String PREF_SELECTED_PLANTS="selected_plants";

    SharedPreferences whatlangPreference, sharedPreferences_tempmode;
    SharedPreferences.Editor edit_whatlang;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences",MODE_PRIVATE);
        Set<String> existingPlantIdsStringSet = sharedPreferences.getStringSet(PREF_SELECTED_PLANTS,new HashSet<>());
        Set<Integer> existingPlantIds = new HashSet<>();
        for (String id : existingPlantIdsStringSet){
            existingPlantIds.add(Integer.valueOf(id));
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Plant Details");
        }
        // Initialize and set up your RecyclerView and Adapter here
        Button DetailsButton = findViewById(R.id.details_button);

        Intent intent = getIntent();
        int ID = intent.getIntExtra("id", 0);
        if(!existingPlantIds.contains(ID)){
            DetailsButton.setText("ADD PLANT");
            DetailsButton.setBackgroundResource(R.drawable.background_button_add);
            DetailsButton.setTextColor(getResources().getColor(R.color.lilwhite));
            DetailsButton.setOnClickListener(v -> {
                //Add function
                showConfirmationDialogAdd(ID);
            });
        }else{
            DetailsButton.setText("Remove Plant");
            DetailsButton.setBackgroundResource(R.drawable.background_button_remove);
            DetailsButton.setTextColor(getResources().getColor(R.color.lilwhite));
            DetailsButton.setOnClickListener(v -> {
                //Remove function
                showConfirmationDialogRemove(ID);
            });
        }


        ArrayList<String> scientificNames = intent.getStringArrayListExtra("scientific_name");
        String commonName = intent.getStringExtra("common_name");
        String cycle = intent.getStringExtra("cycle");
        String watering = intent.getStringExtra("watering");
        String originalUrl = intent.getStringExtra("original_url");
        ArrayList<String> sunlight = intent.getStringArrayListExtra("sunlight");
        Log.d("Common",""+ commonName);

        Log.d("Okabcde","Original URL: " + originalUrl);
        Log.d("Okabcde","Common Name: " + commonName);

        // Find TextView elements
        TextView scientificNameTextView = findViewById(R.id.Scientific_details_name);
        TextView commonNameTextView = findViewById(R.id.Common_details_name);
        TextView cycleTextView = findViewById(R.id.cycle_details);
        TextView wateringTextView = findViewById(R.id.humidity_details);
        ImageView BigImageDetails = findViewById(R.id.ImageBigDetails);

        // Set text in TextView elements
        scientificNameTextView.setText(joinStrings(Objects.requireNonNull(scientificNames)));
        commonNameTextView.setText(commonName);
        cycleTextView.setText(cycle);
        customizeSunlightText(Objects.requireNonNull(sunlight));
        wateringTextView.setText(watering);
        customizeHumidityText(Objects.requireNonNull(watering));

        if (originalUrl != null && !originalUrl.isEmpty()) {
            Picasso.get().load(originalUrl).into(BigImageDetails);
        }
        else {
            Picasso.get().load(R.drawable.ic_thumbnail).into(BigImageDetails);
        }

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        String apiKey     = "sk-gAIS6560794454fbf2885";   // Quy's API key
        //String apiKey     = "sk-O0QK655e2575b0b303082";   // Nguyen Main
        //String apiKey     = "sk-JAdj65704f90038483358";   // Nguyen 2nd
        //String apiKey     = "sk-PEwA657057073ee313360";   // Quy 2nd
        //String apiKey = "sk-V27h658e9a807e9213607"; // Quy 3rd

        //String apiKey ="sk-MUZ5659b830f829253689"; //Nguyen 3rd
        fetchPlantDetails(ID,apiKey);
    }


    protected void onRestart() {
        super.onRestart();
        TextView descriptionDetails = findViewById(R.id.descriptionDetails);
        String desDetail = descriptionDetails.getText().toString();

        TextView SeasonsDetails = findViewById(R.id.SeasonsDetails);
        String strSeasonsDetails = SeasonsDetails.toString();

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setTargetLanguage("fr")
                .setSourceLanguage("en")
                .build();
        Translator translator = Translation.getClient(options);
        translator.downloadModelIfNeeded();

        Task<String> results = translator.translate(desDetail).addOnSuccessListener(s -> Toast.makeText(PlantDetailsActivity.this, "hehe", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(PlantDetailsActivity.this, "haha", Toast.LENGTH_SHORT).show());

    }

    //Show confirmation and Remove Plant
    private void showConfirmationDialogRemove(int id) {AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to REMOVE this plant to your list?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            removeSelectedPlants(id);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();

    }
    //Remove selected plants
    private void removeSelectedPlants(int IntId) {
        Set<String> existingPlantIdsStringSet = sharedPreferences.getStringSet(PREF_SELECTED_PLANTS,new HashSet<>());
        Set<Integer> existingPlantIds = new HashSet<>();
        for (String id : existingPlantIdsStringSet){
            existingPlantIds.add(Integer.valueOf(id));
        }
        existingPlantIds.remove(IntId);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_SELECTED_PLANTS,convertSetToStringSet(existingPlantIds));
        editor.apply();
        Log.d("Selected plant IDs", existingPlantIds.toString());
        recreate();
    }

    // Show confirmation and Add the plant
    private void showConfirmationDialogAdd(int IntId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to ADD this plant to your list?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            dialog.dismiss();
            saveSelectedPlants(IntId);
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Save the selected plant
    private void saveSelectedPlants(int IntId){
        Set<String> existingPlantIdsStringSet = sharedPreferences.getStringSet(PREF_SELECTED_PLANTS,new HashSet<>());
        Set<Integer> existingPlantIds = new HashSet<>();
        for (String id : existingPlantIdsStringSet){
            existingPlantIds.add(Integer.valueOf(id));
        }
        existingPlantIds.add(IntId);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_SELECTED_PLANTS,convertSetToStringSet(existingPlantIds));
        editor.apply();
        Log.d("Selected plant IDs", existingPlantIds.toString());
        recreate();
    }

    private Set<String> convertSetToStringSet(Set<Integer> IntegerSet) {
        Set<String> stringSet = new HashSet<>();
        for(Integer value : IntegerSet){
            stringSet.add(String.valueOf(value));
        }
        return stringSet;
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<PlantDetailsResponse> call, @NonNull Response<PlantDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlantDetailsResponse plantDetails = response.body();
                    String description = plantDetails.getDescription();
                    String watering_period = plantDetails.getWatering_period();
                    TextView flowering_season = findViewById(R.id.SeasonsDetails);
                    TextView description_details = findViewById(R.id.descriptionDetails);
                    ImageView flowering_Image = findViewById(R.id.Flowering_Season_Icon);
                    if (plantDetails.getFlowering_season() != null) {
                        setSeasonIcon(plantDetails.getFlowering_season(), flowering_Image);
                        flowering_season.setText(plantDetails.getFlowering_season());
                    } else {
                        flowering_season.setText("Not specified");
                        flowering_Image.setImageResource(R.drawable.season_cycle_icon);
                    }
                    description_details.setText(description);
                    Log.e("PlantDetails","Watering Period: "+watering_period);
                } else {
                    Log.e("PlantDetails", "Error: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<PlantDetailsResponse> call, @NonNull Throwable t) {
                Log.e("PlantDetails", "Error: " + t.getMessage());
            }
        });
    }
    private void setSeasonIcon(String season, ImageView seasonIconImageView) {
        int iconResource;

        switch (season.toLowerCase()) {
            case "spring":
                iconResource = R.drawable.spring_icon;
                break;
            case "summer":
                iconResource = R.drawable.summer_icon;
                break;
            case "autumn":
                iconResource = R.drawable.autumn_icon;
                break;
            case "winter":
                iconResource = R.drawable.winter_icon;
                break;
            default:
                iconResource = R.drawable.season_cycle_icon;
                break;
        }
        seasonIconImageView.setImageResource(iconResource);
    }
    private void customizeHumidityText(String watering) {
        TextView humidityTextView = findViewById(R.id.humidity_details);
        String humidityText;
        switch (watering.toLowerCase()) {
            case "frequent":
                humidityText = "High";
                break;
            case "average":
                humidityText = "Medium";
                break;
            case "minimum":
                humidityText = "Low";
                break;
            case "none":
                humidityText = "None";
                break;
            default:
                humidityText = "Not specified";
                break;
        }
        humidityTextView.setText(humidityText);
    }
    private void customizeSunlightText(List<String> sunlight) {
        TextView temperatureTextView = findViewById(R.id.temperatureDetails);
        // Create lists to store minimum and maximum temperatures
        List<Integer> minTemps = new ArrayList<>();
        List<Integer> maxTemps = new ArrayList<>();

        // Loop through each sunlight value and populate the temperature lists
        for (String value : sunlight) {
            switch (value.toLowerCase()) {
                case "full shade":
                    minTemps.add(15);
                    maxTemps.add(24);
                    break;
                case "part shade":
                case "part sun/part shade":
                    minTemps.add(18);
                    maxTemps.add(27);
                    break;
                case "filtered shade":
                    minTemps.add(16);
                    maxTemps.add(25);
                    break;
                case "full sun":
                    minTemps.add(21);
                    maxTemps.add(32);
                    break;
                default:
                    // Handle other cases if needed
                    break;
            }
        }

        // Find the overall minimum and maximum temperatures
        int overallMinTemp = Collections.min(minTemps);
        int overallMaxTemp = Collections.max(maxTemps);

        float overMinTemp, overMaxTemp;


        sharedPreferences_tempmode = getSharedPreferences("MODE_TEMP", Context.MODE_PRIVATE);
        tempMode = sharedPreferences_tempmode.getBoolean("tempMode", false);

        // Display the customized temperature text
        String temperatureValue;
        if(!tempMode){
            temperatureValue = overallMinTemp + "°C - " + overallMaxTemp + "°C";
        }
        else{
            overMinTemp = (float) (overallMinTemp*1.8 + 32); overMaxTemp = (float) (overallMaxTemp*1.8 + 32);
            temperatureValue = overMinTemp + "°F - " + overMaxTemp + "°F";
        }
        temperatureTextView.setText(temperatureValue);

    }
}