package io.github.kajdreef.smartphonesensing;

import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Accelerometer;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.abstractSensor;
import io.github.kajdreef.smartphonesensing.Classification.*;


public class MainMenu extends ActionBarActivity {

    SensorManager sm;
    abstractSensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        sm =(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = new Accelerometer(sm);

    }

    @Override
    public void onStart(){
        super.onStart();
        accelerometer.register();
    }

    @Override
    public void onResume(){
        super.onResume();
        accelerometer.register();
    }

    @Override
    public void onPause(){
        super.onPause();
        accelerometer.unregister();
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
        sm.unregisterListener(accelerometer);
    }
}
