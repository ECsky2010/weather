package com.example.administrator.weather;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.weather.gson.Forecast;
import com.example.administrator.weather.gson.Weahter;
import com.example.administrator.weather.util.HttpUtil;
import com.example.administrator.weather.util.Utility;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.air.now.AirNow;
import interfaces.heweather.com.interfacesmodule.bean.weather.Weather;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.view.HeConfig;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;

    private ImageView bingPicImg;

    public DrawerLayout drawerLayout;

    private Button navaButton;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comforText;

    private TextView carWashText;

    private TextView sportText;
    private  String weatherId;

    private com.example.administrator.weather.gson.Weather weather = new com.example.administrator.weather.gson.Weather();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        inint();
        weatherId = getIntent().getStringExtra("weather_id");
        weatherLayout.setVisibility(View.INVISIBLE);
        requestWeather(weatherId);
    }
    public void inint(){
        bingPicImg = findViewById(R.id.bing_pic_img);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String bingPic = prefs.getString("bing_pic",null);
        if(bingPic != null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        } else{
            loadBingPic();
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        navaButton = findViewById(R.id.nav_button);
        navaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        weatherLayout = findViewById(R.id.weather_layout);
        titleCity = findViewById(R.id.title_city);
        degreeText = findViewById(R.id.degree_text);
        titleUpdateTime = findViewById(R.id.title_update_time);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText = findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comforText = findViewById(R.id.comfor_text);
        carWashText = findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
    }

    public void requestWeather(final String weatherId) {
        HeConfig.init("HE1901251846501487", "a59a82f7e5714601aa4953a8a3e52e64");
        HeConfig.switchToFreeServerNode();
        HeWeather.getWeather(this, weatherId, Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherDataListBeansListener() {
            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onSuccess(List<Weather> list) {
                String weatherContent = new Gson().toJson(list);
                weather.setBasic(list.get(0).getBasic().getParent_city());
                weather.setUpdateTime(list.get(0).getUpdate().getLoc());
                weather.setTmp(list.get(0).getNow().getTmp());
                weather.setWeatherInfoText(list.get(0).getNow().getCond_txt());
                weather.setForecastBases(list.get(0).getDaily_forecast());
                weather.setComfort(list.get(0).getLifestyle().get(0).getTxt());
                weather.setCarWash(list.get(0).getLifestyle().get(6).getTxt());
                weather.setSport(list.get(0).getLifestyle().get(3).getTxt());
                weather.setDir(list.get(0).getNow().getWind_dir());
                weather.setSc(list.get(0).getNow().getWind_sc());
                showWeatherInfo();
            }
        });
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("weather_id",weatherId + "");
        editor.apply();
    }

    public void showWeatherInfo(){
        loadBingPic();
        String cityName = weather.getBasic();
        String updateTime = weather.getUpdateTime();
        String degree = weather.getTmp() + "℃";
        String weatherInfo = weather.getWeatherInfoText();
        titleCity.setText(cityName);
        degreeText.setText(degree);
        titleUpdateTime.setText(updateTime);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
          for(ForecastBase forecastBase : weather.getForecastBases()){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView maxText = view.findViewById(R.id.max_text);
            TextView minText = view.findViewById(R.id.min_text);
            dateText.setText(forecastBase.getDate());
            infoText.setText(forecastBase.getCond_txt_d());
            maxText.setText(forecastBase.getTmp_max() + "℃");
            minText.setText(forecastBase.getTmp_min() + "℃");
            forecastLayout.addView(view);
        }

            aqiText.setText(weather.getSc());
            pm25Text.setText(weather.getDir());

        String comfort = "舒适度" + weather.getComfort();
        String carWash = "洗车指数" + weather.getCarWash();
        String sport = "运动建议" + weather.getSport();
        comforText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
    }
    public void loadBingPic(){
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }
        });
    }
}
