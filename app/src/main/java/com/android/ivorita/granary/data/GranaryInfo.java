package com.android.ivorita.granary.data;

public class GranaryInfo {

    private int id;

    private String datetime;

    private float carbon_dioxide;

    private float temperature;

    private float humidity;

    private float illumination;

    private float gas;

    public GranaryInfo(int id, String datetime, float carbon_dioxide, float temperature, float humidity, float illumination, float gas) {
        this.id = id;
        this.datetime = datetime;
        this.carbon_dioxide = carbon_dioxide;
        this.temperature = temperature;
        this.humidity = humidity;
        this.illumination = illumination;
        this.gas = gas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public float getCarbon_dioxide() {
        return carbon_dioxide;
    }

    public void setCarbon_dioxide(float carbon_dioxide) {
        this.carbon_dioxide = carbon_dioxide;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public float getIllumination() {
        return illumination;
    }

    public void setIllumination(float illumination) {
        this.illumination = illumination;
    }

    public float getGas() {
        return gas;
    }

    public void setGas(float gas) {
        this.gas = gas;
    }

    @Override
    public String toString() {
        return "GranaryInfo{" +
                "id=" + id +
                ", datetime='" + datetime + '\'' +
                ", carbon_dioxide=" + carbon_dioxide +
                ", temperature=" + temperature +
                ", humidity=" + humidity +
                '}';
    }

}
