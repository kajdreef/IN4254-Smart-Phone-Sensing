package ClassificationTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
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
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(0), ActivityType.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(1), ActivityType.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(2), ActivityType.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(3), ActivityType.WALK));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(5), ActivityType.QUEUE));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(5), ActivityType.QUEUE));
        trainingSet.add(new LabeledFeatureSet(new FeatureSet(6), ActivityType.QUEUE));

        this.knnc1 = new KNN(1, trainingSet);
        this.knnc3 = new KNN(3, trainingSet);
        this.knnc5 = new KNN(5, trainingSet);
        this.knnc7 = new KNN(7, trainingSet);
    }

    @Test
    public void testClassify(){
        Assert.assertNotSame(ActivityType.WALK, ActivityType.QUEUE);
        Assert.assertEquals(knnc1.classify(new FeatureSet((float) 0)), (ActivityType.WALK));
        Assert.assertEquals(knnc1.classify(new FeatureSet((float) 3)), (ActivityType.WALK));
        Assert.assertEquals(knnc1.classify(new FeatureSet((float) 5.5)), (ActivityType.QUEUE));
        Assert.assertEquals(knnc3.classify(new FeatureSet((float) 1.5)), (ActivityType.WALK));
        Assert.assertEquals(knnc3.classify(new FeatureSet((float) 3.5)), (ActivityType.WALK));
        Assert.assertEquals(knnc3.classify(new FeatureSet((float) 5)), (ActivityType.QUEUE));
        Assert.assertEquals(knnc5.classify(new FeatureSet((float) 1)), (ActivityType.WALK));
        Assert.assertEquals(knnc5.classify(new FeatureSet((float) 4)), (ActivityType.QUEUE));
        Assert.assertEquals(knnc5.classify(new FeatureSet((float) 8)), (ActivityType.QUEUE));
        Assert.assertEquals(knnc7.classify(new FeatureSet((float) 0)), (ActivityType.WALK));
        Assert.assertEquals(knnc7.classify(new FeatureSet((float) 8)), (ActivityType.WALK));
    }

    @Test
    public void testLeaveOneOut(){
        Assert.assertEquals(1,knnc1.leaveOneOut(),0.001);
        Assert.assertEquals(1, knnc3.leaveOneOut(), 0.001);
    }
}