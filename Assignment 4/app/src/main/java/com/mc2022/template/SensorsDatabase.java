package com.mc2022.template;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {AccelerationModel.class, ProximityModel.class, LocationModel.class}, version = 4)
public abstract class SensorsDatabase extends RoomDatabase {

    public abstract SensorsDAO sensorsDAO();
    public static SensorsDatabase sensorsDatabaseInstance;
    public static SensorsDatabase getInstance(Context c) {
        if (sensorsDatabaseInstance==null) {
            sensorsDatabaseInstance = Room.databaseBuilder(c.getApplicationContext(), SensorsDatabase.class, "sensors_database").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return sensorsDatabaseInstance;
    }
}
