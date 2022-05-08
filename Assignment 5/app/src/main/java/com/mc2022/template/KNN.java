package com.mc2022.template;

import android.content.Context;
import android.util.Log;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// help from https://www.youtube.com/watch?v=4HKqjENq9OU&t=823s
public class KNN {
    public static int K=3;

    private static class sortOnDistance implements Comparator<WifiLocationModel> {
        private int[] mainPoint;
        sortOnDistance(int[] mainPoint){
            this.mainPoint=mainPoint;
        }
        @Override
        public int compare(WifiLocationModel t1, WifiLocationModel t2) {
            int[] sigT1={t1.getSignalStrength0(),t1.getSignalStrength1(),t1.getSignalStrength2(),
                    t1.getSignalStrength3(),t1.getSignalStrength4(),t1.getSignalStrength5()};
            int[] sigT2={t2.getSignalStrength0(),t2.getSignalStrength1(),t2.getSignalStrength2(),
                    t2.getSignalStrength3(),t2.getSignalStrength4(),t2.getSignalStrength5()};

            double d1 = calcDistance(sigT1,mainPoint);
            double d2 = calcDistance(sigT2,mainPoint);

            return (int) (d1-d2);
        }
    }

    public static String getNN(int[] signals, Context c) {
        if (signals.length!=6)
            return "Cannot process input signals";

        List<WifiLocationModel> allData=SensorsDatabase.getInstance(c).sensorsDAO().getWifiLocations();
        Collections.sort(allData,new sortOnDistance(signals));
        Log.d("AfterSort", "getNN: "+allData);
        if (allData.size()==0){
            return "No Data Found!!";
        }
        // location, count
        HashMap<String, Integer> locCount=new HashMap<String,Integer>();
        for (int i=0; i<Math.min(KNN.K,allData.size()); ++i) {
            if (!locCount.containsKey(allData.get(i)))
                locCount.put(allData.get(i).getLocation(),1);
            else
                locCount.put(allData.get(i).getLocation(),locCount.get(allData.get(i).getLocation())+1);
        }

        // Help from https://stackoverflow.com/questions/5911174/finding-key-associated-with-max-value-in-a-java-map
        Map.Entry<String, Integer> maxEntry = null;

        for (Map.Entry<String, Integer> entry : locCount.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }

        return maxEntry.getKey();

    }

    public static double calcDistance(int[] p1, int[] p2) {
        double sum=0;
        if (p1.length!=p2.length)
            return -1;
        for (int i=0; i<p1.length; ++i) {
            sum+=Math.pow((double) (p1[i]-p2[i]), 2);
        }
        return Math.sqrt(sum);
    }
}
