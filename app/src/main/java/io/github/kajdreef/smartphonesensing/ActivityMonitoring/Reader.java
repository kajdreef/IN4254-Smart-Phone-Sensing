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
            result.add(Float.parseFloat(split[3]));                             // X
            result.add(Float.parseFloat(split[4]));                             // X

            Log.d("Reader", result.toString());
        }catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
