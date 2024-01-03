package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.soicondition.network.model.PlantData;

public class RemovePlantsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Plant_Add_Recycle_Adapter plantRemoveRecycleAdapter;
    private Button btnRemovePlants;

    private static final String PREF_SELECTED_PLANTS = "selected_plants";
    private List<PlantData> plantList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_plants);
        Toolbar toolbar = findViewById(R.id.Remove_toolbar);
        setSupportActionBar(toolbar);

        Button button = new Button(this);
        int color = ContextCompat.getColor(this, R.color.black);
        int colorBtn = ContextCompat.getColor(this, R.color.white);
        button.setBackgroundColor(color);
        button.setTextColor(colorBtn);
        button.setText("All");

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                plantRemoveRecycleAdapter.switchAllChecked();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(button, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.END
        ));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        // Get your plant list
        SharedPreferences sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences",MODE_PRIVATE);
        Set<String> selectedPlantIdsStringSet = sharedPreferences.getStringSet("selected_plants",new HashSet<>());

        Set<Integer> selectedPlantIds = new HashSet<>();
        for (String id : selectedPlantIdsStringSet){
            selectedPlantIds.add(Integer.valueOf(id));
        }
        //recycle View display your plant list
        Intent intent = getIntent();
        if (intent.hasExtra("plantList")) {
            plantList = intent.getParcelableArrayListExtra("plantList");
            Log.d("New Data", "" + plantList);
            List<PlantData> allSelectedPlants = getAllSelectedPlants(sharedPreferences);
            recyclerView = findViewById(R.id.plant_remove_recycle_View);
            plantRemoveRecycleAdapter = new Plant_Add_Recycle_Adapter(this, allSelectedPlants);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(plantRemoveRecycleAdapter);
            //Button checkAllButton = findViewById(R.id.CheckRemoveButtonAll);
        }
    }
    private Set<String> convertSetToStringSet(Set<Integer> integerSet) {
        Set<String> stringSet = new HashSet<>();
        for (Integer value : integerSet) {
            stringSet.add(String.valueOf(value));
        }
            return stringSet;
    }
    @Override
    public boolean onOptionsItemSelected (@NonNull MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
            return super.onOptionsItemSelected(item);
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
}
