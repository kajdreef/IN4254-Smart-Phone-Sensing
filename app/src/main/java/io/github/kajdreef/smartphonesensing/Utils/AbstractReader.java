package io.github.kajdreef.smartphonesensing.Utils;

import android.content.Context;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    protected AbstractReader(Context ctx){
        allStates = new ArrayList<>();
        allX = new ArrayList<>();
        allY = new ArrayList<>();
        allZ = new ArrayList<>();
    }


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

            result.add(Float.parseFloat(split[2]));                             // X
            result.add(Float.parseFloat(split[3]));                             // Y
            result.add(Float.parseFloat(split[4]));                             // Z

        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }

    public void readAll(){
        String str = "";
        String[] split;
        try {
            while(this.available()) {
                str = fInpStream.readLine();
                if (str == null) {
                    return;
                }
                split = str.split(" ");
                allStates.add(ActivityType.fromString(split[1]));               // Label
                allX.add(Float.parseFloat(split[2]));                           // X
                allY.add(Float.parseFloat(split[3]));                           // Y
                allZ.add(Float.parseFloat(split[4]));                           // Z
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public int size(){
        return allStates.size();
    }

    public void empty(){
        allStates.clear();
        allX.clear();
        allY.clear();
        allZ.clear();
    }

    public void emptyStates(){
        allStates.clear();
    }
    public void emptyX(){
        allX.clear();
    }
    public void emptyY(){
        allY.clear();
    }
    public void emptyZ(){
        allZ.clear();
    }

    public ArrayList<ActivityType> getAllStates(){ return allStates;}
    public ArrayList<Float> getAllX(){return allX;}
    public ArrayList<Float> getAllY(){return allY;}
    public ArrayList<Float> getAllZ(){return allZ;}

    public List<ActivityType> getSubListStates(int windowSize){
        int size = allStates.size();
        return allStates.subList(size-windowSize,size);
    }
    public List<Float> getSubListX(int windowSize){
        int size = allX.size();
        return allX.subList(size-windowSize,size);
    }
    public List<Float> getSubListY(int windowSize){
        int size = allY.size();
        return allY.subList(size-windowSize,size);
    }
    public List<Float> getSubListZ(int windowSize){
        int size = allZ.size();
        return allZ.subList(size-windowSize,size);
    }
}
