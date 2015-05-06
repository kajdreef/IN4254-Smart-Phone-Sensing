package io.github.kajdreef.smartphonesensing.Activities;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractor;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorAC;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorMean;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorSD;
import io.github.kajdreef.smartphonesensing.Classification.KNN;
import io.github.kajdreef.smartphonesensing.Classification.LabeledFeatureSet;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Utils.AbstractReader;
import io.github.kajdreef.smartphonesensing.Utils.ReaderTest;

/**
 * Created by kajdreef on 02/05/15.
 * KNN performance Activity
 */
public class KNNPerformance extends ActionBarActivity {

    SensorManager sm;

    KNN knn;
    final int k = 5;
    final int WINDOW_SIZE = 30;

    ArrayList<Float> x;
    ArrayList<Float> y;
    ArrayList<Float> z;
    ArrayList<ActivityType> labels;

    AbstractReader trainReader;
    AbstractReader validationReader;

    public void initReader(){
        trainReader = new ReaderTest(this, R.raw.accelerometer_data_set);
        validationReader = new ReaderTest(this, R.raw.validationdata);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        sm =(SensorManager)getSystemService(SENSOR_SERVICE);

        initReader();
        float result;

        // Check the performance of Feature Mean
        result = performance(new FeatureExtractorMean());
        Log.d("Performance of Mean: ", "" + result);

        // Check the performance of Feature Standard Deviation
        result = performance(new FeatureExtractorSD());
        Log.d("Performance of SD: ", "" + result);

        // Check the performance of Feature Auto Correlation
        result = performance(new FeatureExtractorAC());
        Log.d("Performance of AC: ", "" + result);

        // Check the performance of Feature Mean
        result = performanceLeaveOneOut(new FeatureExtractorMean());
        Log.d("LOO of Mean: ", "" + result);

        // Check the performance of Feature Standard Deviation
        result = performanceLeaveOneOut(new FeatureExtractorSD());
        Log.d("LOO of SD: ", "" + result);

        // Check the performance of Feature Auto Correlation
        result = performanceLeaveOneOut(new FeatureExtractorAC());
        Log.d("LOO of AC: ", "" + result);
    }

    public float performanceLeaveOneOut(FeatureExtractor extractor){
        // Get all data fromt the accelerometerData.txt
        trainReader.readAll();
        if(trainReader.size() >= WINDOW_SIZE) {
            x = trainReader.getAllX();
            y = trainReader.getAllY();
            z = trainReader.getAllZ();
            labels = trainReader.getAllStates();
        }

        // Initialise KNN and train it
        ArrayList<LabeledFeatureSet> train = FeatureExtractor.generateDataSet(labels, x, y, z, extractor, WINDOW_SIZE);
        knn = new KNN(k,train);

        // Compare classification with reality
        return knn.leaveOneOut();
    }

    public float performance(FeatureExtractor extractor){
        // Get all data fromt the accelerometerData.txt
        trainReader.readAll();
        if(trainReader.size() >= WINDOW_SIZE) {
            x = trainReader.getAllX();
            y = trainReader.getAllY();
            z = trainReader.getAllZ();
            labels = trainReader.getAllStates();
        }

        // Initialise KNN and train it
        ArrayList<LabeledFeatureSet> train = FeatureExtractor.generateDataSet(labels, x, y, z, extractor, WINDOW_SIZE);
        knn = new KNN(k,train);


        // Read validation saved data from the accelerometer
        validationReader.readAll();
        if(validationReader.size() >= WINDOW_SIZE) {
            x = validationReader.getAllX();
            y = validationReader.getAllY();
            z = validationReader.getAllZ();
            labels = validationReader.getAllStates();
        }

        //Test the KNN with the validation data
        ArrayList<LabeledFeatureSet> test = FeatureExtractor.generateDataSet(labels,x,y,z, extractor, WINDOW_SIZE);

        // Compare classification with reality
        return knn.test(test);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
