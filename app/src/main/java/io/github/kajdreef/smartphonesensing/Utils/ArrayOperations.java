package io.github.kajdreef.smartphonesensing.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Uncle John on 05/05/2015.
 */
public class ArrayOperations {
    public static float maximumValue(ArrayList<Float> in){
        if(in.isEmpty())
            return 0;
        float max = in.get(0);
        for(Float f:in){
            if(f>max)
                max = f;
        }
        return max;
    }
    public static float sum(List<Float> in){
        float s = 0 ;
        for(Float f : in){
            s += f;
        }
        return s;
    }
}
