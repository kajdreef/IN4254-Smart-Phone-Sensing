package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationView.WalkedPath;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.ParticleFilter;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Sensor.Magnetometer;
import io.github.kajdreef.smartphonesensing.Utils.ArrayOperations;

/**
 * Created by kajdreef on 20/05/15.
 */
public class LocalizationMonitoring {

    private ActivityType activityList;
    private ParticleFilter pf;
    private FloorPlan floorPlan;
    private WalkedPath walkedPath;
    private int WINDOW_SIZE_ACC;
    private int WINDOW_SIZE_MAG;

    private float angle = 0.0f;

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

    /**
     * Reset the localizationMonitoring
     */
    public void reset(){
        WalkedPath.getInstance().reset();
        pf.resetParticleFilter();
        activityList.empty();
    }

    public FloorPlan getFloorPlan(){
        return this.floorPlan;
    }

    public ArrayList<Particle> getParticles(){
        return pf.getParticles();
    }

    public float getAngle(){
        return this.angle;
    }

    /**
     * Update the particles based on the data from the sensors
     * @param aDataX
     * @param aDataY
     * @param aDataZ
     * @param mDataX
     * @param mDataY
     * @param mDataZ
     * @param time
     * @return
     */
    public boolean update(ArrayList<Float> aDataX, ArrayList<Float> aDataY, ArrayList<Float> aDataZ,
                          ArrayList<Float> mDataX, ArrayList<Float> mDataY, ArrayList<Float> mDataZ, float time){
        Type activity = activityList.getType(activityList.size() - 1);

        if (activity == Type.WALK || activity == Type.QUEUE ) {
            angle = 0f;
            // Take average angle over Window size of samples.
            for (int i = 0; i < WINDOW_SIZE_MAG; i++) {
                float[] gravity = {aDataX.get(i), aDataY.get(i), aDataZ.get(i)};
                float[] mData = {mDataX.get(i), mDataY.get(i), mDataZ.get(i)};
                angle += Magnetometer.calulateAngle(gravity, mData) / WINDOW_SIZE_MAG;
            }

            // If activity Type update the movement of partcicles
            if(activity == Type.WALK){
                pf.movement(angle, time);
               // Log.i("BP TEST", "x=" + pf.bestParticle().getCurrentLocation().getX() + "y=" + pf.bestParticle().getCurrentLocation().getY());
            }

            return true;
        }
        return false;
    }
    public Location hasConverged(){
        return pf.converged(3f);
    }

    public Particle forceConverge(){
        WalkedPath walkedPath = WalkedPath.getInstance();
        Particle bestParticle = pf.bestParticle();
        walkedPath.setPath(bestParticle.getCurrentLocation());
        return bestParticle;
    }

    public void initialBelief(ArrayList<ArrayList<Integer>> rssiData){
        pf.initialBelief(rssiData);
        activityList.empty();
    }
}
