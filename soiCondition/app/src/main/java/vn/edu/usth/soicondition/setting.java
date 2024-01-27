package vn.edu.usth.soicondition;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;

import android.content.Context;

import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

public class setting extends AppCompatActivity {
    SwitchCompat lightswitch, tempswitch;
    boolean nightMode, tempMode;
    String tempValue, temperaTure, whatlang;
    private ImageView arrowImageView;
    int langspin;
    SharedPreferences sharedPreferences, sharedPreferences_tempvalue, sharedPreferences_tempmode, langspinPreference, whatlangPreference;
    SharedPreferences.Editor editor, editor_tempvalue, editor_mode, edit_langspin, edit_whatlang;
    private Button CelButton, FahButton;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle("Settings");
        }
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
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("nightMode", true);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                editor.apply();
            }
            recreate();
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        tempValue = getIntent().getStringExtra("message");
        sharedPreferences_tempvalue = getSharedPreferences("MODE_TEMPVALUE", Context.MODE_PRIVATE);
        temperaTure = sharedPreferences_tempvalue.getString("temperaTure", "");
        sharedPreferences_tempmode = getSharedPreferences("MODE_TEMP", Context.MODE_PRIVATE);
        tempMode = sharedPreferences_tempmode.getBoolean("tempMode", false);

        langspinPreference = getSharedPreferences("SPINMODE", Context.MODE_PRIVATE);
        langspin = langspinPreference.getInt("langspin", 0);
        Spinner spinner = findViewById(R.id.spinner);

        if (!tempMode) {
            // Celsius is the current mode
            CelButton.setBackground(ContextCompat.getDrawable(setting.this, R.drawable.background_button_settings));
            FahButton.setBackground(ContextCompat.getDrawable(setting.this, R.drawable.background_button_settings_dark));
        }
        else {
            // Fahrenheit is the current mode
            FahButton.setBackground(ContextCompat.getDrawable(setting.this, R.drawable.background_button_settings));
            CelButton.setBackground(ContextCompat.getDrawable(setting.this, R.drawable.background_button_settings_dark));
        }

        CelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FahButton.setBackground(ContextCompat.getDrawable(setting.this, R.drawable.background_button_settings_dark));
                CelButton.setBackground(ContextCompat.getDrawable(setting.this, R.drawable.background_button_settings));
                convertTemperatureAndSave(false);
            }
        });
        FahButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CelButton.setBackground(ContextCompat.getDrawable(setting.this, R.drawable.background_button_settings_dark));
                FahButton.setBackground(ContextCompat.getDrawable(setting.this, R.drawable.background_button_settings));
                convertTemperatureAndSave(true);
            }

        });

        String[] courses = { "English", "France", "Vietnam" };
        ArrayAdapter<String> adapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, courses);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapt);
        spinner.setSelection(langspin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edit_langspin = langspinPreference.edit();
                edit_langspin.putInt("langspin", position);
                edit_langspin.apply();

                String langchance = spinner.getItemAtPosition(position).toString();
                langchance = langchance.substring(0, 2);
                langchance = langchance.toLowerCase();
                Toast.makeText(setting.this, "ahihi "  +position+ langchance, Toast.LENGTH_SHORT).show();
                edit_whatlang = whatlangPreference.edit();
                edit_whatlang.putString("whatlang", langchance);
                edit_whatlang.apply();
                //setAppLocale(langchance);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setAppLocale(String language) {
        //Locale locale = new Locale(language);
        //Locale.setDefault(locale);

        //Resources resources = getResources();
        //Configuration configuration = resources.getConfiguration();
        //configuration.setLocale(locale);
        //resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        //ConfigurationUtils.updateLocale(setting.this, locale);

        edit_whatlang = whatlangPreference.edit();
        edit_whatlang.putString("whatlang", language);
        edit_whatlang.apply();

        recreate();
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



