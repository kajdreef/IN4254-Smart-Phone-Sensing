package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.content.Context;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Activities.ActivityMonitoringActivity;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractor;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorAC;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorMag;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorSD;
import io.github.kajdreef.smartphonesensing.Classification.FeatureSet;
import io.github.kajdreef.smartphonesensing.Classification.KNN;
import io.github.kajdreef.smartphonesensing.Classification.LabeledFeatureSet;
import io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering.SpeedExtractor;
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
    private ArrayList<ActivityType> activityList;

    // Readers
    private AbstractReader trainReader;
    private AbstractReader accelerometerReader;

    // Initialise the Feature type!
    ArrayList<FeatureExtractor> extractor;

    // k-Nearest Neighbors
    private KNN knn;
    private final int K = 5;
    public static final int WINDOW_SIZE = 160;

    // Data to calculate solution
    ArrayList<Float> x;
    ArrayList<Float> y;
    ArrayList<Float> z;
    ArrayList<ActivityType> labels;

    //Try to estimate speed of walking
    private float speed;

    public ActivityMonitoring(Context ctx){
        // initialise the readers to train kNN
        trainReader = new ReaderTest(ctx, R.raw.accelerometer_data_set_high_sample_rate);
        accelerometerReader = new Reader(ctx, ActivityMonitoringActivity.SENSOR_DATA_FILE);
        initKNN();
    }

    /**
     * Initialise kNN
     */
    public void initKNN(){
        activityList = new ArrayList<>();

        // Get all data from the trainingData file in resources
        trainReader.readAll();
        if(trainReader.size() >= WINDOW_SIZE) {
            x = trainReader.getAllX();
            y = trainReader.getAllY();
            z = trainReader.getAllZ();
            labels = trainReader.getAllStates();
        }

        // Initialise KNN and train it
        extractor = new ArrayList<>();
        extractor.add(new FeatureExtractorMag());
        extractor.add(new FeatureExtractorSD());
        extractor.add(new FeatureExtractorAC());
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

            // Read the new data and return arraylists as big as the window size.
            accelerometerReader.readAll();
            x = new ArrayList<>(accelerometerReader.getSubListX(WINDOW_SIZE));
            y = new ArrayList<>(accelerometerReader.getSubListY(WINDOW_SIZE));
            z = new ArrayList<>(accelerometerReader.getSubListZ(WINDOW_SIZE));
            labels = new ArrayList<>(accelerometerReader.getSubListStates(WINDOW_SIZE));
            
            // Extract features and classify them
            FeatureSet fs = new FeatureSet();
            for (FeatureExtractor ext : extractor) {
                fs.addFeature(ext.extractFeatures(x, y, z));
            }
            ActivityType label = knn.classify(fs);
            activityList.add(label);
            if(label == ActivityType.WALK){
                speed = SpeedExtractor.calculateSpeed(x,y,z);
            }
            else{
                speed = 0;
            }
            amountOfNewSamples = 0;
        }
    }

    /**
     * Return the current Activity that the user is doing.
     * @return activity: Queueing, Walking, or None (to be determined activity)
     */
    public ActivityType getActivity(){
        if(activityList.size() == 0){
            return ActivityType.NONE;
        }
        return activityList.get(activityList.size() - 1);
    }

    public ArrayList<ActivityType> getActivityList(){
        return this.activityList;
    }
    public float getSpeed(){return this.speed;}
}
