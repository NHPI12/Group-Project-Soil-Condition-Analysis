package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vn.edu.usth.soicondition.network.model.PlantData;

public class AddPlantsActivity extends AppCompatActivity {
    private ListView listView;
    private Plant_Add_ListView_Adapter plantAddListViewAdapter;
    private Button btnAddPlants;
    private SharedPreferences sharedPreferences;
    private static final String PREF_SELECTED_PLANTS = "selected_plants";
    private List<PlantData> plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences", MODE_PRIVATE);
        btnAddPlants = findViewById(R.id.btnAddPlants);
        btnAddPlants.setVisibility(View.GONE);

        Intent intent = getIntent();
        if (intent.hasExtra("plantList")) {
            plantList = intent.getParcelableArrayListExtra("plantList");
            listView = findViewById(R.id.plant_add_list_View);
            plantAddListViewAdapter = new Plant_Add_ListView_Adapter(this, plantList);
            listView.setAdapter(plantAddListViewAdapter);
            plantAddListViewAdapter.setOnCheckedChangeListener(new Plant_Add_ListView_Adapter.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean isAtLeastOneChecked) {
                    btnAddPlants.setVisibility(isAtLeastOneChecked ? View.VISIBLE : View.GONE);
                }
            });
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
                    plantAddListViewAdapter.switchAllChecked();
                });
                checkBox.setOnClickListener(v -> {
                    plantAddListViewAdapter.switchAllChecked();
                });
                btnAddPlants.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmationDialog();
                    }
                });
            }
        } else {
            Toast.makeText(this, "No plant data available", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        int selectedPlantCount = plantAddListViewAdapter.getSelectedPlantIds().size();
        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to add " + selectedPlantCount + " plant(s) to your plants list?");
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
        existingPlantIds.addAll(plantAddListViewAdapter.getSelectedPlantIds());
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