package io.github.kajdreef.smartphonesensing.Localization;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kajdreef on 15/05/15.
 */
public class FloorPlan {
    private float size = 20f, offsetX = 50f, offSetY = 50f;
    private ArrayList<Wall> allWalls;
    private Location    A = new Location(offsetX + 0f*size,     offSetY + 0f*size),
                        B = new Location(offsetX + 8f*size,     offSetY + 0f*size),
                        C = new Location(offsetX + 8f*size,     offSetY + 6.1f*size),
                        D = new Location(offsetX + 12f*size,    offSetY + 6.1f*size),
                        E = new Location(offsetX + 12f*size,    offSetY + 0f*size),
                        F = new Location(offsetX + 16f*size,    offSetY + 0f*size),
                        G = new Location(offsetX + 16f*size,    offSetY + 6.1f*size),
                        H = new Location(offsetX + 20f*size,    offSetY + 0f*size),
                        I = new Location(offsetX + 20f*size,    offSetY + 6.1f*size),
                        J = new Location(offsetX + 72f*size,    offSetY + 6.1f*size),
                        K = new Location(offsetX + 72f*size,    offSetY + 8.2f*size),
                        L = new Location(offsetX + 64f*size,    offSetY + 8.2f*size),
                        M = new Location(offsetX + 64f*size,    offSetY + 14.3f*size),
                        N = new Location(offsetX + 60f*size,    offSetY + 14.3f*size),
                        O = new Location(offsetX + 60f*size,    offSetY + 8.2f*size),
                        P = new Location(offsetX + 56f*size,    offSetY + 14.3f*size),
                        Q = new Location(offsetX + 56f*size,    offSetY + 8.2f*size),
                        R = new Location(offsetX + 16f*size,    offSetY + 8.2f*size),
                        S = new Location(offsetX + 16f*size,    offSetY + 14.3f*size),
                        T = new Location(offsetX + 12f*size,    offSetY + 14.3f*size),
                        U = new Location(offsetX + 12f*size,    offSetY + 11.3f*size),
                        V = new Location(offsetX + 14.5f*size,  offSetY + 11.3f*size),
                        W = new Location(offsetX + 14.5f*size,  offSetY + 8.2f*size),
                        X = new Location(offsetX + 0f*size,     offSetY + 8.2f*size);



    public FloorPlan(){
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

    public ArrayList<Wall> getWalls(){
        return allWalls;
    }

    public void setWalls(ArrayList<Wall> newWalls){
        this.allWalls = newWalls;
    }

    public void addWall(Location loc1, Location loc2){
        this.allWalls.add(new Wall(loc1, loc2));
    }
}
