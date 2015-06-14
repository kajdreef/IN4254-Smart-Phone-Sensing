package io.github.kajdreef.smartphonesensing.Utils;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.ActivityMonitoring.Type;

/**
 * Created by kajdreef on 09/06/15.
 */
public class QueueMath {

    /**
     * Calculate the total queue time and the average queue time
     * @param activityList
     * @param WINDOW_TIME
     * @return result[0] is the total queue time; result[1] is the average queue time per person in the queue
     */
    public static float[] calculateSQTime(ArrayList<Type> activityList, float WINDOW_TIME){
        float time = 0f;
        float result = 0f;
        float[] returnValue = new float[2];

        // amount of moving forward steps during the queue.
        int steps = 0;
        Type previous = Type.NONE;
        for(Type at : activityList) {
            // As long as time is lower than 10.0f we can still be in the queue.
            if (time < 10.0f){
                if (at == Type.QUEUE) {
                    // Update the total queuing time
                    result += WINDOW_TIME;
                    time = 0;
                    previous = Type.QUEUE;
                } else if(result > 0){
                    // update the walking time if bigger than 10 seconds than we are out of the queue.
                    time += WINDOW_TIME;
                    // Check if the previous one was NOT walk so it can be seen as 1 step.
                    if(previous != Type.WALK) {
                        steps++;
                    }
                    previous = Type.WALK;
                }
            }
            else {
                break;
            }
        }

        returnValue[0] = result;
        if (steps == 0)
            returnValue[1] = result;
        else
            returnValue[1] = result/steps;

        return returnValue;
    }
}
