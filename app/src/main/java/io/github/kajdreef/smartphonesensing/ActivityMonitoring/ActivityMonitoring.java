package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractor;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorAC;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorFFT;
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
    private AbstractReader SDReader;
//    private AbstractReader MagReader;
//    private AbstractReader ACReader;
    private AbstractReader FFTReader;

    // Initialise the Feature type!
    ArrayList<FeatureExtractor> extractor;

    // k-Nearest Neighbors
    private KNN knn;
    private final int K = 5;

//    //Try to estimate speed of walking
//    private float speed;

    public ActivityMonitoring(Context ctx){

        // initialise the readers to train kNN
        SDReader = new ReaderTest(ctx, R.raw.std_feature);
//        MagReader = new ReaderTest(ctx, R.raw.max_feature);
//        ACReader = new ReaderTest(ctx, R.raw.ac_feature);
        FFTReader = new ReaderTest(ctx, R.raw.fft_feature);

        // Choose which features you want
        extractor = new ArrayList<>();
        extractor.add(new FeatureExtractorSD());
        //extractor.add(new FeatureExtractorMag());
//        extractor.add(new FeatureExtractorAC());
        extractor.add(new FeatureExtractorFFT());

        activityList = ActivityType.getInstance();

        ArrayList<Float> SDList = new ArrayList<>();
//        ArrayList<Float> MagList = new ArrayList<>();
//        ArrayList<Float> ACList = new ArrayList<>();
        ArrayList<Float> FFTList = new ArrayList<>();
        ArrayList<Type> labelsList = new ArrayList<>();


        // Get all data from the trainingData file in resources
        SDReader.readData();
//        MagReader.readData();
//        ACReader.readData();
        FFTReader.readData();
        SDList = SDReader.getAllX();
//         MagList = MagReader.getAllX();
//        ACList = ACReader.getAllX();
        FFTList = FFTReader.getAllX();
        labelsList = SDReader.getAllStates();



        ArrayList<LabeledFeatureSet> train = new ArrayList<>();
        for (int i = 0; i < labelsList.size(); i++) {
            FeatureSet f = new FeatureSet();
            f.addFeature(SDList.get(i));
//            f.addFeature(MagList.get(i));
//            f.addFeature(ACList.get(i));
            f.addFeature(FFTList.get(i));
            train.add(new LabeledFeatureSet(f, labelsList.get(i)));
        }
        knn = new KNN(K,train);
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

    public void update(ArrayList<Float> x, ArrayList<Float> y, ArrayList<Float> z){
        // Extract features and classify them
        FeatureSet fs = new FeatureSet();
        for (FeatureExtractor ext : extractor) {
            fs.addFeature(ext.extractFeatures(x, y, z));
        }
        Type label = knn.classify(fs);
        activityList.addType(label);
    }
}
