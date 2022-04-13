package com.mc2022.template;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

public class GraphsActivity extends AppCompatActivity {
    private int fragmentNumber;
    FragmentManager fragmentManager;
    Button accButton, proxButton;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragmentNumber",fragmentNumber);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        accButton=(Button) findViewById(R.id.acc_chart_button);
        proxButton=(Button) findViewById(R.id.prox_chart_button);

        accButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNumber=0;
                fragmentManager.beginTransaction().replace(R.id.graph_area, new AccelerationGraphFragment()).commitAllowingStateLoss();
            }
        });

        proxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNumber=1;
                fragmentManager.beginTransaction().replace(R.id.graph_area, new ProximityGraphFragment()).commitAllowingStateLoss();
            }
        });

        if (savedInstanceState!=null){
            fragmentNumber=savedInstanceState.getInt("fragmentNumber");
        }
        else {
            fragmentNumber=0;
        }

        fragmentManager = getSupportFragmentManager();

        if (fragmentNumber==0) {
            // start acceleration fragment
            fragmentManager.beginTransaction().add(R.id.graph_area, new AccelerationGraphFragment()).commitAllowingStateLoss();
        }
        else {
            // start proximity fragment
            fragmentManager.beginTransaction().add(R.id.graph_area, new ProximityGraphFragment()).commitAllowingStateLoss();
        }
    }
}