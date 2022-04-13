package com.mc2022.template;

import android.location.Location;
import android.location.LocationListener;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

// Help from the tutorial
public class GPSLocationListener implements LocationListener {
    static String TAG = "GPS";
    private TextView latitude, longitude;
    private double latitudeVal, longitudeVal;

    GPSLocationListener(TextView latitude, TextView longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(TAG, "onLocationChanged: longitude:" + location.getLongitude());
        Log.d(TAG, "onLocationChanged: latitude:" + location.getLatitude());

        latitude.setText(Double.toString(location.getLatitude()));
        longitude.setText(Double.toString(location.getLongitude() ));

        latitudeVal=location.getLatitude();
        longitudeVal=location.getLongitude();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    public double getLatitude() {
        return latitudeVal;
    }

    public double getLongitude() {
        return longitudeVal;
    }
}
