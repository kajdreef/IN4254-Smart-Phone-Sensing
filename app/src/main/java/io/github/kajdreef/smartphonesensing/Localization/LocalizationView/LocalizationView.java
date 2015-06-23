package io.github.kajdreef.smartphonesensing.Localization.LocalizationView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import io.github.kajdreef.smartphonesensing.Localization.Particle;

/**
 * Created by kajdreef on 15/05/15.
 */
public class LocalizationView extends View {

    public CopyOnWriteArrayList<Particle> particles;
    private float offSetX;
    private float offSetY;
    private final float size = 0.98f;
    private float scale;
    private Paint particlePaint, wallPaint;
    private Compass compass;
    private WalkedPath walkedPath;

    private Path wallPath;

    public LocalizationView(Context ctx, Path floorPlan, ArrayList<Particle> allParticles, float _width, float _height){
        super(ctx);

        this.wallPath = floorPlan;
        this.particles = new CopyOnWriteArrayList<Particle>(allParticles);
        this.walkedPath = WalkedPath.getInstance();

        // Initialise the walls
        Matrix scaleMatrix = new Matrix();
        RectF rectF = new RectF();
        this.wallPath.computeBounds(rectF, true);

        // Scale the floorplan depending on size of the screen.
        this.scale = (size*_width)/rectF.width();
        scaleMatrix.setScale(scale, scale, rectF.left, rectF.top);
        this.wallPath.transform(scaleMatrix);

        this.offSetX = (_width - _width*size)/2;
        this.offSetY = (_height - _height*size)/2;


        // Offset of the dx and dy
        wallPath.offset(offSetX, offSetY);

        walkedPath.initTransform(scaleMatrix, offSetX, offSetY);

        // Initialise the compass
        compass = new Compass((int) _width, (int) _height, 0f, 100);

        particlePaint = new Paint();
        particlePaint.setStrokeWidth(3);
        particlePaint.setColor(Color.RED);

        wallPaint = new Paint();
        wallPaint.setStyle(Paint.Style.STROKE);
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(3);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw Walls
        canvas.drawPath(wallPath, wallPaint);

        // Draw the compass
        compass.draw(canvas);

        // Check ift he particles have converged.
        if(particlePaint.getColor() == Color.GREEN){
            walkedPath.draw(canvas);
        }

        // Draw Particles
        for(Particle p : this.particles) {
            canvas.drawPoint(p.getCurrentLocation().getX()*scale + offSetX, p.getCurrentLocation().getY()*scale + offSetY , particlePaint);
        }
    }

    /**
     * Update the angle of the compass
     * @param _angle
     */
    public void setAngle(float _angle){
        compass.setAngle(_angle);
    }

    /**
     * Set the array of particles.
     * @param newParticles
     */
    public void setParticles(ArrayList<Particle> newParticles) {
        particles = new CopyOnWriteArrayList<Particle>(newParticles);
    }

    /**
     * Set the color of the particles (used when particles have converged)
     * @param _color
     */
    public void setColor(int _color){
        particlePaint.setColor(_color);
    }

    /**
     * Reset the views parameters
     */
    public void reset(){
        particlePaint.setColor(Color.RED);
    }
}
