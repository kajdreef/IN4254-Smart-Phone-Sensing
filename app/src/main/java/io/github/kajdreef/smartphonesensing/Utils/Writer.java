package io.github.kajdreef.smartphonesensing.Utils;

import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;

/**
 * Created by kajdreef on 23/04/15.
 * Writes data to the file specified.
 */
public class Writer {
    private static final String wr_e = "Writer";

    private File file;
    private DataOutputStream fOutStream;
    private Date date;
    private Date startTime;

    public Writer(String fileName){
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
        file.setReadable(true);

        this.date = new Date();
        this.startTime = new Date();

        try {
            fOutStream = new DataOutputStream(new FileOutputStream(file));
            Log.d(wr_e, file.getAbsolutePath());
        } catch(FileNotFoundException e ){
            e.printStackTrace();
        }
    }

    /**
     * Used to write data for the accelerometer
     * @param x
     * @param y
     * @param z
     * @param state
     */
    public void appendData(float x, float y, float z,  Type state) {
        try{
            date = new Date();
            long delta = date.getTime() - startTime.getTime();

            fOutStream.write((delta + " " + state.toString() + " " + x + " " + y + " " + z + "\n").getBytes());
            //Log.d("Writer", delta + " "+ state.toString() + " " + x + " " + y + " " + z + "\n");
            fOutStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Can be used to write everything in string form to a file.
     * @param msg
     */
    public void appendString(String msg){
        try{
            fOutStream.write((msg + "\n").getBytes());
            //Log.d("Writer", delta + " "+ state.toString() + " " + x + " " + y + " " + z + "\n");
            fOutStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}