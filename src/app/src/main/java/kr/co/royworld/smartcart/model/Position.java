package kr.co.royworld.smartcart.model;

public class Position {
    private int posX;
    private int posY;
    private double distance;
    private Position prev;
    private int astarG;
    private int astarH;
    private int astarF;

    public Position() {}

    public Position(int x, int y){
        this.posX = x;
        this.posY = y;
    }

    public Position(int x, int y, double dist){
        this.posX = x;
        this.posY = y;
        this.distance = dist;
    }

    public Position(int x, int y, double dist, int astarF, int astarG, int astarH){
        this.posX = x;
        this.posY = y;
        this.distance = dist;
        this.astarF = astarF;
        this.astarG = astarG;
        this.astarH = astarH;
    }

    public String toString(){
        return "posX:"+posX+", posY:"+posY+", distance:"+distance + ", astarF:" + astarF +", astarG:"+astarG+", astarH:"+astarH;
    }

    public int getPosX() {
        return posX;
    }
    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }
    public void setPosY(int posY) {
        this.posY = posY;
    }

    public double getDistance() {
        return distance;
    }
    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Position getPrev() { return prev; }
    public void setPrev(Position prev) { this.prev = prev; }

    public int getAstarG() { return astarG; }
    public void setAstarG(int astarG) { this.astarG = astarG; }

    public int getAstarH() { return astarH; }
    public void setAstarH(int astarH) { this.astarH = astarH; }

    public int getAstarF() { return astarF; }
    public void setAstarF(int astarF) { this.astarF = astarF; }
}
