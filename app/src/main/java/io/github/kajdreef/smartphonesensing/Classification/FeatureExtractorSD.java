package io.github.kajdreef.smartphonesensing.Classification;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Utils.ArrayOperations;

public class FeatureExtractorSD extends FeatureExtractor {

    @Override
    public FeatureSet extractFeatures(ArrayList<Float> x, ArrayList<Float> y, ArrayList<Float> z) {
        ArrayList<Float> magnitude = new ArrayList<>(x.size());

        for (int i = 0;i<x.size();i++){
            magnitude.add(i,(float)Math.sqrt(Math.pow(x.get(i),2.0)+Math.pow(y.get(i),2.0)+Math.pow(z.get(i),2.0)));
        }

        double mean = ArrayOperations.sum(magnitude)/x.size();

        double SD = 0;

        for (int i = 0;i<x.size();i++){
            SD = SD + Math.pow(magnitude.get(i)-mean,2.0);
        }

        SD = SD/x.size();
        SD = Math.sqrt(SD);
        return new FeatureSet((float)SD);
    }
}
