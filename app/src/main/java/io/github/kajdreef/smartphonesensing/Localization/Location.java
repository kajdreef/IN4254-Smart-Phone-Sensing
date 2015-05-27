package io.github.kajdreef.smartphonesensing.Localization;

/**
 * Created by kajdreef on 15/05/15.
 */
public class Location {

    private float x, y;

    public Location(float x, float y){
        this.x = x;
        this.y = y;
    }

    public Location(Location newLocation){
        this(newLocation.getX(), newLocation.getY());
    }

    public void setLocation(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setLocation(Location newLocation){
        this.x = newLocation.getX();
        this.y = newLocation.getY();
    }

    public void move(float dx, float dy){
        this.x += dx;
        this.y += dy;
    }

    public void setX(float x){
        this.x = x;
    }

    public void setY(float y){
        this.y = y;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    /**
     * Check if two location are the same
     * @param loc1 (= a location)
     * @return true if the locations are the same; false if not
     */
    public boolean isEqual(Location loc1){
        return loc1.getY() == this.y && loc1.getX() == this.x;
    }
}
