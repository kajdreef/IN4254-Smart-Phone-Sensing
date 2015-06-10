package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.os.Handler;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.R;

/**
 * Created by kajdreef on 09/06/15.
 */
public class RunActivity implements Runnable{

    private ArrayList<Float> aX, aY, aZ;
    private ActivityMonitoring am;
    TextView stateText;

    public RunActivity(ActivityMonitoring _am, ArrayList<Float> _x, ArrayList<Float> _y, ArrayList<Float> _z){
        this.am = _am;
        this.aX = _x;
        this.aY = _y;
        this.aZ = _z;
    };

    @Override
    public void run() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
        am.update(this.aX, this.aY, this.aZ);

        aX.clear();
        aY.clear();
        aZ.clear();
    }
}