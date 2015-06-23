package io.github.kajdreef.smartphonesensing.Localization.LocalizationView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import io.github.kajdreef.smartphonesensing.Localization.FloorPlan;
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
    ArrayList<Float> pathX;
    ArrayList<Float> pathY;

    private WalkedPath(){
        dx = new ArrayList<Float>();
        dy = new ArrayList<Float>();

        pathX = new ArrayList<Float>();
        pathY = new ArrayList<>();

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
                pathX.add(convergenceLocation.getX());
                pathY.add(convergenceLocation.getY());

                float previousX = startX;
                float previousY = startY;

                Iterator iteratorX = dx.iterator();
                Iterator iteratorY = dy.iterator();

                // Iterate over all dx to get the walked path so far
                while (iteratorX.hasNext() && iteratorY.hasNext()) {
                    float dx = (float) iteratorX.next();
                    float dy = (float) iteratorY.next();

                    previousX = previousX - dx;
                    previousY = previousY - dy;

                    Log.d("WalkedPath", "Relative dx: " + dx + ", dy: " + dy);

                    Log.d("WalkedPath", "position x: " + previousX + ", y: " + previousY);

                    pathX.add(previousX);
                    pathY.add(previousY);

                    walkedPath.lineTo(previousX, previousY);
//                    walkedPath.rLineTo(-dx,dy);
                }
                Log.d("WalkedPath", pathX.toString());
                Log.d("WalkedPath", pathY.toString());
                Log.d("WalkedPath", walkedPath.toString());

                this.transform();

            }
//            else {
//                Iterator iteratorX = dx.iterator();
//                Iterator iteratorY = dy.iterator();
//
//                // Iterate over all dx to get the walked path so far
//                if (iteratorX.hasNext() && iteratorY.hasNext()) {
//                    float x = startX + dx.iterator().next();
//                    float y = startY + dy.iterator().next();
//                    Log.d("WalkedPath", "Added Line");
//                    walkedPath.lineTo(x, y);
//
//                    startX = x;
//                    startY = y;
//                } else {
//                    dx.clear();
//                    dy.clear();
//                }
//            }
        }
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

    public void initTransform(Matrix _scaleMatrix, float _offSetX, float _offSetY){
//        RectF rectF = new RectF();
//        walkedPath.computeBounds(rectF, true);
//
//        // Scale the floorplan depending on size of the screen.
//        float scale = (size*width)/rectF.width();
//        scaleMatrix  = new Matrix();
//        scaleMatrix.setScale(scale, scale, rectF.left, rectF.top);
//
//        this.offsetX = (width - width*size)/2;
//        this.offsetY = (height - height*size)/2;

        this.scaleMatrix = new Matrix();
        scaleMatrix.set(_scaleMatrix);

        this.offsetX = _offSetX;
        this.offsetY = _offSetY;
    }

    public void transform(){
        walkedPath.transform(scaleMatrix);
        walkedPath.offset(offsetX, offsetY);
    }

    public void draw(Canvas canvas){
        canvas.drawPath(walkedPath, walkedColor);
    }

    public ArrayList<Float> getPathX(){return this.pathX;}
    public ArrayList<Float> getPathY(){return this.pathY;}
}
