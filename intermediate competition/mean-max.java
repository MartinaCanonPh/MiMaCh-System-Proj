import java.util.*;

import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        // game loop
        while (true) {
            int myScore = in.nextInt();
            int enemyScore1 = in.nextInt();
            int enemyScore2 = in.nextInt();
            int myRage = in.nextInt();
            int enemyRage1 = in.nextInt();
            int enemyRage2 = in.nextInt();
            int unitCount = in.nextInt();
            Map map=new Map(in, unitCount);

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("0 0 300");
            System.out.println("WAIT");
            System.out.println("WAIT");
        }
    }
}

class Map{
    Entity[] mapping;

    public Map(Scanner in, int unitCount){
        mapping=new Entity[unitCount];
        for(int i=0; i<unitCount; i++){
            mapping[i]=new Entity(in);
        }
    }

    public Entity[] getMapping() {
        return this.mapping;
    }

    public void setMapping(Entity[] mapping) {
        this.mapping = mapping;
    }

}


class Entity{
    int unitId;
    int unitType;
    int player;
    float mass;
    int radius;
    int x;
    int y;
    int vx;
    int vy;
    int extra;
    int extra2;

    public Entity(Scanner in){
        unitId = in.nextInt();
        unitType = in.nextInt();
        player = in.nextInt();
        mass = in.nextFloat();
        radius = in.nextInt();
        x = in.nextInt();
        y = in.nextInt();
        vx = in.nextInt();
        vy = in.nextInt();
        extra = in.nextInt();
        extra2 = in.nextInt();
    }

    //funzione distanza tra se stesso e un x, y dato
    //funzione è nella pozzanghera? boolean

    public int getUnitId() {
        return this.unitId;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    public int getUnitType() {
        return this.unitType;
    }

    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }

    public int getPlayer() {
        return this.player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public float getMass() {
        return this.mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVx() {
        return this.vx;
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public int getVy() {
        return this.vy;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public int getExtra() {
        return this.extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

    public int getExtra2() {
        return this.extra2;
    }

    public void setExtra2(int extra2) {
        this.extra2 = extra2;
    }


    
}