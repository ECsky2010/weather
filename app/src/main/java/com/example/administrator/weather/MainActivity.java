package com.example.administrator.weather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getString("weather_id",null) != null){
            Intent intent = new Intent(this,WeatherActivity.class);
            intent.putExtra("weather_id",prefs.getString("weather_id",""));
            startActivity(intent);
            finish();
        }
    }
}
