package io.github.kajdreef.smartphonesensing.Activities;

import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import io.github.kajdreef.smartphonesensing.Sensor.AbstractSensor;
import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.R;

/**
 * Created by kajdreef on 02/05/15.
 * Activity monitoring activity
 */
public class ActivityMonitoringActivity extends ActionBarActivity implements ObserverSensor {
    ActivityMonitoring am;
    SensorManager sm;
    AbstractSensor accelerometer;
    public int WINDOW_SIZE;
    private int amountOfNewSamples = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = this.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE);

        String acceleroFileLocation = res.getString(R.string.accelerometer_data_file);

        setContentView(R.layout.activity_monitoring_menu);
        sm =(SensorManager)getSystemService(SENSOR_SERVICE);

        am = new ActivityMonitoring(this);
        accelerometer = new Accelerometer(sm, acceleroFileLocation);

        accelerometer.attach(this);
        accelerometer.register();
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
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
    public void onDestroy(){
        super.onDestroy();
        accelerometer.unregister();
    }


    public void update(int ServerType) {

        am.update(ServerType);

        TextView t = (TextView) this.findViewById(R.id.textView2);
        t.setText(am.getActivity().toString() +" "+ Float.toString(am.getSpeed())+ "m/s");
    }
}
