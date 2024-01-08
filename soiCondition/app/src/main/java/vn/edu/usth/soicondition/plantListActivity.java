package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;


import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.default_Image;

public class plantListActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private Plant_List_Recycle_Adapter plantListRecycleAdapter;
    private List<PlantData> plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        // RecycleView
        Intent intent = getIntent();
        if (intent.hasExtra("plantList")) {
            plantList = intent.getParcelableArrayListExtra("plantList");
            Log.d("New Data", "" + plantList);
            recyclerView = findViewById(R.id.plant_list_recycle_View);
            plantListRecycleAdapter = new Plant_List_Recycle_Adapter(this, plantList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            plantListRecycleAdapter.setOnItemClickListener(position -> {
                PlantData clickedPlant = plantList.get(position);
                default_Image clickedPlantImage = clickedPlant.getDefaultImage();
                Intent intent1 = new Intent(plantListActivity.this, PlantDetailsActivity.class);
                // Pass data to PlantDetailsActivity
                intent1.putExtra("original_url", clickedPlantImage.getOriginalUrl());
                Log.d("PlantListActivity", "Original URL: " + clickedPlantImage.getOriginalUrl());
                intent1.putExtra("scientific_name", new ArrayList<>(clickedPlant.getScientific_name()));
                intent1.putExtra("sunlight", new ArrayList<>(clickedPlant.getSunlight()));
                intent1.putExtra("common_name", clickedPlant.getCommon_name());
                intent1.putExtra("cycle", clickedPlant.getCycle());
                intent1.putExtra("watering", clickedPlant.getWatering());
                intent1.putExtra("id", clickedPlant.getId());
                startActivity(intent1);

            });
        }
        recyclerView.setAdapter(plantListRecycleAdapter);

        //Navigation menu
        drawerLayout = findViewById(R.id.plant_list_nav_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.plant_list_nav);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.stats_plant) {
                Intent intent12 = new Intent(plantListActivity.this, MainActivity.class);
                intent12.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent12);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                finish();
            }
            else if (id == R.id.item_5) {
                openSettings();
                drawerLayout.closeDrawer(GravityCompat.START);

            }else if (id == R.id.item_2) {
                Intent intent12 = new Intent(plantListActivity.this, Your_Plant_Activity.class);
                intent12.putParcelableArrayListExtra("plantList", new ArrayList<>(plantList));
                startActivity(intent12);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                finish();
            }
            return false;
        });

        TextView addTextView = findViewById(R.id.add_text);
        TextView removeTextView = findViewById(R.id.remove_text);
        
        addTextView.setOnClickListener(v -> {
            Intent addIntent = new Intent(plantListActivity.this, AddPlantsActivity.class);
            addIntent.putExtra("plantList", new ArrayList<>(plantList));
            startActivity(addIntent);
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
        });
        removeTextView.setOnClickListener(v -> {
            Intent removeIntent = new Intent(plantListActivity.this, RemovePlantsActivity.class);
            removeIntent.putExtra("plantList", new ArrayList<>(plantList));
            startActivity(removeIntent);
            overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);

        });
        }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        Intent intent = new Intent(plantListActivity.this, setting.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_bar_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.search_action_bar);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                plantListRecycleAdapter.filterList(newText);
                return true;
            }
        });
        return  super.onCreateOptionsMenu(menu);
        }
}



