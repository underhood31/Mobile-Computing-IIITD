package com.mc2022.template;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddPlaceFragment extends Fragment {

    private EditText nameView, addressView;
    private Button addPlaceButton;
    private SensorsDatabase sensorsDatabaseInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name",nameView.getText().toString());
        outState.putString("address",addressView.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameView=(EditText) getView().findViewById(R.id.gps_name);
        addressView=(EditText) getView().findViewById(R.id.gps_address);
        addPlaceButton=(Button) getView().findViewById(R.id.add_place_button);
        sensorsDatabaseInstance=SensorsDatabase.getInstance(getContext());
        if (savedInstanceState!=null) {
            nameView.setText(savedInstanceState.getString("name"));
            addressView.setText(savedInstanceState.getString("address"));
        }

        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double[] loc = GPSTasksActivity.getLocation();
                sensorsDatabaseInstance.sensorsDAO().insertLocation(
                        new LocationModel(
                                loc[0],
                                loc[1],
                                nameView.getText().toString(),
                                addressView.getText().toString()
                        )
                );
            }
        });
    }
}