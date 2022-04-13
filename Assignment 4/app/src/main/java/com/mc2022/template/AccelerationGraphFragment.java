package com.mc2022.template;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;


public class AccelerationGraphFragment extends Fragment {

    private LineChart accChart;
    private SensorsDatabase sensorsDatabaseInstance;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_acceleration_graph, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        sensorsDatabaseInstance=SensorsDatabase.getInstance(getContext());
        // help from https://www.youtube.com/watch?v=DD1CxoVONFE
        super.onViewCreated(view, savedInstanceState);
        accChart=(LineChart) view.findViewById(R.id.acc_chart);
        accChart.setDragEnabled(true);
        accChart.setScaleEnabled(false);

        List<AccelerationModel> accData = sensorsDatabaseInstance.sensorsDAO().getAccelerationReverse();
        ArrayList<Entry> yData = new ArrayList<Entry>();

        for (int i=Integer.min(9,accData.size()-1),j=0; i>=0;--i,++j){

            AccelerationModel instance=accData.get(i);
            Entry e = new Entry(j, (float) ((instance.getX()+instance.getY()+instance.getZ())/3));
            yData.add(e);

        }
        LineDataSet set = new LineDataSet(yData,"Acceleration Average Data");
        set.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);

        accChart.setBackgroundColor(Color.WHITE);
        accChart.setData(data);
    }
}