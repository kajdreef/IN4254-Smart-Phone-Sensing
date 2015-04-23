package io.github.kajdreef.smartphonesensing.ActivityMonitoring;


import android.os.Environment;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.String;

/**
 * Writes data to the file specified.
 */
public class writer {
    private static final String wr_e = "Writer";

    File file;
    String fileName;
    String filePath;
    DataOutputStream fOutStream = null;

    public writer(String fileName){
        this.fileName = fileName;
        this.filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+ "/" + fileName;
        file = new File(filePath);
        file.setWritable(true);
    }

    /**
     * Write data to the created file.
     */
    public void appendData(double x, double  y, double z){
        try {
            fOutStream = new DataOutputStream(new FileOutputStream(filePath));
            fOutStream.writeDouble(x);
            fOutStream.flush();
        } catch (FileNotFoundException e) {
                e.printStackTrace();

        } catch (IOException e) {
                e.printStackTrace();
        }

        System.out.println("x-coord: " + x);
        System.out.println("y-coord: " + y);
        System.out.println("z-coord: " + z);
    }
}
