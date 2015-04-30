package io.github.kajdreef.smartphonesensing.Classification.TestClassification;

import android.test.ActivityTestCase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractor;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorSD;
import io.github.kajdreef.smartphonesensing.Classification.KNN;
import io.github.kajdreef.smartphonesensing.Classification.LabeledFeatureSet;


public class testClassification extends ActivityTestCase {
    private KNN knn;
    @Override
    protected void setUp() throws Exception  {
        //Get data from files as 3 arraylists for 1 training point (15-20 points?)
        int step = 3;
        ArrayList<ActivityType> labels = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> z = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            labels.add(ActivityType.QUEUE);
            x.add((float) 1+i/300);
            y.add((float) 1-i/200);
            z.add((float) 1+i/1000);
        }
        for (int i = 0; i < 10; i++) {
            labels.add(ActivityType.WALK);
            x.add(new Random().nextFloat());
            y.add(new Random().nextFloat());
            z.add(new Random().nextFloat());
        }
        ArrayList<LabeledFeatureSet> train = FeatureExtractor.generateDataSet(labels,x,y,z,new FeatureExtractorSD(),step);
        knn = new KNN(5,train);
    }
    public void testClassification(){
        int step = 3;
        ArrayList<ActivityType> labels = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> z = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            labels.add(ActivityType.QUEUE);
            x.add((float) 1+i/1000);
            y.add((float) 1+i/300);
            z.add((float) 1-i/500);
        }
        for (int i = 0; i < 10; i++) {
            labels.add(ActivityType.WALK);
            x.add(new Random().nextFloat());
            y.add(new Random().nextFloat());
            z.add(new Random().nextFloat());
        }
        ArrayList<LabeledFeatureSet> test = FeatureExtractor.generateDataSet(labels,x,y,z,new FeatureExtractorSD(),step);
        float correct = knn.test(test);
        Log.i("testResults",Float.toString(correct));
    }
}