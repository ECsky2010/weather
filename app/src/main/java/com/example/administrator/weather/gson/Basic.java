package com.example.administrator.weather.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherID;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
