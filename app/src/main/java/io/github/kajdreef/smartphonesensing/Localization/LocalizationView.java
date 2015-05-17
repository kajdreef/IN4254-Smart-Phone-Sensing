package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by kajdreef on 15/05/15.
 */
public class LocalizationView extends View {

    public ArrayList<Wall> walls;
    public ArrayList<Particle> particles;
    private final float offsetX = 50f;
    private final float offsetY = 50f;

    Path wallPath;

    public LocalizationView(Context ctx, Path floorPlan, ArrayList<Particle> allParticles){
        super(ctx);
        this.wallPath = floorPlan;
        this.particles = allParticles;

        // Offset of the dx and dy
        wallPath.offset(offsetX, offsetY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawPaint(paint);

        // Use Color.parseColor to define HTML colors
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);

        // Draw Walls
        canvas.drawPath(wallPath, paint);

        paint.setStrokeWidth(5);

        paint.setColor(Color.RED);
        // Draw Particles
        for(Particle p : this.particles) {
            canvas.drawPoint(p.getCurrentLocation().getX() + offsetX, p.getCurrentLocation().getY() + offsetY , paint);
        }
    }

    public void setParticles(ArrayList<Particle> newParticles) {
        this.particles = newParticles;
    }
}
