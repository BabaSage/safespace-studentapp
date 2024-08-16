package com.example.safespace;
import java.io.Serializable;
public class Crime implements Serializable {
    private double latitude;
    private double longitude;
    private String crimeType;
    private String crimeDate;
    private String crimeTime;
    private String crimeDescription;

    public Crime(double latitude, double longitude, String crimeType, String crimeDate, String crimeTime, String crimeDescription) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.crimeType = crimeType;
        this.crimeDate = crimeDate;
        this.crimeTime = crimeTime;
        this.crimeDescription = crimeDescription;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCrimeType() {
        return crimeType;
    }

    public String getCrimeDate() {
        return crimeDate;
    }

    public String getCrimeTime() {
        return crimeTime;
    }

    public String getCrimeDescription() {
        return crimeDescription;
    }
}
