package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

/**
 * Created by kajdreef on 23/04/15.
 */
public enum ActivityType {
    NONE{
        public ActivityType fromStringToType(String s){
            if(NONE.toString().equals(s))
                return ActivityType.NONE;
            else
                return null;
        }
    },
    WALK{
        public ActivityType fromStringToType(String s){
            if(WALK.toString().equals(s))
                return ActivityType.WALK;
            else
                return null;
        }
    },
    QUEUE{
        public ActivityType fromStringToType(String s){
            if(QUEUE.toString().equals(s))
                return ActivityType.QUEUE;
            else
                return null;
        }
    };

    protected abstract ActivityType fromStringToType(String s);

    public static ActivityType fromString(String s) {
        ActivityType result = null;
        for(ActivityType t : ActivityType.values()){
            result = t.fromStringToType(s);
            if(result != null){
                return result;
            }
        }
        return result;
    }

    public static ActivityType fromInt(int num) {
        for(ActivityType t : ActivityType.values()){
            if(t.ordinal() == num) {
                return t;
            }
        }
        return ActivityType.NONE;
    }
}
