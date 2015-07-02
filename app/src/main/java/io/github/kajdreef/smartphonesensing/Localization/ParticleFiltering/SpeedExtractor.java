package io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorMean;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorSD;
import io.github.kajdreef.smartphonesensing.Utils.ArrayOperations;

/**
 * Created by Uncle John on 15/05/2015.
 */
public class SpeedExtractor {
    public static final int INDEX_FROM = 40;
    public static final float STEP_DISTANCE = (float)0.8;
    public static final float dt = (float)0.005;
    public static float calculateSpeed(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z){
        ArrayList<Float> magnitude = new ArrayList<>(x.size());

        for (int i = 0;i<x.size();i++){
            magnitude.add(i,(float)Math.sqrt(Math.pow(x.get(i),2.0)+Math.pow(y.get(i),2.0)+Math.pow(z.get(i),2.0)));
        }
        float mean = new FeatureExtractorMean().extractFeatures(x,y,z).getData().get(0);
        float sigma = new FeatureExtractorSD().extractFeatures(x,y,z).getData().get(0);
        ArrayList<Float> out = new ArrayList<>(magnitude.size());
        for (int i = 0; i < magnitude.size(); i++) {
            out.add((float)0);
        }
        for (int i = 0; i < out.size()-1; i++) {
            for (int j = 0; j < magnitude.size()-i; j++) {
                out.set(i,out.get(i)+(magnitude.get(j)-mean)*(magnitude.get(i+j)-mean));
            }
            out.set(i,out.get(i)/(magnitude.size()-i)/sigma/sigma);
        }
        int maxIndex = ArrayOperations.indexFirstMaximumFrom(INDEX_FROM,out);
        return STEP_DISTANCE/(maxIndex*dt);
    }
}
