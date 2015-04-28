package io.github.kajdreef.smartphonesensing.Classification.TestClassification;

import android.test.ActivityTestCase;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureSet;
import io.github.kajdreef.smartphonesensing.Classification.KNN;
import io.github.kajdreef.smartphonesensing.Classification.Label;
import io.github.kajdreef.smartphonesensing.Classification.LabeledFeatureSet;

public class TestKNN extends ActivityTestCase {
    private KNN knnc1;
    private KNN knnc3;
    private KNN knnc5;
    private KNN knnc7;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Test
        ArrayList<LabeledFeatureSet> trainingSet = new ArrayList<>();
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(0), Label.WALKING));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(1), Label.WALKING));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(2), Label.WALKING));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(3), Label.WALKING));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(5), Label.STANDING));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(5), Label.STANDING));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(6), Label.STANDING));
        this.knnc1 = new KNN(1, trainingSet);
        this.knnc3 = new KNN(3, trainingSet);
        this.knnc5 = new KNN(5, trainingSet);
        this.knnc7 = new KNN(7, trainingSet);
    }
    public void testClassify(){
        assertNotSame(Label.WALKING,Label.STANDING);
        assertEquals(knnc1.classify(new FeatureSet((float) 0)), (Label.WALKING));
        assertEquals(knnc1.classify(new FeatureSet((float) 3)), (Label.WALKING));
        assertEquals(knnc1.classify(new FeatureSet((float) 5.5)), (Label.STANDING));
        assertEquals(knnc3.classify(new FeatureSet((float) 1.5)), (Label.WALKING));
        assertEquals(knnc3.classify(new FeatureSet((float) 3.5)), (Label.WALKING));
        assertEquals(knnc3.classify(new FeatureSet((float) 5)), (Label.STANDING));
        assertEquals(knnc5.classify(new FeatureSet((float) 1)), (Label.WALKING));
        assertEquals(knnc5.classify(new FeatureSet((float) 4)), (Label.STANDING));
        assertEquals(knnc5.classify(new FeatureSet((float) 8)), (Label.STANDING));
        assertEquals(knnc7.classify(new FeatureSet((float) 0)), (Label.WALKING));
        assertEquals(knnc7.classify(new FeatureSet((float) 8)), (Label.WALKING));
    }
}