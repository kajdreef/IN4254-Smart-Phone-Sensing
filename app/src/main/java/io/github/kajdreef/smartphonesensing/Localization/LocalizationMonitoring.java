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

    // Data to calculate solution
    ArrayList<Float> aDataX;
    ArrayList<Float> aDataY;
    ArrayList<Float> aDataZ;

    ArrayList<Float> mDataX;
    ArrayList<Float> mDataY;
    ArrayList<Float> mDataZ;

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
        // Data to calculate solution
        aDataX = new ArrayList<>(WINDOW_SIZE);
        aDataY = new ArrayList<>(WINDOW_SIZE);
        aDataZ = new ArrayList<>(WINDOW_SIZE);
        mDataX = new ArrayList<>(WINDOW_SIZE);
        mDataY = new ArrayList<>(WINDOW_SIZE);
        mDataZ = new ArrayList<>(WINDOW_SIZE);
    }

    public FloorPlan getFloorPlan(){
        return this.floorPlan;
    }

    public ArrayList<Particle> getParticles(){
        return pf.getParticles();
    }

    public boolean update(int SensorType){

        if(Sensor.TYPE_ACCELEROMETER == SensorType && aDataX.size() < WINDOW_SIZE) {
            this.aDataX.add(Accelerometer.getGravity()[0]);
            this.aDataY.add(Accelerometer.getGravity()[1]);
            this.aDataZ.add(Accelerometer.getGravity()[2]);
            return false;
        }

        if(Sensor.TYPE_MAGNETIC_FIELD == SensorType && mDataX.size() <= WINDOW_SIZE){
            this.mDataX.add(Magnetometer.getGeomagnetic()[0]);
            this.mDataY.add(Magnetometer.getGeomagnetic()[1]);
            this.mDataZ.add(Magnetometer.getGeomagnetic()[2]);
            return false;
        }

        // If activity type == walking then update the location!
        if( mDataX.size() >= WINDOW_SIZE && aDataX.size() >= WINDOW_SIZE) {
            if (activityList.getType(activityList.size() - 1) == Type.WALK) {
                float angle = 0f;
                // Take average angle over Window size of samples.
                for (int i = 0; i < WINDOW_SIZE; i++) {
                    float[] gravity = {this.aDataX.get(i), this.aDataY.get(i), this.aDataZ.get(i)};
                    float[] mData = {this.mDataX.get(i), this.mDataY.get(i), this.mDataZ.get(i)};
                    angle += Magnetometer.calulateAngle(gravity, mData) / WINDOW_SIZE;
                }
                pf.movement(angle, 1f, WINDOW_SIZE);
                aDataX.clear();
                aDataY.clear();
                aDataZ.clear();
                mDataX.clear();
                mDataY.clear();
                mDataZ.clear();
                return true;
            }
        }
        return false;
    }
}
