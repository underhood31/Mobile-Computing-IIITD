package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class GPSTasksActivity extends AppCompatActivity {
    private static GPSLocationListener locationListener;
    private LocationManager locationManager;
    private TextView latitude, longitude;
    private int fragmentNumber;
    private FragmentManager fragmentManager;
    private Button addPlaceButton, findPlaceButton;
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragmentNumber",fragmentNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpstasks);
        latitude=(TextView) findViewById(R.id.gps_latitude);
        longitude=(TextView) findViewById(R.id.gps_longitude);

        // Location things
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new GPSLocationListener(latitude,longitude);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 1, locationListener);
        fragmentManager = getSupportFragmentManager();

        addPlaceButton=(Button) findViewById(R.id.add_place_fragment_button);
        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNumber=0;
                fragmentManager.beginTransaction().replace(R.id.gps_task_area, new AddPlaceFragment()).commitAllowingStateLoss();
            }
        });

        findPlaceButton=(Button) findViewById(R.id.find_place_fragment_button);
        findPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNumber=1;
                fragmentManager.beginTransaction().replace(R.id.gps_task_area, new FindMyPlaceFragment()).commitAllowingStateLoss();
            }
        });
        if (savedInstanceState!=null) {
            fragmentNumber=savedInstanceState.getInt("fragmentNumber");

        }
        else {
            fragmentNumber=0;
        }

    }
    public static double[] getLocation() {
        return new double[]{locationListener.getLatitude(),locationListener.getLongitude()};
    }
}