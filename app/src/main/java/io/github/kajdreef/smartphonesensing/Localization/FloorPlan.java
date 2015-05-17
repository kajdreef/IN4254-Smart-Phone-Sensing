package io.github.kajdreef.smartphonesensing.Localization;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;

import java.util.ArrayList;

import io.github.kajdreef.smartphonesensing.Utils.LineIntersect;

/**
 * Created by kajdreef on 15/05/15.
 */
public class FloorPlan {
    private float size = 20f;
    private ArrayList<Wall> allWalls;
    private Path wallPath;
    private Region floorRegion;

    private Location    A = new Location(0f*size,     0f*size),
                        B = new Location(8f*size,     0f*size),
                        C = new Location(8f*size,     6.1f*size),
                        D = new Location(12f*size,    6.1f*size),
                        E = new Location(12f*size,    0f*size),
                        F = new Location(16f*size,    0f*size),
                        G = new Location(16f*size,    6.1f*size),
                        H = new Location(20f*size,    0f*size),
                        I = new Location(20f*size,    6.1f*size),
                        J = new Location(72f*size,    6.1f*size),
                        K = new Location(72f*size,    8.2f*size),
                        L = new Location(64f*size,    8.2f*size),
                        M = new Location(64f*size,    14.3f*size),
                        N = new Location(60f*size,    14.3f*size),
                        O = new Location(60f*size,    8.2f*size),
                        P = new Location(56f*size,    14.3f*size),
                        Q = new Location(56f*size,    8.2f*size),
                        R = new Location(16f*size,    8.2f*size),
                        S = new Location(16f*size,    14.3f*size),
                        T = new Location(12f*size,    14.3f*size),
                        U = new Location(12f*size,    11.3f*size),
                        V = new Location(14.5f*size,  11.3f*size),
                        W = new Location(14.5f*size,  8.2f*size),
                        X = new Location(0f*size,     8.2f*size);



    public FloorPlan(){
        wallPath = new Path();

        // Add the walls to the path
        wallPath.moveTo(A.getX(), A.getY());
        wallPath.lineTo(B.getX(), B.getY());
        wallPath.lineTo(C.getX(), C.getY());
        wallPath.lineTo(D.getX(), D.getY());
        wallPath.lineTo(E.getX(), E.getY());
        wallPath.lineTo(F.getX(), F.getY());
        wallPath.lineTo(G.getX(), G.getY());
        wallPath.lineTo(F.getX(), F.getY());
        wallPath.lineTo(H.getX(), H.getY());
        wallPath.lineTo(I.getX(), I.getY());
        wallPath.lineTo(J.getX(), J.getY());
        wallPath.lineTo(K.getX(), K.getY());
        wallPath.lineTo(L.getX(), L.getY());
        wallPath.lineTo(M.getX(), M.getY());
        wallPath.lineTo(N.getX(), N.getY());
        wallPath.lineTo(O.getX(), O.getY());
        wallPath.lineTo(N.getX(), N.getY());
        wallPath.lineTo(P.getX(), P.getY());
        wallPath.lineTo(Q.getX(), Q.getY());
        wallPath.lineTo(R.getX(), R.getY());
        wallPath.lineTo(S.getX(), S.getY());
        wallPath.lineTo(T.getX(), T.getY());
        wallPath.lineTo(U.getX(), U.getY());
        wallPath.lineTo(V.getX(), V.getY());
        wallPath.lineTo(W.getX(), W.getY());
        wallPath.lineTo(X.getX(), X.getY());
        wallPath.lineTo(A.getX(), A.getY());

        // Initialise the bounds
        RectF rectF = new RectF();
        wallPath.computeBounds(rectF, true);
        floorRegion = new Region();
        floorRegion.setPath(wallPath, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        allWalls = new ArrayList<>();

        addWall(A, B);
        addWall(B, C);
        addWall(C, D);
        addWall(D, E);
        addWall(E, F);
        addWall(F, G);
        addWall(F, H);
        addWall(H, I);
        addWall(I, J);
        addWall(J, K);
        addWall(K, L);
        addWall(L, M);
        addWall(M, N);
        addWall(N, O);
        addWall(P, N);
        addWall(P, Q);
        addWall(Q, R);
        addWall(R, S);
        addWall(S, T);
        addWall(T, U);
        addWall(U, V);
        addWall(V, W);
        addWall(W, X);
        addWall(X, A);
    }

    public Path getPath(){
        return wallPath;
    }

    public ArrayList<Wall> getWalls(){
        return allWalls;
    }

    public void setWalls(ArrayList<Wall> newWalls){
        this.allWalls = newWalls;
    }

    public void addWall(Location loc1, Location loc2){
        this.allWalls.add(new Wall(loc1, loc2));
    }

    /**
     * Check if the particle is inside a specific region
     * @param particle
     * @return true = intersecting or false = no intersecting
     */
    public boolean particleInside(Particle particle){
        Location loc = particle.getCurrentLocation();
        return floorRegion.contains((int) loc.getX(), (int) loc.getY());
    }

    private int orientation(Location p, Location q, Location r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
                - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0.0)
            return 0; // colinear
        return (val > 0) ? 1 : 2; // clock or counterclock wise
    }

    /**
     * Collision detection with wall
     * @param p
     * @return true (=collision) or false (=no collision)
     */
    public boolean particleCollision(Particle p) {
        for(Wall w : allWalls) {
            int o1 = orientation(p.getPreviousLocation(), p.getCurrentLocation(), w.start);
            int o2 = orientation(p.getPreviousLocation(), p.getCurrentLocation(), w.end);
            int o3 = orientation(w.start, w.end, p.getPreviousLocation());
            int o4 = orientation(w.start, w.end, p.getCurrentLocation());

            if (o1 != o2 && o3 != o4)
                return true;
        }
        return false;
    }

    public int getWidth(){
        return floorRegion.getBounds().width();
    }

    public int getHeight(){
        return floorRegion.getBounds().height();
    }
}
