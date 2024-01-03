package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.soicondition.network.model.PlantData;

public class AddPlantsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Plant_Add_Recycle_Adapter plantAddRecycleAdapter;
    private Button btnAddPlants;
    private SharedPreferences sharedPreferences;
    private static final String PREF_SELECTED_PLANTS = "selected_plants";
    private List<PlantData> plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plants);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent.hasExtra("plantList")) {
            plantList = intent.getParcelableArrayListExtra("plantList");

            recyclerView = findViewById(R.id.plant_add_recycle_View);
            plantAddRecycleAdapter = new Plant_Add_Recycle_Adapter(this, plantList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(plantAddRecycleAdapter);
            Button checkAllButton = findViewById(R.id.CheckButtonAll);
            checkAllButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    plantAddRecycleAdapter.switchAllChecked();
                }
            });
        } else {
            Toast.makeText(this, "No plant data available", Toast.LENGTH_SHORT).show();
            finish();
        }
        sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences", MODE_PRIVATE);
        btnAddPlants = findViewById(R.id.btnAddPlants);
        btnAddPlants.setVisibility(View.GONE); // initially set the button as gone
        plantAddRecycleAdapter.setOnCheckedChangeListener(new Plant_Add_Recycle_Adapter.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isAtLeastOneChecked) {
                btnAddPlants.setVisibility(isAtLeastOneChecked ? View.VISIBLE : View.GONE);
            }
        });
        btnAddPlants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int selectedPlantCount = plantAddRecycleAdapter.getSelectedPlantIds().size();
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to add " + selectedPlantCount +" plant(s) to your plants list?");
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
        // Add new plant IDs to the existing set
        existingPlantIds.addAll(plantAddRecycleAdapter.getSelectedPlantIds());
        // Save the updated set back to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(PREF_SELECTED_PLANTS, convertSetToStringSet(existingPlantIds));
        editor.apply();
        Log.d("Selected Plant IDs", existingPlantIds.toString());
        Intent intent = new Intent(AddPlantsActivity.this, MainActivity.class);
        intent.putParcelableArrayListExtra("plantList", new ArrayList<>(plantList));
        startActivity(intent);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }
    private Set<String> convertSetToStringSet(Set<Integer> integerSet) {
        Set<String> stringSet = new HashSet<>();
        for (Integer value : integerSet) {
            stringSet.add(String.valueOf(value));
        }
        return stringSet;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}