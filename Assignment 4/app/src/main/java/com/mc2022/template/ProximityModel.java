package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Proximity")
public class ProximityModel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "val")
    private double val;

    public ProximityModel(double val) {
        this.val=val;
        this.time=System.currentTimeMillis() / 1000L;
    }

    public int getId() {
        return id;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public long getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
