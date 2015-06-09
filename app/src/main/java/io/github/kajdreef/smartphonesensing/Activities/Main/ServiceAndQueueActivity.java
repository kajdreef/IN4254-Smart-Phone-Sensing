package io.github.kajdreef.smartphonesensing.Activities.Main;

import android.app.Activity;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
    private int amountOfNewSamples = 0;

    private float WINDOW_TIME = WINDOW_SIZE_ACC /200;

    private float serviceTime;
    private float queueTime;

    private ActivityType activityList;

    // GUI
    private Button start, stop;
    private TextView serviceText = (TextView) this.findViewById(R.id.serviceData);
    private TextView queueText = (TextView) this.findViewById(R.id.queueData);
    private TextView stateText = (TextView) this.findViewById(R.id.stateData);

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

        // Get needed file locations where data needs to be stored.
        Resources res = this.getResources();
        String acceleroFileLocation = res.getString(R.string.accelerometer_data_file);

        // Start accelerometer and attacht this Activity as Observer
        accelerometer = new Accelerometer(sm, acceleroFileLocation);
        accelerometer.attach(this);

        // Create walk button, when clicked on the button state will change state to WALK.
        start = (Button) findViewById(R.id.startButton);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accelerometer.register();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                float[] result = QueueMath.calculateSQTime(activityList.getTypeList(), WINDOW_SIZE_ACC);

                                serviceText.setText("TBD");

                                queueText.setText("TBD");
                            }
                        });
                    }
                });
                serviceTime = 0.0f;
                queueTime = 0.0f;
            }
        });

        // Create queueing button, when clicked on the button state will change state to QUEUE.
        stop = (Button) findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accelerometer.unregister();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                float[] result = QueueMath.calculateSQTime(activityList.getTypeList(), WINDOW_SIZE_ACC);

                                serviceText.setText("" + result[0] + "s");

                                queueText.setText("" + result[1] + "s");
                            }
                        });
                    }
                });
            }
        });

        accelX = new ArrayList<>(WINDOW_SIZE_ACC);
        accelY = new ArrayList<>(WINDOW_SIZE_ACC);
        accelZ = new ArrayList<>(WINDOW_SIZE_ACC);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set window size
        Resources res = this.getResources();
        WINDOW_SIZE_ACC = res.getInteger(R.integer.WINDOW_SIZE_ACC);

        setContentView(R.layout.service_queue_menu);
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
            this.accelX.add(Accelerometer.getGravity()[0]);
            this.accelY.add(Accelerometer.getGravity()[1]);
            this.accelZ.add(Accelerometer.getGravity()[2]);
        }

        if (accelX.size() >= WINDOW_SIZE_ACC) {
            Runnable runActivity = new RunActivity(am, (ArrayList<Float>) accelX.clone(), (ArrayList<Float>) accelY.clone(), (ArrayList<Float>) accelZ.clone(), stateText);
            executor.execute(runActivity);
            accelX.clear();
            accelY.clear();
            accelZ.clear();
        }

    }
}

