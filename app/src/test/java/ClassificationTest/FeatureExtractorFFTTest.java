package ClassificationTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorFFT;

public class FeatureExtractorFFTTest {
    private FeatureExtractorFFT extractorFFT;

    @Before
    public  void setUp() {
        this.extractorFFT = new FeatureExtractorFFT();
    }

    @Test
    public void testFeatureFFT(){
        ArrayList<Float> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> z = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            x.add((float)Math.sin(i*6*Math.PI/100));
            y.add((float)0);
            z.add((float)0);
        }
        ArrayList<Float> x1 = new ArrayList<>();
        ArrayList<Float> y1 = new ArrayList<>();
        ArrayList<Float> z1 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            x1.add((float)i/100);
            y1.add((float)0);
            z1.add((float)0);
        }
        Assert.assertEquals(21.241621017456055, extractorFFT.extractFeatures(x, y, z).getData().get(0), 0.001);
        Assert.assertEquals(15.918111801147461, extractorFFT.extractFeatures(x1, y1, z1).getData().get(0), 0.001);
    }
}
