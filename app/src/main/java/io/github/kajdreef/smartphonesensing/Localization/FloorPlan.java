package io.github.kajdreef.smartphonesensing.Localization;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

import java.util.ArrayList;

/**
 * Created by kajdreef on 15/05/15.
 */
public class FloorPlan {
    private float size = 20f;
    private ArrayList<Wall> allWalls;
    private Path wallPath;
    private Region floorRegion;

    private Location    A = new Location(0f,     0f),
                        B = new Location(8f,     0f),
                        C = new Location(8f,     6.1f),
                        D = new Location(12f,    6.1f),
                        E = new Location(12f,    0f),
                        F = new Location(16f,    0f),
                        G = new Location(16f,    6.1f),
                        H = new Location(20f,    0f),
                        I = new Location(20f,    6.1f),
                        J = new Location(72f,    6.1f),
                        K = new Location(72f,    8.2f),
                        L = new Location(64f,    8.2f),
                        M = new Location(64f,    14.3f),
                        N = new Location(60f,    14.3f),
                        O = new Location(60f,    8.2f),
                        P = new Location(56f,    14.3f),
                        Q = new Location(56f,    8.2f),
                        R = new Location(16f,    8.2f),
                        S = new Location(16f,    14.3f),
                        T = new Location(12f,    14.3f),
                        U = new Location(12f,    11.3f),
                        V = new Location(14.5f,  11.3f),
                        W = new Location(14.5f,  8.2f),
                        X = new Location(0f,     8.2f);

    //Angle in degrees counted positively from x to y
    private static int northAngle = 200;




    public FloorPlan(){
        wallPath = new Path();

        // Add the walls to the path (used to draw the path)
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

        // Used for collision detection!
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
     * @return true = inside or false = not inside
     */
    public boolean particleInside(Particle particle){
        Location loc = particle.getCurrentLocation();
        return floorRegion.contains((int) loc.getX(), (int) loc.getY());
    }

    /**
     * Based on: http://stackoverflow.com/questions/25830932/how-to-find-if-two-line-segments-intersect-or-not-in-java
     * @param p
     * @param q
     * @param r
     * @return
     */
    private int orientation(Location p, Location q, Location r) {
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
                - (q.getX() - p.getX()) * (r.getY() - q.getY());

        if (val == 0.0)
            return 0;
        return (val > 0) ? 1 : 2;
    }

    /**
     * Collision detection with wall
     * based on http://stackoverflow.com/questions/25830932/how-to-find-if-two-line-segments-intersect-or-not-in-java
     * @param p
     * @return true (=collision) or false (=no collision)
     */
    public boolean particleCollision(Particle p) {
        for(Wall w : allWalls) {
            int o1 = orientation(p.getPreviousLocation(), p.getCurrentLocation(), w.start);
            int o2 = orientation(p.getPreviousLocation(), p.getCurrentLocation(), w.end);
            int o3 = orientation(w.start, w.end, p.getPreviousLocation());
            int o4 = orientation(w.start, w.end, p.getCurrentLocation());

            if (o1 != o2 && o3 != o4) {
                return true;
            }
        }
        return false;
    }

    public int getWidth(){
        return floorRegion.getBounds().width();
    }

    public int getHeight(){
        return floorRegion.getBounds().height();
    }
    public static float getNorthAngle(){return northAngle;}
}
