package io.github.kajdreef.smartphonesensing.Sensor;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ObserverSensor;

/**
 * Created by kajdreef on 23/04/15.
 * Abstract class for sensor implementation.
 */
public abstract class AbstractSensor implements SensorEventListener {

    protected Sensor type;
    protected SensorManager sm;
    protected ArrayList<ObserverSensor> observerSensorList;

    public AbstractSensor(SensorManager sm){
        this.sm = sm;
        this.observerSensorList = new ArrayList<>();
    }

    /**
     * Register the event listener for certain sensor.
     */
    public void register(){
        sm.registerListener(this, type, SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Unregister the event listener for certain sensor.
     */
    public void unregister(){
        sm.unregisterListener(this);
    }

    /**
     * Add class that needs to be notified when something happens.
     * @param obs
     */
    public void attach(ObserverSensor obs){
        this.observerSensorList.add(obs);
    }

    /**
     * Remove class that needs to be notified when something happens.
     * @param obs
     */
    public void detach(ObserverSensor obs){
        this.observerSensorList.remove(obs);
    }

    /**
     * Notify all classes in the ObserverList.
     */
    public void notifyObserver(int SensorType){
        for(ObserverSensor obs: observerSensorList){
            obs.update(SensorType);
        }
    }
}
