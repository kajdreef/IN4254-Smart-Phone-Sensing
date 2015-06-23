package io.github.kajdreef.smartphonesensing.Sensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uncle John on 23/06/2015.
 */
public class WifiReceiver extends BroadcastReceiver{
    private WifiManager wifi;
    private ArrayList<String> wifiPoints;
    private ArrayList<ArrayList<Integer>> RSSI;

    public WifiReceiver(WifiManager _wifi){
        super();
        this.wifi = _wifi;
        wifiPoints = new ArrayList<>();
        RSSI = new ArrayList<>();
    }

    public ArrayList<ArrayList<Integer>> getRSSI(){
        return this.RSSI;
    }
    public ArrayList<String> getWifiPoints(){
        return this.wifiPoints;
    };
    @Override
    public void onReceive(Context context, Intent intent) {

        List<ScanResult> results = wifi.getScanResults();
        try {
            ArrayList<Integer> out =  new ArrayList<>();
            for (int j = 0; j < wifiPoints.size(); j++) {
                out.add(0);
            }
            for (int n = 0; n < results.size(); n++) {
                int i = wifiPoints.lastIndexOf(results.get(n).BSSID);
                if(i >= 0){
                    //already know this wifi point
                    out.set(i, results.get(n).level);
                }
                else{
                    //add new wifi point
                    wifiPoints.add(results.get(n).BSSID);
                    out.add(results.get(n).level);
                    for (ArrayList<Integer> t : RSSI){
                        t.add(0);
                    }
                }
            }
            RSSI.add(out);
        }
        catch (Exception e) {
            Log.i("Wifi test","exception " + e.toString() );
        }
    }
}
