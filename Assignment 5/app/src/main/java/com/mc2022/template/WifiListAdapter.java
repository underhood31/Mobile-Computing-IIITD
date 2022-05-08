package com.mc2022.template;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
// Help from https://www.youtube.com/watch?v=w2CqhwFDE1k
public class WifiListAdapter extends BaseAdapter {

    Context c;
    LayoutInflater layoutInflater;
    List<ScanResult> networkList;

    public WifiListAdapter(Context c, List<ScanResult> wifiList) {
        this.c = c;
        this.networkList = wifiList;
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return networkList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ItemHolder holder;
        View nview = view;

        if (nview==null){
            nview = layoutInflater.inflate(R.layout.list_item,null);
            holder=new ItemHolder();
            holder.name=(TextView) nview.findViewById(R.id.wifiName);
            holder.bssid=(TextView) nview.findViewById(R.id.bssid);
            holder.signal=(TextView) nview.findViewById(R.id.signal);
            nview.setTag(holder);
        }
        else {
            holder=(ItemHolder) nview.getTag();
        }

        holder.name.setText(networkList.get(i).SSID);
        holder.bssid.setText(networkList.get(i).BSSID);
        holder.signal.setText(Integer.toString(networkList.get(i).level));
        return nview;
    }

    class ItemHolder {
        TextView name;
        TextView bssid;
        TextView signal;
    }
}
