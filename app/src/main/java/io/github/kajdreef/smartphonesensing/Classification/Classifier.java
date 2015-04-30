package io.github.kajdreef.smartphonesensing.Classification;

import java.util.List;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;

/**
 * Created by Uncle John on 26/04/2015.
 */
public interface Classifier {

    public void addTrainingData(List<LabeledFeatureSet> trainingDataSet);
    public float test(List<LabeledFeatureSet> testDataSet);
    public List<ActivityType> classifyList(List<FeatureSet> inputData);
}
