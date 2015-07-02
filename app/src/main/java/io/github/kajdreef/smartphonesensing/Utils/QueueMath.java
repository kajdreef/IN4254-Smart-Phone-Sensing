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
        float tempResult = 0f;
        float result = 0f;
        int stepsResult = 0;
        float[] returnValue = new float[2];
        boolean queueStart = false;

        // amount of moving forward steps during the queue.
        int tempSteps = 0;
        Type previous = Type.NONE;
        for(Type at : activityList) {
            // As long as time is lower than 10.0f we can still be in the queue.
            if (time < 10.0f){
                if (at == Type.QUEUE) {
                    // Update the total queuing time
                    tempResult += WINDOW_TIME + time;

                    // Check if the previous one was NOT walk so it can be seen as 1 step.
                    if(previous == Type.WALK && queueStart == true) {
                        tempSteps++;
                    }

                    // Reset values for after queue step
                    time = 0;
                    queueStart = true;
                    previous = Type.QUEUE;

                }
                else if (at == Type.WALK && queueStart == true){
                    // update the walking time if bigger than 10 seconds than we are out of the queue.
                    time += WINDOW_TIME;
                    previous = Type.WALK;
                }
            }
            else {
                // During multple queues only store the longest queue
                if(result < tempResult) {
                    result = tempResult;
                    stepsResult = tempSteps;
                }

                // Reset everything
                queueStart = false;
                time = 0f;
                tempResult = 0f;
                tempSteps = 0;
            }
        }


        if(result < tempResult){
            result = tempResult;
            stepsResult = tempSteps;
        }

        returnValue[0] = result;
        returnValue[1] = result/(stepsResult+1);

        return returnValue;
    }
}
