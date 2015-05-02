package io.github.kajdreef.smartphonesensing.Utils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;

/**
 * Created by kajdreef on 01/05/15.
 */
public abstract class AbstractReader {


    protected String fileName;
    protected DataInputStream fInpStream = null;

    protected ArrayList<ActivityType> allStates;
    protected ArrayList<Float> allX;
    protected ArrayList<Float> allY;
    protected ArrayList<Float> allZ;


    public boolean available(){
        try {
            return (fInpStream.available() > 0);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return false;
    }

    public ArrayList<Float> readString(){
        String str = "";
        String[] split;
        ArrayList<Float> result = new ArrayList<>();
        try {
            str = fInpStream.readLine();
            if (str == null) {
                return null;
            }
            split = str.split(" ");

            result.add((float) ActivityType.fromString(split[1]).ordinal());     // State
            result.add(Float.parseFloat(split[2]));                             // X
            result.add(Float.parseFloat(split[3]));                             // Y
            result.add(Float.parseFloat(split[4]));                             // Z

        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public void readAll(){
        allStates = new ArrayList<>();
        allX = new ArrayList<>();
        allY = new ArrayList<>();
        allZ = new ArrayList<>();

        while(available()){
            ArrayList<Float> str = readString();
            allStates.add(ActivityType.fromInt(str.get(0).intValue()));
            allX.add(str.get(1));
            allY.add(str.get(2));
            allZ.add(str.get(3));
        }
    }

    public ArrayList<ActivityType> getAllStates(){ return allStates;}
    public ArrayList<Float> getAllX(){return allX;}
    public ArrayList<Float> getAllY(){return allY;}
    public ArrayList<Float> getAllZ(){return allZ;}
}
