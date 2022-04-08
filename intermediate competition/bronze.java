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

    double dis(int x2,int y2)
    {
        return java.lang.Math.sqrt((x2-this.x)*(x2-this.x) + (y2-this.y)*(y2-this.y));
    }

    Entity near(List<Entity> entities)
    {
        for(Entity e:entities)
        {
            if(this.dis(e.x,e.y)<=1000)
                return e;
        }
        return null;
    }

    boolean enemyOnWreck(List<Entity> enemies)
    {
        for(Entity e:enemies)
            if(this.dis(e.x, e.y)<=e.radius)
                return true;
        return false;
    }

    Entity minDistanceEntity(List<Entity> entities, double d, int radius)
    {
        Entity minEntity=null;
        for(Entity e:entities)
        {
            double newD=this.dis(e.x,e.y);
            if(radius==0)
                newD-=(double)(e.radius);

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
        boolean grenadeUsed=false;
        int countOilPools=0;
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
                
                if(unitType==0 && player==0)                
                    playerEntity=temp;
                
                if(unitType==0 && player!=0)                
                    enemies.add(temp);

                if(unitType==1 && player==1)                
                    destroyerEntity=temp;
                
                if(unitType==3)                
                    tanks.add(temp);    

                if(unitType==2 && player==2)
                    doofEntity=temp;             
            }
            
            //COMPORTAMENTO DEL PLAYER
             //raggiungo la pozzanghera con distanza minima
            Entity wreckMinDistanceFromPlayer=playerEntity.minDistanceEntity(wrecks, 1200, -1);
            System.out.println(wreckMinDistanceFromPlayer.x+" "+wreckMinDistanceFromPlayer.y+" 300");
            
            //COMPORTAMENTO DEL DESTROYER
             //raggiungo il tank con distanza minima
            Entity tankMinDistanceFromDestroyer=destroyerEntity.minDistanceEntity(tanks, 1200, 0);
            if(tankMinDistanceFromDestroyer!=null && !tanks.isEmpty())
            {
                System.out.println(tankMinDistanceFromDestroyer.x+" "+tankMinDistanceFromDestroyer.y+" 300");
            }
            else
            {
                System.out.println("0 0 300");
            }
            
            //COMPORTAMENTO DEL DOOF
            //raggiungo il mio Destroyer e sparo verso la pozzenghera più vicina con nemici
            Entity wreckMinDis=doofEntity.minDistanceEntity(wrecks, 1200, -1);
            if(myRage>60 && wreckMinDis!=null && wreckMinDis.enemyOnWreck(enemies))
            {
                System.out.println("SKILL "+wreckMinDis.x+" "+wreckMinDis.y);
                countOilPools++;
            }
            else
                System.out.println(destroyerEntity.x+" "+destroyerEntity.y+" 300");

        }
    }
}