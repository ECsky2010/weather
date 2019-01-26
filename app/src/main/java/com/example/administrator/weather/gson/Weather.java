package com.example.administrator.weather.gson;

import java.util.List;

import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.Forecast;
import interfaces.heweather.com.interfacesmodule.bean.weather.forecast.ForecastBase;

public class Weather {
    private String basic;

    private String dir;

    private String sc;

    private String updateTime;

    private String comfort;

    private String sport;

    private String carWash;

    private String tmp;

    private String weatherInfoText;

    private List<ForecastBase> forecastBases;
    public String getBasic() {
        return basic;
    }

    public void setBasic(String basic) {
        this.basic = basic;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getWeatherInfoText() {
        return weatherInfoText;
    }

    public void setWeatherInfoText(String weatherInfoText) {
        this.weatherInfoText = weatherInfoText;
    }

    public List<ForecastBase> getForecastBases() {
        return forecastBases;
    }

    public void setForecastBases(List<ForecastBase> forecastBases) {
        this.forecastBases = forecastBases;
    }

    public String getCarWash() {
        return carWash;
    }

    public void setCarWash(String carWash) {
        this.carWash = carWash;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getComfort() {
        return comfort;
    }

    public void setComfort(String comfort) {
        this.comfort = comfort;
    }


    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }
}
