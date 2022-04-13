package com.mc2022.template;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SensorsDAO {

    @Query("select * from Acceleration order by id desc ")
    List<AccelerationModel> getAccelerationReverse();

    @Query("select * from Proximity order by id desc ")
    List<ProximityModel> getProximityReverse();

    @Query("select * from Location where name=:name")
    List<LocationModel> getLocations(String name);

    @Insert
    void insertAcceleration(AccelerationModel acc);

    @Insert
    void insertProximity(ProximityModel prox);

    @Insert
    void insertLocation(LocationModel loc);
}
