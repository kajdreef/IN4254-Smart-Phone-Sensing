package io.github.kajdreef.smartphonesensing.Localization;

/**
 * Created by kajdreef on 15/05/15.
 */
public class Particle {
    private Location currentLocation, previousLocation;

    public Particle(float x, float y){
        currentLocation = new Location(x,y);
        previousLocation = new Location(x,y);
    }

    public Particle(Location current){
        currentLocation = new Location(current);
        previousLocation = new Location(current);
    }

    /**
     * Constructor to create a specific particle with current and previous location
     * @param current
     * @param previous
     */
    public Particle(Location current, Location previous){
        this.currentLocation = new Location(current);
        this.previousLocation = new Location(previous);
    }

    public void updateLocation(float dx, float dy){
        previousLocation.setLocation(currentLocation);
        currentLocation.move(dx, dy);
    }

    public void setCurrentLocation(Location newLocation){
        this.currentLocation = newLocation;
    }

    public Location getCurrentLocation(){
        return this.currentLocation;
    }

    public void setPreviousLocation(Location newLocation){
        this.previousLocation = newLocation;
    }

    public Location getPreviousLocation(){
        return this.previousLocation;
    }

    public Particle clone(){
        return new Particle(this.currentLocation, this.previousLocation);
    }
}
