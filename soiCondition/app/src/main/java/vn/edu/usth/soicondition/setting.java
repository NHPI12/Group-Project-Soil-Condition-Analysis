package vn.edu.usth.soicondition;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Objects;

public class setting extends AppCompatActivity {
    SwitchCompat lightswitch, tempswitch;
    boolean nightMode, tempMode;
    int lang;
    SharedPreferences sharedPreferences, sharedPreferences_temp, langspinPreference;
    SharedPreferences.Editor editor, editor_temp, editlang;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

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
        //Tempunit
        tempswitch = findViewById(R.id.tempswitch);
        sharedPreferences_temp = getSharedPreferences("MODE_TEMP", Context.MODE_PRIVATE);
        tempMode = sharedPreferences_temp.getBoolean("tempMode", false);

        if (tempMode) {
            tempswitch.setChecked(true);
        }

        tempswitch.setOnClickListener(view -> {
            if (tempMode) {

                editor_temp = sharedPreferences_temp.edit();
                editor_temp.putBoolean("tempMode", false);
                tempMode = false;
                Toast.makeText(setting.this, "Ahihi " + "1", Toast.LENGTH_SHORT).show();
            } else {

                editor_temp = sharedPreferences_temp.edit();
                editor_temp.putBoolean("tempMode", true);
                tempMode = true;
                Toast.makeText(setting.this, "Ahihi " + "2", Toast.LENGTH_SHORT).show();
            }
            editor_temp.apply();
        });


        Spinner spinner = findViewById(R.id.spinner);
        String[] lanGuage = {"ENG", "FRA", "VIE"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(setting.this, android.R.layout.simple_list_item_activated_1, lanGuage);
        spinner.setAdapter(adapter);

        langspinPreference = getSharedPreferences("Lang", Context.MODE_PRIVATE);
        lang = langspinPreference.getInt("lang", 0);
        spinner.setSelection(lang);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                //Toast.makeText(setting.this, "Ahihi " + spinner.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                editlang = langspinPreference.edit();
                editlang.putInt("lang", i);
                editlang.apply();

                if (i == 0) {
                    setAppLocale(setting.this,"en");
                } else if (i == 1) {
                    setAppLocale(setting.this,"fr");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setAppLocale(@NonNull Context context, String localeCode){


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
}




