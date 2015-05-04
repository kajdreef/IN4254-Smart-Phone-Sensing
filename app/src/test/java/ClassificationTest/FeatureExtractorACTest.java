package ClassificationTest;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorAC;

public class FeatureExtractorACTest {
    private FeatureExtractorAC extractorAC;

    @Before
    protected void setUp() {
        this.extractorAC = new FeatureExtractorAC();
    }

    @Test
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

        assert false;
    }
}
