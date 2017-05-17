package edu.upc.pes.wallachange.Models;

/**
 * Created by carlota on 17/5/17.
 */

public class Coordenades {
    private int x;
    private int y;

    public Coordenades() {

    }
    public void setX(int x) {
        this.x = x;
    }
    public void sety(int y) {
        this.y = y;
    }
    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
