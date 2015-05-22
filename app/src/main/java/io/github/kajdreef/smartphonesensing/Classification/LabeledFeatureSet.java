package io.github.kajdreef.smartphonesensing.Classification;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;

/**
 * Created by Uncle John on 26/04/2015.
 */
public class LabeledFeatureSet extends FeatureSet{
    private FeatureSet feat;
    private Type label;
    public LabeledFeatureSet(FeatureSet feat,Type lab){
        super(feat);
        this.feat = feat;
        this.label = lab;
    }
    public Type getLabel() {
        return label;
    }
    public FeatureSet getFeatureSet(){
        return this.feat;
    }

    public void setLabel(Type label) {
        this.label = label;
    }
}
