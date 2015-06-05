package io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.Particle;
import io.github.kajdreef.smartphonesensing.R;

/**
 * Created by Uncle John on 20/05/2015.
 */
public class ParticleFilter {
    private ArrayList<Particle> particles;
    private FloorPlan floorPlan;
    private final int N_INIT;
    private final float RATIO = 9/(float)10;

    //sampling frequency
    private final int f = 200;

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
     * @param velocity
     */
    protected float[] motionModel(float alpha, float velocity,int windowSize){
        //Gaussian distribution of mean 1 and SD 0.2m/s
        float v = velocity + (float) new Random().nextGaussian()*velocity/5f;

        //Gaussian distribution of mean alpha and SD alphaDeviation
        float alphaDeviation = 20f;

        // Add gaussian noise to the angle
        float alphaNoise = alpha + floorPlan.getNorthAngle() + 90f
                + (float) new Random().nextGaussian()*2*alphaDeviation-alphaDeviation;

        // Caluclate the dx/dy based on the window size and alpha
        float dx = v*windowSize * (float) Math.cos(Math.toRadians((double)alphaNoise))/f;
        float dy = v*windowSize * (float) Math.sin(Math.toRadians((double)alphaNoise))/f;
        float[] out = {dx,dy};
        return out;
    }

    /**
     * Move the particles with a distance of dx in the x-direction and dy in y-direction.
     * @param alpha angle of movement w.r.t. north
     * @param velocity velocity of movement
     */
    public void movement(float alpha,float velocity,int windowSize){
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
            float[] mov = motionModel(alpha,velocity,windowSize);
            p.updateLocation(mov[0], mov[1]);
            if(floorPlan.particleCollision(p)){
                collisionParticles.add(p);
            }
            else if (!floorPlan.particleInside(p)){
                collisionParticles.add(p);
            }
        }

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

    public ArrayList<Particle> getParticles(){ return this.particles;}
}
