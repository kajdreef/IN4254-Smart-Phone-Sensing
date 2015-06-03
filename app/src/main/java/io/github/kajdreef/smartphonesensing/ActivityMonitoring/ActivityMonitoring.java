package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractor;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorMag;
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
 * Classify the allMag from the accelerometer to Walking/Queueing
 * with the help of kNN
 */
public class ActivityMonitoring {
    // This instance keeps track of the activities performed
    ActivityType activityList;

    // Readers
    private AbstractReader trainReader;
    private AbstractReader accelerometerReader;

    // Initialise the Feature type!
    ArrayList<FeatureExtractor> extractor;

    // k-Nearest Neighbors
    private KNN knn;
    private final int K = 5;

    // Data to calculate solution
    ArrayList<Float> x;
    ArrayList<Float> y;
    ArrayList<Float> z;
    ArrayList<Type> labels;

    //Try to estimate speed of walking
    private float speed;

    // Contains the window size as a constant
    private int WINDOW_SIZE = R.integer.WINDOW_SIZE_ACC;

    public ActivityMonitoring(Context ctx){

        x = new ArrayList<>();
        y = new ArrayList<>();
        z = new ArrayList<>();
        labels = new ArrayList<>();

        // initialise the readers to train kNN
        trainReader = new ReaderTest(ctx, R.raw.accelerometer_data_set_high_sample_rate);

        // Get the needed resources
        Resources res = ctx.getResources();
        WINDOW_SIZE = res.getInteger(R.integer.WINDOW_SIZE_ACC);
        accelerometerReader = new Reader(ctx, res.getString(R.string.accelerometer_data_file));
        initKNN();
    }

    /**
     * Initialise kNN
     */
    public void initKNN(){
        activityList = ActivityType.getInstance();

        ArrayList<Float> xList = new ArrayList<>();
        ArrayList<Float> yList = new ArrayList<>();
        ArrayList<Float> zList = new ArrayList<>();
        ArrayList<Type> labelsList = new ArrayList<>();

        // Get all allMag from the trainingData file in resources
        trainReader.readData();

        if(trainReader.size() >= WINDOW_SIZE) {
            xList = trainReader.getAllX();
            yList = trainReader.getAllY();
            zList = trainReader.getAllZ();
            labelsList = trainReader.getAllStates();
        }
        else{
            Log.e("ActivityMonitoring", "Not enougth training data");
            System.exit(-1);
        }
        // Initialise KNN and train it
        extractor = new ArrayList<>();
        extractor.add(new FeatureExtractorMag());
        extractor.add(new FeatureExtractorSD());
        ArrayList<LabeledFeatureSet> train = FeatureExtractor.generateDataSet(labelsList, xList, yList, zList, extractor, WINDOW_SIZE);
        knn = new KNN(K,train);
        xList.clear();
        yList.clear();
        zList.clear();
        labelsList.clear();
    }

    /**
     * Return the current Activity that the user is doing.
     * @return activity: Queueing, Walking, or None (to be determined activity)
     */
    public Type getActivity(){
        if(activityList.size() == 0){
            return Type.NONE;
        }
        return activityList.getType(activityList.size() - 1);
    }

    public float getSpeed(){return this.speed;}

    public void update(ArrayList<Float> x, ArrayList<Float> y, ArrayList<Float> z){
        // Extract features and classify them
        FeatureSet fs = new FeatureSet();
        for (FeatureExtractor ext : extractor) {
            fs.addFeature(ext.extractFeatures(x, y, z));
        }
        Type label = knn.classify(fs);
        activityList.addType(label);
//        if (label == Type.WALK) {
//            activityList.addSpeed(SpeedExtractor.calculateSpeed(x, y, z));
//        } else {
//            speed = 0;
//        }
    }
}
