package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "WifiLocation")
public class WifiLocationModel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;


    @ColumnInfo(name = "SignalStrength0")
    private int SignalStrength0;
    @ColumnInfo(name = "SignalStrength1")
    private int SignalStrength1;
    @ColumnInfo(name = "SignalStrength2")
    private int SignalStrength2;
    @ColumnInfo(name = "SignalStrength3")
    private int SignalStrength3;
    @ColumnInfo(name = "SignalStrength4")
    private int SignalStrength4;
    @ColumnInfo(name = "SignalStrength5")
    private int SignalStrength5;

    @ColumnInfo(name="Location")
    private String Location;

    public WifiLocationModel(int SignalStrength0,int SignalStrength1,
            int SignalStrength2,int SignalStrength3,int SignalStrength4,
            int SignalStrength5, String Location) {
        this.SignalStrength0=SignalStrength0;
        this.SignalStrength1=SignalStrength1;
        this.SignalStrength2=SignalStrength2;
        this.SignalStrength3=SignalStrength3;
        this.SignalStrength4=SignalStrength4;
        this.SignalStrength5=SignalStrength5;
        this.Location=Location;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSignalStrength0() {
        return SignalStrength0;
    }

    public void setSignalStrength0(int signalStrength0) {
        SignalStrength0 = signalStrength0;
    }

    public int getSignalStrength1() {
        return SignalStrength1;
    }

    public void setSignalStrength1(int signalStrength1) {
        SignalStrength1 = signalStrength1;
    }

    public int getSignalStrength2() {
        return SignalStrength2;
    }

    public void setSignalStrength2(int signalStrength2) {
        SignalStrength2 = signalStrength2;
    }

    public int getSignalStrength3() {
        return SignalStrength3;
    }

    public void setSignalStrength3(int signalStrength3) {
        SignalStrength3 = signalStrength3;
    }

    public int getSignalStrength4() {
        return SignalStrength4;
    }

    public void setSignalStrength4(int signalStrength4) {
        SignalStrength4 = signalStrength4;
    }

    public int getSignalStrength5() {
        return SignalStrength5;
    }

    public void setSignalStrength5(int signalStrength5) {
        SignalStrength5 = signalStrength5;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
