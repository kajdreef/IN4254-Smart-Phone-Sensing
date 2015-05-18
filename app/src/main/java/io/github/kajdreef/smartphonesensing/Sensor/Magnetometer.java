package io.github.kajdreef.smartphonesensing.Sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

/**
* Created by kajdreef on 17/05/15.
*/
public class Magnetometer extends AbstractSensor{

    private static float azimuth, pitch, roll;

    public Magnetometer(SensorManager sm){
        super(sm);
        type = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public static float getAzimuth(){
        return azimuth;
    }
    public static float getPitch(){
        return pitch;
    }
    public static float getRoll(){
        return roll;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        // Check if changed sensor is the Accelerometer.
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            azimuth = event.values[0];
            pitch = event.values[1];
            roll = event.values[2];
        }
    }
}
