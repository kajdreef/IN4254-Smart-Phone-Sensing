package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

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

    Path wallPath;

    public LocalizationView(Context ctx, Path floorPlan, ArrayList<Particle> allParticles, float width, float height){
        super(ctx);

        this.wallPath = floorPlan;
        this.particles = new CopyOnWriteArrayList<Particle>(allParticles);

        // Initialise the walls
        Matrix scaleMatrix = new Matrix();
        RectF rectF = new RectF();
        this.wallPath.computeBounds(rectF, true);

        // Scale the floorplan depending on size of the screen.
        this.scale = (size*width)/rectF.width();
        scaleMatrix.setScale(scale, scale, rectF.left, rectF.top);
        this.wallPath.transform(scaleMatrix);

        this.offSetX = (width - width*size)/2;
        this.offSetY = (height - height*size)/2;


        // Offset of the dx and dy
        wallPath.offset(offSetX, offSetY);

        // Initialise the compass
        compass = new Compass((int) width, (int) height, 0f, 100);

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
