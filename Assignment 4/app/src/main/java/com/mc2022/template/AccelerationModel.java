package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Acceleration")
public class AccelerationModel {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "time")
    private long time;

    @ColumnInfo(name = "x")
    private double x;

    @ColumnInfo(name = "y")
    private double y;

    @ColumnInfo(name = "z")
    private double z;

    public AccelerationModel(double x, double y, double z) {
        this.time=System.currentTimeMillis() / 1000L;
        this.x=x;
        this.y=y;
        this.z=z;

    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public int getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
