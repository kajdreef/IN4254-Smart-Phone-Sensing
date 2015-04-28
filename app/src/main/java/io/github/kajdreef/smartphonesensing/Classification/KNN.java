package io.github.kajdreef.smartphonesensing.Classification;



import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;

public class KNN implements Classifier {
    private int k;
    private List<LabeledFeatureSet> data;

    public KNN(int k,List<LabeledFeatureSet> data){
        if(data.size()<k) {
        //throw exception
        }
        //if k even throw exception
        this.k = k;
        this.data = data;
    }

    @Override
    public void train(List<LabeledFeatureSet> trainingDataSet) {
        this.data.addAll(trainingDataSet);
    }

    @Override
    public float test(List<LabeledFeatureSet> testDataSet) {
        //Perform LOO
        return (float) 0;
    }

    @Override
    public List<Label> classifyList(List<FeatureSet> inputList) {

        List<Label> labelList = new ArrayList<>();
        for (FeatureSet set : inputList) {
            labelList.add(classify(set));
        }
        return labelList;
    }

    public Label classify(FeatureSet inputData) {
        //Construct mapping of distances to inputData
        TreeMap<Float,ArrayList<Label>> distanceMapping = new TreeMap<>();
        for(LabeledFeatureSet known : this.data){
                float d = inputData.distance(known);
            if(!distanceMapping.containsKey(d)){
                distanceMapping.put(d, new ArrayList<Label>());
            }
            distanceMapping.get(d).add(known.getLabel());
        }
        //Find K closest labels to inputData, and count how many times each label is present
        List<Label>kClosestLabels = new ArrayList<>();
        List<Integer> count = new ArrayList<>();
        int neighbor = 0;
        while (neighbor < k) {
            ArrayList<Label> labelListAtDistance = distanceMapping.pollFirstEntry().getValue();
            for (Label label : labelListAtDistance) {
                if (kClosestLabels.contains(label)) {
                    int index = kClosestLabels.indexOf(label);
                    count.set(index, count.get(index) + 1);
                } else {
                    kClosestLabels.add(label);
                    count.add(1);
                }
                neighbor++;
                if (neighbor == k){
                    break;
               }
            }
        }
        //Find index of most frequent label
        int max = count.get(0);
        int maxIndex = 0;
        for (int i = 1; i < count.size(); i++) {
            if(count.get(i) > max) {
                max = count.get(i);
                maxIndex = i;
            }
        }
        return kClosestLabels.get(maxIndex);
    }
}
