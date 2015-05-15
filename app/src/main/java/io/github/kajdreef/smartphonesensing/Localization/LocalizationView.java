package io.github.kajdreef.smartphonesensing.Localization;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by kajdreef on 15/05/15.
 */
public class LocalizationView extends View {

    public ArrayList<Wall> walls;
    public ArrayList<Particle> particles;

    public LocalizationView(Context ctx, ArrayList<Wall> allWalls, ArrayList<Particle> allParticles){
        super(ctx);
        this.walls = allWalls;
        this.particles = allParticles;
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
        paint.setColor(Color.BLACK);

        // Draw Walls
        for(Wall w : this.walls) {
            canvas.drawLine(w.start.getX(), w.start.getY(), w.end.getX(), w.end.getY(), paint);
        }

        paint.setStrokeWidth(5);

        paint.setColor(Color.RED);
        // Draw Particles
        for(Particle p : this.particles) {
            canvas.drawPoint(p.getCurrentLocation().getX(), p.getCurrentLocation().getX(), paint);
        }
    }

    public void setParticles(ArrayList<Particle> newParticles) {
        this.particles = newParticles;
    }
}
