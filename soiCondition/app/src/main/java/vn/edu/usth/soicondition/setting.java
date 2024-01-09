package vn.edu.usth.soicondition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.MenuItem;

import java.util.Objects;


public class setting extends AppCompatActivity {

    SwitchCompat lightswitch, tempswitch;

    boolean nightMode, tempMode;
    SharedPreferences sharedPreferences, sharedPreferences_temp;
    SharedPreferences.Editor editor;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        lightswitch = findViewById(R.id.lighswitch);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightmode", false);

        if(nightMode){
            lightswitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        lightswitch.setOnClickListener(view -> {
            if(nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor = sharedPreferences.edit();
                editor.putBoolean("nightmode", false);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor = sharedPreferences.edit();
                editor.putBoolean("nightmode", true);
            }
            editor.apply();
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //Tempunit
        tempswitch = findViewById(R.id.tempswitch);
        sharedPreferences_temp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        tempMode = sharedPreferences_temp.getBoolean("tempmode", false);
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




