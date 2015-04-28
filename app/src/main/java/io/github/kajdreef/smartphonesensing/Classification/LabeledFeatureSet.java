package io.github.kajdreef.smartphonesensing.Classification;

import java.util.List;

/**
 * Created by Uncle John on 26/04/2015.
 */
public class LabeledFeatureSet extends FeatureSet{
    private FeatureSet feat;
    private Label label;
    public LabeledFeatureSet(FeatureSet feat,Label lab){
        super(feat);
        this.feat = feat;
        this.label = lab;
    }
    public Label getLabel() {
        return label;
    }
    public FeatureSet getFeatureSet(){
        return this.feat;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
