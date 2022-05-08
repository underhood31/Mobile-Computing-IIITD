package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "PDRLocation")
public class PDRLocationModel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name="Latitude")
    private float Latitude;

    @ColumnInfo(name="Longitude")
    private float Longitude;


    @ColumnInfo(name="Location")
    private String Location;


    public PDRLocationModel(float Latitude, float Longitude, String Location) {
        this.Latitude=Latitude;
        this.Longitude=Longitude;
        this.Location=Location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLocation() {
        return Location;
    }
}
