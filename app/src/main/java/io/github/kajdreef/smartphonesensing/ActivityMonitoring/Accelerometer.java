package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;


/**
 * Implementation of the accelerometer.
 * based on: http://webtutsdepot.com/2011/08/20/android-sdk-accelerometer-example-tutorial/
 *  And slides of Lecture 1.
 */
public class Accelerometer extends abstractSensor{

    public Accelerometer(SensorManager sm){
        super(sm);
        type = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    /**
     * TODO implement a way to save the data to a database??
     * @param x direction value of the accelerometer.
     * @param y direction value of the accelerometer.
     * @param z direction value of the accelerometer.
     */
    public void addToFile(double x, double y, double z){
        System.out.println("x-coord: " + x);
        System.out.println("y-coord: " + y);
        System.out.println("z-cooord: " + z);
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
            this.addToFile(x,y,z);
        }
    }
}
