package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.LayoutTransition;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    TextView humidDetail, tempDetail, soilDetail;
    LinearLayout humidLayout, tempLayout, soilLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        humidDetail = findViewById(R.id.humidDetails);
        tempDetail  = findViewById(R.id.tempDetails);
        soilDetail  = findViewById(R.id.soilDetails);
        humidLayout = findViewById(R.id.layout_humid);
        tempLayout  = findViewById(R.id.layout_temperature);
        soilLayout  = findViewById(R.id.layout_soilMoisture);
        humidLayout .getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        tempLayout  .getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        soilLayout  .getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://smart-pot-1d7b5-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("sensor_data");
        DatabaseReference databaseReference2 = databaseReference1.child("soil moisture");

        TextView soilData = findViewById(R.id.soilData);
        TextView tempData = findViewById(R.id.tempData);
        TextView humidData = findViewById(R.id.humidData);

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    String soilMoistureValue = dataSnapshot.getValue(String.class);
                    if (soilMoistureValue != null) {
                        soilData.setText(soilMoistureValue);
                    } else {
                        // Handle the case where data is null
                        soilData.setText("NaN");
                    }
                } else {
                    // Handle the case where data doesn't exist
                    soilData.setText("NaN");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                soilData.setText("Error fetching data");
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });




                //Navigation Menu
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.stats_plant) {
                    // Open PlantListActivity
                    Log.d("MainActivity", "Plant List Clicked");
                    openStatsActivity();
                    return true;
                }
                if (id == R.id.list_plants) {
                    // Open PlantListActivity
                    Log.d("MainActivity", "Plant List Clicked");
                    openPlantListActivity();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // Method to open PlantListActivity
    private void openStatsActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void openPlantListActivity() {
        Intent intent = new Intent(MainActivity.this, plantListActivity.class);
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandHumid(View view) {
        expand(humidDetail,humidLayout,tempDetail,soilDetail);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandTemp(View view) {
        expand(tempDetail,tempLayout,humidDetail,soilDetail);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandSoil(View view) {
        expand(soilDetail,soilLayout,humidDetail,tempDetail);
    }
    private void expand(TextView clickedText, LinearLayout clickedLayout, TextView... otherTexts) {
        if (clickedText.getVisibility() == View.VISIBLE) {
            clickedText.setVisibility(View.GONE);
        } else {
            clickedText.setVisibility(View.VISIBLE);
            // Set other details to GONE
            for (TextView otherText : otherTexts) {
                otherText.setVisibility(View.GONE);
            }
        }
        TransitionManager.beginDelayedTransition(clickedLayout, new AutoTransition());
    }
}