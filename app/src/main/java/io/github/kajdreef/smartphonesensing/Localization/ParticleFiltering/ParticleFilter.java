package io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.LocalizationView.WalkedPath;
import io.github.kajdreef.smartphonesensing.Localization.Location;
import io.github.kajdreef.smartphonesensing.Localization.Particle;
import io.github.kajdreef.smartphonesensing.R;
import io.github.kajdreef.smartphonesensing.Utils.ArrayOperations;

/**
 * Created by Uncle John on 20/05/2015.
 */
public class ParticleFilter {
    private ArrayList<Particle> particles;
    private FloorPlan floorPlan;
    private final int N_INIT;
    private final float RATIO = 9/(float)10;
    private Random rand;
    private ArrayList<Float> dx;
    private ArrayList<Float> dy;

    /**
     * Constructs ParticleFilter with particles that are uniformly distributed over the map.
     * @param n number of particles that need to be generated
     * @param floorPlan map of the interior
     */
    public ParticleFilter(final int n, FloorPlan floorPlan){
        this.particles = new ArrayList<>(n);
        this.N_INIT = n;
        this.floorPlan = floorPlan;
        this.generateParticles(N_INIT);
        this.rand = new Random();
        this.dx = new ArrayList<>();
        this.dy = new ArrayList<>();
    }

    public void resetParticleFilter(){
        particles.clear();
        this.generateParticles(N_INIT);
    }

    /**
     * Generate particles that are uniformly distributed over the map.
     * @param numOfParticles that need to be generated
     */
    private void generateParticles(final int numOfParticles){
        int i = 0;
        int height = floorPlan.getHeight();
        int width = floorPlan.getWidth();

        while(i < numOfParticles){
            Particle p = new Particle((float)(Math.random() * width), (float)(Math.random()* height));
            if(floorPlan.particleInside(p)){
                //Log.d("Particle Location: " ,p.getCurrentLocation().getX() + ", " + p.getCurrentLocation().getY());
                particles.add(p);
                i++;
            }
        }
    }

    /**
     * Motion model to estimate dx and dy from alpha angle and velocity. Uses Gaussian distributions
     * @param alpha
     * @param time
     */
    protected float[] motionModel(float alpha, float time){
        //Gaussian distribution of mean 1 and SD 0.2m/s
        float v = 1.4f;
        v = v + (float) rand.nextGaussian()*v/8f;

        //Gaussian distribution of mean alpha and SD alphaDeviation
        float alphaDeviation = 20f;

        // Add gaussian noise to the angle
        float alphaNoise = alpha + floorPlan.getNorthAngle() + 90f
                + (float) rand.nextGaussian()*alphaDeviation;

        // Caluclate the dx/dy based on the window size and alpha
        float dx = v*time * (float) Math.cos(Math.toRadians((double)alphaNoise));
        float dy = v*time * (float) Math.sin(Math.toRadians((double)alphaNoise));
        float[] out = {dx,dy};
        return out;
    }

    /**
     * Move the particles with a distance of dx in the x-direction and dy in y-direction.
     * @param alpha angle of movement w.r.t. north
     * @param time time of window capture of movement
     */
    public void movement(float alpha, float time){
        ArrayList<Particle> particleSave = new ArrayList<Particle>(particles.size());
        ArrayList<Particle> cloneParticles = new ArrayList<Particle>(particles.size());

        // Clone all items from particle to particleSave
        for(Particle p : particles) {
            particleSave.add(new Particle(p.getCurrentLocation(), p.getPreviousLocation()));
            cloneParticles.add(new Particle(p.getCurrentLocation(), p.getPreviousLocation()));
        }

        ArrayList<Particle> collisionParticles = new ArrayList<>();

        // Check if a particle has collision upon moving the particle
        for (Particle p : cloneParticles){
            float[] mov = motionModel(alpha,time);
            p.updateLocation(mov[0], mov[1]);
            if(floorPlan.particleCollision(p)){
                collisionParticles.add(p);
            }
            else if (!floorPlan.particleInside(p)){
                collisionParticles.add(p);
            }
            else{
                dx.add(mov[0]);
                dy.add(mov[1]);
            }
        }

        // If 90% of particles have died than don't update the particleList
        if(collisionParticles.size() > cloneParticles.size() * 0.9f){
            return ;
        }

        // New movement so update the walkedPath.
        WalkedPath walkedPath = WalkedPath.getInstance();


        walkedPath.setDx(ArrayOperations.mean(this.dx));
        walkedPath.setDy(ArrayOperations.mean(this.dy));

        // Remove the collided particles from the particle list.
        cloneParticles.removeAll(collisionParticles);

        // Clear out all the particles
        particles.clear();

        // Clone all new particles to the particle list
        for(Particle p : cloneParticles) {
            particles.add(new Particle(p.getCurrentLocation(), p.getPreviousLocation()));
        }

        int safeSize = particles.size();
        
        // Replace the particles that collide with a wall by adding new particles on top of survived particles.
        for (int i = 0; i < collisionParticles.size()*RATIO ; i++) {
            int index = new Random().nextInt(safeSize);
            particles.add(new Particle(particles.get(index).getCurrentLocation().getX(),
                                        particles.get(index).getCurrentLocation().getY()));
        }

        //1-ratio in particleSave
        for (int i = 0; i < collisionParticles.size()*(1-RATIO)-1 ; i++) {
            int index = new Random().nextInt(particleSave.size());
            particles.add(new Particle(particleSave.get(index).getPreviousLocation().getX(),
                    particleSave.get(index).getPreviousLocation().getY()));
        }
    }

    /**
     * Check if the particles have converged, if they have return the location of convergence
     * @param radius
     * @return Location, if converged; null, if not.
     */
    public Location converged(float radius){
        float xmean = 0f;
        float ymean = 0f;
        float xSD = 0f;
        float ySD = 0f;
        for (Particle p : particles){
            xmean += p.getCurrentLocation().getX()/particles.size();
            ymean += p.getCurrentLocation().getY()/particles.size();
        }
        for (Particle p : particles){
            xSD += (p.getCurrentLocation().getX()-xmean)*(p.getCurrentLocation().getX()-xmean);
            ySD += (p.getCurrentLocation().getY()-ymean)*(p.getCurrentLocation().getY()-ymean);
        }
        xSD = xSD/particles.size();
        xSD = (float)Math.sqrt(xSD);
        ySD = ySD/particles.size();
        ySD = (float)Math.sqrt(ySD);

        if (xSD < radius && ySD < radius ){
            return new Location(xmean,ymean);
        }
        return null;
    }

    public ArrayList<Particle> getParticles(){ return this.particles;}
}
