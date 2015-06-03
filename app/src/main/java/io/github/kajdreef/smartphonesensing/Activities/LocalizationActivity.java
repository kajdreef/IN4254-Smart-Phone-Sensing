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
    public int WINDOW_SIZE_ACC;
    public int WINDOW_SIZE_MAG;

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
        WINDOW_SIZE_ACC = res.getInteger(R.integer.WINDOW_SIZE_ACC);
        WINDOW_SIZE_MAG = res.getInteger(R.integer.WINDOW_SIZE_MAG);

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

        accelX = new ArrayList<>();
        accelY = new ArrayList<>();
        accelZ = new ArrayList<>();

        magnX = new ArrayList<>();
        magnY = new ArrayList<>();
        magnZ = new ArrayList<>();
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
        if(Sensor.TYPE_ACCELEROMETER == SensorType && accelX.size() <= WINDOW_SIZE_ACC) {
            this.accelX.add(Accelerometer.getGravity()[0]);
            this.accelY.add(Accelerometer.getGravity()[1]);
            this.accelZ.add(Accelerometer.getGravity()[2]);
        }

        if(Sensor.TYPE_MAGNETIC_FIELD == SensorType && magnX.size() <= WINDOW_SIZE_MAG){
            this.magnX.add(Magnetometer.getGeomagnetic()[0]);
            this.magnY.add(Magnetometer.getGeomagnetic()[1]);
            this.magnZ.add(Magnetometer.getGeomagnetic()[2]);
        }

        if(this.accelX.size() >= WINDOW_SIZE_ACC && this.magnX.size() >= WINDOW_SIZE_MAG) {
            RunMovement runMovement = new RunMovement(accelX, accelY, accelZ, magnX, magnY, magnZ);

            executor.submit(runMovement);

            accelX.clear();
            accelY.clear();
            accelZ.clear();

            magnX.clear();
            magnY.clear();
            magnZ.clear();
        }

    }

    public class RunMovement implements Runnable {
        private ArrayList<Float> accelXClone = new ArrayList<>();
        private ArrayList<Float> accelYClone = new ArrayList<>();
        private ArrayList<Float> accelZClone = new ArrayList<>();

        private ArrayList<Float> magnXClone = new ArrayList<>();
        private ArrayList<Float> magnYClone = new ArrayList<>();
        private ArrayList<Float> magnZClone = new ArrayList<>();

        public RunMovement(ArrayList<Float> xA, ArrayList<Float> yA, ArrayList<Float> zA, ArrayList<Float> xM, ArrayList<Float> yM, ArrayList<Float> zM){
            this.accelXClone = (ArrayList<Float>) xA.clone();
            this.accelYClone = (ArrayList<Float>) yA.clone();
            this.accelZClone = (ArrayList<Float>) zA.clone();
            this.magnXClone = (ArrayList<Float>) xM.clone();
            this.magnYClone = (ArrayList<Float>) yM.clone();
            this.magnZClone = (ArrayList<Float>) zM.clone();
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            activityMonitoring.update( accelXClone, accelYClone, accelZClone);

            if (localizationMonitoring.update( accelXClone, accelYClone, accelZClone,
                    magnXClone, magnYClone, magnZClone)) {

                localizationView.setParticles(localizationMonitoring.getParticles());

                localizationView.post(new Runnable() {
                    public void run() {
                        localizationView.invalidate();
                    }
                });
            }
        }
    }
}
