package io.github.kajdreef.smartphonesensing.Localization;

/**
 * Created by kajdreef on 15/05/15.
 */
public class Wall{
    public Location start, end;

    public Wall(Location loc1, Location loc2){
        this.start = loc1;
        this.end = loc2;
    }

    public Wall(float x1, float y1, float x2, float y2){
        this(new Location(x1, y1), new Location(x2, y2));
    }
}
