package io.github.kajdreef.smartphonesensing.Sensor;

import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import io.github.kajdreef.smartphonesensing.Activities.ActivityMonitoringActivity;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Utils.Writer;


/**
 * Created by kajdreef on 23/04/15.
 *
 * Implementation of the accelerometer.
 * It acquires the data and then writes it to a file.
 */
public class Accelerometer extends AbstractSensor {

    private Writer wr;
    private static Type state = Type.NONE;
    private float[] gravity = {0f,0f,0f};

    public static void setState(Type newState){
        Accelerometer.state = newState;
    }

    public static Type getState(){
        return Accelerometer.state;
    }

    public Accelerometer(SensorManager sm, String fileLocation){
        super(sm);



        wr = new Writer(fileLocation);
        type = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        // Check if changed sensor is the Accelerometer.
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            gravity[0] =event.values[0];
            gravity[1] =event.values[1];
            gravity[2] =event.values[2];

            // Add data to File (Accelerometer state is NONE when not changed for )
            wr.appendData(gravity[0], gravity[1], gravity[2], Accelerometer.state);
            this.notifyObserver();
        }
    }

    public float[] getGravity(){
        return gravity;
    }
}
