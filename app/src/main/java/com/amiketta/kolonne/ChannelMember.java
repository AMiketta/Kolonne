package com.amiketta.kolonne;

/**
 * Created by AMiketta on 28.02.2016.
 */
public class ChannelMember {

    public ChannelMember() {
    }

    public ChannelMember(String name, double longitude, double latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.online = true;
    }

    private String name;
    private Boolean online;
    private double longitude;
    private double latitude;

    public String getName() {
        return name;
    }

    public Boolean getOnline() {
        return online;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
