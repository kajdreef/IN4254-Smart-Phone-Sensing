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

/**
 * Created by kajdreef on 15/05/15.
 */
public class LocalizationView extends View {

    public ArrayList<Particle> particles;
    private float offSetX;
    private float offSetY;
    private final float size = 0.8f;
    private float scale;
    private Point compassDir;
    private Point compassPlac;
    private final int compassRadius = 100;

    Path wallPath;

    public LocalizationView(Context ctx, Path floorPlan, ArrayList<Particle> allParticles, float width, float height){
        super(ctx);

        this.wallPath = floorPlan;
        this.particles = allParticles;

        Matrix scaleMatrix = new Matrix();
        RectF rectF = new RectF();
        this.wallPath.computeBounds(rectF, true);

        // Scale the floorplan depending on size of the screen.
        this.scale = (size*width)/rectF.width();
        scaleMatrix.setScale(scale, scale, rectF.left, rectF.top);
        this.wallPath.transform(scaleMatrix);

        this.offSetX = (width - width*0.8f)/2;
        this.offSetY = (height - height*0.8f)/2;

        // Initialise the compass
        compassDir = new Point();
        compassPlac = new Point((int)(width/2), (int)(height/2));
        setAngle(0f);

        // Offset of the dx and dy
        wallPath.offset(offSetX, offSetY);
    }

    public void setAngle(float _angle){
        compassDir.set( compassPlac.x + (int)(compassRadius*Math.cos(Math.toRadians((double) _angle + 90 + FloorPlan.getNorthAngle()))),
                        compassPlac.y + (int)(compassRadius*Math.sin(Math.toRadians((double) _angle + 90 + FloorPlan.getNorthAngle()))));

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

        // Draw Compass circle
        canvas.drawCircle(compassPlac.x, compassPlac.y, compassRadius, paint);
        paint.setStrokeWidth(20);
        paint.setColor(Color.BLUE);
        // Draw Direction of the compass
        canvas.drawLine(compassPlac.x, compassPlac.y, compassDir.x, compassDir.y, paint);
        //canvas.drawPoint(compassDir.x, compassDir.y, paint);

        paint.setStrokeWidth(5);
        paint.setColor(Color.RED);
        // Draw Direction of the compass
        canvas.drawPoint(compassDir.x, compassDir.y, paint);

        // Draw Particles
        for(Particle p : this.particles) {
            canvas.drawPoint(p.getCurrentLocation().getX()*scale + offSetX, p.getCurrentLocation().getY()*scale + offSetY , paint);
        }
    }

    public void setParticles(ArrayList<Particle> newParticles) {
        this.particles = newParticles;
    }
}
