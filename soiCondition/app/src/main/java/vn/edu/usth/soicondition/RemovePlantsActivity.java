package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.soicondition.network.model.PlantData;

public class RemovePlantsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private List<PlantData> plantList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_plants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent.hasExtra("plantList")) {
            plantList = intent.getParcelableArrayListExtra("plantList");
        }
        sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences", MODE_PRIVATE);
        Set<String> selectedPlantIdsStringSet = sharedPreferences.getStringSet("selected_plants",new HashSet<>());


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
    private PlantData getPlantDataById(int plantId){
        for (PlantData plantData : plantList){
            if(plantData.getId() == plantId){
                String commonName = plantData.getCommon_name();
                String thumbnailUrl = plantData.getDefaultImage().getThumbnail();
                return plantData;
            }
        }
        return null;
    }
}