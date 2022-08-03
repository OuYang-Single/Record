package com.mmt.record.mvp.model.entity;

public class GpsEntitys {
    private  double longitude;
    private double latitude;
    private long gps_time;

    public long getGps_time() {
        return gps_time;
    }

    public void setGps_time(long gps_time) {
        this.gps_time = gps_time;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
