package io.github.kajdreef.smartphonesensing.Utils;

import android.content.Context;
import android.os.Environment;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by kajdreef on 29/04/15.
 *
 * Able to read the data from a .txt file line by line.
 */
public class Reader extends AbstractReader{

    public Reader(Context ctx, String newFileName) {
        super(ctx);
        this.fileName = newFileName;
        try {
            fInpStream = new DataInputStream(new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
