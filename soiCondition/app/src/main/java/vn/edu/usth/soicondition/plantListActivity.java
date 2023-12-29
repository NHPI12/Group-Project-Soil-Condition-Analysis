package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import vn.edu.usth.soicondition.PlantDetailsActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.soicondition.network.JSONPlaceHolder;
import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.PlantResponse;
import vn.edu.usth.soicondition.network.model.default_Image;

public class plantListActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerView;
    private Plant_List_Recycle_Adapter plantListRecycleAdapter;
    private NavigationView navigationView;
    private List<PlantData> plantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_list);

        //Navigation menu
        drawerLayout = findViewById(R.id.plant_list_nav_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        navigationView = findViewById(R.id.plant_list_nav);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.plant_list_nav);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.stats_plant) {
                    Intent intent = new Intent(plantListActivity.this, MainActivity.class);
                    Log.d("Troi oi cuoc doi List", "" + plantList);
                    openActivity(MainActivity.class);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                else if (id == R.id.item_5) {
                    openSettings();
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
                Intent addIntent = new Intent(plantListActivity.this, AddPlantsActivity.class);
                addIntent.putExtra("plantList", new ArrayList<>(plantList));
                openActivity(AddPlantsActivity.class);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_up_out);
            }
        });
        removeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(RemovePlantsActivity.class);
                overridePendingTransition(R.anim.slide_up, R.anim.slide_up_out);
            }
        });
        // RecycleView
        Intent intent = getIntent();
        if (intent.hasExtra("plantList")) {
            List<PlantData> plantList = intent.getParcelableArrayListExtra("plantList");
            Log.d("New Data", "" + plantList);
            recyclerView = findViewById(R.id.plant_list_recycle_View);
            plantListRecycleAdapter = new Plant_List_Recycle_Adapter(this, plantList);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            plantListRecycleAdapter.setOnItemClickListener(new Plant_List_Recycle_Adapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    PlantData clickedPlant = plantList.get(position);
                    default_Image clickedPlantImage = clickedPlant.getDefaultImage();
                    Intent intent = new Intent(plantListActivity.this, PlantDetailsActivity.class);
                    // Pass data to PlantDetailsActivity
                    intent.putExtra("original_url", clickedPlantImage.getOriginalUrl());
                    Log.d("PlantListActivity", "Original URL: " + clickedPlantImage.getOriginalUrl());
                    intent.putExtra("scientific_name", new ArrayList<>(clickedPlant.getScientific_name()));
                    intent.putExtra("common_name", clickedPlant.getCommon_name());
                    intent.putExtra("cycle", clickedPlant.getCycle());
                    intent.putExtra("watering", clickedPlant.getWatering());
                    intent.putExtra("id", clickedPlant.getId());
                    startActivity(intent);

                }
            });
        }
            recyclerView.setAdapter(plantListRecycleAdapter);

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
        //finish();
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
                return false;
            }
        });
        return  super.onCreateOptionsMenu(menu);
        }
    private void openActivity(Class<?> destinationClass) {
        Intent intent = new Intent(plantListActivity.this, destinationClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    }



