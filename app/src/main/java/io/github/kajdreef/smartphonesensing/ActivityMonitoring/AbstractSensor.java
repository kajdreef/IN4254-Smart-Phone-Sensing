package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;


/**
 * Created by kajdreef on 23/04/15.
 * Abstract class for sensor implementation.
 */
public abstract class AbstractSensor implements SensorEventListener {

    protected Sensor type;
    protected SensorManager sm;
    protected ArrayList<Observer> observerList;

    public AbstractSensor(SensorManager sm){
        this.sm = sm;
        this.observerList = new ArrayList<>();
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
    public void attach(Observer obs){
        this.observerList.add(obs);
    }

    /**
     * Remove class that needs to be notified when something happens.
     * @param obs
     */
    public void detach(Observer obs){
        this.observerList.remove(obs);
    }

    /**
     * Notify all classes in the ObserverList.
     */
    public void notifyObserver(){
        for(Observer  obs: observerList){
            obs.update();
        }
    }
}
