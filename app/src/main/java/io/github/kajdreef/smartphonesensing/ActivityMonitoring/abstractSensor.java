package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


public abstract class abstractSensor implements SensorEventListener {

    protected Sensor type;
    protected SensorManager sm;

    public abstractSensor(SensorManager sm){
        this.sm = sm;
    }

    /**
     * Register the event listener for certain sensor.
     */
    public void register(){
        sm.registerListener(this, type, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Unregister the event listener for certain sensor.
     */
    public void unregister(){
        sm.unregisterListener(this);
    }
}
