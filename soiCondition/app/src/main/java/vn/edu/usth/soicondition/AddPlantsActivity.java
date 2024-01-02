package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import vn.edu.usth.soicondition.network.model.PlantData;

public class AddPlantsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Plant_Add_Recycle_Adapter plantAddRecycleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plants);
        Button checkAllButton = findViewById(R.id.CheckButtonAll);
        checkAllButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                plantAddRecycleAdapter.switchAllChecked();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        if(intent.hasExtra("plantList")){
            List<PlantData> plantList = intent.getParcelableArrayListExtra("plantList");
            Log.d("New Data",""+ plantList);
            recyclerView = findViewById(R.id.plant_add_recycle_View);
            plantAddRecycleAdapter = new Plant_Add_Recycle_Adapter(this,plantList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

        }
        recyclerView.setAdapter(plantAddRecycleAdapter);

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