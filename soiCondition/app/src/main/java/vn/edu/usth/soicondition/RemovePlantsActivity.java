package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.soicondition.network.model.PlantData;

public class RemovePlantsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Plant_Add_Recycle_Adapter plantRemoveRecycleAdapter;
    private Button btnRemovePlants;
    private SharedPreferences sharedPreferences;
    private static final String PREF_SELECTED_PLANTS = "selected_plants";
    private List<PlantData> plantList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_plants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Get your plant list
        sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences",MODE_PRIVATE);
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
            // Inflate custom ActionBar layout
            LayoutInflater inflater = LayoutInflater.from(this);
            View customActionBarView = inflater.inflate(R.layout.actionbar_custom_layout, null);

            // Set custom ActionBar layout
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowCustomEnabled(true);
                actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.MATCH_PARENT,
                        ActionBar.LayoutParams.MATCH_PARENT
                ));

                LinearLayout checkAllView = findViewById(R.id.checkAllView);
                CheckBox checkBox = findViewById(R.id.checkAllCheckBox);

                checkAllView.setOnClickListener(v -> {
                    checkBox.setChecked(!checkBox.isChecked());
                    plantRemoveRecycleAdapter.switchAllChecked();
                });
                checkBox.setOnClickListener(v -> {
                    plantRemoveRecycleAdapter.switchAllChecked();
                });
            }
        }
        btnRemovePlants = findViewById(R.id.btnRemovePlants);
        btnRemovePlants.setVisibility(View.GONE); // initially set the button as gone
        plantRemoveRecycleAdapter.setOnCheckedChangeListener(new Plant_Add_Recycle_Adapter.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isAtLeastOneChecked) {
                btnRemovePlants.setVisibility(isAtLeastOneChecked ? View.VISIBLE : View.GONE);
            }
        });
        btnRemovePlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
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
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int selectedPlantCount = plantRemoveRecycleAdapter.getSelectedPlantIds().size();
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to remove " + selectedPlantCount +" plant(s) from your plants list?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                saveSelectedPlants();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void saveSelectedPlants() {
        // Retrieve existing set from SharedPreferences
        Set<String> existingPlantIdsStringSet = sharedPreferences.getStringSet(PREF_SELECTED_PLANTS, new HashSet<>());
        // Convert the existing set to a Set<Integer>
        Set<Integer> existingPlantIds = new HashSet<>();
        for (String id : existingPlantIdsStringSet) {
            existingPlantIds.add(Integer.valueOf(id));
        }
        // Remove the plants that were selected for removal
        existingPlantIds.removeAll(plantRemoveRecycleAdapter.getSelectedPlantIds());
        // Save the updated set back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_SELECTED_PLANTS, convertSetToStringSet(existingPlantIds));
        editor.apply();
        Log.d("Selected Plant IDs", existingPlantIds.toString());
        Intent intent = new Intent(RemovePlantsActivity.this, MainActivity.class);
        intent.putParcelableArrayListExtra("plantList", new ArrayList<>(plantList));
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
}
