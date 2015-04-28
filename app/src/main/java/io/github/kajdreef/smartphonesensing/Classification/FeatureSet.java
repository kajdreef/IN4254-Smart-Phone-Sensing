package io.github.kajdreef.smartphonesensing.Classification;

public class FeatureSet {

    private float data;
    public FeatureSet(FeatureSet feat){
        this.data = feat.getData();
    }
    public FeatureSet(float data){
        this.data = data;
    }
    public float getData() {
        return data;
    }
    public float distance(FeatureSet feat){
        return Math.abs(this.data - feat.getData());
    }
}
