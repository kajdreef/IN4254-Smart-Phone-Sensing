package io.github.kajdreef.smartphonesensing.Classification;

import java.util.ArrayList;

public abstract class FeatureExtractor {
    public abstract FeatureSet extractFeatures(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z);
    public LabeledFeatureSet addLabel(FeatureSet f, Label l){
        return new LabeledFeatureSet(f,l);
    }
}
