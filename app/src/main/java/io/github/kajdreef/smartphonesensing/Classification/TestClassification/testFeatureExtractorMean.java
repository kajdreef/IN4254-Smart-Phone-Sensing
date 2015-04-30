package io.github.kajdreef.smartphonesensing.Classification.TestClassification;

import android.test.ActivityTestCase;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorMean;


public class testFeatureExtractorMean extends ActivityTestCase {
    private FeatureExtractorMean FEM;

    @Override
    protected void setUp() throws Exception  {
        this.FEM = new FeatureExtractorMean();
    }

    public void testFeatureMean(){
        ArrayList<Float> x = new ArrayList<>();
        x.add((float)1.0);
        x.add((float)0.0);
        x.add((float)0.0);

        ArrayList<Float> y = new ArrayList<>();
        y.add((float) 0.0);
        y.add((float) 1.0);
        y.add((float) 0.0);

        ArrayList<Float> z = new ArrayList<>();
        z.add((float) 0.0);
        z.add((float) 0.0);
        z.add((float) 1.0);
        assertEquals((float) 1,FEM.extractFeatures(x,y,z).getData());
    }
}
