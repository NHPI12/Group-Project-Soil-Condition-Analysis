package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class PlantDetailsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        recyclerView = findViewById(R.id.imageListRecycleView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);


        List<Integer> imageList = Arrays.asList(
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail,
                R.drawable.ic_thumbnail
        );
        Plant_Details_Image_Recycle_Adapter adapter = new Plant_Details_Image_Recycle_Adapter(this, imageList);
        recyclerView.setAdapter(adapter);
    }

}