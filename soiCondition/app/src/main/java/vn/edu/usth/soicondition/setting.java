package vn.edu.usth.soicondition;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.annotation.SuppressLint;

import android.content.Context;

import android.content.SharedPreferences;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class setting extends AppCompatActivity {
    SwitchCompat lightswitch, tempswitch;
    boolean nightMode, tempMode;
    String tempValue, temperaTure;
    private String currentSelectedLanguage = "English";
    private ImageView arrowImageView;
    int lang;
    SharedPreferences sharedPreferences, sharedPreferences_tempvalue, sharedPreferences_tempmode, langspinPreference, whatlangPreference;
    SharedPreferences.Editor editor, editor_tempvalue, editor_mode, editlang, edit_whatlang;
    private Button CelButton, FahButton;
    private LanguageAdapter languageAdapter;
    private List<String> languageList = new ArrayList<>(Arrays.asList("English", "Vietnamese", "France"));
    private boolean isCardViewExpanded = false;
    private CardView cardViewLanguage;
    private RecyclerView recyclerViewLanguages;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        CelButton = findViewById(R.id.CelciusButton);
        FahButton = findViewById(R.id.FahrenheitButton);
        lightswitch = findViewById(R.id.lighswitch);
        arrowImageView = findViewById(R.id.arrowImageView);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);
        cardViewLanguage = findViewById(R.id.cardViewLanguage);
        arrowImageView.setOnClickListener(v -> toggleCardView());

        setupLanguageRecyclerView();

        lightswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                editor = sharedPreferences.edit();
                editor.putBoolean("nightMode", isChecked);
                editor.apply();
            }
        });

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
        langspinPreference = getSharedPreferences("Lang", Context.MODE_PRIVATE);
        lang = langspinPreference.getInt("lang", 0);
        whatlangPreference = getSharedPreferences("whatlang", Context.MODE_PRIVATE);

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
    private void setupLanguageRecyclerView() {
        recyclerViewLanguages = findViewById(R.id.recyclerViewLanguages);
        recyclerViewLanguages.setLayoutManager(new LinearLayoutManager(this));
        // Initially, only show the selected language (English)
        languageAdapter = new LanguageAdapter(this, new String[] {"English"}, this::onLanguageSelected);
        recyclerViewLanguages.setAdapter(languageAdapter);
    }
    private void toggleCardView() {
        if (isCardViewExpanded) {
            collapseLanguageList();
        } else {
            expandLanguageList();
        }
        rotateArrow(isCardViewExpanded ? 0 : 180); // Rotate the arrow
        isCardViewExpanded = !isCardViewExpanded;
    }

    private void collapseLanguageList() {
        languageAdapter.setLanguages(Collections.singletonList(currentSelectedLanguage));
        recyclerViewLanguages.getAdapter().notifyDataSetChanged();
    }

    private void expandLanguageList() {
        // Update to show all languages, with the selected language at the top
        List<String> allLanguages = new ArrayList<>(Arrays.asList("English", "Vietnamese", "France"));
        allLanguages.remove(currentSelectedLanguage);
        allLanguages.add(0, currentSelectedLanguage);
        languageAdapter.setLanguages(allLanguages);
        recyclerViewLanguages.getAdapter().notifyDataSetChanged();
    }
    private void rotateArrow(float degrees) {
        arrowImageView.animate().rotation(degrees).setDuration(300).start();
    }
    private void onLanguageSelected(String language, int position) {
        currentSelectedLanguage = language; // Update the current selected language

        // Move the selected language to the top of the language list
        if (position != 0) {
            languageList.remove(language);
            languageList.add(0, language);
            languageAdapter.notifyItemMoved(position, 0);
        }

        // Update the adapter with only the selected language
        languageAdapter.setLanguages(Collections.singletonList(currentSelectedLanguage));
        recyclerViewLanguages.getAdapter().notifyDataSetChanged();

        // Check if the CardView is expanded, and collapse it
        if (isCardViewExpanded) {
            toggleCardView();
        }
    }
    private void setAppLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        // Store the language preferences
        SharedPreferences.Editor editor_lang = langspinPreference.edit();
        SharedPreferences.Editor editor_whatlang = whatlangPreference.edit();

        editor_whatlang.putString("whatlang", language);

        switch (language) {
            case "en":
                editor_lang.putInt("lang", 0);
                break;
            case "fr":
                editor_lang.putInt("lang", 1);
                break;
            case "vn":
                editor_lang.putInt("lang", 2);
                break;
        }

        editor_lang.apply();
        editor_whatlang.apply();

        // Notify the system to apply the language change
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


