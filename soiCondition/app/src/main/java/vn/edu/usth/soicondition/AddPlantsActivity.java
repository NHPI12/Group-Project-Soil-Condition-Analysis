package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.soicondition.network.model.PlantData;

public class AddPlantsActivity extends AppCompatActivity {
    private List<PlantData> plantList = new ArrayList<>(); // Initialize the plantList
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plants);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Retrieve the plant list from the intent
        Intent intent = getIntent();// Display the plant list with checkboxes
    }
    private void displayPlantList(ArrayList<PlantData> plantList) {
        LinearLayout plantContainer = findViewById(R.id.plantContainer);
        // Check if the list is not null before using it
            for (PlantData plant : plantList) {
                // Create a checkbox for each plant
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(plant.getCommon_name());
                checkBox.setTag(plant);
                plantContainer.addView(checkBox);
            }
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