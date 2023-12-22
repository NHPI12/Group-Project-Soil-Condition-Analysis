package vn.edu.usth.soicondition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

import vn.edu.usth.soicondition.Plant_Details_Image_Recycle_Adapter;
import vn.edu.usth.soicondition.network.model.PlantData;

public class PlantDetailsActivity extends AppCompatActivity {

    private static final String ARG_PLANT = "plant";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        // Initialize and set up your RecyclerView and Adapter here
        RecyclerView recyclerView = findViewById(R.id.imageListRecycleView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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