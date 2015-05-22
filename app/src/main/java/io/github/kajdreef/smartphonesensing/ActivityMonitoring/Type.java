package io.github.kajdreef.smartphonesensing.ActivityMonitoring;

/**
 * Created by kajdreef on 23/04/15.
 */
public enum Type {
    NONE{
        public Type fromStringToType(String s){
            if(NONE.toString().equals(s))
                return Type.NONE;
            else
                return null;
        }
    },
    WALK{
        public Type fromStringToType(String s){
            if(WALK.toString().equals(s))
                return Type.WALK;
            else
                return null;
        }
    },
    QUEUE{
        public Type fromStringToType(String s){
            if(QUEUE.toString().equals(s))
                return Type.QUEUE;
            else
                return null;
        }
    };

    protected abstract Type fromStringToType(String s);

    public static Type fromString(String s) {
        Type result = null;
        for(Type t : Type.values()){
            result = t.fromStringToType(s);
            if(result != null){
                return result;
            }
        }
        return result;
    }

    public static Type fromInt(int num) {
        for(Type t : Type.values()){
            if(t.ordinal() == num) {
                return t;
            }
        }
        return Type.NONE;
    }
}
