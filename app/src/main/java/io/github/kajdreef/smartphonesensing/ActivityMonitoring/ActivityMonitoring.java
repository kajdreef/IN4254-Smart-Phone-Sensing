package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Activities.ActivityMonitoringActivity;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractor;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorSD;
import io.github.kajdreef.smartphonesensing.Classification.FeatureSet;
import io.github.kajdreef.smartphonesensing.Classification.KNN;
import io.github.kajdreef.smartphonesensing.Classification.LabeledFeatureSet;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Utils.AbstractReader;
import io.github.kajdreef.smartphonesensing.Utils.Reader;
import io.github.kajdreef.smartphonesensing.Utils.ReaderTest;

/**
 * Created by kajdreef on 04/05/15.
 *
 * Classify the data from the accelerometer to Walking/Queueing
 * with the help of kNN
 */
public class ActivityMonitoring implements Observer {

    private int amountOfNewSamples = 0;
    private KNN knn;
    private AbstractReader trainReader;
    private AbstractReader accelerometerReader;
    final int K = 5;
    public static final int WINDOW_SIZE = 30;
    private ActivityType activity = ActivityType.NONE;

    ArrayList<Float> x;
    ArrayList<Float> y;
    ArrayList<Float> z;
    ArrayList<ActivityType> labels;

    public ActivityMonitoring(Context ctx){
        // initialise the reader to train kNN
        trainReader = new ReaderTest(ctx, R.raw.trainingdata);
        accelerometerReader = new Reader(ctx, ActivityMonitoringActivity.SENSOR_DATA_FILE);
        initKNN();
    }

    public void initKNN(){
        // Get all data from the trainingData file in resources
        trainReader.readAll();
        if(trainReader.size() >= WINDOW_SIZE) {
            x = trainReader.getAllX();
            y = trainReader.getAllY();
            z = trainReader.getAllZ();
            labels = trainReader.getAllStates();
        }

        // Initialise KNN and train it
        ArrayList<LabeledFeatureSet> train = FeatureExtractor.generateDataSet(labels, x, y, z, new FeatureExtractorSD(), WINDOW_SIZE);
        knn = new KNN(K,train);
        x.clear();
        y.clear();
        z.clear();
        labels.clear();
    }

    @Override
    public void update(){
        amountOfNewSamples++;
        if(amountOfNewSamples > WINDOW_SIZE){
            x = accelerometerReader.getAllX();
            y = accelerometerReader.getAllY();
            z = accelerometerReader.getAllZ();

            FeatureExtractor extractor = new FeatureExtractorSD();

            FeatureSet fs = extractor.extractFeatures(x,y,z);
            activity = knn.classify(fs);

            amountOfNewSamples = 0;
        }
    }

    public ActivityType getActivity(){
        return activity;
    }
}
