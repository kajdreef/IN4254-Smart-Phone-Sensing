package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

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

        this.date = new Date();
        this.startTime = new Date();

        try {
            fOutStream = new DataOutputStream(new FileOutputStream(file));
            Log.d(wr_e, file.getAbsolutePath());
        } catch(FileNotFoundException e ){
            e.printStackTrace();
        }
    }

    public void appendData(double x, double y, double z,  ActivityType state) {
        try{
            date = new Date();
            long delta = date.getTime() - startTime.getTime();

            fOutStream.write((delta + " " + state.toString() + " " + x + " " + y + " " + z + "\n").getBytes());
            Log.d("Writer", delta + " "+ state.toString() + " " + x + " " + y + " " + z + "\n");
            fOutStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}