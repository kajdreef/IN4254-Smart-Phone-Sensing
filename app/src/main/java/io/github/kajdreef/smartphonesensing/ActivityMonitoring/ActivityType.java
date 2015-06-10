package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import java.util.ArrayList;

/**
 * Created by kajdreef on 20/05/15.
 */
public class ActivityType {

    private ArrayList<Type> activityList;
    private ArrayList<Float> speedList;

    private static ActivityType singleton = null;

    private ActivityType(){
        this.activityList = new ArrayList<>();
        this.speedList = new ArrayList<>();
    }

    public static ActivityType getInstance(){
        if (singleton == null){
            singleton = new ActivityType();
        }
        return singleton;
    }

    public void empty(){
        activityList.clear();
        speedList.clear();
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

    public Type getLast(){
        if(activityList.size()>0)
            return activityList.get(this.activityList.size()-1);
        else
            return Type.NONE;
    }

    public ArrayList<Type> getTypeList(){
        return (ArrayList<Type>)activityList.clone();
    }

    public void addSpeed(float speed){
        speedList.add(speed);
    }

    public float getSpeed(int index){
        return speedList.get(index);
    }
}
