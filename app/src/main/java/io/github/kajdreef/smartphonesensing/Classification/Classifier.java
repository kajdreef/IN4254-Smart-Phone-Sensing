package io.github.kajdreef.smartphonesensing.Classification;

import java.util.List;

/**
 * Created by Uncle John on 26/04/2015.
 */
public interface Classifier {

    public void train(List<LabeledFeatureSet> trainingDataSet);
    public float test(List<LabeledFeatureSet> testDataSet);
    public List<Label> classifyList(List<FeatureSet> inputData);
}
