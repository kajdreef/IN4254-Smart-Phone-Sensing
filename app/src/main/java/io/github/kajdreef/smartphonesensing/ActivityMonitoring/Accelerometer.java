package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;


/**
 * Implementation of the accelerometer.
 * It acquires the data and then writes it to a file.
 */
public class Accelerometer extends abstractSensor{

    private writer wr;

    public Accelerometer(SensorManager sm){
        super(sm);
        wr = new writer("accelerometerData.txt");
        type = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        double x,y,z;

        // Check if changed sensor is the Accelerometer.
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            x=event.values[0];
            y=event.values[1];
            z=event.values[2];

            // Add data to File
            wr.appendData(x,y,z);
        }
    }
}
