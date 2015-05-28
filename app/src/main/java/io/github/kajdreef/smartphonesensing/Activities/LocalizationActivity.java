package io.github.kajdreef.smartphonesensing.Activities;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationMonitoring;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationView;
import io.github.kajdreef.smartphonesensing.Localization.Particle;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.Sensor.Magnetometer;

/**
 * Created by kajdreef on 15/05/15.
 */
public class LocalizationActivity extends ActionBarActivity implements ObserverSensor {

    private FloorPlan floorPlan;
    private LocalizationView localizationView;
    private ActivityMonitoring activityMonitoring;
    private LocalizationMonitoring localizationMonitoring;
    private Accelerometer accelerometer;
    private Magnetometer magnetometer;
    private SensorManager sm;
    public int WINDOW_SIZE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        floorPlan = new FloorPlan();

        // Get resources from the resource folder
        Resources res = this.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE);

        activityMonitoring = new ActivityMonitoring(this.getApplicationContext());

        // Generate x amount of particles
        localizationMonitoring = new LocalizationMonitoring(1000,this.getApplicationContext());

        // Initialise Sensors;
        //initSensors();

        ArrayList<Particle> particleList = localizationMonitoring.getParticles();

        // Get window size;
        Point windowSize = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(windowSize);

        // Create the localization view with screen orientation in landscape and set it
        localizationView = new LocalizationView(this, floorPlan.getPath(), particleList, windowSize.x, windowSize.y);

        setContentView(localizationView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Initialise the sensors
     */
    public void initSensors(){
        sm =(SensorManager)getSystemService(SENSOR_SERVICE);

        // Get needed file locations where data needs to be stored.
        Resources res = this.getResources();

        String magmetoFileLocation = res.getString(R.string.magnetometer_data_file);
        String acceleroFileLocation = res.getString(R.string.accelerometer_data_file);

        // Create sensor instances
        accelerometer = new Accelerometer(sm, acceleroFileLocation);
        magnetometer = new Magnetometer(sm, magmetoFileLocation);

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


    public void update(int SensorType) {
        activityMonitoring.update(SensorType);
        localizationMonitoring.update(SensorType);
        localizationView.setParticles(localizationMonitoring.getParticles());
    }

}
