package io.github.kajdreef.smartphonesensing.Activities;

import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
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

    // Floorplan of the 9th floor of EWI
    private FloorPlan floorPlan;

    // GUI
    private LocalizationView localizationView;

    // Monitoring classes
    private ActivityMonitoring activityMonitoring;
    private LocalizationMonitoring localizationMonitoring;

    // Sensors
    private Accelerometer accelerometer;
    private Magnetometer magnetometer;
    private SensorManager sm;

    // Thread Queue
    private ExecutorService executor;

    // Window size of the application.
    public int WINDOW_SIZE;

    // Arrays with the accelerometer data
    private ArrayList<Float> accelX;
    private ArrayList<Float> accelY;
    private ArrayList<Float> accelZ;

    // Arrays with the magnetometer data
    private ArrayList<Float> magnX;
    private ArrayList<Float> magnY;
    private ArrayList<Float> magnZ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        executor  = Executors.newSingleThreadExecutor();

        floorPlan = new FloorPlan();

        // Get resources from the resource folder
        Resources res = this.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE);

        activityMonitoring = new ActivityMonitoring(this.getApplicationContext());

        // Generate x amount of particles
        localizationMonitoring = new LocalizationMonitoring(100,this.getApplicationContext());

        // Initialise Sensors;
        initSensors();

        ArrayList<Particle> particleList = localizationMonitoring.getParticles();

        // Get window size;
        Point windowSize = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(windowSize);

        // Create the localization view with screen orientation in landscape and set it
        localizationView = new LocalizationView(this, floorPlan.getPath(), particleList, windowSize.x, windowSize.y);

        setContentView(localizationView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        accelX = new ArrayList<>(WINDOW_SIZE);
        accelY = new ArrayList<>(WINDOW_SIZE);
        accelZ = new ArrayList<>(WINDOW_SIZE);

        magnX = new ArrayList<>(WINDOW_SIZE);
        magnY = new ArrayList<>(WINDOW_SIZE);
        magnZ = new ArrayList<>(WINDOW_SIZE);
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


    public void update(final int SensorType) {

        // If the sensor type is Accelerometer than get the new data and put it in the array list.
        if(Sensor.TYPE_ACCELEROMETER == SensorType && accelX.size() <= WINDOW_SIZE) {
            this.accelX.add(Accelerometer.getGravity()[0]);
            this.accelY.add(Accelerometer.getGravity()[1]);
            this.accelZ.add(Accelerometer.getGravity()[2]);
        }

        if(Sensor.TYPE_MAGNETIC_FIELD == SensorType && magnX.size() <= WINDOW_SIZE){
            this.magnX.add(Magnetometer.getGeomagnetic()[0]);
            this.magnY.add(Magnetometer.getGeomagnetic()[1]);
            this.magnZ.add(Magnetometer.getGeomagnetic()[2]);
        }

        if(accelX.size() >= WINDOW_SIZE && magnX.size() >= WINDOW_SIZE) {
            Runnable runMovement = new Runnable() {
                @Override
                public void run() {
                    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

                    activityMonitoring.update((ArrayList<Float>) accelX.clone(), (ArrayList<Float>) accelY.clone(), (ArrayList<Float>) accelZ.clone());

                    if (localizationMonitoring.update((ArrayList<Float>) accelX.clone(), (ArrayList<Float>) accelY.clone(), (ArrayList<Float>) accelZ.clone(),
                                            (ArrayList<Float>) magnX.clone(), (ArrayList<Float>) magnY.clone(), (ArrayList<Float>) magnZ.clone())) {

                        localizationView.setParticles(localizationMonitoring.getParticles());

                        localizationView.post(new Runnable() {
                            public void run() {
                                localizationView.invalidate();
                            }
                        });
                    }
                }
            };

            accelX.clear();
            accelY.clear();
            accelZ.clear();

            magnX.clear();
            magnY.clear();
            magnZ.clear();

            executor.execute(runMovement);
        }

    }
}
