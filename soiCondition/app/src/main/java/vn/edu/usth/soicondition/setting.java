package vn.edu.usth.soicondition;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;


import android.annotation.SuppressLint;

import android.content.Context;

import android.content.SharedPreferences;

import android.os.Bundle;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Objects;

public class setting extends AppCompatActivity {
    SwitchCompat lightswitch, tempswitch;
    boolean nightMode, tempMode;
    String tempValue, temperaTure;
    int lang;
    SharedPreferences sharedPreferences, sharedPreferences_tempvalue, sharedPreferences_tempmode, langspinPreference, whatlangPreference;
    SharedPreferences.Editor editor, editor_tempvalue, editor_mode, editlang, edit_whatlang;
    private Button CelButton, FahButton;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CelButton = findViewById(R.id.CelciusButton);
        FahButton = findViewById(R.id.FahrenheitButton);
        lightswitch = findViewById(R.id.lighswitch);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (nightMode) {
            lightswitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        lightswitch.setOnClickListener(view -> {
            if (nightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit();
                editor.putBoolean("nightMode", false);

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("nightMode", true);

            }
            editor.apply();
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        tempValue = getIntent().getStringExtra("message");

        sharedPreferences_tempvalue = getSharedPreferences("MODE_TEMPVALUE", Context.MODE_PRIVATE);
        temperaTure = sharedPreferences_tempvalue.getString("temperaTure", "");
        sharedPreferences_tempmode = getSharedPreferences("MODE_TEMP", Context.MODE_PRIVATE);
        tempMode = sharedPreferences_tempmode.getBoolean("tempMode", false);
        if (!tempMode) {
            // Celsius is the current mode
            CelButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_settings));
            FahButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_settings_dark));
        } else {
            // Fahrenheit is the current mode
            FahButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_settings));
            CelButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_settings_dark));
        }
        /////lang
        Spinner spinner = findViewById(R.id.spinner);
        String[] lanGuage = {"ENG", "FRA", "VIE"};
        CustomArrayAdapter customArrayAdapter = new CustomArrayAdapter(setting.this, R.layout.spinner_list, lanGuage);
        spinner.setAdapter(customArrayAdapter);

        langspinPreference = getSharedPreferences("Lang", Context.MODE_PRIVATE);
        lang = langspinPreference.getInt("lang", 0);
        whatlangPreference = getSharedPreferences("whatlang", Context.MODE_PRIVATE);


        spinner.setSelection(lang);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                //Toast.makeText(setting.this, "Ahihi " + spinner.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                editlang = langspinPreference.edit();
                editlang.putInt("lang", i);
                editlang.apply();

                if (i == 0) {
                    //setAppLocale(setting.this,"en");
                    edit_whatlang = whatlangPreference.edit();
                    edit_whatlang.putString("whatlang", "en");

                } else if (i == 1) {
                    //setAppLocale(setting.this,"fr");
                    edit_whatlang = whatlangPreference.edit();
                    edit_whatlang.putString("whatlang", "fr");

                } else {
                    edit_whatlang = whatlangPreference.edit();
                    edit_whatlang.putString("whatland", "vn");
                }
                edit_whatlang.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                customArrayAdapter.setDropDownShown(true);
                v.performClick();
            }
            return false;
        });

        CelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FahButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_settings_dark));
                CelButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_settings));
                convertTemperatureAndSave(false);
            }
        });
        FahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CelButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_settings_dark));
                FahButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.background_button_settings));
                convertTemperatureAndSave(true);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This is called when the up/back button is pressed
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void convertTemperatureAndSave(boolean toFahrenheit) {
        // Assuming the original value is always in Celsius and stored in 'tempValue'
        float originalTemp = Float.parseFloat(tempValue);
        float convertedTemp;

        // Check if the current mode is already Fahrenheit
        if (toFahrenheit && !tempMode) {
            // Convert from Celsius to Fahrenheit
            convertedTemp = (originalTemp * 1.8f) + 32;
        } else if (!toFahrenheit && tempMode) {
            // Convert from Fahrenheit to Celsius
            convertedTemp = (originalTemp - 32) / 1.8f;
        } else {
            // No conversion needed if the current mode matches the desired mode
            convertedTemp = originalTemp;
        }

        convertedTemp = (float) Math.floor(convertedTemp * 10) / 10;

        editor_tempvalue = sharedPreferences_tempvalue.edit();
        editor_tempvalue.putString("temperaTure", String.valueOf(convertedTemp));
        editor_tempvalue.apply();

        editor_mode = sharedPreferences_tempmode.edit();
        editor_mode.putBoolean("tempMode", toFahrenheit);
        editor_mode.apply();
    }
}



