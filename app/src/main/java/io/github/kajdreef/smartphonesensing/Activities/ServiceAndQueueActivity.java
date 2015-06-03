package io.github.kajdreef.smartphonesensing.Activities;

import android.content.res.Resources;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.Sensor.AbstractSensor;
import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityMonitoring;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;
import io.github.kajdreef.smartphonesensing.R;

/**
 * Created by kajdreef on 02/05/15.
 * Activity monitoring activity
 */
public class ServiceAndQueueActivity extends ActionBarActivity implements ObserverSensor {
    ActivityMonitoring am;
    SensorManager sm;
    AbstractSensor accelerometer;
    public int WINDOW_SIZE;
    private int amountOfNewSamples = 0;

    float WINDOW_TIME = WINDOW_SIZE/200;

    float serviceTime;
    float queueTime;

    ActivityType activityList;

    // Button for on the screen;
    Button start, stop;

    private void initAccelerometerAndButtons(){
        // Initialise the Activity Monitoring
        am = new ActivityMonitoring(this);

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
                serviceTime = 0.0f;
                queueTime = 0.0f;
            }
        });

        // Create queueing button, when clicked on the button state will change state to QUEUE.
        stop = (Button) findViewById(R.id.stopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                accelerometer.unregister();
                calculateSQTime(activityList.getTypeList());
                update(0);
            }
        });
    }

    public void calculateSQTime(ArrayList<Type> activityList){
        float time = 0;
        float result = 0;
        // amount of moving forward steps during the queue.
        int steps = 0;
        Type previous = Type.NONE;
        for(Type at : activityList) {
            // As long as time is lower than 10.0f we can still be in the queue.
            if (time < 10.0f){
                if (at == Type.QUEUE) {
                    // Update the total queuing time
                    result+= WINDOW_TIME;
                    time = 0;
                    previous = Type.QUEUE;
                } else if(result > 0){
                    // update the walking time if bigger than 10 seconds than we are out of the queue.
                    time += WINDOW_TIME;
                    // Check if the previous one was NOT walk so it can be seen as 1 step.
                    if(previous != Type.WALK)
                        steps++;
                    previous = Type.WALK;
                }
            }
            else
                break;
        }
        queueTime = result;
        serviceTime = result/steps;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources res = this.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE_ACC);
        setContentView(R.layout.service_queue_menu);
        sm =(SensorManager)getSystemService(SENSOR_SERVICE);

        activityList = ActivityType.getInstance();
        initAccelerometerAndButtons();
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

    @Override
    public void update(int SensorType){
        // First update am so the new speed and activity is available
//        am.update(SensorType);


        TextView stateText = (TextView) this.findViewById(R.id.stateData);
        stateText.setText(am.getActivity().toString());

        TextView serviceText = (TextView) this.findViewById(R.id.serviceData);
        serviceText.setText("" + serviceTime + "s");

        TextView queueText = (TextView) this.findViewById(R.id.queueData);
        queueText.setText("" + queueTime + "s");
    }
}
