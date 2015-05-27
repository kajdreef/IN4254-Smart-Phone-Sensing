package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.github.kajdreef.smartphonesensing.Activities.LocalizationActivity;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Observer;
import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.ParticleFilter;
import io.github.kajdreef.smartphonesensing.R;
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
    private int WINDOW_SIZE;

    //Aquisition frequency
    private static final int f = 200;

    public LocalizationMonitoring(int numParticles, Context ctx){

        Resources res = ctx.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE);
        magnetometerReader = new Reader(ctx, res.getString(R.string.magnetometer_data_file));
        accelerometerReader = new Reader(ctx, res.getString(R.string.accelerometer_data_file));

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
            List<Float> mDataX = magnetometerReader.getSubListX(WINDOW_SIZE);
            List<Float> mDataY = magnetometerReader.getSubListY(WINDOW_SIZE);
            List<Float> mDataZ = magnetometerReader.getSubListZ(WINDOW_SIZE);
            List<Float> aDataX = accelerometerReader.getSubListX(WINDOW_SIZE);
            List<Float> aDataY = accelerometerReader.getSubListY(WINDOW_SIZE);
            List<Float> aDataZ = accelerometerReader.getSubListZ(WINDOW_SIZE);

            float angle = 0;

            // Take average angle over Window size of samples.
            for(int i = 0; i < WINDOW_SIZE; i++){
                float[] gravity = {aDataX.get(i), aDataY.get(i), aDataZ.get(i)};
                float[] mData = {mDataX.get(i), mDataY.get(i), mDataZ.get(i)};
                angle += Magnetometer.calulateAngle(gravity, mData)/WINDOW_SIZE;
            }

            pf.movement(angle, 1f, WINDOW_SIZE);
        }
    }
}
