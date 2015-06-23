package io.github.kajdreef.smartphonesensing.Utils;

import java.lang.reflect.Array;
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
    public static float maximumValueFrom(int i,float[] in){
        float max = in[i];
        for(int index = i;index<in.length;index++){
            if(in[index]>max)
                max = in[index];
        }
        return max;
    }
    public static int indexFirstMaximumFrom(int i,ArrayList<Float> in){
        float max = in.get(i);
        int maxIndex = i;
        for(int index = i;index<in.size();index++){
            if(in.get(index)>max) {
                max = in.get(index);
                maxIndex = index;
            }
        }
        return maxIndex;
    }
    public static float sum(List<Float> in){
        float s = 0 ;
        for(Float f : in){
            s += f;
        }
        return s;
    }

    public static float mean(List<Float> in){
        return sum(in)/in.size();
    }

    public static float standardDeviation(List<Float> in){
        float mean = ArrayOperations.sum(in)/in.size();

        double SD = 0;

        for (int i = 0;i<in.size();i++){
            SD = SD + Math.pow(in.get(i)-mean,2.0);
        }

        SD = SD/in.size();
        SD = Math.sqrt(SD);
        return (float) SD;
    }
}
