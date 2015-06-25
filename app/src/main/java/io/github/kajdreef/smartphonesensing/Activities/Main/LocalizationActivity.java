package io.github.kajdreef.smartphonesensing.Activities.Main;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.kajdreef.smartphonesensing.Activities.Test.TestActivity;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationMonitoring;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationView.LocalizationView;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationView.WalkedPath;
import io.github.kajdreef.smartphonesensing.Localization.Particle;
import io.github.kajdreef.smartphonesensing.Localization.RunMovement;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.Sensor.Magnetometer;
import io.github.kajdreef.smartphonesensing.Sensor.WifiReceiver;

/**
 * Created by kajdreef on 15/05/15.
 */
public class LocalizationActivity extends Activity implements ObserverSensor {

    // Floorplan of the 9th floor of EWI
    private FloorPlan floorPlan;

    // GUI
    private LocalizationView localizationView;
    private Button initialBelief, startButton, stopButton, convButton;
    private TextView activityText;
    private LinearLayout localizationLayout;


    // Monitoring classes
    private ActivityMonitoring activityMonitoring;
    private LocalizationMonitoring localizationMonitoring;

    // Sensors
    private Accelerometer accelerometer;
    private Magnetometer magnetometer;
    private SensorManager sm;
    private WifiManager wifiManager;
    private WifiReceiver wifiReceiver;
    // Thread Queue
    private ExecutorService executor;

    // Activity List
    ActivityType activityList = ActivityType.getInstance();

    // Window size of the application.
    public int WINDOW_SIZE_ACC;
    public int WINDOW_SIZE_MAG;

    // Time started with filling window.
    private long timeStart;

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

        executor = Executors.newSingleThreadExecutor();

        floorPlan = new FloorPlan();

        // Get resources from the resource folder
        Resources res = this.getResources();
        WINDOW_SIZE_ACC = res.getInteger(R.integer.WINDOW_SIZE_ACC);
        WINDOW_SIZE_MAG = res.getInteger(R.integer.WINDOW_SIZE_MAG);

        activityMonitoring = new ActivityMonitoring(getApplicationContext());
        // Generate x amount of particles
        localizationMonitoring = new LocalizationMonitoring(1000,this.getApplicationContext());

        // Initialize Sensors;
        initSensors();

        // Initialize View;
        initView();
    }

    public void initView(){
        // Get window size;
        Point windowSize = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(windowSize);

        // Set layout
        setContentView(R.layout.localization_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Create the localization view with screen orientation in landscape and set it
        localizationView = new LocalizationView(this, floorPlan.getPath(), localizationMonitoring.getParticles(), windowSize.x, windowSize.y);

        // add the localization view (floorplan + particles) to the GUI
        localizationLayout = (LinearLayout)findViewById(R.id.floorPlan);
        localizationLayout.addView(localizationView);

        // Add functionality to the Button (Reset the particle filter)
        initialBelief = (Button) findViewById(R.id.initialBelief);
        initialBelief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wifiReceiver.getWifiPoints().isEmpty()){
                    localizationMonitoring.reset();
                }
                else{
                    registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
                    int size = wifiReceiver.getRSSI().size();

                    // set var == false;
                    wifiManager.startScan();

                    // if(var == true){do stuff}

                    Log.i("WIFITEST","done :"+ size + " "+ wifiReceiver.getRSSI().size());
                    //localizationMonitoring.initialBelief(wifiReceiver.getRSSI());
                    WalkedPath.getInstance().reset();
                }
                localizationView.setParticles(localizationMonitoring.getParticles());
                localizationView.reset();
                localizationView.post(new Runnable() {
                    @Override
                    public void run() {
                        localizationView.invalidate();
                    }
                });
            }
        });

        stopButton = (Button) findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accelerometer.unregister();
                magnetometer.unregister();
                try {
                    unregisterReceiver(wifiReceiver);
                }
                catch (Exception e){}
            }
        });

        startButton = (Button) findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accelerometer.register();
                magnetometer.register();
                registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            }
        });

        convButton = (Button) findViewById(R.id.forceConv);
        convButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread convThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Particle convergeLocation = localizationMonitoring.forceConverge();
                        Log.d("LocalizationActivity","Converge Location: " + convergeLocation.getCurrentLocation().getX() + ", " + convergeLocation.getCurrentLocation().getY() );
                        localizationView.setConvergeLocation(convergeLocation);
                        localizationView.post(new Runnable() {
                            @Override
                            public void run() {
                                localizationView.invalidate();
                            }
                        });
                    }
                });
                convThread.start();
            }
        });

    }


    /**
     * Initialise the sensors
     */
    public void initSensors() {
        // Init arrays were the sensor data is placed
        accelX = new ArrayList<>();
        accelY = new ArrayList<>();
        accelZ = new ArrayList<>();

        magnX = new ArrayList<>();
        magnY = new ArrayList<>();
        magnZ = new ArrayList<>();

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Create sensor instances
        accelerometer = new Accelerometer(sm);
        magnetometer = new Magnetometer(sm);

        accelerometer.attach(this);
        magnetometer.attach(this);

        //Init wifi
            wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            if(!wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(true);
            }
        wifiReceiver = new WifiReceiver(wifiManager);
            registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
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
        wifiReceiver.clear();
        try {
            unregisterReceiver(wifiReceiver);
        }
        catch (Exception e){
        }
    }


    public void update(final int SensorType) {

        if(this.accelX.size() == 0 && this.magnX.size() == 0){
            timeStart = System.currentTimeMillis();
        }

        // If the sensor type is Accelerometer than get the new data and put it in the array list.
        if (Sensor.TYPE_ACCELEROMETER == SensorType && accelX.size() <= WINDOW_SIZE_ACC) {
            this.accelX.add(Accelerometer.getGravity()[0]);
            this.accelY.add(Accelerometer.getGravity()[1]);
            this.accelZ.add(Accelerometer.getGravity()[2]);
        }

        if (Sensor.TYPE_MAGNETIC_FIELD == SensorType && magnX.size() <= WINDOW_SIZE_MAG) {
            this.magnX.add(Magnetometer.getGeomagnetic()[0]);
            this.magnY.add(Magnetometer.getGeomagnetic()[1]);
            this.magnZ.add(Magnetometer.getGeomagnetic()[2]);
        }

        if (this.accelX.size() >= WINDOW_SIZE_ACC && this.magnX.size() >= WINDOW_SIZE_MAG) {

            float deltaTime = (float)(Double.valueOf(System.currentTimeMillis() - timeStart)/1000d);

            Log.d("LocalizationActivity", "deltaTime = " + Double.toString(deltaTime) + " s");

            // Create runnable
            RunMovement runMovement = new RunMovement(accelX, accelY, accelZ, magnX, magnY, magnZ,
                                                activityMonitoring, localizationMonitoring, localizationView, deltaTime);

            // Add runnable to queue
            executor.submit(runMovement);
            if (activityMonitoring.getActivity() == Type.WALK){
                wifiManager.startScan();
            }
            // Clear data of accelerometer
            accelX.clear();
            accelY.clear();
            accelZ.clear();

            // Clear data of Magnetometer.
            magnX.clear();
            magnY.clear();
            magnZ.clear();

            activityText = (TextView) findViewById(R.id.activityText);
            activityText.setText(activityList.getLast().toString());
        }

    }
}
