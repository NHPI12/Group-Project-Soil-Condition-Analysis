package vn.edu.usth.soicondition;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class setting extends AppCompatActivity {

    SwitchCompat lightswitch, tempswitch;

    boolean nightMode, tempMode;
    SharedPreferences sharedPreferences, sharedPreferences_temp;
    SharedPreferences.Editor editor, editor_temp;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        View inflatedView = getLayoutInflater().inflate(R.layout.activity_main, null);
        TextView temperature = (TextView)inflatedView.findViewById(R.id.tempData);

        lightswitch = findViewById(R.id.lighswitch);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightmode", false);

        if(nightMode){
            lightswitch.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        lightswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
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
            }
        });


        //Tempunit
        tempswitch = findViewById(R.id.tempswitch);
        sharedPreferences_temp = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        tempMode = sharedPreferences_temp.getBoolean("tempmode", false);














    }
}




