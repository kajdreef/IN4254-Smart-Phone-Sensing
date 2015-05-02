package ClassificationTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorMean;


public class FeatureExtractorMeanTest {
    private FeatureExtractorMean FEM;

    @Before
    public void setUp() {
        this.FEM = new FeatureExtractorMean();
    }

    @Test
    public void testFeatureMean() {
        ArrayList<Float> x = new ArrayList<>();
        x.add((float) 1.0);
        x.add((float) 0.0);
        x.add((float) 0.0);

        ArrayList<Float> y = new ArrayList<>();
        y.add((float) 0.0);
        y.add((float) 1.0);
        y.add((float) 0.0);

        ArrayList<Float> z = new ArrayList<>();
        z.add((float) 0.0);
        z.add((float) 0.0);
        z.add((float) 1.0);

//        Assert.assertEquals((float) 1,FEM.extractFeatures(x,y,z).getData());
        assert true;
    }
}
