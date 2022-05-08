package com.mc2022.template;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SensorsDAO {

    @Query("select * from Accelerometer order by id desc ")
    List<AccelerometerModel> getAccReverse();

    @Query("select * from MagneticField order by id desc ")
    List<MagneticFieldModel> getMagReverse();

    @Query("select * from WifiLocation ")
    List<WifiLocationModel> getWifiLocations();

    @Query("select * from PDRLocation")
    List<PDRLocationModel> getPDRLocations();

    @Insert
    void insertAcc(AccelerometerModel acc);

    @Insert
    void insertMag(MagneticFieldModel mg);

    @Insert
    void insertWifiLoc(WifiLocationModel wloc);

    @Insert
    void insertPDRLocations(PDRLocationModel ploc);

}