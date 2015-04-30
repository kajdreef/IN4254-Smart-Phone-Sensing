package io.github.kajdreef.smartphonesensing.Classification.TestClassification;

import android.test.ActivityTestCase;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.Classification.FeatureSet;
import io.github.kajdreef.smartphonesensing.Classification.KNN;
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
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(0), ActivityType.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(1), ActivityType.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(2), ActivityType.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(3), ActivityType.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(5), ActivityType.QUEUE));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(5), ActivityType.QUEUE));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(6), ActivityType.QUEUE));
        this.knnc1 = new KNN(1, trainingSet);
        this.knnc3 = new KNN(3, trainingSet);
        this.knnc5 = new KNN(5, trainingSet);
        this.knnc7 = new KNN(7, trainingSet);
    }
    public void testClassify(){
        assertNotSame(ActivityType.WALK,ActivityType.QUEUE);
        assertEquals(knnc1.classify(new FeatureSet((float) 0)), (ActivityType.WALK));
        assertEquals(knnc1.classify(new FeatureSet((float) 3)), (ActivityType.WALK));
        assertEquals(knnc1.classify(new FeatureSet((float) 5.5)), (ActivityType.QUEUE));
        assertEquals(knnc3.classify(new FeatureSet((float) 1.5)), (ActivityType.WALK));
        assertEquals(knnc3.classify(new FeatureSet((float) 3.5)), (ActivityType.WALK));
        assertEquals(knnc3.classify(new FeatureSet((float) 5)), (ActivityType.QUEUE));
        assertEquals(knnc5.classify(new FeatureSet((float) 1)), (ActivityType.WALK));
        assertEquals(knnc5.classify(new FeatureSet((float) 4)), (ActivityType.QUEUE));
        assertEquals(knnc5.classify(new FeatureSet((float) 8)), (ActivityType.QUEUE));
        assertEquals(knnc7.classify(new FeatureSet((float) 0)), (ActivityType.WALK));
        assertEquals(knnc7.classify(new FeatureSet((float) 8)), (ActivityType.WALK));
    }
}