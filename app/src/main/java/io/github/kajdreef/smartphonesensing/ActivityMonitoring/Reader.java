package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kajdreef on 29/04/15.
 *
 * Able to read the data from a .txt file line by line.
 */
public class Reader {

    private String fileName;
    private DataInputStream fInpStream = null;

    private ArrayList<Integer> allStates;
    private ArrayList<Float> allX;
    private ArrayList<Float> allY;
    private ArrayList<Float> allZ;

    public Reader(String newFileName) {
        this.fileName = newFileName;
        try {
            fInpStream = new DataInputStream(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName));
        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.exit(0);
        }
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

            result.add((float) ActivityType.fromString(split[1]).ordinal());     // State
            result.add(Float.parseFloat(split[2]));                             // X
            result.add(Float.parseFloat(split[3]));                             // Y
            result.add(Float.parseFloat(split[4]));                             // Z

            Log.d("Reader", result.toString());
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
            allStates.add(str.get(0).intValue());
            allX.add(str.get(1));
            allY.add(str.get(2));
            allZ.add(str.get(3));
        }
    }

    public ArrayList<Integer> getAllStates(){ return allStates;}
    public ArrayList<Float> getAllX(){return allX;}
    public ArrayList<Float> getAllY(){return allY;}
    public ArrayList<Float> getAllZ(){return allZ;}
}
