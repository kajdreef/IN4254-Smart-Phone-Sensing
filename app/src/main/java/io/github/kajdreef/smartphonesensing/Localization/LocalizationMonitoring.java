package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.ParticleFilter;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Sensor.Magnetometer;

/**
 * Created by kajdreef on 20/05/15.
 */
public class LocalizationMonitoring {

    private ActivityType activityList;
    private ParticleFilter pf;
    private FloorPlan floorPlan;
    private int WINDOW_SIZE_ACC;
    private int WINDOW_SIZE_MAG;

    //Aquisition frequency
    private static final int f = 200;

    public LocalizationMonitoring(int numParticles, Context ctx){

        Resources res = ctx.getResources();
        WINDOW_SIZE_ACC = res.getInteger(R.integer.WINDOW_SIZE_ACC);
        WINDOW_SIZE_MAG = res.getInteger(R.integer.WINDOW_SIZE_MAG);

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
            for (int i = 0; i < WINDOW_SIZE_MAG; i++) {
                float[] gravity = {aDataX.get(i), aDataY.get(i), aDataZ.get(i)};
                float[] mData = {mDataX.get(i), mDataY.get(i), mDataZ.get(i)};
                angle += Magnetometer.calulateAngle(gravity, mData) / WINDOW_SIZE_MAG;
            }
            pf.movement(angle, 1.4f, WINDOW_SIZE_ACC);

            return true;
        }
        return false;
    }
}
