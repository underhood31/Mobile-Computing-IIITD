package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    // Assignment 4

    private ToggleButton gyroToggle, accToggle, tempToggle, lightToggle, proxToggle, orientToggle;
    private TextView gyroX, gyroY, gyroZ, accX, accY, accZ, tempVal, lightVal, proxVal,
            orientAzimut, orientPitch, orientRoll, stationaryInfo;
    private SensorManager sensorManager;
    private Sensor accelerometer, proximity, light, temperature, gyroscope, magnetometer, statAccel;
    private Button graphButton, gpsButton;
    private Bundle myState;


    private float[] accPrev, gyroPrev, stateAccPrev;
    private float[] acc_data;
    private float[] mag_data;
    private boolean waveStatus;

    static boolean loop = true;
    private SensorsDatabase sensorsDatabaseInstance;

    public void setStationary() {
        stationaryInfo.setText("Yes");
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("gyro", gyroToggle.isChecked());
        outState.putBoolean("acc", accToggle.isChecked());
        outState.putBoolean("temp", tempToggle.isChecked());
        outState.putBoolean("light", lightToggle.isChecked());
        outState.putBoolean("prox", proxToggle.isChecked());
        outState.putBoolean("orient", orientToggle.isChecked());

        gyroToggle.setChecked(false);
        accToggle.setChecked(false);
        tempToggle.setChecked(false);
        lightToggle.setChecked(false);
        proxToggle.setChecked(false);
        orientToggle.setChecked(false);
        sensorManager.unregisterListener(MainActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));

    }

    void saveWaveState(int type) {
        myState.putBoolean("gyro", gyroToggle.isChecked());
        myState.putBoolean("acc", accToggle.isChecked());
        myState.putBoolean("temp", tempToggle.isChecked());
        myState.putBoolean("light", lightToggle.isChecked());
        myState.putBoolean("prox", proxToggle.isChecked());
        myState.putBoolean("orient", orientToggle.isChecked());

        if (type==0){
            gyroToggle.setChecked(false);
            accToggle.setChecked(false);
            tempToggle.setChecked(false);
            lightToggle.setChecked(false);
            orientToggle.setChecked(false);
        }
        else {
            gyroToggle.setChecked(false);
            tempToggle.setChecked(false);
            lightToggle.setChecked(false);
        }

    }

    void restoreWaveState() {
        gyroToggle.setChecked(myState.getBoolean("gyro"));
        accToggle.setChecked(myState.getBoolean("acc"));
        tempToggle.setChecked(myState.getBoolean("temp"));
        lightToggle.setChecked(myState.getBoolean("light"));
        orientToggle.setChecked(myState.getBoolean("orient"));
        myState.clear();
    }

    void onActionDetected() {
        if (waveStatus){
            saveWaveState(0);
            waveStatus=false;
        }
        else {
            restoreWaveState();
            waveStatus=true;
        }
    }

    void onActionDetected(int type) {
        if (waveStatus){
            saveWaveState(type);
            waveStatus=false;
        }
        else {
            restoreWaveState();
            waveStatus=true;
        }
    }


    // Not being called!!!!!!!!!
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Toast.makeText(this, "onRestore", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "onRestoreInstanceState: !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waveStatus=true;
        myState = new Bundle();
        setContentView(R.layout.activity_main);

        accPrev = new float[3];
        gyroPrev = new float[3];

        graphButton = findViewById(R.id.graphs);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), GraphsActivity.class);
//              myIntent.putExtra("key", value); //Optional parameters
                view.getContext().startActivity(myIntent);
            }
        });

        gpsButton = findViewById(R.id.gpsButton);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), GPSTasksActivity.class);
//              myIntent.putExtra("key", value); //Optional parameters
                view.getContext().startActivity(myIntent);
            }
        });

        gyroToggle = (ToggleButton) findViewById(R.id.gyro_toggle);
        accToggle = (ToggleButton) findViewById(R.id.acc_toggle);
        tempToggle = (ToggleButton) findViewById(R.id.temp_toggle);
        lightToggle = (ToggleButton) findViewById(R.id.light_toggle);
        proxToggle = (ToggleButton) findViewById(R.id.prox_toggle);
        orientToggle = (ToggleButton) findViewById(R.id.orient_toggle);

        gyroX = (TextView) findViewById(R.id.gyro_x);
        gyroY = (TextView) findViewById(R.id.gyro_y);
        gyroZ = (TextView) findViewById(R.id.gyro_z);
        accX = (TextView) findViewById(R.id.acc_x);
        accY = (TextView) findViewById(R.id.acc_y);
        accZ = (TextView) findViewById(R.id.acc_z);
        tempVal = (TextView) findViewById(R.id.temp_val);
        lightVal = (TextView) findViewById(R.id.light_val);
        proxVal = (TextView) findViewById(R.id.prox_val);
        orientAzimut = (TextView) findViewById(R.id.orient_azimut);
        orientPitch = (TextView) findViewById(R.id.orient_pitch);
        orientRoll = (TextView) findViewById(R.id.orient_roll);
        stationaryInfo = (TextView) findViewById(R.id.stationary_info);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorsDatabaseInstance = SensorsDatabase.getInstance(getApplicationContext());

        statAccel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (statAccel != null) {

            sensorManager.registerListener(MainActivity.this, statAccel, SensorManager.SENSOR_DELAY_NORMAL);
        }

        loop = true;
        // help from https://stackoverflow.com/questions/5161951/android-only-the-original-thread-that-created-a-view-hierarchy-can-touch-its-vi
        Thread stationary = new Thread(){
            @Override
            public void run() {
                while (loop) {
                    try {
                        synchronized (this) {
                            wait(2000);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        setStationary();
                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        };
        stationary.start();

        gyroToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
                    if (gyroscope != null) {

                        sensorManager.registerListener(MainActivity.this, gyroscope, SensorManager.SENSOR_DELAY_NORMAL);
                        Toast.makeText(MainActivity.this, "Started recording gyroscope data", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Accelerometer not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sensorManager.unregisterListener(MainActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
                    gyroX.setText("0.00");
                    gyroY.setText("0.00");
                    gyroZ.setText("0.00");
                    Toast.makeText(MainActivity.this, "Stopped recording gyroscope data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        accToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                    if (accelerometer != null) {

                        sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                        Toast.makeText(MainActivity.this, "Started recording accelerometer data", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Accelerometer not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sensorManager.unregisterListener(MainActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION));
                    accX.setText("0.00");
                    accY.setText("0.00");
                    accZ.setText("0.00");
                    acc_data = null;
                    accelerometer = null;
                    Toast.makeText(MainActivity.this, "Stopped recording accelerometer data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tempToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
                    if (temperature != null) {

                        sensorManager.registerListener(MainActivity.this, temperature, SensorManager.SENSOR_DELAY_NORMAL);
                        Toast.makeText(MainActivity.this, "Started recording temperature data", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, " Temperature sensor not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sensorManager.unregisterListener(MainActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE));
                    tempVal.setText("0.00");
                    Toast.makeText(MainActivity.this, "Stopped recording temperature data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        lightToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
                    if (light != null) {

                        sensorManager.registerListener(MainActivity.this, light, SensorManager.SENSOR_DELAY_NORMAL);
                        Toast.makeText(MainActivity.this, "Started recording light data", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, " Light sensor not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sensorManager.unregisterListener(MainActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
                    lightVal.setText("0.00");
                    Toast.makeText(MainActivity.this, "Stopped recording light data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Help from https://stackoverflow.com/questions/20339942/get-device-angle-by-using-getorientation-function
        // Orientation sensor needs both accelerometer and magnetometer to be on.
        orientToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                    if (magnetometer != null && accelerometer != null) {
                        sensorManager.registerListener(MainActivity.this, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
                        Toast.makeText(MainActivity.this, "Started recording orientation data", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, " Magnetometer or accelerometer not working", Toast.LENGTH_SHORT).show();
                        compoundButton.setChecked(false);
                    }
                } else {
                    sensorManager.unregisterListener(MainActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
                    orientAzimut.setText("0.00");
                    orientPitch.setText("0.00");
                    orientRoll.setText("0.00");
                    mag_data = null;
                    Toast.makeText(MainActivity.this, "Stopped recording orientation data", Toast.LENGTH_SHORT).show();
                }
            }
        });


        proxToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    proximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                    if (proximity != null) {

                        sensorManager.registerListener(MainActivity.this, proximity, SensorManager.SENSOR_DELAY_NORMAL);
                        Toast.makeText(MainActivity.this, "Started recording proximity data", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(MainActivity.this, "Proximity sensor not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sensorManager.unregisterListener(MainActivity.this, sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
                    proxVal.setText("0.00");
                    Toast.makeText(MainActivity.this, "Stopped recording proximity data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (savedInstanceState != null) {
            gyroToggle.setChecked(savedInstanceState.getBoolean("gyro"));
            accToggle.setChecked(savedInstanceState.getBoolean("acc"));
            tempToggle.setChecked(savedInstanceState.getBoolean("temp"));
            lightToggle.setChecked(savedInstanceState.getBoolean("light"));
            proxToggle.setChecked(savedInstanceState.getBoolean("prox"));
            orientToggle.setChecked(savedInstanceState.getBoolean("orient"));
        }





    }

    // Help from the tutorial
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor sensor = sensorEvent.sensor;
        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION
                || sensor.getType() != Sensor.TYPE_GYROSCOPE
                || sensor.getType() != Sensor.TYPE_MAGNETIC_FIELD) {
//            stationaryInfo.setText("No");
        }

        if (sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x, y, z;
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            acc_data = new float[]{x, y, z};

            if (mag_data != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean matrixReturned = SensorManager.getRotationMatrix(R, I, acc_data, mag_data);
                if (matrixReturned) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    /*
                    From https://developer.android.com/reference/android/hardware/SensorManager#getOrientation(float[],%20float[])
                    values[0]: Azimuth, angle of rotation about the -z axis. This value
                    represents the angle between the device's y axis and the magnetic north pole.
                    When facing north, this angle is 0, when facing south, this angle is π.
                    Likewise, when facing east, this angle is π/2, and when facing west, this
                    angle is -π/2. The range of values is -π to π.

                    values[1]: Pitch, angle of rotation about the x axis. This value represents the
                    angle between a plane parallel to the device's screen and a plane parallel to
                    the ground. Assuming that the bottom edge of the device faces the user and that
                    the screen is face-up, tilting the top edge of the device toward the ground
                    creates a positive pitch angle. The range of values is -π/2 to π/2.

                    values[2]: Roll, angle of rotation about the y axis. This value represents the
                    angle between a plane perpendicular to the device's screen and a plane
                    perpendicular to the ground. Assuming that the bottom edge of the device
                    faces the user and that the screen is face-up, tilting the left edge of the
                    device toward the ground creates a positive roll angle. The range of values is
                    -π to π.
                    */
                    orientAzimut.setText(new DecimalFormat("##.##").format(orientation[0]));
                    orientPitch.setText(new DecimalFormat("##.##").format(orientation[1]));
                    orientRoll.setText(new DecimalFormat("##.##").format(orientation[2]));

                    double proxLast = sensorsDatabaseInstance.sensorsDAO().getProximityReverse().get(0).getVal();
                    Log.d("TAG", "onSensorChanged(Magnetic): waveStatus"+waveStatus);
                    if (Math.abs( orientation[1])>1 && waveStatus && proxLast==0.0) {
                        onActionDetected(1);
                    }
                    else if (Math.abs( orientation[1])<0.8 && !waveStatus && proxLast!=0.0) {
                        onActionDetected(1);
                    }
                }
            }

            if (accPrev[0] != x || accPrev[1] != y || accPrev[2] != z) {
                sensorsDatabaseInstance.sensorsDAO().insertAcceleration(new AccelerationModel(x, y, z));
                accX.setText(new DecimalFormat("##.##").format(sensorEvent.values[0]));
                accY.setText(new DecimalFormat("##.##").format(sensorEvent.values[1]));
                accZ.setText(new DecimalFormat("##.##").format(sensorEvent.values[2]));
                accPrev = acc_data;
            }

        } else if (sensor.getType() == Sensor.TYPE_LIGHT) {
            lightVal.setText(new DecimalFormat("##.##").format(sensorEvent.values[0]));
            if (sensorEvent.values[0] <= 10000)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            List<ProximityModel> proxVals=sensorsDatabaseInstance.sensorsDAO().getProximityReverse();
            Log.d("TAG", "onSensorChanged: sensor "+sensorEvent.values[0]);
            Log.d("TAG", "onSensorChanged: sensor "+proxVals.get(0).getVal());
            Log.d("TAG", "onSensorChanged: sensor "+proxVals.get(1).getVal());
            if (proxVals.size()>=2 && sensorEvent.values[0]>proxVals.get(0).getVal()
                    && proxVals.get(1).getVal()>proxVals.get(0).getVal()
                    && proxVals.get(0).getVal()<=2
                    && sensorEvent.values[0]>2
                    && proxVals.get(1).getVal()>2){
                onActionDetected();
            }
            proxVal.setText(new DecimalFormat("##.##").format(sensorEvent.values[0]));
            sensorsDatabaseInstance.sensorsDAO().insertProximity(new ProximityModel(sensorEvent.values[0]));


        } else if (sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            tempVal.setText(new DecimalFormat("##.##").format(sensorEvent.values[0]));
        } else if (sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            float x, y, z;
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];

            if (gyroPrev[0] != x || gyroPrev[1] != y || gyroPrev[2] != z) {
                gyroX.setText(new DecimalFormat("##.##").format(sensorEvent.values[0]));
                gyroY.setText(new DecimalFormat("##.##").format(sensorEvent.values[1]));
                gyroZ.setText(new DecimalFormat("##.##").format(sensorEvent.values[2]));
                gyroPrev = new float[]{x, y, z};
            }


        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float x, y, z;
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            mag_data = new float[]{x, y, z};

            if (acc_data != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean matrixReturned = SensorManager.getRotationMatrix(R, I, acc_data, mag_data);
                if (matrixReturned) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    /*
                    From https://developer.android.com/reference/android/hardware/SensorManager#getOrientation(float[],%20float[])
                    values[0]: Azimuth, angle of rotation about the -z axis. This value
                    represents the angle between the device's y axis and the magnetic north pole.
                    When facing north, this angle is 0, when facing south, this angle is π.
                    Likewise, when facing east, this angle is π/2, and when facing west, this
                    angle is -π/2. The range of values is -π to π.

                    values[1]: Pitch, angle of rotation about the x axis. This value represents the
                    angle between a plane parallel to the device's screen and a plane parallel to
                    the ground. Assuming that the bottom edge of the device faces the user and that
                    the screen is face-up, tilting the top edge of the device toward the ground
                    creates a positive pitch angle. The range of values is -π/2 to π/2.

                    values[2]: Roll, angle of rotation about the y axis. This value represents the
                    angle between a plane perpendicular to the device's screen and a plane
                    perpendicular to the ground. Assuming that the bottom edge of the device
                    faces the user and that the screen is face-up, tilting the left edge of the
                    device toward the ground creates a positive roll angle. The range of values is
                    -π to π.
                    */
                    orientAzimut.setText(new DecimalFormat("##.##").format(orientation[0]));
                    orientPitch.setText(new DecimalFormat("##.##").format(orientation[1]));
                    orientRoll.setText(new DecimalFormat("##.##").format(orientation[2]));

                    double proxLast = sensorsDatabaseInstance.sensorsDAO().getProximityReverse().get(0).getVal();
                    Log.d("TAG", "onSensorChanged(Magnetic): waveStatus"+waveStatus);
                    if (Math.abs( orientation[1])>1 && waveStatus && proxLast==0.0) {
                        onActionDetected(1);
                    }
                    else if (Math.abs( orientation[1])<0.8 && !waveStatus && proxLast!=0.0) {
                        onActionDetected(1);
                    }
                }
            }
        }
        else if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x, y, z;
            x = sensorEvent.values[0];
            y = sensorEvent.values[1];
            z = sensorEvent.values[2];
            if (stateAccPrev==null){
                stateAccPrev=new float[]{x,y,z};
                stationaryInfo.setText("No");

            }
            else if (stateAccPrev[0] != x || stateAccPrev[1] != y || stateAccPrev[2] != z) {

                stateAccPrev = new float[]{x,y,z};
                stationaryInfo.setText("No");
            }
//            else {
//                stationaryInfo.setText("Yes");
//            }

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}




}

