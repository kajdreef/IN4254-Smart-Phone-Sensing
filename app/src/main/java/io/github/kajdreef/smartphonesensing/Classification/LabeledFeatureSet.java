package io.github.kajdreef.smartphonesensing.Classification;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.ActivityType;

/**
 * Created by Uncle John on 26/04/2015.
 */
public class LabeledFeatureSet extends FeatureSet{
    private FeatureSet feat;
    private ActivityType label;
    public LabeledFeatureSet(FeatureSet feat,ActivityType lab){
        super(feat);
        this.feat = feat;
        this.label = lab;
    }
    public ActivityType getLabel() {
        return label;
    }
    public FeatureSet getFeatureSet(){
        return this.feat;
    }

    public void setLabel(ActivityType label) {
        this.label = label;
    }
}
