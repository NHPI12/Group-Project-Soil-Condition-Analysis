package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import vn.edu.usth.soicondition.network.TimeAxisValueFormatter;
import vn.edu.usth.soicondition.network.model.PlantData;
import vn.edu.usth.soicondition.network.model.PlantResponse;


public class MainActivity extends AppCompatActivity implements SelectedPlantsAdapter.OnItemClickListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private LineChart lineChartTemp, lineChartSoil, lineChartHumid;
    LinearLayout humidLayout, tempLayout, soilLayout;
    private HandlerThread handlerThread;
    private Handler handler;
    private List<PlantData> plantList;
    private List<PlantData> allSelectedPlants;
    private CardView selectedPlantsCardView;
    private RecyclerView selectedPlantsRecyclerView;
    private SelectedPlantsAdapter selectedPlantsAdapter;
    private ImageView arrowImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        selectedPlantsCardView = findViewById(R.id.selectedPlantsCardView);
        selectedPlantsRecyclerView = findViewById(R.id.selectedPlantsRecyclerView);
        arrowImageView = findViewById(R.id.ArrowSelectedPlant);

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
        // Start fetching data periodically
        fetchData();
        databaseReferenceSoil.addValueEventListener(new ValueEventListener() {
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                soilData.setText("Error");
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        databaseReferenceTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getValue() != null) {
                    try {
                        Double tempValue = dataSnapshot.getValue(Double.class);
                        if (tempValue != null) {
                            tempData.setText(String.valueOf(tempValue));
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                tempData.setText("Error");
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
        databaseReferenceHumid.addValueEventListener(new ValueEventListener() {
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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.list_plants) {
                Intent intent = new Intent(MainActivity.this, plantListActivity.class);
                intent.putParcelableArrayListExtra("plantList", new ArrayList<>(plantList));
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
            } else if (id == R.id.item_5) {
                openSettings();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }else if (id == R.id.item_2){
                Intent intent = new Intent(MainActivity.this,Your_Plant_Activity.class);
                intent.putParcelableArrayListExtra("plantList",new ArrayList<>(plantList));
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
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
        });
        removeTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RemovePlantsActivity.class);
            intent.putParcelableArrayListExtra("plantList",new ArrayList<>(plantList));
            startActivity(intent);
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
        Intent intent = new Intent(MainActivity.this, setting.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandHumid(View view) {
        expand(lineChartHumid, humidLayout, lineChartTemp, lineChartSoil);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandTemp(View view) {
        expand(lineChartTemp, tempLayout, lineChartHumid, lineChartSoil);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void expandSoil(View view) {
        expand(lineChartSoil, soilLayout, lineChartHumid, lineChartTemp);
    }
    private void expand(LineChart clickedText, LinearLayout clickedLayout, LineChart... otherTexts) {
        if (clickedText.getVisibility() == View.VISIBLE) {
            clickedText.setVisibility(View.GONE);
        } else {
            clickedText.setVisibility(View.VISIBLE);
            // Set other details to GONE
            for (LineChart otherText : otherTexts) {
                otherText.setVisibility(View.GONE);
            }
        }
        TransitionManager.beginDelayedTransition(clickedLayout, new AutoTransition());
    }
    private void startFetchingData(int wateringValue, int temperatureValue, int soilValue) {
        // Define a Runnable that fetches data and updates UI
        Runnable fetchDataRunnable = new Runnable() {
            @Override
            public void run() {
                List<Measurements> newData = fetchDataFromLocalDatabase();
                runOnUiThread(() -> updateUI(newData, wateringValue, temperatureValue, soilValue));

                // Schedule the next data fetch after 2 seconds
                handler.postDelayed(this, 2000);
            }
        };
        // Schedule the initial data fetch with a delay of 0 seconds
        handler.postDelayed(fetchDataRunnable, 0);
    }
    private void updateUI(List<Measurements> newData, int wateringValue, int temperatureValue, int soilValue) {
        ApiServiceDatabase apiService = RetrofitDatabase.getApiService();
        Call<List<Measurements>> call = apiService.fetchData();
        call.enqueue(new Callback<List<Measurements>>() {
            @Override
            public void onResponse(@NonNull Call<List<Measurements>> call, @NonNull Response<List<Measurements>> response) {
                if (response.isSuccessful()) {
                    List<Measurements> data = response.body();
                    List<String> timestamps = new ArrayList<>();
                    List<Float> humidityValues = new ArrayList<>();
                    List<Float> temperatureValues = new ArrayList<>();
                    List<Float> soilMoistureValues = new ArrayList<>();
                    ArrayList<String> xLabel = new ArrayList<>();

                    for (Measurements item : data) {
                        timestamps.add(item.getTimestamps());
                        xLabel.addAll(timestamps);
                        humidityValues.add(item.getHumidity());
                        temperatureValues.add(item.getTemperature());
                        soilMoistureValues.add(item.getSoil_moisture());
                    }
                    List<Entry> humidityEntries = TimeAxisValueFormatter.createEntryList(timestamps, humidityValues);
                    List<Entry> temperatureEntries = TimeAxisValueFormatter.createEntryList(timestamps, temperatureValues);
                    List<Entry> soilMoistureEntries = TimeAxisValueFormatter.createEntryList(timestamps, soilMoistureValues);

                    if (!timestamps.isEmpty()) {
                        // Update the line charts with the new data
                        updateLineChart(lineChartHumid, humidityEntries, "Humidity", timestamps, wateringValue);
                        updateLineChart(lineChartTemp, temperatureEntries, "Temperature", timestamps, temperatureValue);
                        updateLineChart(lineChartSoil, soilMoistureEntries, "Soil Moisture", timestamps, soilValue);
                    } else {
                        // Handle the case when there are no timestamps
                        Log.d("YourActivity", "No timestamps available for updating line charts");
                    }
                    Log.d("YourActivity", "Data loaded successfully: " + data.size() + " items");
                } else {
                    // Handle the case when the response is not successful
                    Log.e("SoilActivity", "Data loading failed. Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Measurements>> call, @NonNull Throwable t) {
                Log.e("NguActivity", "Data loading failed. Exception: " + t.getMessage());
            }
        });
    }
    private List<Measurements> fetchDataFromLocalDatabase() {
        ApiServiceDatabase apiService = RetrofitDatabase.getApiService();
        Call<List<Measurements>> call = apiService.fetchData();
        call.enqueue(new Callback<List<Measurements>>() {
            @Override
            public void onResponse(@NonNull Call<List<Measurements>> call, @NonNull Response<List<Measurements>> response) {
                if (response.isSuccessful()) {
                    List<Measurements> data = response.body();
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
        return null;
    }
    private void updateLineChart(LineChart lineChart, List<Entry> entries, String label, List<String> timestamps, int Value) {
        int maxDataPoints = 100;
        int dataSize = entries.size();
        if (dataSize > maxDataPoints) {
            entries = entries.subList(dataSize - maxDataPoints, dataSize);
            timestamps = timestamps.subList(dataSize - maxDataPoints, dataSize);
        }
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

                        // Parse timestamp and format time
                        Date date = dateFormat.parse(currentTimestamp);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        String formattedTime = timeFormat.format(date);

                        return formattedTime;
                    } else {
                        Log.e("YourActivity", "Index out of bounds: " + dataIndex);
                        return "";
                    }
                } catch (ParseException e) {
                    Log.e("YourActivity", "Error parsing timestamp: " + e.getMessage());
                    return "";
                } catch (Exception e) {
                    Log.e("YourActivity", "An unexpected error occurred: " + e.getMessage());
                    return "";
                }
            }
        });
        YAxis leftAxis = lineChart.getAxisRight();
        // Add a LimitLine for the watering value
        LimitLine limitLine = new LimitLine(Value, "Average Level");
        limitLine.setLineWidth(2f);
        limitLine.setLineColor(Color.RED);
        leftAxis.addLimitLine(limitLine); 
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90f);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove any pending callbacks when the activity is destroyed
        handler.removeCallbacksAndMessages(null);
        // Quit the background thread
        handlerThread.quit();
    }
    private void statusbarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.verybrown, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.verybrown));
        }
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
        String apiKey     = "sk-O0QK655e2575b0b303082";   // Nguyen Main
        //String apiKey     = "sk-JAdj65704f90038483358";   // Nguyen 2nd
        //String apiKey     = "sk-PEwA657057073ee313360";   // Quy 2nd
        //String apiKey = "sk-V27h658e9a807e9213607"; // Quy 3rd
        //String apiKey = "sk-yMXy658e9fa1e97613609"; // Quy 4rd
        boolean isDataFetched = false;
        if (!isDataFetched) {
                // Fetch data only if it hasn't been fetched yet
                fetchDatafromMultiplePages(jsonPlaceHolder, apiKey, 1);
            }
    }

    private void fetchDatafromMultiplePages(JSONPlaceHolder jsonPlaceHolder, String apiKey, int pageNumber) {
        Call<PlantResponse> call = jsonPlaceHolder.getData(apiKey, pageNumber);
        call.enqueue(new Callback<PlantResponse>() {
            @Override
            public void onResponse(@NonNull Call<PlantResponse> call, @NonNull Response<PlantResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PlantResponse plantResponse = response.body();
                    plantList = plantResponse.getPlantDataList();
                    updateSelectedPlantsCard(plantList);
                    // Pass data to Activity
                    if (pageNumber < 2) {
                        fetchDatafromMultiplePages(jsonPlaceHolder, apiKey, pageNumber + 1);
                    }
                } else {
                    Log.e("PlantList", "Error" + response.code());
                }
            }
            @Override
            public void onFailure(@NonNull Call<PlantResponse> call, @NonNull Throwable t) {
                Log.d("error", t.getMessage());
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
                selectedPlantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                selectedPlantsRecyclerView.setAdapter(selectedPlantsAdapter);
                arrowImageView.setOnClickListener(v -> selectedPlantsAdapter.toggleRecyclerViewVisibility(arrowImageView, selectedPlantsRecyclerView));
                PlantData topItemPlantData = selectedPlantsAdapter.getTopItem();
                if (topItemPlantData != null) {
                    // Now you can access watering and sunlight information
                    int wateringValue = topItemPlantData.convertWateringToValue();

                    // Convert sunlight to numerical value
                    int sunlightValue = topItemPlantData.convertSunlightToValue();
                    int soilMoistureValue = topItemPlantData.convertWateringToSoilMoisture();
                    // Use the information as needed
                    Log.d("Selected Plant Details", "Watering Before Clicked: " + wateringValue);
                    Log.d("Selected Plant Details", "Sunlight Before Clicked: " + sunlightValue);
                    Log.d("Selected Plant Details", "SoilMoisture Before Clicked: " + soilMoistureValue);
                    startFetchingData(wateringValue,sunlightValue,soilMoistureValue);
                }
            }
        } else {
            // If no plants are selected, hide the CardView
            selectedPlantsCardView.setVisibility(View.GONE);
        }
    }
    private PlantData getPlantDataById(int plantId, List<PlantData> plantList) {
        if (plantList == null || plantList.isEmpty()) {
            Log.d("Selected Plant Details" ,"Null " + plantList.size());
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

}