package io.github.kajdreef.smartphonesensing.Activities;

import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Observer;
import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationMonitoring;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationView;
import io.github.kajdreef.smartphonesensing.Localization.Particle;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.ParticleFilter;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.Sensor.Magnetometer;

/**
 * Created by kajdreef on 15/05/15.
 */
public class LocalizationActivity extends ActionBarActivity implements Observer{

    private LocalizationView localizationView;
    private Accelerometer accelerometer;
    private Magnetometer magnetometer;
    private SensorManager sm;
    public static final int WINDOW_SIZE = 150;

    private int amountOfNewSamples = 0;

    private LocalizationMonitoring lm;
    private ActivityMonitoring am;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lm = new LocalizationMonitoring(1000, this);
        am = new ActivityMonitoring(this);

        // Initialise Sensors;
        initSensors();

        FloorPlan fp = lm.getFloorPlan();

        // Create the localization view and set it
        localizationView = new LocalizationView(this, fp.getPath(), lm.getParticles());

        // set contentview to localizationview  with screen orientation in landscape.
        setContentView(localizationView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Initialise the sensors
     */
    public void initSensors(){
        sm =(SensorManager)getSystemService(SENSOR_SERVICE);

        accelerometer = new Accelerometer(sm);
        magnetometer = new Magnetometer(sm);

        accelerometer.attach(this);
        magnetometer.attach(this);

        accelerometer.register();
        magnetometer.register();
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
    }


    public void update() {
        amountOfNewSamples++;
        if(amountOfNewSamples > WINDOW_SIZE){
            // First update am so the new speed and activity is available
            am.update();
            lm.update();

            localizationView.setParticles(lm.getParticles());
            amountOfNewSamples = 0;
        }
    }

}
