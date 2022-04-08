import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Entity {

    //variabili con visibilità di file
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
    int myRage;

    //costruttore con parametri
    public Entity(int unitId, int unitType, int player, float mass, int radius, int x, int y, int vx, int vy, int extra, int extra2) {
        this.unitId = unitId;
        this.unitType = unitType;
        this.player = player;
        this.mass = mass;
        this.radius = radius;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.extra = extra;
        this.extra2 = extra2;
    }

    //costruttore senza parametri
    public Entity()
    {

    }

    double distance(int x2,int y2)
    {
        return java.lang.Math.sqrt((x2-this.x)*(x2-this.x) + (y2-this.y)*(y2-this.y));
    }

    Entity near(List<Entity> entities)
    {
        for(Entity e:entities)
        {
            if(this.distance(e.x,e.y)<=3000)
                return e;
        }
        return null;
    }

    boolean enemyOnWreck(List<Entity> enemies)
    {
        for(Entity e:enemies)
            if(this.distance(e.x, e.y)<=e.radius)
                return true;
        return false;
    }

    boolean tankOnWreck(List<Entity> tanks)
    {
        for(Entity e:tanks)
            if(this.distance(e.x, e.y)<=e.radius)
                return true;
        return false;
    }

    boolean playerOnWreck(List<Entity> wrecks)
    {
        for(Entity e:wrecks)
            if(this.distance(e.x, e.y)<=e.radius)
                return true;
        return false;
    }

    Entity wreckOfThePlayer(List<Entity> wrecks)
    {
        for(Entity e:wrecks)
            if(this.distance(e.x, e.y)<=e.radius)
                return e;
        return null;
    }

    Entity wreckOfTheEnemy(List<Entity> wrecks)
    {
        for(Entity e:wrecks)
            if(this.distance(e.x, e.y)<=e.radius)
                return e;
        return null;
    }

    int howManyOnWreck(List<Entity> entities)
    {
        int count = 0;
        for(Entity e:entities)
        {
            if(e.unitType!=4 && this.distance(e.x, e.y)<=this.radius)
                count++;
        }
        return count;
    }

    Entity minDistanceEntity(List<Entity> entities, double d, int radius)
    {
        Entity minEntity=null;
        for(Entity e:entities)
        {
            double newD=this.distance(e.x,e.y);
            if(radius==0)
                newD-=(double)(e.radius-100);

            if(newD<d)
            {
                d=newD;
                minEntity=e;
            }
        }
        return minEntity;
    }
}

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

            //new structures
            List<Entity> wrecks=new ArrayList<Entity>();
            List<Entity> enemies=new ArrayList<Entity>();
            List<Entity> destroyers=new ArrayList<Entity>();
            List<Entity> doofs=new ArrayList<Entity>();
            Entity playerEntity=new Entity();

            List<Entity> tanks=new ArrayList<Entity>();
            Entity destroyerEntity=new Entity();

            Entity doofEntity=new Entity();

            for (int i = 0; i < unitCount; i++) {
                int unitId = in.nextInt();
                int unitType = in.nextInt();
                int player = in.nextInt();
                float mass = in.nextFloat();
                int radius = in.nextInt();
                int x = in.nextInt();
                int y = in.nextInt();
                int vx = in.nextInt();
                int vy = in.nextInt();
                int extra = in.nextInt();
                int extra2 = in.nextInt();

                //adding a new Entity temp in the new structures
                Entity temp=new Entity(unitId,unitType,player,mass,radius,x,y,vx,vy,extra,extra2);

                if(unitType==4)                
                    wrecks.add(temp);
                
                else if(unitType==3)
                    tanks.add(temp);
                
                else if(unitType==0 && unitId!=0) 
                {               
                    enemies.add(temp);
                }
                else if(unitType==1 && unitId!=1)
                {
                    destroyers.add(temp);
                }
                else if(unitType==2 && unitId!=2)
                {
                    doofs.add(temp);
                }
                else if(unitType==0 && unitId==0)
                    playerEntity=temp;
                else if(unitType==1 && unitId==1)
                    destroyerEntity=temp;
                else if(unitType==2 && unitId==2)
                    doofEntity=temp;
     
            }
            
            //COMPORTAMENTO DEL PLAYER
            //raggiungo la pozzanghera con distanza minima, senza nemici o senza tanks
            if(!wrecks.isEmpty())
            {
                int sum=0;
                if(!playerEntity.playerOnWreck(wrecks))
                {
                    Entity wreckMinDistanceFromPlayer=playerEntity.minDistanceEntity(wrecks, 12000, -1);
                    sum=wreckMinDistanceFromPlayer.howManyOnWreck(tanks)+wreckMinDistanceFromPlayer.howManyOnWreck(destroyers)+wreckMinDistanceFromPlayer.howManyOnWreck(doofs);
                    //System.err.println("conteggio "+sum);
                    if(sum<3)
                        System.out.println(wreckMinDistanceFromPlayer.x+" "+wreckMinDistanceFromPlayer.y+" 300");
                    else
                        System.out.println("0 0 300");
                }
                else
                {
                    System.out.println(playerEntity.x+" "+playerEntity.y+" 10");
                }
            }
            else
                System.out.println("0 0 300");

            
            //COMPORTAMENTO DEL DESTROYER
            Entity tankMinDistanceFromDestroyer=destroyerEntity.minDistanceEntity(tanks, 12000, 0);

            if(myRage>=120 && playerEntity.distance((enemies.get(1).x),(enemies.get(1).y))>1000 && enemies.get(1).wreckOfTheEnemy(wrecks)!=null)
            {
                int newX = (enemies.get(1).x+300)+enemies.get(1).vx;
                int newY = (enemies.get(1).y+300)+enemies.get(1).vy;
                System.out.println("SKILL "+newX+" "+newY);
            }
            else if(tankMinDistanceFromDestroyer!=null && !tanks.isEmpty() && tankMinDistanceFromDestroyer.distance(0,0)<5000)
            {
                System.out.println(tankMinDistanceFromDestroyer.x+" "+tankMinDistanceFromDestroyer.y+" 300");
            }
            else
                System.out.println("300 300 300");
            
            //COMPORTAMENTO DEL DOOF
            System.out.println(enemies.get(0).x+" "+enemies.get(0).y+" 300");

        }
    }
}
