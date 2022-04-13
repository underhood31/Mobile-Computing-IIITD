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

import java.util.ArrayList;
import java.util.List;

public class ProximityGraphFragment extends Fragment {

    private LineChart proxChart;
    private SensorsDatabase sensorsDatabaseInstance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_proximity_graph, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sensorsDatabaseInstance=SensorsDatabase.getInstance(getContext());
        // help from https://www.youtube.com/watch?v=DD1CxoVONFE
        super.onViewCreated(view, savedInstanceState);
        proxChart=(LineChart) view.findViewById(R.id.prox_chart);
        proxChart.setDragEnabled(true);
        proxChart.setScaleEnabled(false);

        List<ProximityModel> proxData = sensorsDatabaseInstance.sensorsDAO().getProximityReverse();
        ArrayList<Entry> yData = new ArrayList<Entry>();
        for (int i=Integer.min(9,proxData.size()-1),j=0; i>=0;--i,++j){

            ProximityModel instance=proxData.get(i);
            Entry e = new Entry(j, (float) (instance.getVal()));
            yData.add(e);

        }
        LineDataSet set = new LineDataSet(yData,"Proximity Data");
        set.setFillAlpha(110);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set);

        LineData data = new LineData(dataSets);

        proxChart.setBackgroundColor(Color.WHITE);
        proxChart.setData(data);
    }
}