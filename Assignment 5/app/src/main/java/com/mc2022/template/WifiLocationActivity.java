package com.mc2022.template;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WifiLocationActivity extends AppCompatActivity {

    WifiManager wifiManager;
    WifiReceiver wifiReceiver;
    WifiListAdapter wifiListAdapter;
    ListView wifiList;
    List wifiRawList;
    ArrayList<ScanResult> wifiNetworkM;
    Button refershWifiButton, getLocationButton, setLocationButton;
    SensorsDatabase sensorsDatabaseInstance;
    TextView location;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sensorsDatabaseInstance=SensorsDatabase.getInstance(getApplicationContext());
        setContentView(R.layout.activity_wifi_location);
        wifiList=findViewById(R.id.wifiList);
        refershWifiButton=findViewById(R.id.refresh_wifi);
        location=findViewById(R.id.CurrentLocation);
        refershWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanWifiList();
            }
        });

        getLocationButton=findViewById(R.id.getLocationButton);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ScanWifiList();
                if (wifiNetworkM.size()==0)
                    return;
                int[] signal=new int[NinthFloorRouters.listOfAPs.length];
                for(int i=0; i<NinthFloorRouters.listOfAPs.length;++i){
                    signal[i]=-100;
                }
                for (ScanResult scanR : wifiNetworkM) {
                    for(int i=0; i<NinthFloorRouters.listOfAPs.length;++i){
                        if (scanR.BSSID.equals(NinthFloorRouters.listOfAPs[i])) {
                            signal[i]=scanR.level;
                        }
                    }
                }
                String result = KNN.getNN(signal,getApplicationContext());
                location.setText(result);
            }
        });

        setLocationButton=findViewById(R.id.setLocation);
        setLocationButton.setOnClickListener(new View.OnClickListener() {
            // help from https://www.youtube.com/watch?v=4GYKOzgQDWI
            @Override
            public void onClick(View view) {
//                ScanWifiList();
                if (((EditText)findViewById(R.id.locName)).getText().toString().isEmpty()){
                    Toast.makeText(WifiLocationActivity.this, "Enter some location", Toast.LENGTH_SHORT).show();
                }
                else {
                    int[] signal=new int[NinthFloorRouters.listOfAPs.length];
                    for(int i=0; i<NinthFloorRouters.listOfAPs.length;++i){
                        signal[i]=-100;
                    }
                    for (ScanResult scanR : wifiNetworkM) {
                        for(int i=0; i<NinthFloorRouters.listOfAPs.length;++i){
                            if (scanR.BSSID.equals(NinthFloorRouters.listOfAPs[i])) {
                                signal[i]=scanR.level;
                            }

                        }
                    }
                    sensorsDatabaseInstance.sensorsDAO().insertWifiLoc(new WifiLocationModel(
                            signal[0],
                            signal[1],
                            signal[2],
                            signal[3],
                            signal[4],
                            signal[5],
                            ((EditText) findViewById(R.id.locName)).getText().toString()
                    ));
                    Toast.makeText(WifiLocationActivity.this, "Location Saved!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        wifiManager= (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver=new WifiReceiver();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        ScanWifiList();
    }

    private void ScanWifiList() {
        boolean success = wifiManager.startScan();
        if (success) {
            wifiRawList = wifiManager.getScanResults();
            Log.d("TAG", "setAdapter: " + wifiRawList);
            wifiNetworkM = new ArrayList<ScanResult>();
            for (Object sr : wifiRawList) {
                ScanResult src = (ScanResult) sr;
                //            Log.d("TAG", "ScanWifiList: "+src.BSSID+"\t"+src.BSSID.equals(NinthFloorRouters.listOfAPs[0]));
                //            Log.d("TAG", "ScanWifiList: "+src.BSSID+"\t"+src.BSSID.equals(NinthFloorRouters.listOfAPs[1]));
                //            Log.d("TAG", "ScanWifiList: "+src.BSSID+"\t"+src.BSSID.equals(NinthFloorRouters.listOfAPs[2]));
                //            Log.d("TAG", "ScanWifiList: "+src.BSSID+"\t"+src.BSSID.equals(NinthFloorRouters.listOfAPs[3]));

                if (src.SSID.equals("STUDENTS-M")
                        && (src.BSSID.equals(NinthFloorRouters.listOfAPs[0]) ||
                        src.BSSID.equals(NinthFloorRouters.listOfAPs[1]) ||
                        src.BSSID.equals(NinthFloorRouters.listOfAPs[2]) ||
                        src.BSSID.equals(NinthFloorRouters.listOfAPs[3]) ||
                        src.BSSID.equals(NinthFloorRouters.listOfAPs[4]) ||
                        src.BSSID.equals(NinthFloorRouters.listOfAPs[5]))
                ) {
                    wifiNetworkM.add(src);
                }
            }
            setAdapter(wifiNetworkM);
        }
        else{
            Toast.makeText(this, "Scan Failed!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter(List<ScanResult> wifiNetworkM) {
        wifiListAdapter = new WifiListAdapter(getApplicationContext(),wifiNetworkM);
        wifiList.setAdapter(wifiListAdapter);
    }

    class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}