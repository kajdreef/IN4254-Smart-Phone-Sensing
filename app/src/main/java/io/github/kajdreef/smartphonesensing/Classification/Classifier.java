package io.github.kajdreef.smartphonesensing.Classification;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;

/**
 * Created by Uncle John on 26/04/2015.
 */
public abstract class Classifier {

    public abstract void addTrainingData(List<LabeledFeatureSet> trainingDataSet);
    public abstract ActivityType classify(FeatureSet inputData);
    public abstract void clearTrainingData();
    public abstract List<LabeledFeatureSet> getAllTrainingData();

    public void retrain(List<LabeledFeatureSet> trainingDataSet) {
        clearTrainingData();
        addTrainingData(trainingDataSet);
    }
    public List<ActivityType> classifyList(List<FeatureSet> inputList) {
        List<ActivityType> labelList = new ArrayList<>();
        for (FeatureSet set : inputList) {
            labelList.add(classify(set));
        }
        return labelList;
    }
    public float test(List<LabeledFeatureSet> testDataSet) {
        int correct=0;
        for (LabeledFeatureSet featSet : testDataSet ){
            ActivityType label = classify(featSet.getFeatureSet());
            if(label.equals(featSet.getLabel())){
                correct++;
            }
        }
        return (float) correct/testDataSet.size();
    }
    public static ArrayList<LabeledFeatureSet> extractLabeledData(ArrayList<LabeledFeatureSet> data, ActivityType a){
        ArrayList<LabeledFeatureSet> out = new ArrayList<>();
        for(LabeledFeatureSet f : data){
            if (f.getLabel().equals(a)){
                out.add(f);
            }
        }
        return out;
    }
    public  List<ActivityType> getLabelList(){
        ArrayList<ActivityType> out = new ArrayList<>();
        for(LabeledFeatureSet f : getAllTrainingData()){
            if (!out.contains(f.getLabel())){
                out.add(f.getLabel());
            }
        }
        return out;
    }
    public float crossValidation(int k){
        ArrayList<LabeledFeatureSet> save = new ArrayList<>(getAllTrainingData());
        ArrayList<ActivityType> labelList = new ArrayList<>(getLabelList());
        float correct = 0;
        ArrayList<LabeledFeatureSet> test = new ArrayList<>();
        ArrayList<LabeledFeatureSet> all = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            for (ActivityType a : labelList){
                all.addAll(extractLabeledData(save,a));
                int N = all.size();
                int step = N/k;
                List<LabeledFeatureSet> subTest = all.subList(i * step, (i + 1) * step);
                test.addAll(subTest);
                all.removeAll(subTest);
                addTrainingData(all);
            }
            correct = correct + test(test)/k;
            clearTrainingData();
            test.clear();
            all.clear();
        }
        retrain(save);
        return correct;
    }
    public float leaveOneOut(){
        ArrayList<LabeledFeatureSet> save = new ArrayList<>(getAllTrainingData());
        int correct = 0;
        ArrayList<LabeledFeatureSet> all = new ArrayList<>();
        clearTrainingData();
        for (LabeledFeatureSet f:save){
            all.addAll(save);
            all.remove(f);
            retrain(all);
            if(classify(f).equals(f.getLabel())){
                correct++;
            }
            all.clear();
        }
        retrain(save);
        return correct/save.size();
    }
}
