package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import java.util.ArrayList;

/**
 * Created by kajdreef on 20/05/15.
 */
public class ActivityType {

    private ArrayList<Type> activityList;
    private ArrayList<Float> speedList;

    private static ActivityType singleton = null;

    private void ImplActivityType(){
        this.activityList = new ArrayList<>();
    }

    public static ActivityType getInstance(){
        if (singleton == null){
            singleton = new ActivityType();
        }
        return singleton;
    }

    public int size(){
        return activityList.size();
    }

    public void addType(Type label){
        activityList.add(label);
    }

    public Type getType(int index){
        return activityList.get(index);
    }

    public ArrayList<Type> getTypeList(){
        return activityList;
    }

    public void addSpeed(float speed){
        speedList.add(speed);
    }

    public float getSpeed(int index){
        return speedList.get(index);
    }
}
