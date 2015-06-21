package io.github.kajdreef.smartphonesensing.Localization.LocalizationView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import io.github.kajdreef.smartphonesensing.Localization.Location;

/**
 * Created by kajdreef on 21/06/15.
 */
public class WalkedPath {
    ArrayList<Float> dx;
    ArrayList<Float> dy;
    Location convergenceLocation;
    Path walkedPath;
    boolean converged = false;

    private Matrix scaleMatrix;
    private float offsetX;
    private float offsetY;

    private static WalkedPath singleton = null;

    // Store the last known location of the user
    private float startX = -1;
    private float startY = -1;
    Paint walkedColor;

    private WalkedPath(){
        dx = new ArrayList<Float>();
        dy = new ArrayList<Float>();
        walkedPath = new Path();

        walkedColor = new Paint();
        walkedColor.setStrokeWidth(3);
        walkedColor.setColor(Color.BLUE);
        walkedColor.setStyle(Paint.Style.STROKE);
    }

    public static WalkedPath getInstance(){
        if(singleton == null){
            singleton = new WalkedPath();
        }

        return singleton;
    }

    public void setDx(float _dx){
        dx.add(_dx);
    }

    public void setDy(float _dy){
        dy.add(_dy);
    }

    public void setPath(Location convergencePoint) {
        if(convergencePoint != null) {
            converged = true;
            if (walkedPath.isEmpty()) {
                // Initial Location
                startX = convergencePoint.getX();
                startY = convergencePoint.getY();

                // Convergence location
                convergenceLocation = new Location(startX, startY);
                walkedPath.moveTo(convergenceLocation.getX(), convergenceLocation.getY());

                float previousX = startX;
                float previousY = startY;

                Iterator iteratorX = dx.iterator();
                Iterator iteratorY = dy.iterator();

                // Iterate over all dx to get the walked path so far
                if (iteratorX.hasNext() && iteratorY.hasNext()) {

                    float x = previousX - dx.iterator().next();
                    float y = previousY - dy.iterator().next();
                    walkedPath.lineTo(x, y);
                    Log.d("WalkedPath", "Line to: " + x + ", "+ y);
                    previousX = x;
                    previousY = y;
                } else {
                    dx.clear();
                    dy.clear();
                }
                walkedPath.moveTo(startX, startY);

            } else {
                Iterator iteratorX = dx.iterator();
                Iterator iteratorY = dy.iterator();

                // Iterate over all dx to get the walked path so far
                if (iteratorX.hasNext() && iteratorY.hasNext()) {
                    float x = startX + dx.iterator().next();
                    float y = startY + dy.iterator().next();
                    Log.d("WalkedPath", "Added Line");
                    walkedPath.lineTo(x, y);

                    startX = x;
                    startY = y;
                } else {
                    dx.clear();
                    dy.clear();
                }
            }
        }
    }

    public void setTransform(Matrix _scaleMatrix){
        this.scaleMatrix = _scaleMatrix;
    }
    public void setOffset(float _offsetX, float _offSetY){
        this.offsetX = _offsetX;
        this.offsetY = _offSetY;
    }

    public void reset(){
        walkedPath.reset();
        dx.clear();
        dy.clear();
        converged = false;
        convergenceLocation = null;

    }

    public boolean hasConverged(){
        return converged;
    }

    public void draw(Canvas canvas){
        walkedPath.offset(offsetX, offsetY);
        walkedPath.transform(scaleMatrix);
        canvas.drawPath(walkedPath, walkedColor);
    }
}
