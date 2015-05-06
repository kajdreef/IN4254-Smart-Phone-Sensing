package ClassificationTest;

import android.test.ActivityTestCase;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorSD;


public class FeatureExtractorSDTest {
    private FeatureExtractorSD FESD;

    @Before
    public void setUp() {
        this.FESD = new FeatureExtractorSD();
    }

    @Test
    public void FeatureSDTest(){
        ArrayList<Float> x = new ArrayList<>();
        x.add((float)0.0);
        x.add((float)0.0);
        x.add((float)0.0);

        ArrayList<Float> y = new ArrayList<>();
        y.add((float) 0.0);
        y.add((float) 1.0);
        y.add((float) 0.0);

        ArrayList<Float> z = new ArrayList<>();
        z.add((float) 0.0);
        z.add((float) 0.0);
        z.add((float) 2.0);
        float result =  FESD.extractFeatures(x, y, z).getData();
        Assert.assertEquals(0.81, result, 0.01);
    }
}
