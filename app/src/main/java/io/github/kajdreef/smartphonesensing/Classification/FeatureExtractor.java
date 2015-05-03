package io.github.kajdreef.smartphonesensing.Classification;

import java.util.ArrayList;
import java.util.List;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;

public abstract class FeatureExtractor {
    public abstract FeatureSet extractFeatures(ArrayList<Float> x,ArrayList<Float> y,ArrayList<Float> z);
    public LabeledFeatureSet addLabel(FeatureSet f, ActivityType l){
        return new LabeledFeatureSet(f,l);
    }
    public static ArrayList<LabeledFeatureSet> generateDataSet(ArrayList<ActivityType> labels,
                                                               ArrayList<Float> x,
                                                               ArrayList<Float> y,
                                                               ArrayList<Float> z,
                                                               FeatureExtractor extractor,
                                                               final int stepSize){
        //Extract LabeledFeatureSet
        ArrayList<LabeledFeatureSet> train = new ArrayList<>();
        int index = 0;
        while(index < x.size()-stepSize){
            if(labels.get(index) == labels.get(index+stepSize)) {
                ArrayList<Float> xlist =  new ArrayList<>(x.subList(index, index + stepSize));
                ArrayList<Float> ylist =  new ArrayList<>(y.subList(index, index + stepSize));
                ArrayList<Float> zlist =  new ArrayList<>(z.subList(index, index + stepSize));
                train.add(new LabeledFeatureSet(extractor.extractFeatures(xlist,ylist,zlist),labels.get(index)));
            }
            index = index+stepSize;
        }
        return train;
    }
}
