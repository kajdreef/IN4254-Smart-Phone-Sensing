package io.github.kajdreef.smartphonesensing.Sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Utils.Writer;


/**
 * Created by kajdreef on 23/04/15.
 *
 * Implementation of the accelerometer.
 * It acquires the data and then writes it to a file.
 */
public class Accelerometer extends AbstractSensor {

    private Writer wr;
    private static float[] gravity = {0f,0f,0f};
    private int numSamples = 0;

    public Accelerometer(SensorManager sm){
        super(sm);
        type = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(type != null){
            Log.d("Accelerometer", "Sensor is initialized.");
            sensorAvailable = true;
        }
        else{
            Log.d("Accelerometer", "Sensor is not available.");
            sensorAvailable = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        // Check if changed sensor is the Accelerometer.
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            gravity[0] = event.values[0];
            gravity[1] = event.values[1];
            gravity[2] = event.values[2];

            this.notifyObserver(Sensor.TYPE_ACCELEROMETER);
        }
    }

    public static float[] getGravity(){
        return gravity;
    }
}
