package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Created by kajdreef on 23/04/15.
 * Abstract class for sensor implementation.
 */
public abstract class AbstractSensor implements SensorEventListener {

    protected Sensor type;
    protected SensorManager sm;

    public AbstractSensor(SensorManager sm){
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
