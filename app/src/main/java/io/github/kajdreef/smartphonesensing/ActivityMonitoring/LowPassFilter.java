package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

/**
 * Created by Uncle John on 22/04/2015.
 */
public class LowPassFilter
{
    private float alpha;
    private float[] prevValue;

    public LowPassFilter(float alpha, float[] prevValue){
        this.alpha = alpha;
        this.prevValue = prevValue;
    }

    public float[] lowPassFilter(float[] input)
    {
        // Update the Android Developer low-pass filter
        // y[i] = y[i] * alpha + (1 - alpha) * x[i]
        this.prevValue[0] = alpha * prevValue[0]
                + (1 - alpha) * input[0];
        this.prevValue[1] = alpha * prevValue[1]
                + (1 - alpha) * input[1];
        this.prevValue[2] = alpha * prevValue[2]
                + (1 - alpha) * input[2];

        return prevValue;
    }
}

