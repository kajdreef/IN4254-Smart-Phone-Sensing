package io.github.kajdreef.smartphonesensing.Localization;

/**
 * Created by kajdreef on 15/05/15.
 */
public class Particle {
    private Location currentLocation, previousLocation;
    private float weight;

    public Particle(float x, float y){
        currentLocation = new Location(x,y);
        previousLocation = new Location(x,y);

        this.weight = 1f;
    }

    public void updateLocation(float dx, float dy){
        previousLocation.setLocation(currentLocation);
        currentLocation.move(dx, dy);
    }

    //TODO: implement this method
    public void calculateWeight(){
        this.weight = 1f;
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
}
