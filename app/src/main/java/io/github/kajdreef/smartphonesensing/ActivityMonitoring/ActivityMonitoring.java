package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Activities.ActivityMonitoringActivity;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractor;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorAC;
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
    private ActivityType activity = ActivityType.NONE;

    // Readers
    private AbstractReader trainReader;
    private AbstractReader accelerometerReader;

    // Initialise the Feature type!
    FeatureExtractor extractor = new FeatureExtractorSD();

    // k-Nearest Neighbors
    private KNN knn;
    private final int K = 5;
    private static final int WINDOW_SIZE = 30;

    // Data to calculate solution
    ArrayList<Float> x;
    ArrayList<Float> y;
    ArrayList<Float> z;
    ArrayList<ActivityType> labels;

    public ActivityMonitoring(Context ctx){
        // initialise the readers to train kNN
        trainReader = new ReaderTest(ctx, R.raw.trainingdata);
        accelerometerReader = new Reader(ctx, ActivityMonitoringActivity.SENSOR_DATA_FILE);
        initKNN();
    }

    /**
     * Initialise kNN
     */
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
        ArrayList<LabeledFeatureSet> train = FeatureExtractor.generateDataSet(labels, x, y, z, extractor, WINDOW_SIZE);
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
            accelerometerReader.readAll();
            x = accelerometerReader.getAllX();
            y = accelerometerReader.getAllY();
            z = accelerometerReader.getAllZ();
            FeatureSet fs = extractor.extractFeatures(x,y,z);
            activity = knn.classify(fs);
            amountOfNewSamples = 0;
        }
    }

    /**
     * Return the current Activity that the user is doing.
     * @return activity: Queueing, Walking, or None (to be determined activity)
     */
    public ActivityType getActivity(){
        return activity;
    }
}
