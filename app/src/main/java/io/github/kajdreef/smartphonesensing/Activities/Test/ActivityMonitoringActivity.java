package io.github.kajdreef.smartphonesensing.Activities.Test;

import android.app.Activity;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.kajdreef.smartphonesensing.Sensor.AbstractSensor;
import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.R;

/**
 * Created by kajdreef on 02/05/15.
 * Activity monitoring activity
 */
public class ActivityMonitoringActivity extends Activity implements ObserverSensor {
    ActivityMonitoring activityMonitoring;
    SensorManager sm;
    AbstractSensor accelerometer;
    public int WINDOW_SIZE;
    private int amountOfNewSamples = 0;

    // Arrays with the accelerometer data
    private ArrayList<Float> accelX;
    private ArrayList<Float> accelY;
    private ArrayList<Float> accelZ;

    // Thread Queue
    private ExecutorService executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = this.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE_ACC);

        String acceleroFileLocation = res.getString(R.string.accelerometer_data_file);

        executor = Executors.newSingleThreadExecutor();

        setContentView(R.layout.activity_monitoring_menu);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        activityMonitoring = new ActivityMonitoring(this);
        accelerometer = new Accelerometer(sm, acceleroFileLocation);

        accelerometer.attach(this);
        accelerometer.register();

        accelX = new ArrayList<>(WINDOW_SIZE);
        accelY = new ArrayList<>(WINDOW_SIZE);
        accelZ = new ArrayList<>(WINDOW_SIZE);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        accelerometer.unregister();
    }


    public void update(int SensorType) {

        // If the sensor type is Accelerometer than get the new data and put it in the array list.
        if (Sensor.TYPE_ACCELEROMETER == SensorType && accelX.size() < WINDOW_SIZE) {
            this.accelX.add(Accelerometer.getGravity()[0]);
            this.accelY.add(Accelerometer.getGravity()[1]);
            this.accelZ.add(Accelerometer.getGravity()[2]);
        }

        if (accelX.size() >= WINDOW_SIZE) {
            Runnable runMovement = new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
                    activityMonitoring.update(accelX, accelY, accelZ);

                    accelX.clear();
                    accelY.clear();
                    accelZ.clear();
                }
            };

            executor.execute(runMovement);

            TextView t = (TextView) this.findViewById(R.id.textView2);
            t.setText(activityMonitoring.getActivity().toString());
        }
    }
}
