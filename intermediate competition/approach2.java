import java.util.*;
import java.io.*;
import java.math.*;

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

    double dis(int x2,int y2)
    {
        return java.lang.Math.sqrt((x2-this.x)*(x2-this.x) + (y2-this.y)*(y2-this.y));
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
            Entity playerEntity=new Entity();

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
                
                if(player==0)                
                    playerEntity=temp;
                
                if(unitType==0 && player!=0)                
                    enemies.add(temp);                
            }

            //raggiungo la pozzanghera con distanza minima
            double distance=12000;
            Entity wreckMinDistance=new Entity();
            for(Entity w:wrecks)
            {   
                if(distance > playerEntity.dis(w.x,w.y))
                {
                    distance = playerEntity.dis(w.x,w.y);
                    wreckMinDistance = w;
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.out.println(wreckMinDistance.x+" "+wreckMinDistance.y+" 200");
            System.out.println("WAIT");
            System.out.println("WAIT");
            System.out.println("WAIT");
        }        
    }
}