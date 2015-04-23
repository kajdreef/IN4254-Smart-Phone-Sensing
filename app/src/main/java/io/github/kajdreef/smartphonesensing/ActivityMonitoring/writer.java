package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.os.Environment;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
* Writes data to the file specified.
*/
public class writer {
    private static final String wr_e = "Writer";

    private File file;
    private int i = 0;
    private DataOutputStream fOutStream;

    public writer(String fileName){
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
        file.setWritable(true);

        try {
            fOutStream = new DataOutputStream(new FileOutputStream(file));
            Log.d(wr_e, file.getAbsolutePath());
        } catch(FileNotFoundException e ){
            e.printStackTrace();
        }
    }

    public void appendData(double x, double y, double z,  ActivityType state) {
        try {
            fOutStream.write(("x: \t " + state.toString() + "\t" + x + "\n").getBytes());
            fOutStream.write(("y: \t " + state.toString() + "\t" + y + "\n").getBytes());
            fOutStream.write(("z: \t " + state.toString() + "\t" + z + "\n").getBytes());
            fOutStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}