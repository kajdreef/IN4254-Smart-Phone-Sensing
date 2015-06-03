package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.ParticleFilter;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Sensor.Accelerometer;
import io.github.kajdreef.smartphonesensing.Sensor.Magnetometer;
import io.github.kajdreef.smartphonesensing.Utils.AbstractReader;
import io.github.kajdreef.smartphonesensing.Utils.Reader;

/**
 * Created by kajdreef on 20/05/15.
 */
public class LocalizationMonitoring {

    private ActivityType activityList;
    private ParticleFilter pf;
    private FloorPlan floorPlan;
    private int WINDOW_SIZE;

    //Aquisition frequency
    private static final int f = 200;

    public LocalizationMonitoring(int numParticles, Context ctx){

        Resources res = ctx.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE);

        activityList = ActivityType.getInstance();

        // Initialise floorplan
        floorPlan = new FloorPlan();

        // Initialise the particle filter with numParticles.
        pf = new ParticleFilter(numParticles, floorPlan);
    }

    public FloorPlan getFloorPlan(){
        return this.floorPlan;
    }

    public ArrayList<Particle> getParticles(){
        return pf.getParticles();
    }

    public boolean update(ArrayList<Float> aDataX, ArrayList<Float> aDataY, ArrayList<Float> aDataZ,
                          ArrayList<Float> mDataX, ArrayList<Float> mDataY, ArrayList<Float> mDataZ){
        if (activityList.getType(activityList.size() - 1) == Type.WALK) {
            float angle = 0f;
            // Take average angle over Window size of samples.
            for (int i = 0; i < WINDOW_SIZE; i++) {
                float[] gravity = {aDataX.get(i), aDataY.get(i), aDataZ.get(i)};
                float[] mData = {mDataX.get(i), mDataY.get(i), mDataZ.get(i)};
                angle += Magnetometer.calulateAngle(gravity, mData) / WINDOW_SIZE;
            }
            pf.movement(angle, 1f, WINDOW_SIZE);

            return true;
        }
        return false;
    }
}
