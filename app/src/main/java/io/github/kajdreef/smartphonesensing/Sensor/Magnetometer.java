package io.github.kajdreef.smartphonesensing.Sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.util.Log;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Utils.Writer;

/**
* Created by kajdreef on 17/05/15.
*/
public class Magnetometer extends AbstractSensor{

    private Writer wr;
    private static float[] geomagnetic = new float[3];
    int numSamples =0;

    public Magnetometer(SensorManager sm, String fileLocation){
        super(sm);
        type = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        wr = new Writer(fileLocation);
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
//            wr.appendData(geomagnetic[0],geomagnetic[1],geomagnetic[2], Type.NONE);
            this.notifyObserver(Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    public static float[] getGeomagnetic(){
        return geomagnetic;
    }

    /**
     * Calculate the angle between the North and the direction you are going
     * @param gravity
     * @return yaw (in degrees)
     */
    public static float calulateAngle(float[] gravity,float[] geomagnetic){
        float[] m_rotationMatrix = new float[16];
        float[] orientation = new float[3];

        SensorManager.getRotationMatrix(m_rotationMatrix, null, gravity, geomagnetic);
        SensorManager.getOrientation(m_rotationMatrix, orientation);

        float yaw = (float) (Math.toDegrees(orientation[0]));
        return yaw;
    }
}
