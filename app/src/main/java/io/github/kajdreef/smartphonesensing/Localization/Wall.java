package io.github.kajdreef.smartphonesensing.Localization;

import io.github.kajdreef.smartphonesensing.Utils.LineIntersect;

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

    public boolean intersect(Location loc1, Location loc2){
        return LineIntersect.linesIntersect(loc1.getX(), loc1.getY(),       // start of line1
                                            loc2.getX(), loc2.getY(),       // end of line1
                                            start.getX(), start.getY(),     // start of line2
                                            end.getX(), end.getY()          // end of line2
        );

    }


}
