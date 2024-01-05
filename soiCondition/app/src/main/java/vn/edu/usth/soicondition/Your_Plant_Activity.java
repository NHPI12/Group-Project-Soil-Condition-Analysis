package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.default_Image;

public class Your_Plant_Activity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private Plant_List_Recycle_Adapter YourPlantRecycleAdapter;
    private List<PlantData> plantList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_plant);

        drawerLayout = findViewById(R.id.your_plant_nav_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        NavigationView navigationView = findViewById(R.id.your_plant_nav);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.stats_plant) {
                    Intent intent = new Intent(Your_Plant_Activity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    finish();
                } else if (id == R.id.item_5) {
                    openSettings();
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else if (id == R.id.list_plants){
                    Intent intent = new Intent(Your_Plant_Activity.this, plantListActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    intent.putParcelableArrayListExtra("plantList", new ArrayList<>(plantList));
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        TextView addTextView = findViewById(R.id.add_text);
        TextView removeTextView = findViewById(R.id.remove_text);
        addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(Your_Plant_Activity.this, AddPlantsActivity.class);
                addIntent.putExtra("plantList", new ArrayList<>(plantList));
                startActivity(addIntent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
        removeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent removeIntent = new Intent(Your_Plant_Activity.this, RemovePlantsActivity.class);
                removeIntent.putExtra("plantList", new ArrayList<>(plantList));
                startActivity(removeIntent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences", MODE_PRIVATE);
        Set<String> selectedPlantIdsStringSet = sharedPreferences.getStringSet("selected_plants", new HashSet<>());

        Set<Integer> selectedPlantIds = new HashSet<>();
        for (String id : selectedPlantIdsStringSet) {
            selectedPlantIds.add(Integer.valueOf(id));
        }

        Intent intent = getIntent();
        if (intent.hasExtra("plantList")) {
            plantList = intent.getParcelableArrayListExtra("plantList");
            Log.d("New Data", "" + plantList);
            List<PlantData> allSelectedPlants = getAllSelectedPlants(sharedPreferences);

            recyclerView = findViewById(R.id.your_plant_recycle_View);
            YourPlantRecycleAdapter = new Plant_List_Recycle_Adapter(this, allSelectedPlants);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            YourPlantRecycleAdapter.setOnItemClickListener(new Plant_List_Recycle_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    PlantData clickedPlant = plantList.get(position);
                    default_Image clickedPlantImage = clickedPlant.getDefaultImage();
                    Intent intent = new Intent(Your_Plant_Activity.this, PlantDetailsActivity.class);
                    // Pass data to PlantDetailsActivity
                    intent.putExtra("original_url", clickedPlantImage.getOriginalUrl());
                    Log.d("PlantListActivity", "Original URL: " + clickedPlantImage.getOriginalUrl());
                    intent.putExtra("scientific_name", new ArrayList<>(clickedPlant.getScientific_name()));
                    intent.putExtra("sunlight", new ArrayList<>(clickedPlant.getSunlight()));
                    intent.putExtra("common_name", clickedPlant.getCommon_name());
                    intent.putExtra("cycle", clickedPlant.getCycle());
                    intent.putExtra("watering", clickedPlant.getWatering());
                    intent.putExtra("id", clickedPlant.getId());
                    startActivity(intent);
                }
            });
        }
        recyclerView.setAdapter(YourPlantRecycleAdapter);
    }
    private PlantData getPlantDataById(int plantId, List<PlantData> plantList) {
        if (plantList == null || plantList.isEmpty()) {
            Log.d("Selected Plant Details" ,"Null " + plantList.size());
            return null;
        }
        else Log.d("Selected Plant Details" ,"Not Null " + plantList.size());
        for (PlantData plantData : plantList) {
            if (plantData.getId() == plantId) {
                String commonName = plantData.getCommon_name();
                Log.d("Selected Plant Details", "Common Name: " + commonName);
                return plantData; // Return the entire PlantData object if needed
            }
        }
        // Plant with the specified ID not found
        Log.e("Selected Plant Details", "Plant with ID " + plantId + " not found");
        return null;
    }
    private List<PlantData> getAllSelectedPlants(SharedPreferences sharedPreferences) {
        Set<String> selectedPlantIdsStringSet = sharedPreferences.getStringSet("selected_plants", new HashSet<>());
        List<PlantData> selectedPlants = new ArrayList<>();
        for (String id : selectedPlantIdsStringSet) {
            int plantId = Integer.parseInt(id);
            PlantData plantData = getPlantDataById(plantId, plantList);
            if (plantData != null) {
                selectedPlants.add(plantData);
            }
        }
        return selectedPlants;
    }
    private void openSettings() {
        Intent intent = new Intent(Your_Plant_Activity.this, setting.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        //finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}