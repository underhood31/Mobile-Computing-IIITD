package com.mc2022.template;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
// Map help from https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial

public class MainActivity extends AppCompatActivity implements SensorEventListener, OnMapReadyCallback {
    private Sensor magnetometer,accelerometer;
    private SensorManager sensorManager;
    private TextView stepCount,azimText;
    private SensorsDatabase sensorsDatabaseInstance;
    private double prevMagnitude;
    private int steps;
    private Spinner gender, feet, inches;
    private static final String[] genderArray = new String[]{"Male","Female"};
    private static final Integer[] feetArray = new Integer[]{5,6};
    private static final Integer[] inchesArray = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11};
    private Button wifiActButton, editButton;
    private boolean isEditHeightEnabled, isLastAccCopied=false, isLastMagCopied=false;
    private ImageView compassImage;
    // Arrays needed for orientation
    private float[] prevAcc = new float[3];
    private float[] prevMag = new float[3];
    private float[] rotationMat = new float[9];
    private float[] orientation = new float[3];
    private long prevTime=0;
    private boolean locationPermissionGranted;
    private float compassDegree=0;
    public static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private GoogleMap map;
    private final LatLng defaultLocation = new LatLng(28.54829223068449, 77.27431552071639);
    private LatLng prevLocation = defaultLocation;
    private static final int DEFAULT_ZOOM = 70;
    private Button addLocation,getLocation;
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("steps",steps);
        outState.putDouble("prevMagnitude",prevMagnitude);
        outState.putBoolean("isEditHeightEnabled",isEditHeightEnabled);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        sensorManager.unregisterListener(this);
    }

    public float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorsDatabaseInstance=SensorsDatabase.getInstance(getApplicationContext());



        // ----------Get the UI elements----------------

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        stepCount=findViewById(R.id.stepCount);
        gender = findViewById(R.id.genderSpinner);
        feet = findViewById(R.id.feetSpinner);
        inches = findViewById(R.id.inchSpinner);
        isEditHeightEnabled=false;
        compassImage=findViewById(R.id.compassImage);
        azimText=findViewById(R.id.azim);
        wifiActButton=findViewById(R.id.wifiActivityButton);
        addLocation=findViewById(R.id.PDRSetLocation);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText)findViewById(R.id.PDRLocName)).getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Enter some location", Toast.LENGTH_SHORT).show();
                }
                else {

                    sensorsDatabaseInstance.sensorsDAO().insertPDRLocations(new PDRLocationModel(
                            (float)prevLocation.latitude,
                            (float)prevLocation.longitude,
                            ((EditText) findViewById(R.id.PDRLocName)).getText().toString()
                    ));
                }
            }
        });

        getLocation=findViewById(R.id.PDRGetLocation);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float minDist = Float.MAX_VALUE;
                String loc="Not Found";
                List<PDRLocationModel> plm = sensorsDatabaseInstance.sensorsDAO().getPDRLocations();
                for (PDRLocationModel point : plm) {
                    float distance = dist((float)prevLocation.latitude,(float)prevLocation.longitude,point.getLatitude(),point.getLongitude());
                    if (distance<minDist) {
                        minDist=distance;
                        loc = point.getLocation();
                    }
                }

                ((TextView)findViewById(R.id.PDRCurrentLocation)).setText(loc);
            }
        });

        ArrayAdapter genderAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genderArray);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(genderAdapter);


        ArrayAdapter feetAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, feetArray);
        feetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feet.setAdapter(feetAdapter);
        feet.setSelection(0);


        ArrayAdapter inchAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, inchesArray);
        inchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inches.setAdapter(inchAdapter);
        inches.setSelection(5);


//        resetButton = findViewById(R.id.reset_button);
        editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEditHeightEnabled) {
                    isEditHeightEnabled=false;
                    gender.setEnabled(isEditHeightEnabled);
                    feet.setEnabled(isEditHeightEnabled);
                    inches.setEnabled(isEditHeightEnabled);
                    editButton.setText("Edit");
                }
                else {
                    isEditHeightEnabled=true;
                    gender.setEnabled(isEditHeightEnabled);
                    feet.setEnabled(isEditHeightEnabled);
                    inches.setEnabled(isEditHeightEnabled);
                    editButton.setText("Lock");
                }
            }
        });

        wifiActButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WifiLocationActivity.class);
                startActivity(intent);
            }
        });


        // ----------Restoring State----------------
        if (savedInstanceState!=null) {
            steps = savedInstanceState.getInt("steps");
            stepCount.setText(Integer.toString(steps));
            prevMagnitude = savedInstanceState.getDouble("prevMagnitude");
            isEditHeightEnabled = savedInstanceState.getBoolean("isEditHeightEnabled");
        }
        else{
            steps=0;
            prevMagnitude= Double.MAX_VALUE;
            isEditHeightEnabled=false;
        }

        gender.setEnabled(isEditHeightEnabled);
        feet.setEnabled(isEditHeightEnabled);
        inches.setEnabled(isEditHeightEnabled);


        // Other stuff
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Always on magentometer
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magnetometer != null ) {
            sensorManager.registerListener(MainActivity.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
//            Toast.makeText(MainActivity.this, "Started recording orientation data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, " Magnetometer or accelerometer not working", Toast.LENGTH_SHORT).show();
        }

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null ) {
            sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//            Toast.makeText(MainActivity.this, "Started recording orientation data", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, " Magnetometer or accelerometer not working", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // step counter help from https://www.youtube.com/watch?v=o-qpVefrfVA
        // compass help from https://www.youtube.com/watch?v=IzzGVLnZBfQ


        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x, y, z;
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
//            sensorsDatabaseInstance.sensorsDAO().insertMag(new MagneticFieldModel(x,y,z));

            //------------------------ Compass Direction Code ---------------------------------//

            System.arraycopy(sensorEvent.values, 0 ,prevAcc,0, sensorEvent.values.length);
            isLastAccCopied=true;

        }
        else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x, y, z;
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            sensorsDatabaseInstance.sensorsDAO().insertAcc(new AccelerometerModel(x,y,z));

            //------------------------Step Counter Code---------------------------------//
            double magnitude = Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2));
            double mag_delta = magnitude-prevMagnitude;
            prevMagnitude=magnitude;
            if (mag_delta>4) {
                Log.d("Step", "onSensorChanged: Step, delta: "+mag_delta);
                steps+=1;
                stepCount.setText(Integer.toString(steps));

                int feet_int = (int) feet.getSelectedItem();
                int gender_int = (int) gender.getSelectedItemId();
                int inches_int = (int) inches.getSelectedItem();
                float stride_meter = StrideLengths.strideMeter(gender_int,feet_int,inches_int);
                float d=stride_meter/1000,R=6371;
                Log.d("Location", "onSensorChanged: "+stride_meter);
                // help from https://stackoverflow.com/questions/45158779/create-coordinate-based-on-distance-and-direction
                float binder = (float) Math.toRadians(-compassDegree);
                float prevLat= (float) Math.toRadians(prevLocation.latitude);
                float prevLong= (float) Math.toRadians(prevLocation.longitude);

                float newLat = (float)( Math.asin( Math.sin(prevLat)*Math.cos(d/R) +
                        Math.cos(prevLat)*Math.sin(d/R)*Math.cos(binder) ));


                float newLong = (float) (prevLong + Math.atan2(Math.sin(binder)*Math.sin(d/R)*Math.cos(prevLat),
                                        Math.cos(d/R)-Math.sin(prevLat)*Math.sin(newLat)));
                LatLng newLocation = new LatLng(Math.toDegrees(newLat), Math.toDegrees(newLong));
                Log.d("Location", "onSensorChanged: "+prevLocation);
                Log.d("Location", "onSensorChanged: "+newLocation);

                Polyline line = map.addPolyline(new PolylineOptions()
                        .add(prevLocation, newLocation)
                        .width(5)
                        .color(Color.RED));
                prevLocation=newLocation;
            }





            //------------------------ Compass Direction Code ---------------------------------//

            System.arraycopy(sensorEvent.values, 0 ,prevMag,0, sensorEvent.values.length);
            isLastMagCopied=true;
        }

        long currTime=System.currentTimeMillis();

        if (isLastMagCopied && isLastAccCopied && (currTime-prevTime>200)){
            SensorManager.getRotationMatrix(rotationMat,null, prevAcc,prevMag);
            SensorManager.getOrientation(rotationMat,orientation);

            float azimRad = orientation[0];
            float azimDeg = (float) Math.toDegrees(azimRad);

            RotateAnimation rotAnim=new RotateAnimation(compassDegree,azimDeg, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            rotAnim.setDuration(200);
            rotAnim.setFillAfter(true);
            compassImage.startAnimation(rotAnim);

            compassDegree=azimDeg;
            prevTime=currTime;
            azimText.setText(Integer.toString((int)compassDegree)+"°");

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.map = googleMap;


        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                return null;
            }
        });

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }

        map.moveCamera(CameraUpdateFactory
                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
        map.getUiSettings().setMyLocationButtonEnabled(false);

    }


}