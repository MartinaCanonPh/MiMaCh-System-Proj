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
    
    int choosePosition()
    {
        Random r = new Random();
        int[] arr = {0, 6000, -6000};
        int randomIndex = r.nextInt(arr.length);
        int randomVal = arr[randomIndex];

        return randomVal;
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
 
             //raggiungo il tank con distanza minima
            double newDistance=6000;
            Entity tankMinDistance=new Entity();
            for(Entity t:tanks)
            {   
                if(newDistance > destroyerEntity.dis(t.x,t.y)-t.radius)
                {
                    newDistance = destroyerEntity.dis(t.x,t.y)-t.radius;
                    tankMinDistance = t;
                }
            }

            System.out.println(wreckMinDistance.x+" "+wreckMinDistance.y+" 300");
            System.out.println(tankMinDistance.x+" "+tankMinDistance.y+" 300");

            int R=(int) Math.hypot(doofEntity.x, doofEntity.y);
            if(R<5500){
                int sinA=doofEntity.y/R;
                int R1= 6000-R;
                int angle=(int) Math.toDegrees(Math.asin(sinA));
                int x1=(int) ((Math.cos(Math.toRadians(angle)))*R1);
                int y1=(int) ((Math.sin(Math.toRadians(angle)))*R1);

                System.out.println((x1+doofEntity.x) + " " + (y1+doofEntity.y) + " 300");
            }
            else{
                int sinA=doofEntity.y/R;
                int angle=(int) Math.toDegrees(Math.asin(sinA))+30;
                int x1=(int) ((Math.cos(Math.toRadians(angle)))*R);
                int y1=(int) ((Math.sin(Math.toRadians(angle)))*R);
                System.out.println(x1+ " " + y1 + " 300");
            }
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
        }
    }
}