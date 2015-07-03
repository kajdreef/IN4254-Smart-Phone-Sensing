package io.github.kajdreef.smartphonesensing.Utils;

import android.content.Context;

import java.io.DataInputStream;

/**
 * Created by kajdreef on 03/05/15.
 */
public class ReaderTest extends AbstractReader {
    public ReaderTest(Context ctx, int resourceId){
        super(ctx);
        fInpStream = new DataInputStream(ctx.getResources().openRawResource(resourceId));
    }
}
