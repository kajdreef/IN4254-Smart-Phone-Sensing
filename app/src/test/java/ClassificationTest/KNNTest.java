package ClassificationTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;
import io.github.kajdreef.smartphonesensing.Classification.FeatureSet;
import io.github.kajdreef.smartphonesensing.Classification.KNN;
import io.github.kajdreef.smartphonesensing.Classification.LabeledFeatureSet;

public class KNNTest {
    private KNN knnc1;
    private KNN knnc3;
    private KNN knnc5;
    private KNN knnc7;

    @Before
    public void setUp() {
        //Test
        ArrayList<LabeledFeatureSet> trainingSet = new ArrayList<>();
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(0), Type.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(1), Type.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(2), Type.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(3), Type.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(5), Type.QUEUE));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(5), Type.QUEUE));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(6), Type.QUEUE));

        this.knnc1 = new KNN(1, trainingSet);
        this.knnc3 = new KNN(3, trainingSet);
        this.knnc5 = new KNN(5, trainingSet);
        this.knnc7 = new KNN(7, trainingSet);
    }

    @Test
    public void testClassify(){
        Assert.assertNotSame(Type.WALK, Type.QUEUE);
        Assert.assertEquals(knnc1.classify(new FeatureSet((float) 0)), (Type.WALK));
        Assert.assertEquals(knnc1.classify(new FeatureSet((float) 3)), (Type.WALK));
        Assert.assertEquals(knnc1.classify(new FeatureSet((float) 5.5)), (Type.QUEUE));
        Assert.assertEquals(knnc3.classify(new FeatureSet((float) 1.5)), (Type.WALK));
        Assert.assertEquals(knnc3.classify(new FeatureSet((float) 3.5)), (Type.WALK));
        Assert.assertEquals(knnc3.classify(new FeatureSet((float) 5)), (Type.QUEUE));
        Assert.assertEquals(knnc5.classify(new FeatureSet((float) 1)), (Type.WALK));
        Assert.assertEquals(knnc5.classify(new FeatureSet((float) 4)), (Type.QUEUE));
        Assert.assertEquals(knnc5.classify(new FeatureSet((float) 8)), (Type.QUEUE));
        Assert.assertEquals(knnc7.classify(new FeatureSet((float) 0)), (Type.WALK));
        Assert.assertEquals(knnc7.classify(new FeatureSet((float) 8)), (Type.WALK));
    }

    @Test
    public void testLeaveOneOut(){
        Assert.assertEquals(1,knnc1.leaveOneOut(),0.001);
        Assert.assertEquals(1, knnc3.leaveOneOut(), 0.001);
    }
}