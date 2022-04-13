package com.mc2022.template;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FindMyPlaceFragment extends Fragment {
    private EditText nameView;
    private Button findLocationButton;
    private SensorsDatabase sensorsDatabaseInstance;
    private TextView[] nearestLocations = new TextView[3];

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",nameView.getText().toString());
        outState.putString("loc0",nearestLocations[0].getText().toString());
        outState.putString("loc1",nearestLocations[1].getText().toString());
        outState.putString("loc2",nearestLocations[2].getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_my_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameView=(EditText) getView().findViewById(R.id.gps_find_name);
        findLocationButton=(Button) getView().findViewById(R.id.find_place_button);
        sensorsDatabaseInstance=SensorsDatabase.getInstance(getContext());
        nearestLocations[0] = (TextView) getView().findViewById(R.id.nearest_locations_1);
        nearestLocations[1] = (TextView) getView().findViewById(R.id.nearest_locations_2);
        nearestLocations[2] = (TextView) getView().findViewById(R.id.nearest_locations_3);

        if(savedInstanceState!=null) {
            nameView.setText(savedInstanceState.getString("name"));
            nearestLocations[0].setText(savedInstanceState.getString("loc0"));
            nearestLocations[1].setText(savedInstanceState.getString("loc1"));
            nearestLocations[2].setText(savedInstanceState.getString("loc2"));
        }

        findLocationButton.setOnClickListener(new View.OnClickListener() {
            class sortLocation implements Comparator<LocationModel> {
                double x,y;
                sortLocation(double x,double y) {
                    this.x=x;
                    this.y=y;
                }

                private double findDistance(double x1, double y1, double x2, double y2) {
                    return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
                }

                @Override
                public int compare(LocationModel t1, LocationModel t2) {
                    return -1 * (int)(findDistance(x,y, t1.getLatitude(), t1.getLongitude())-findDistance(x,y, t2.getLatitude(), t2.getLongitude()));
                }
            }
            @Override
            public void onClick(View view) {
                for (int i=0; i<3 ; ++i) {
                    nearestLocations[i].setText("");
                }
                String name=nameView.getText().toString();
                double[] locationValues = GPSTasksActivity.getLocation();
                List<LocationModel> locations = sensorsDatabaseInstance.sensorsDAO().getLocations(name);
                Collections.sort(locations,new sortLocation(locationValues[0],locationValues[1]));
                Log.d("GPS", "onClickFindLocation: "+locations);
                for (int i=0; i<3 && i<locations.size(); ++i) {
                    String text="Your "+Integer.toString(i+1)+" nearest location is at latitude "
                            + Double.toString(locations.get(i).getLatitude()) + ", longitude is " +
                            Double.toString(locations.get(i).getLongitude()) + " and address is " +
                            locations.get(i).getAddress();
                    nearestLocations[i].setText(text);
                }
            }
        });
    }
}