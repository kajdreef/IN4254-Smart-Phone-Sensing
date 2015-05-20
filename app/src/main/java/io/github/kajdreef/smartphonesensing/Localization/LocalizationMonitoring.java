package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.github.kajdreef.smartphonesensing.Activities.LocalizationActivity;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Observer;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.ParticleFilter;
import io.github.kajdreef.smartphonesensing.Sensor.Magnetometer;
import io.github.kajdreef.smartphonesensing.Utils.AbstractReader;
import io.github.kajdreef.smartphonesensing.Utils.Reader;

/**
 * Created by kajdreef on 20/05/15.
 */
public class LocalizationMonitoring implements Observer {

    private ActivityType activityList;
    private ParticleFilter pf;
    private FloorPlan floorPlan;
    private AbstractReader magnetometerReader;
    private AbstractReader accelerometerReader;

    public LocalizationMonitoring(int numParticles, Context ctx){
        magnetometerReader = new Reader(ctx, "magnetometerData.txt");
        accelerometerReader = new Reader(ctx, "accelerometerData.txt");

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

    @Override
    public void update(){
        // If activity type == walking then update the location!
        if(activityList.getType(activityList.size() - 1) == Type.WALK) {

            List<Float> mData = magnetometerReader.readMagnetometerData();
            accelerometerReader.readAccelerometerData();
            List<Float> aDataX = accelerometerReader.getSubListX(LocalizationActivity.WINDOW_SIZE);
            List<Float> aDataY = accelerometerReader.getSubListY(LocalizationActivity.WINDOW_SIZE);
            List<Float> aDataZ = accelerometerReader.getSubListZ(LocalizationActivity.WINDOW_SIZE);

            float angle = 0;

            // Take average angle over Window size of samples.
            for(int i = 0; i < mData.size(); i++){
                float[] gravity = {aDataX.get(i), aDataY.get(i), aDataZ.get(i)};
                angle = (Magnetometer.calulateAngle(gravity)/mData.size());
            }

            float speed = activityList.getSpeed(activityList.size() - 1);
            float dx = speed * (float) Math.cos(angle);
            float dy = speed * (float) Math.sin(angle);
            pf.movement(dx, dy);
        }
    }
}
