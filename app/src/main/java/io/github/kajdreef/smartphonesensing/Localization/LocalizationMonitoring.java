package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    //Aquisition frequency
    private static final int f = 200;

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

            magnetometerReader.readData();
            accelerometerReader.readData();
            List<Float> mDataX = magnetometerReader.getSubListX(LocalizationActivity.WINDOW_SIZE);
            List<Float> mDataY = magnetometerReader.getSubListY(LocalizationActivity.WINDOW_SIZE);
            List<Float> mDataZ = magnetometerReader.getSubListZ(LocalizationActivity.WINDOW_SIZE);
            List<Float> aDataX = accelerometerReader.getSubListX(LocalizationActivity.WINDOW_SIZE);
            List<Float> aDataY = accelerometerReader.getSubListY(LocalizationActivity.WINDOW_SIZE);
            List<Float> aDataZ = accelerometerReader.getSubListZ(LocalizationActivity.WINDOW_SIZE);

            float angle = 0;

            // Take average angle over Window size of samples.
            for(int i = 0; i < LocalizationActivity.WINDOW_SIZE; i++){
                float[] gravity = {aDataX.get(i), aDataY.get(i), aDataZ.get(i)};
                float[] mData = {mDataX.get(i), mDataY.get(i), mDataZ.get(i)};
                angle += Magnetometer.calulateAngle(gravity, mData)/LocalizationActivity.WINDOW_SIZE;
            }
            //Gaussian distribution of mean 1 and SD 0.2m/s
            float v = 1f + (float) new Random().nextGaussian()*0.2f;
            //Gaussian distribution of mean alpha and SD alphaDeviation
            int alphaDeviation = 20;
            float alpha = angle+floorPlan.getNorthAngle()+90
                    + (float) new Random().nextGaussian()*alphaDeviation;
            float dx = v*LocalizationActivity.WINDOW_SIZE * (float) Math.cos(alpha)/f;
            float dy = v*LocalizationActivity.WINDOW_SIZE * (float) Math.sin(alpha)/f;
            pf.movement(dx, dy);
        }
    }
}
