package io.github.kajdreef.smartphonesensing.Sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.Utils.Writer;

/**
* Created by kajdreef on 17/05/15.
*/
public class Magnetometer extends AbstractSensor{

    private float[] geomagnetic = {0f,0f,0f};
    private Writer wr;

    public Magnetometer(SensorManager sm){
        super(sm);
        type = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        wr = new Writer("magnetometerData.txt");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        // Check if changed sensor is the Accelerometer.
        if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD){
            geomagnetic[0] = event.values[0];
            geomagnetic[1] = event.values[1];
            geomagnetic[2] = event.values[2];
            wr.appendData(geomagnetic[0], geomagnetic[1], geomagnetic[2], ActivityType.NONE);
        }
    }

    public float[] getGeomagnetic(){
        return geomagnetic;
    }

    public float[] calculateOrientation(float[] gravity){

        float[] orientation = {0f,0f,0f};
        float[] R = {0f,0f,0f,0f,0f,0f,0f,0f,0f};
        float[] I = {0f,0f,0f,0f,0f,0f,0f,0f,0f};

        SensorManager.getRotationMatrix(R, I, gravity, geomagnetic);
        SensorManager.getOrientation(R, orientation);
        Log.d("Magnetometer - ", "Orientation = " + orientation[0]+ ", " + orientation[1] + ", " + orientation[2]);

        return orientation;
    }
}
