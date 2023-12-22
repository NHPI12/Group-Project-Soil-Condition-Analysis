package vn.edu.usth.soicondition;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;

import java.util.Arrays;
import java.util.List;

public class PlantDetailsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plant_details, container, false);
        recyclerView = view.findViewById(R.id.imageListRecycleView);
        layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false);
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
        Plant_Details_Image_Recycle_Adapter adapter = new Plant_Details_Image_Recycle_Adapter(requireContext(), imageList);
        recyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }

}