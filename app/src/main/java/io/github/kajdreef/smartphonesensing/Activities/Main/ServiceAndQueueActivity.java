package io.github.kajdreef.smartphonesensing.Activities.Main;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.RunActivity;
import io.github.kajdreef.smartphonesensing.Sensor.AbstractSensor;
import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Utils.QueueMath;

/**
 * Created by kajdreef on 02/05/15.
 * Activity monitoring activity
 */
public class ServiceAndQueueActivity extends Activity implements ObserverSensor {
    private ActivityMonitoring am;
    private SensorManager sm;
    private AbstractSensor accelerometer;
    private int WINDOW_SIZE_ACC;
    private double WINDOW_TIME = 1.0d;

    private float serviceTime;
    private float queueTime;

    private ActivityType activityList;

    // GUI
    private Button start, stop;
    private TextView serviceText;
    private TextView queueText;
    private TextView stateText;


    private long timeStart;
    private long numSamples=0;

    // Thread Queue
    private ExecutorService executor;

    // Arrays with the accelerometer data
    private ArrayList<Float> accelX;
    private ArrayList<Float> accelY;
    private ArrayList<Float> accelZ;
    
    private void initAccelerometerAndButtons(){
        sm =(SensorManager)getSystemService(SENSOR_SERVICE);

        // Initialise the Activity Monitoring
        am = new ActivityMonitoring(this);

        executor = Executors.newSingleThreadExecutor();

        // Start accelerometer and attacht this Activity as Observer
        accelerometer = new Accelerometer(sm);
        accelerometer.attach(this);

        // Create walk button, when clicked on the button state will change state to WALK.
        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accelerometer.register();

                // Initialise displayed text to TTD
                serviceText = (TextView)findViewById(R.id.serviceData);
                serviceText.setText("TBD");
                queueText = (TextView)findViewById(R.id.queueData);
                queueText.setText("TBD");
                activityList.empty();

                serviceTime = 0.0f;
                queueTime = 0.0f;
            }
        });

        // Create queueing button, when clicked on the button state will change state to QUEUE.
        stop = (Button) findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accelerometer.unregister();

                float[] result = QueueMath.calculateSQTime(activityList.getTypeList(), (float)WINDOW_TIME);

                // Show calculated results on screen.
                serviceText = (TextView)findViewById(R.id.serviceData);
                serviceText.setText("" + result[0] + "s");
                queueText = (TextView)findViewById(R.id.queueData);
                queueText.setText("" + result[1] + "s");
            }
        });

        accelX = new ArrayList<>(WINDOW_SIZE_ACC);
        accelY = new ArrayList<>(WINDOW_SIZE_ACC);
        accelZ = new ArrayList<>(WINDOW_SIZE_ACC);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set window size and Window_time
        Resources res = this.getResources();
        WINDOW_SIZE_ACC = res.getInteger(R.integer.WINDOW_SIZE_ACC);

        setContentView(R.layout.service_queue_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        activityList = ActivityType.getInstance();

        initAccelerometerAndButtons();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        accelerometer.unregister();
    }

    @Override
    public void update(int SensorType){
        // If the sensor type is Accelerometer than get the new data and put it in the array list.
        if (Sensor.TYPE_ACCELEROMETER == SensorType && accelX.size() < WINDOW_SIZE_ACC) {
            if(this.accelX.size() == 0){
                timeStart = System.currentTimeMillis();
            }
            this.accelX.add(Accelerometer.getGravity()[0]);
            this.accelY.add(Accelerometer.getGravity()[1]);
            this.accelZ.add(Accelerometer.getGravity()[2]);
        }

        if (accelX.size() >= WINDOW_SIZE_ACC) {
            numSamples++;

            // calculates the time between last window size of data.
            double deltaTime = Double.valueOf(System.currentTimeMillis() - timeStart)/1000d;
            double samples = Double.valueOf(numSamples);

            if(deltaTime >= 1.2d * WINDOW_TIME){
                deltaTime = WINDOW_TIME;
            }

            WINDOW_TIME =  ((samples - 1d)/samples)*WINDOW_TIME + deltaTime/samples;

            Log.d("SQActivity", "deltaTime = " + Double.toString(deltaTime) + " s");
            Log.d("SQActivity", "Average time to fill window size = " + Double.toString(WINDOW_TIME) + " s");

            Runnable runActivity = new RunActivity(am, (ArrayList<Float>) accelX.clone(), (ArrayList<Float>) accelY.clone(), (ArrayList<Float>) accelZ.clone());
            executor.execute(runActivity);
            accelX.clear();
            accelY.clear();
            accelZ.clear();

            stateText = (TextView) this.findViewById(R.id.stateData);
            stateText.setText(activityList.getLast().toString());
        }
    }
}

