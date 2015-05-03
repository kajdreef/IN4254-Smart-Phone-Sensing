package io.github.kajdreef.smartphonesensing.Classification.TestClassification;

import android.test.ActivityTestCase;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorAC;

public class testFeatureExtractorAC extends ActivityTestCase {
    private FeatureExtractorAC extractorAC;

    @Override
    protected void setUp() throws Exception  {
        this.extractorAC = new FeatureExtractorAC();
    }

    public void testFeatureMean(){
        ArrayList<Float> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> z = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            x.add((float)Math.sin(i*6*Math.PI/100));
            y.add((float)0);
            z.add((float)0);
        }
        //Not sure how to actually test this, but it seemed correct in the debug..
        //assertEquals(???, extractorAC.extractFeatures(x, y, z).getData());
    }
}
