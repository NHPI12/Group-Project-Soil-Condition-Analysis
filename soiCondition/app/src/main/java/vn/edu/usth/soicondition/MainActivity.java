package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.Button;
import java.util.Calendar;
import java.util.stream.Collectors;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.usth.soicondition.network.ApiServiceDatabase;
import vn.edu.usth.soicondition.network.DatabaseLocal.Measurements;
import vn.edu.usth.soicondition.network.DatabaseLocal.RetrofitDatabase;
import vn.edu.usth.soicondition.network.JSONPlaceHolder;
import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.PlantDetailsResponse;
import vn.edu.usth.soicondition.network.model.PlantResponse;
import vn.edu.usth.soicondition.network.model.plantDetailsObject;



public class MainActivity extends AppCompatActivity implements SelectedPlantsAdapter.OnItemClickListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private LineChart lineChartTemp, lineChartSoil, lineChartHumid;
    LinearLayout humidLayout, tempLayout, soilLayout, buttonsHumid, buttonsTemp, buttonsSoil;
    private List<Measurements> allData = new ArrayList<>();
    private HandlerThread handlerThread;
    private boolean exitConfirmationShown = false;
    private Handler handler;
    private boolean isTimeFrameSelected = false;
    private List<plantDetailsObject> plantDetailsList;
    private List<PlantData> plantList;
    private List<PlantData> allSelectedPlants;
    private CardView selectedPlantsCardView ;
    private RecyclerView selectedPlantsRecyclerView;
    private SelectedPlantsAdapter selectedPlantsAdapter;
    private ImageView arrowImageView;

    private LinearLayout PlantDetailsLinearLayout_1, plantdetailslinelayout_2;
    private TextView textViewWatering, textViewSunlight, textViewCareLevel,textViewWateringPeriod;
    SwitchCompat lightswitch, tempswitch;
    boolean nightMode, tempMode;
    String temperaTure;
    SharedPreferences sharedPreferences, sharedPreferences_tempvalue, sharedPreferences_tempmode;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ///////////////////////night
        arrowImageView = findViewById(R.id.ArrowSelectedPlant);
        Drawable drw;
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if(nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            drw = getResources().getDrawable(R.drawable.lenn, getTheme());
            arrowImageView.setImageDrawable(drw);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            drw = getResources().getDrawable(R.drawable.arrow_up, getTheme());
            arrowImageView.setImageDrawable(drw);
        }
        allData = new ArrayList<>();

        sharedPreferences_tempmode = getSharedPreferences("MODE_TEMP", Context.MODE_PRIVATE);
        tempMode = sharedPreferences_tempmode.getBoolean("tempMode", false);
        sharedPreferences_tempvalue = getSharedPreferences("MODE_VALUE", Context.MODE_PRIVATE);
        temperaTure = sharedPreferences_tempvalue.getString("temperaTure", "a");

        TextView tempUnit = findViewById(R.id.tempUnit);
        if(tempMode){
            tempUnit.setText("°F");
        }
        else {
            tempUnit.setText("°C");
        }

        /////////////////////////////////////////
        buttonsHumid = findViewById(R.id.buttonsHumid);
        buttonsTemp = findViewById(R.id.buttonsTemp);
        buttonsSoil = findViewById(R.id.buttonsSoil);

        Button button1dHumid = findViewById(R.id.button_1d_humid);
        Button button3dHumid = findViewById(R.id.button_3d_humid);
        Button button1wHumid = findViewById(R.id.button_1w_humid);
        Button button1mHumid = findViewById(R.id.button_1m_humid);
        Button buttonAllTimeHumid = findViewById(R.id.button_alltime_humid);

        Button button1dtemp = findViewById(R.id.button_1d_temp);
        Button button3dtemp = findViewById(R.id.button_3d_temp);
        Button button1wtemp = findViewById(R.id.button_1w_temp);
        Button button1mtemp = findViewById(R.id.button_1m_temp);
        Button buttonAllTimetemp = findViewById(R.id.button_alltime_temp);

        Button button1dsoil = findViewById(R.id.button_1d_soil);
        Button button3dsoil = findViewById(R.id.button_3d_soil);
        Button button1wsoil = findViewById(R.id.button_1w_soil);
        Button button1msoil = findViewById(R.id.button_1m_soil);
        Button buttonAllTimesoil = findViewById(R.id.button_alltime_soil);
        lineChartTemp = findViewById(R.id.lineChartTemp);
        lineChartHumid = findViewById(R.id.lineChartHumid);
        lineChartSoil = findViewById(R.id.lineChartSoil);

        // Set up LineCharts individually
        humidLayout = findViewById(R.id.layout_humid);
        tempLayout = findViewById(R.id.layout_temperature);
        soilLayout = findViewById(R.id.layout_soilMoisture);
        humidLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        tempLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        soilLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        plantList = new ArrayList<>();
        plantDetailsList = new ArrayList<>();

        selectedPlantsCardView = findViewById(R.id.selectedPlantsCardView);
        selectedPlantsRecyclerView = findViewById(R.id.selectedPlantsRecyclerView);

        PlantDetailsLinearLayout_1 = findViewById(R.id.PlantDetailsCardView1);
        plantdetailslinelayout_2= findViewById(R.id.PlantDetailsCardView2);
        //textView water and sunlight
        textViewWatering = findViewById(R.id.watering_value_main_activity);
        textViewSunlight = findViewById(R.id.sunlight_textView);
        textViewCareLevel= findViewById(R.id.care_level_textView);
        textViewWateringPeriod = findViewById(R.id.Watering_period_textView);
        PlantDetailsLinearLayout_1.setVisibility(View.GONE);
        plantdetailslinelayout_2.setVisibility(View.GONE);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://smart-pot-1d7b5-default-rtdb.firebaseio.com/");
        DatabaseReference databaseReference1 = firebaseDatabase.getReference("sensor_data");
        DatabaseReference databaseReferenceSoil = databaseReference1.child("soil_moisture");
        DatabaseReference databaseReferenceTemp = databaseReference1.child("temperature");
        DatabaseReference databaseReferenceHumid = databaseReference1.child("humidity");

        TextView soilData = findViewById(R.id.soilData);
        TextView tempData = findViewById(R.id.tempData);
        TextView humidData = findViewById(R.id.humidData);


        // Initialize and start a background thread
        handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();

        // Initialize a handler associated with the background thread
        handler = new Handler(handlerThread.getLooper());
        // Fetch data from local SQL database
        fetchDataFromLocalDatabase();
        // Start fetching data periodically
        fetchData();
        databaseReferenceSoil.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {
                        Double soilMoistureValue = dataSnapshot.getValue(Double.class);
                        if (soilMoistureValue != null) {
                            soilData.setText(String.valueOf(soilMoistureValue));
                        } else {
                            // Handle the case where data is null
                            soilData.setText("No data available");
                        }
                    } catch (DatabaseException e) {
                        soilData.setText("invalid data type");
                    }
                } else {
                    // Handle the case where data doesn't exist
                    soilData.setText("NaN");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                soilData.setText("Error");
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        databaseReferenceTemp.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {
                        Double tempValue = dataSnapshot.getValue(Double.class);
                        if (tempValue != null) {
                            if(!tempMode){
                                tempData.setText(String.valueOf(tempValue));
                            }
                            else {
                                float temper = tempValue.floatValue();
                                temper = (float) (temper*1.8 + 32);
                                tempData.setText(String.valueOf(temper));
                            }

                        } else {
                            // Handle the case where data is null
                            tempData.setText("No data available");
                        }
                    } catch (DatabaseException e) {
                        tempData.setText("invalid data type");
                    }
                } else {
                    // Handle the case where data doesn't exist
                    tempData.setText("NaN");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                tempData.setText("Error");
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        databaseReferenceHumid.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {
                        Double humidValue = dataSnapshot.getValue(Double.class);
                        if (humidValue != null) {
                            humidData.setText(String.valueOf(humidValue));
                        } else {
                            // Handle the case where data is null
                            humidData.setText("No data available");
                        }
                    } catch (DatabaseException e) {
                        humidData.setText("invalid data type");
                    }
                } else {
                    // Handle the case where data doesn't exist
                    humidData.setText("NaN");
                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                humidData.setText("Error");
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        //Navigation Menu
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.list_plants) {
                Intent intent = new Intent(MainActivity.this, plantListActivity.class);
                intent.putParcelableArrayListExtra("plantList", new ArrayList<>(plantList));
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                drawerLayout.closeDrawer(GravityCompat.START);
            } else if (id == R.id.item_5) {
                openSettings();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }else if (id == R.id.item_2){
                Intent intent = new Intent(MainActivity.this,Your_Plant_Activity.class);
                intent.putParcelableArrayListExtra("plantList",new ArrayList<>(plantList));
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return false;
        });

        TextView addTextView = findViewById(R.id.add_text);
        TextView removeTextView = findViewById(R.id.remove_text);
        addTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddPlantsActivity.class);
            intent.putParcelableArrayListExtra("plantList", new ArrayList<>(plantList));
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        removeTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RemovePlantsActivity.class);
            intent.putParcelableArrayListExtra("plantList",new ArrayList<>(plantList));
            startActivity(intent);
            overridePendingTransition(R.anim.zoom_in,R.anim.zoom_out);
            drawerLayout.closeDrawer(GravityCompat.START);
        });
        button1dHumid.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateHumidityChart("1d", currentWateringValue);
        });
        button3dHumid.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateHumidityChart("3d", currentWateringValue);
        });
        button1wHumid.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateHumidityChart("1w", currentWateringValue);
        });
        button1mHumid.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateHumidityChart("1m", currentWateringValue);
        });
        buttonAllTimeHumid.setOnClickListener(v -> {
            isTimeFrameSelected = false;
            updateHumidityChart("all", currentWateringValue);
        });
        button1dtemp.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateTemperatureChart("1d", currentSunlightValue);
        });
        button3dtemp.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateTemperatureChart("3d", currentSunlightValue);
        });
        button1wtemp.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateTemperatureChart("1w", currentSunlightValue);
        });
        button1mtemp.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateTemperatureChart("1m", currentSunlightValue);
        });
        buttonAllTimetemp.setOnClickListener(v -> {
            isTimeFrameSelected = false;
            updateTemperatureChart("all", currentSunlightValue);
        });
        button1dsoil.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateSoilChart("1d", currentSoilMoistureValue);
        });
        button3dsoil.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateSoilChart("3d", currentSoilMoistureValue);
        });
        button1wsoil.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateSoilChart("1w", currentSoilMoistureValue);
        });
        button1msoil.setOnClickListener(v -> {
            isTimeFrameSelected = true;
            updateSoilChart("1m", currentSoilMoistureValue);
        });
        buttonAllTimesoil.setOnClickListener(v -> {
            isTimeFrameSelected = false;
            updateSoilChart("all", currentSoilMoistureValue);
        });
    }
    private List<String> extractTimestamps(List<Measurements> filteredData) {
        List<String> timestamps = new ArrayList<>();
        for (Measurements measurement : filteredData) {
            timestamps.add(measurement.getTimestamps());
        }
        return timestamps;
    }
    private List<Entry> convertToEntriesForHumidity(List<Measurements> filteredData) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < filteredData.size(); i++) {
            Measurements measurement = filteredData.get(i);
            entries.add(new Entry(i, measurement.getHumidity())); // Using index as X-value
        }
        return entries;
    }
    private List<Entry> convertToEntriesForTemperature(List<Measurements> filteredData) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < filteredData.size(); i++) {
            Measurements measurement = filteredData.get(i);
            entries.add(new Entry(i, measurement.getTemperature())); // Using index as X-value
        }
        return entries;
    }
    private List<Entry> convertToEntriesForSoilMoisture(List<Measurements> filteredData) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < filteredData.size(); i++) {
            Measurements measurement = filteredData.get(i);
            entries.add(new Entry(i, measurement.getSoil_moisture())); // Using index as X-value
        }
        return entries;
    }
    private int currentWateringValue = -9999;
    private int currentSunlightValue = -9999;
    private int currentSoilMoistureValue = -9999;
    private void startFetchingDataFromPlant(PlantData plantData) {
        PlantData topItemPlantData = selectedPlantsAdapter.getTopItem();
        if (topItemPlantData != null) {
            int currentPLantId = plantData.getId();
            currentWateringValue = plantData.convertWateringToValue();
            String currentWateringString = plantData.getWatering();
            currentSunlightValue = plantData.convertSunlightToValue();
            currentSoilMoistureValue = plantData.convertWateringToSoilMoisture();
            ReturnPlantDetails(currentPLantId);
            updateCardViewDetails(currentWateringString,String.valueOf(currentSunlightValue));
            Log.d("Selected Plant Details", "Watering Value: " + currentWateringValue);
            Log.d("Selected Plant Details", "Sunlight Value: " + currentSunlightValue);
            Log.d("Selected Plant Details", "Soil Moisture Value: " + currentSoilMoistureValue);
            startPeriodicDataFetch(); // Start fetching data with updated values
        }
    }
    private void startFetchingDataWithDefaults() {
        currentWateringValue = -9999;
        currentSunlightValue = -9999;
        currentSoilMoistureValue = -9999;
        startPeriodicDataFetch();
    }
    private boolean isWithinPeriod(String dataTimestampString, Date latestTimestamp, String period) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date dataTimestamp;
        try {
            dataTimestamp = dateFormat.parse(dataTimestampString);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(latestTimestamp);
        switch (period) {
            case "1d":
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case "3d":
                calendar.add(Calendar.DAY_OF_MONTH, -3);
                break;
            case "1w":
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                break;
            case "1m":
                calendar.add(Calendar.MONTH, -1);
                break;
            case "all":
                return true;
            default:
                return false;
        }
        Date startDate = calendar.getTime();
        return (Objects.requireNonNull(dataTimestamp).equals(startDate) || dataTimestamp.after(startDate)) &&
                (dataTimestamp.equals(latestTimestamp) || dataTimestamp.before(latestTimestamp));
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        sharedPreferences_tempvalue = getSharedPreferences("MODE_TEMPVALUE", Context.MODE_PRIVATE);
        temperaTure = sharedPreferences_tempvalue.getString("temperaTure", "");
        sharedPreferences_tempmode = getSharedPreferences("MODE_TEMP", Context.MODE_PRIVATE);
        tempMode = sharedPreferences_tempmode.getBoolean("tempMode", false);

        TextView tempData = findViewById(R.id.tempData);
        tempData.setText(temperaTure);
        TextView tempUnit = findViewById(R.id.tempUnit);
        if(tempMode){
            tempUnit.setText("°F");
        }else {
            tempUnit.setText("°C");
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openSettings() {
        TextView textView = findViewById(R.id.tempData);
        String tex = textView.getText().toString();
        Intent intent = new Intent(MainActivity.this, setting.class);
        intent.putExtra("message", tex);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandHumid(View view) {
        expand(lineChartHumid, humidLayout,buttonsHumid,buttonsTemp,buttonsSoil, lineChartTemp, lineChartSoil);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandTemp(View view) {
        expand(lineChartTemp, tempLayout,buttonsTemp,buttonsHumid,buttonsSoil,lineChartHumid, lineChartSoil);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandSoil(View view) {
        expand(lineChartSoil, soilLayout,buttonsSoil,buttonsHumid,buttonsTemp, lineChartHumid, lineChartTemp);
    }

    private void expand(LineChart clickedText, LinearLayout clickedLayout,LinearLayout buttonLayout,LinearLayout otherLinear, LinearLayout otherLinear2, LineChart... otherTexts) {
        if (clickedText.getVisibility() == View.VISIBLE) {
            clickedText.setVisibility(View.GONE);
            buttonLayout.setVisibility(View.GONE);
        } else {
            clickedText.setVisibility(View.VISIBLE);
            buttonLayout.setVisibility(View.VISIBLE);
            // Set other details to GONE
            for (LineChart otherText : otherTexts) {
                otherText.setVisibility(View.GONE);
                otherLinear.setVisibility(View.GONE);
                otherLinear2.setVisibility(View.GONE);
            }
        }
        TransitionManager.beginDelayedTransition(clickedLayout, new AutoTransition());
    }
    private void startPeriodicDataFetch() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isTimeFrameSelected) {
                    updateUIBasedOnCurrentPeriod();
                }
                handler.postDelayed(this, 2000); // Reschedule after 2 seconds
            }
        }, 0);
    }
    private void updateUIBasedOnCurrentPeriod() {
        ApiServiceDatabase apiService = RetrofitDatabase.getApiService();
        Call<List<Measurements>> call = apiService.fetchData();
        Log.d("MainActivity", "API call started");

        call.enqueue(new Callback<List<Measurements>>() {
            @Override
            public void onResponse(@NonNull Call<List<Measurements>> call, @NonNull Response<List<Measurements>> response) {
                Log.d("MainActivityDatabase", "API call completed");
                if (response.isSuccessful() && response.body() != null) {
                    allData = response.body();
                    updateCharts();
                } else {
                    Log.e("MainActivityDatabase", "Data loading failed. Error code: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Measurements>> call, @NonNull Throwable t) {
                Log.e("MainActivityDatabase", "Data loading failed. Exception: " + t.getMessage());
            }
        });
    }
    private List<Measurements> filterDataByPeriod(String period, Date latestTimestamp) {
        return allData.stream()
                .filter(measurement -> isWithinPeriod(measurement.getTimestamps(), latestTimestamp, period))
                .collect(Collectors.toList());
    }
    private void updateCharts() {
        List<Measurements> dataToShow;
        Date latestTimestamp = findLatestTimestamp(allData);

        if (isTimeFrameSelected) {
            // If a specific time frame is selected, filter the data accordingly
            String currentPeriod = "all";
            dataToShow = filterDataByPeriod(currentPeriod, latestTimestamp);
        } else {
            // If no specific time frame is selected, show all measurements
            dataToShow = allData;
        }

        // Extract and convert data for each type (humidity, temperature, soil moisture)
        List<String> timestamps = extractTimestamps(dataToShow);
        List<Entry> humidityEntries = convertToEntriesForHumidity(dataToShow);
        List<Entry> temperatureEntries = convertToEntriesForTemperature(dataToShow);
        List<Entry> soilMoistureEntries = convertToEntriesForSoilMoisture(dataToShow);

        // Update each line chart
        updateLineChart(lineChartHumid, humidityEntries, "Humidity", timestamps, currentWateringValue, 0, 130);
        updateLineChart(lineChartTemp, temperatureEntries, "Temperature", timestamps, currentSunlightValue, 0, 40);
        updateLineChart(lineChartSoil, soilMoistureEntries, "Soil Moisture", timestamps, currentSoilMoistureValue, 400, 1300);
    }
    private void updateHumidityChart(String period, int wateringValue) {
        Date latestTimestamp = findLatestTimestamp(allData); // Find the latest timestamp
        List<Measurements> filteredData = filterDataByPeriod(period, latestTimestamp);
        List<Entry> entries = convertToEntriesForHumidity(filteredData);
        List<String> timestamps = extractTimestamps(filteredData);
        updateLineChart(lineChartHumid, entries, "Humidity", timestamps, wateringValue, 0, 130);
    }
    private void updateTemperatureChart(String period, int temperatureValue) {
        Date latestTimestamp = findLatestTimestamp(allData); // Find the latest timestamp
        List<Measurements> filteredData = filterDataByPeriod(period, latestTimestamp);
        List<Entry> entries = convertToEntriesForTemperature(filteredData);
        List<String> timestamps = extractTimestamps(filteredData);
        updateLineChart(lineChartTemp, entries, "Temperature", timestamps, temperatureValue, 0, 40);
    }
    private void updateSoilChart(String period, int soilMoistureValue) {
        Date latestTimestamp = findLatestTimestamp(allData); // Find the latest timestamp
        List<Measurements> filteredData = filterDataByPeriod(period, latestTimestamp);
        List<Entry> entries = convertToEntriesForSoilMoisture(filteredData);
        List<String> timestamps = extractTimestamps(filteredData);
        updateLineChart(lineChartSoil, entries, "Temperature", timestamps, soilMoistureValue, 400, 1300);
    }
    private Date findLatestTimestamp(List<Measurements> allData) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date latestTimestamp = null;

        for (Measurements measurement : allData) {
            try {
                Date timestamp = dateFormat.parse(measurement.getTimestamps());
                if (latestTimestamp == null || (timestamp != null && timestamp.after(latestTimestamp))) {
                    latestTimestamp = timestamp;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return latestTimestamp;
    }

    private void ReturnPlantDetails(int plantId){
        for (plantDetailsObject plantItem : plantDetailsList){
            if (plantItem.getId()== plantId){
                String care_level = plantItem.getCare_level();
                String watering_period = plantItem.getWatering_period();
                Log.d("Watering_period","watering_period:"+watering_period);
                textViewCareLevel.setText(care_level);
                if (watering_period !=null){
                    textViewWateringPeriod.setText(watering_period);
                }
            }
        }
    }
    private void TurnPlantDetailsIntoList(){
        //String apiKey = "sk-gAIS6560794454fbf2885";   // Quy's API key
        //String apiKey     = "sk-O0QK655e2575b0b303082";   // Nguyen Main
        String apiKey     = "sk-JAdj65704f90038483358";   // Nguyen 2nd
        //String apiKey     = "sk-PEwA657057073ee313360";   // Quy 2nd
        //String apiKey = "sk-V27h658e9a807e9213607"; // Quy 3rd
        //String apiKey = "sk-yMXy658e9fa1e97613609"; // Quy 4rd
        SharedPreferences sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences", MODE_PRIVATE);
        Set<String> selectedPlantIdsStringSet = sharedPreferences.getStringSet("selected_plants", new HashSet<>());
        Set<Integer> selectedPlantIds = new HashSet<>();
        for (String id : selectedPlantIdsStringSet) {
            selectedPlantIds.add(Integer.valueOf(id));
        }
        if (plantDetailsList.isEmpty()){
            for (int id : selectedPlantIds){
                fetchPlantDetails(id,apiKey,plantDetailsList);
            }
        }else {
            for (int id : selectedPlantIds){
                if (!isContained(plantDetailsList,id)){
                    fetchPlantDetails(id,apiKey,plantDetailsList);
                }
            }
        }
    }
    private boolean isContained(List<plantDetailsObject> plantDetailsList, int id){
        for (plantDetailsObject plantItems : plantDetailsList){
            if (plantItems.getId() == id){
                return true;
            }
        }
        return false;
    }
    private void fetchPlantDetails(int plantId, String apiKey, List<plantDetailsObject> objectList){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://perenual.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        JSONPlaceHolder jsonPlaceHolder = retrofit.create(JSONPlaceHolder.class);
        Call<PlantDetailsResponse> call = jsonPlaceHolder.getPlantDetails(plantId, apiKey);
        call.enqueue(new Callback<PlantDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlantDetailsResponse> call, @NonNull Response<PlantDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlantDetailsResponse plantDetailsResponse = response.body();
                    int plant_id = plantDetailsResponse.getId();
                    String care_level = plantDetailsResponse.getCare_level();
                    String watering_period = plantDetailsResponse.getWatering_period();
                    plantDetailsObject plantDetailsObjectCheck = new plantDetailsObject(plant_id, care_level,watering_period);
                    objectList.add(plantDetailsObjectCheck);
                }else{
                    Log.e("PlantDetails", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<PlantDetailsResponse> call, @NonNull Throwable t) {
                Log.e("PlantDetails", "Error: " + t.getMessage());
            }
        });
    }

    private void updateCardViewDetails(String wateringValueStr, String sunlightValue) {
        textViewWatering.setText(wateringValueStr);
        textViewSunlight.setText(sunlightValue);
    }


    private void fetchDataFromLocalDatabase() {
        ApiServiceDatabase apiService = RetrofitDatabase.getApiService();
        Call<List<Measurements>> call = apiService.fetchData();
        call.enqueue(new Callback<List<Measurements>>() {
            @Override
            public void onResponse(@NonNull Call<List<Measurements>> call, @NonNull Response<List<Measurements>> response) {
                if (response.isSuccessful()) {
                    List<Measurements> data = response.body(); // THis variable is not used anywhere. DELETE?
                    Log.d("SoilActivity", "Data loading successfully");
                } else {
                    // Handle the case when the response is not successful
                    Log.e("SoilActivity", "Data loading failed. Error code: " + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Measurements>> call, @NonNull Throwable t) {
                // Handle the case when the network request fails
                Log.e("SoilActivity", "Data loading failed. Exception: " + t.getMessage());
            }
        });
    }
    private void updateLineChart(LineChart lineChart, List<Entry> entries, String label, List<String> timestamps, int Value, int minValue, int maxValue) {
        Collections.reverse(timestamps);
        int dataSize = entries.size();
        final List<Entry> finalEntries = new ArrayList<>(entries);
        final List<String> finalTimestamps = new ArrayList<>(timestamps);
        LineDataSet dataSet = new LineDataSet(finalEntries, label);
        LineData lineData = new LineData(dataSet);
        XAxis xAxis = lineChart.getXAxis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                try {
                    int index = (int) value;
                    int dataIndex = dataSize - index; // Calculate the actual index in the data list
                    if (dataIndex >= 0 && dataIndex < finalTimestamps.size()) {
                        String currentTimestamp = finalTimestamps.get(dataIndex);
                        SimpleDateFormat timeFormat;
                        timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        Date date = dateFormat.parse(currentTimestamp);
                        return timeFormat.format(Objects.requireNonNull(date));
                    } else {
                        Log.e("MainActivityGraph", "Index out of bounds: " + dataIndex);
                        return "";
                    }
                } catch (ParseException e) {
                    Log.e("MainActivityGraph", "Error parsing timestamp: " + e.getMessage());
                    return "";
                } catch (Exception e) {
                    Log.e("MainActivityGraph", "An unexpected error occurred: " + e.getMessage());
                    return "";
                }
            }
        });

        float overallAverage = calculateOverallAverage(finalEntries);
        // Dynamically set the limit line color based on the overall average
        Drawable chartBackground = determineChartBackgroundColor(overallAverage, Value);
        int graphColor = determineGraphColor(overallAverage, Value);
        YAxis leftAxis = lineChart.getAxisRight();
        leftAxis.removeAllLimitLines();

        // Add a LimitLine for the watering value
        LimitLine limitLine = new LimitLine(Value, "Average Level");
        limitLine.setTextColor(getResources().getColor(R.color.itemnavi));
        limitLine.setLineWidth(2f);
        limitLine.setLineColor(Color.RED);
        leftAxis.addLimitLine(limitLine);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90f);
        xAxis.setTextColor(getResources().getColor(R.color.itemnavi));
        leftAxis.setTextColor(getResources().getColor(R.color.itemnavi));
        YAxis rightAxis = lineChart.getAxisLeft();
        rightAxis.setEnabled(false);
        dataSet.setLineWidth(1f); // Line width
        dataSet.setDrawCircles(false); // Do not draw circles on data points
        lineChart.getDescription().setEnabled(false); // Disable description label
        lineChart.getLegend().setEnabled(false); // Disable legend
        lineChart.setDrawGridBackground(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        dataSet.setDrawValues(false);
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER); // Smooth line
        dataSet.setDrawFilled(true);
        dataSet.setFillDrawable(chartBackground);
        dataSet.setColor(graphColor);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }
    private float calculateOverallAverage(List<Entry> entries) {
        float sum = 0f;
        for (Entry entry : entries) {
            sum += entry.getY();
        }
        return sum / entries.size();
    }
    private Drawable determineChartBackgroundColor(float overallAverage, int limitValue) {
        float threshold = 15f; // You can adjust this threshold based on your requirements

        // Determine the color based on the overall average
        if (overallAverage <= limitValue + threshold && overallAverage >= limitValue - threshold) {
            // If overall average is within the threshold, use green gradient
            return ContextCompat.getDrawable(this, R.drawable.gradient_green);

        }else if (limitValue == -9999){
            return ContextCompat.getDrawable(this, R.drawable.gradient_neutral);
        }
        else {
            // If overall average exceeds the threshold, use red gradient
            return ContextCompat.getDrawable(this, R.drawable.gradient_red);
        }
    }
    private int determineGraphColor(float overallAverage, int limitValue) {
        float threshold = 15f; // You can adjust this threshold based on your requirements

        // Determine the color based on the overall average
        if (overallAverage <= limitValue + threshold && overallAverage >= limitValue - threshold) {
            // If overall average is within the threshold, use green color
            return ContextCompat.getColor(this,R.color.leaf);
        } else if (limitValue == -9999){
            return ContextCompat.getColor(this, R.color.neutral);
        }else {
            // If overall average exceeds the threshold, use red color
            return ContextCompat.getColor(this,R.color.lightRed);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks when the activity is destroyed
        handler.removeCallbacksAndMessages(null);
        // Quit the background thread
        handlerThread.quit();
    }

    private void fetchData() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://perenual.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        JSONPlaceHolder jsonPlaceHolder = retrofit.create(JSONPlaceHolder.class);


        //String apiKey = "sk-gAIS6560794454fbf2885";   // Quy's API key
        //String apiKey     = "sk-O0QK655e2575b0b303082";   // Nguyen Main
        String apiKey     = "sk-JAdj65704f90038483358";   // Nguyen 2nd
        //String apiKey     = "sk-PEwA657057073ee313360";   // Quy 2nd
        //String apiKey = "sk-V27h658e9a807e9213607"; // Quy 3rd
        //String apiKey = "sk-yMXy658e9fa1e97613609"; // Quy 4rd
        boolean isDataFetched = false;
        if (!isDataFetched) {
                // Fetch data only if it hasn't been fetched yet
                fetchDatafromMultiplePages(jsonPlaceHolder, apiKey, 1);
                TurnPlantDetailsIntoList();
            }

    }

    private void fetchDatafromMultiplePages(JSONPlaceHolder jsonPlaceHolder, String apiKey, int pageNumber) {
        Call<PlantResponse> call = jsonPlaceHolder.getData(apiKey, pageNumber);
        call.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlantResponse> call, @NonNull Response<PlantResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlantResponse plantResponse = response.body();
                    plantList.addAll(plantResponse.getPlantDataList());
                    // Pass data to Activity
                    if (pageNumber < 2) {
                        fetchDatafromMultiplePages(jsonPlaceHolder, apiKey, pageNumber + 1);
                    } else {
                        updateSelectedPlantsCard(plantList);
                    }
                } else {
                    Log.e("PlantList", "Error" + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<PlantResponse> call, @NonNull Throwable t) {
                Log.d("error", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
    private void updateSelectedPlantsCard(List<PlantData> plantList) {
        SharedPreferences sharedPreferences = getSharedPreferences("ID_Plants_Save_Preferences", MODE_PRIVATE);
        Set<String> selectedPlantIdsStringSet = sharedPreferences.getStringSet("selected_plants", new HashSet<>());

        // Convert String set to Set<Integer>
        Set<Integer> selectedPlantIds = new HashSet<>();
        for (String id : selectedPlantIdsStringSet) {
            selectedPlantIds.add(Integer.valueOf(id));
        }

        // Check if there are selected plants
        if (!selectedPlantIds.isEmpty()) {
            // Update the CardView with the details of the last selected plant
            int lastSelectedPlantId = getLastSelectedPlantId(selectedPlantIds);
            PlantData lastSelectedPlant = getPlantDataById(lastSelectedPlantId, plantList);
            if (lastSelectedPlant != null) {
                allSelectedPlants = getAllSelectedPlants(sharedPreferences);
                selectedPlantsAdapter = new SelectedPlantsAdapter(allSelectedPlants, this);
                selectedPlantsCardView.setVisibility(View.VISIBLE);
                PlantDetailsLinearLayout_1.setVisibility(View.VISIBLE);
                plantdetailslinelayout_2.setVisibility(View.VISIBLE);
                selectedPlantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                selectedPlantsRecyclerView.setAdapter(selectedPlantsAdapter);
                arrowImageView.setOnClickListener(v -> selectedPlantsAdapter.toggleRecyclerViewVisibility(arrowImageView, selectedPlantsRecyclerView));
                startFetchingDataFromPlant(lastSelectedPlant);

            }
        }else
        {
            startFetchingDataWithDefaults();
            // If no plants are selected, hide the CardView
            selectedPlantsCardView.setVisibility(View.GONE);

        }
    }



    // New method to start fetching data with default values when no plant is selected

    private PlantData getPlantDataById(int plantId, List<PlantData> plantList) {
        if (plantList == null || plantList.isEmpty()) {
            Log.d("Selected Plant Details" ,"Null " + Objects.requireNonNull(plantList).size());
            return null;
        }
        else Log.d("Selected Plant Details" ,"Not Null " + plantList.size());
        for (PlantData plantData : plantList) {
            if (plantData.getId() == plantId) {
                String commonName = plantData.getCommon_name();
                Log.d("Selected Plant Details", "ID: " + plantId);
                Log.d("Selected Plant Details", "Common Name: " + commonName);
                return plantData; // Return the entire PlantData object if needed
            }
        }
        // Plant with the specified ID not found
        Log.e("Selected Plant Details", "Plant with ID " + plantId + " not found");
        return null;
    }

    private int getLastSelectedPlantId(Set<Integer> selectedPlantIds) {
        return selectedPlantIds.isEmpty() ? -1 : Collections.max(selectedPlantIds);
    }
    private List<PlantData> getAllSelectedPlants(SharedPreferences sharedPreferences) {
        Set<String> selectedPlantIdsStringSet = sharedPreferences.getStringSet("selected_plants", new HashSet<>());
        List<PlantData> selectedPlants = new ArrayList<>();
        for (String id : selectedPlantIdsStringSet) {
            int plantId = Integer.parseInt(id);
            PlantData plantData = getPlantDataById(plantId, plantList);
            if (plantData != null) {
                selectedPlants.add(plantData);
            }
        }
        return selectedPlants;
    }
    @Override
    public void onItemClick(int position) {
        if (allSelectedPlants != null && selectedPlantsAdapter != null && position < allSelectedPlants.size()) {
            Collections.swap(allSelectedPlants, position, 0);
            selectedPlantsAdapter.notifyItemMoved(position, 0);
            PlantData topItemPlantData = selectedPlantsAdapter.getPlantDataAtPosition(0);
            if (topItemPlantData != null) {
                int wateringValue = topItemPlantData.convertWateringToValue();
                int sunlightValue = topItemPlantData.convertSunlightToValue();
                Log.d("Selected Plant Details", "Watering: " + wateringValue);
                Log.d("Selected Plant Details", "Sunlight: " + sunlightValue);
            }
        } else if (allSelectedPlants == null) {
            Log.d("Selected Plant Details", "All selected plant is null");
        } else if (selectedPlantsAdapter == null) {
            Log.d("Selected Plant Details", "Selected Plant Adapter is null");
        }
    }
    @Override
    public void onBackPressed() {
        if (exitConfirmationShown) {
            // If exit confirmation is already shown, perform default behavior
            super.onBackPressed();
        } else {
            // Show exit confirmation
            showExitConfirmation();
        }
    }
    private void showExitConfirmation() {
        // Add code to show your exit confirmation dialog
        // Example: You can use an AlertDialog to prompt the user
        new AlertDialog.Builder(this)
                .setTitle("Exit Confirmation")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    exitConfirmationShown = true;
                    finish(); // Terminate the activity
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // User clicked "No," do nothing or dismiss the dialog
                })
                .show();
    }
}