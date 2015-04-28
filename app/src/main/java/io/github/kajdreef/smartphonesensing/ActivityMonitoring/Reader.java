package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.os.Environment;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

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
        }
    }

    public String readString(){
        String str = "";
        try {
            str = fInpStream.readLine();
        }catch(IOException e){
            e.printStackTrace();
        }
        return str;
    }
}
