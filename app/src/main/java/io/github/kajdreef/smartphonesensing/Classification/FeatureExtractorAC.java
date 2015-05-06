package io.github.kajdreef.smartphonesensing.Classification;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Utils.ArrayOperations;

public class FeatureExtractorAC extends FeatureExtractor {

    @Override
    public FeatureSet extractFeatures(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z){
        ArrayList<Float> magnitude = new ArrayList<>(x.size());

        for (int i = 0;i<x.size();i++){
            magnitude.add(i,(float)Math.sqrt(Math.pow(x.get(i),2.0)+Math.pow(y.get(i),2.0)+Math.pow(z.get(i),2.0)));
        }
        float mean = new FeatureExtractorMean().extractFeatures(x,y,z).getData();
        float sigma = new FeatureExtractorSD().extractFeatures(x,y,z).getData();
        ArrayList<Float> out = new ArrayList<>(magnitude.size());
        for (int i = 0; i < magnitude.size(); i++) {
            out.add((float)0);
        }
        for (int i = 0; i < out.size(); i++) {
            for (int j = 0; j < magnitude.size()-i; j++) {
                out.set(i,out.get(i)+(magnitude.get(j)-mean)*(magnitude.get(i+j)-mean));
            }
            out.set(i,out.get(i)/(magnitude.size()-i)/sigma/sigma);
        }
        out.remove(0);
        return new FeatureSet(ArrayOperations.sum(out.subList(9,11)));
    }
}
