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
    private ArrayList<Float> dx;
    private ArrayList<Float> dy;
    private Location convergenceLocation;
    private Path pathWalked;

    private Matrix scaleMatrix;
    private float offsetX;
    private float offsetY;

    private static WalkedPath singleton = null;

    private Paint walkedColor;
    private ArrayList<Float> pathX;
    private ArrayList<Float> pathY;

    private WalkedPath(){
        dx = new ArrayList<Float>();
        dy = new ArrayList<Float>();

        pathX = new ArrayList<Float>();
        pathY = new ArrayList<>();

        pathWalked = new Path();

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

        pathWalked.reset();
        if(convergencePoint != null) {
//            if (pathWalked.isEmpty()) {
                // Initial Location
                float x = convergencePoint.getX();
                float y = convergencePoint.getY();

                // Convergence location
                convergenceLocation = new Location(x, y);
                pathWalked.moveTo(convergenceLocation.getX(), convergenceLocation.getY());

                pathX.add(convergenceLocation.getX());
                pathY.add(convergenceLocation.getY());

                float previousX = x;
                float previousY = y;

                // Iterate over all dx to get the walked path so far
                for(int i = dx.size()-1; i >= 0; i-- ){
                    float dx = this.dx.get(i);
                    float dy = this.dy.get(i);

                    x = x - dx;
                    y = y - dy;

                    Log.d("WalkedPath", "Relative dx: " + dx + ", dy: " + dy);

                    Log.d("WalkedPath", "position x: " + x + ", y: " + y);

                    pathX.add(x);
                    pathY.add(y);

                    pathWalked.lineTo(x, y);
                }

                this.transform();
//            }
        }
    }

    public void setOffset(float _offsetX, float _offSetY){
        this.offsetX = _offsetX;
        this.offsetY = _offSetY;
    }

    public void initTransform(Matrix _scaleMatrix, float _offSetX, float _offSetY){
        scaleMatrix = new Matrix();
        scaleMatrix.set(_scaleMatrix);

        offsetX = _offSetX;
        offsetY = _offSetY;
    }

    public void transform(){
        pathWalked.transform(scaleMatrix);
        pathWalked.offset(offsetX, offsetY);
    }

    public void reset(){
        dx.clear();
        dy.clear();
        pathX.clear();
        pathY.clear();
        pathWalked.reset();
    }

    public void draw(Canvas canvas){
        canvas.drawPath(pathWalked, walkedColor);
    }

    public ArrayList<Float> getPathX(){return this.pathX;}
    public ArrayList<Float> getPathY(){return this.pathY;}
}
