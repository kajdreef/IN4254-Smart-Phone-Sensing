package ClassificationTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractor;
import io.github.kajdreef.smartphonesensing.Classification.FeatureExtractorSD;
import io.github.kajdreef.smartphonesensing.Classification.KNN;
import io.github.kajdreef.smartphonesensing.Classification.LabeledFeatureSet;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Utils.AbstractReader;
import io.github.kajdreef.smartphonesensing.Utils.ReaderTest;


public class ClassificationTest {
    KNN knn;
    AbstractReader trainReader;
    AbstractReader validationReader;

    @Before
    public void setUp() {
        //Get data from files as 3 arraylists for 1 training point (15-20 points?)
        int step = 3;
        ArrayList<ActivityType> labels = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> z = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            labels.add(ActivityType.QUEUE);
            x.add((float) 1+i/300);
            y.add((float) 1-i/200);
            z.add((float) 1+i/1000);
        }
        for (int i = 0; i < 10; i++) {
            labels.add(ActivityType.WALK);
            x.add(new Random().nextFloat());
            y.add(new Random().nextFloat());
            z.add(new Random().nextFloat());
        }
        ArrayList<FeatureExtractor> extractors = new ArrayList<>();
        extractors.add(new FeatureExtractorSD());
        ArrayList<LabeledFeatureSet> train = FeatureExtractor.generateDataSet(labels,x,y,z,extractors,step);
        knn = new KNN(5,train);
    }

    @Test
    public void testClassification(){
        int step = 3;
        ArrayList<ActivityType> labels = new ArrayList<>();
        ArrayList<Float> x = new ArrayList<>();
        ArrayList<Float> y = new ArrayList<>();
        ArrayList<Float> z = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            labels.add(ActivityType.QUEUE);
            x.add((float) 1+i/1000);
            y.add((float) 1+i/300);
            z.add((float) 1-i/500);
        }

        for (int i = 0; i < 10; i++) {
            labels.add(ActivityType.WALK);
            x.add(new Random().nextFloat());
            y.add(new Random().nextFloat());
            z.add(new Random().nextFloat());
        }
        ArrayList<FeatureExtractor> extractors = new ArrayList<>();
        extractors.add(new FeatureExtractorSD());
        ArrayList<LabeledFeatureSet> test = FeatureExtractor.generateDataSet(labels,x,y,z,extractors,step);
        float correct = knn.test(test);

        System.out.println(Float.toString(correct));
        Assert.assertTrue(correct > (float) 0.5);
    }
}
