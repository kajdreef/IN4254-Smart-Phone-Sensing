package io.github.kajdreef.smartphonesensing.Localization.ParticleFiltering;

import java.util.ArrayList;
import java.util.Random;

import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
import io.github.kajdreef.smartphonesensing.Localization.Particle;

/**
 * Created by Uncle John on 20/05/2015.
 */
public class ParticleFilter {
    private ArrayList<Particle> particles;
    private FloorPlan floorPlan;
    private final int N_INIT;
    private final float RATIO = 9/(float)10;

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
     * Move the particles with a distance of dx in the x-direction and dy in y-direction.
     * @param dx (movement in the x-direction)
     * @param dy (movement in the y-direction)
     */
    public void movement(float dx,float dy){
        ArrayList<Particle> particleSave = new ArrayList<>();
//        particleSave.addAll(particles);
        particleSave.addAll((ArrayList<Particle>) particles.clone());
        ArrayList<Particle> collisionParticles = new ArrayList<>();

        // Check if a particle has collision upon moving the particle
        for (Particle p : particles){
            p.updateLocation( dx, dy);
            if(floorPlan.particleCollision(p)){
                collisionParticles.add(p);
            }
            else if (!floorPlan.particleInside(p)){
                collisionParticles.add(p);
            }
        }

        // Remove the collided particles from the particle list.
        particles.removeAll(collisionParticles);
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
