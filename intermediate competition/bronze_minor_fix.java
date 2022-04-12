public import java.util.*;
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

    //calcolo della distanza tra due punti
    double distance(int x2,int y2)
    {
        return java.lang.Math.sqrt((x2-this.x)*(x2-this.x) + (y2-this.y)*(y2-this.y));
    }

    //ritorna true se il player è su una pozzanghera
    boolean playerOnWreck(List<Entity> wrecks)
    {
        for(Entity e:wrecks)
            if(this.distance(e.x, e.y)<=this.radius)
                return true;
        return false;
    }

    //ritorna il conteggio delle entità del tipo passato nella lista su una pozzanghera
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

    //calcola l'entità della struttura passata nei paramentri a distanza minima dall'entità che richiama il metodo
    //d indica a quanto settare la distanza iniziale
    //radius==0 indica che per il calcolo della distanza bisogna sottrarre anche il raggio dell'entità passata nei parametri
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

    //calcola la pozzanghera a distanza minima e con meno entità
	public Entity rightWreck(List<Entity> wrecks, List<Entity> tanks, List<Entity> destroyers, List<Entity> doofs, int unitCount) {
        Entity wreck=this.minDistanceEntity(wrecks, 12000, -1);

        int countMin=unitCount;
        Entity wreckMinEntities=null;
        for(Entity w:wrecks)
        {
            int countEntities=w.howManyOnWreck(tanks)+w.howManyOnWreck(destroyers)+w.howManyOnWreck(doofs);
            if(countEntities<countMin)
            {
                countMin=countEntities;
                wreckMinEntities=w;
            }
        }
        if(wreckMinEntities.equals(wreck))
        {
            return wreck;
        }
        else
		    return null;
	}
}

class Player {

    //calcola la pozzanghera con più pozzanghere sovrapposte (o vicine)
    public static Entity wreckWithMoreWrecks(List<Entity> wrecks)
    {
        int countMax=0;
        Entity wreckToReturn=null;
        for(Entity w:wrecks)
        {
            int count=0;
            for(Entity w2:wrecks)
            {
                if(w.distance(w2.x, w2.y)<=w.radius)
                    count++;
            }
            count--;
            if(count>countMax)
            {
                countMax=count;
                wreckToReturn=w;
            }
        }
        return wreckToReturn;
    }

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
                    enemies.add(temp);
                
                else if(unitType==1)                
                    destroyers.add(temp);
                
                else if(unitType==2)                
                    doofs.add(temp);
                
                else if(unitType==0 && unitId==0)
                    playerEntity=temp;

                else if(unitType==1 && unitId==1)
                    destroyerEntity=temp;

                else if(unitType==2 && unitId==2)
                    doofEntity=temp;
     
            }
            
            //COMPORTAMENTO DEL PLAYER
            if(!wrecks.isEmpty())
            {
                if(!playerEntity.playerOnWreck(wrecks))
                {
                    Entity wreckMinDistanceFromPlayer=playerEntity.minDistanceEntity(wrecks, 12000, -1);                    
                    Entity wreckToChoose=playerEntity.rightWreck(wrecks,tanks,destroyers,doofs,unitCount);
                    Entity wreck2 = wreckWithMoreWrecks(wrecks);
                    
                    if(wreck2!=null)
                        System.out.println(wreck2.x+" "+wreck2.y+" 300");

                    else if(wreckToChoose!=null)                    
                        System.out.println(wreckToChoose.x+" "+wreckToChoose.y+" 300");
                    
                    else
                        System.out.println(wreckMinDistanceFromPlayer.x+" "+wreckMinDistanceFromPlayer.y+" 300");
                }
                else
                {                
                    System.out.println(playerEntity.x+" "+playerEntity.y+" 300 STAY!!");
                }                
            }
            else
                System.out.println(destroyerEntity.x+" "+destroyerEntity.y+" 300");
            
            //COMPORTAMENTO DEL DESTROYER
            Entity doofMinDistanceFromDestroyer = destroyerEntity.minDistanceEntity(doofs, 12000, 0);
            Entity tankMinDistanceFromDestroyer=destroyerEntity.minDistanceEntity(tanks, 12000, 0);
            int newX = (enemies.get(1).x)+enemies.get(1).vx;
            int newY = (enemies.get(1).y)+enemies.get(1).vy;

            if(myRage>=160 && playerEntity.distance(newX, newY)>1000)            
                System.out.println("SKILL "+newX+" "+newY);
            
            else if(tankMinDistanceFromDestroyer!=null && tankMinDistanceFromDestroyer.distance(0,0)<5000)            
                System.out.println(tankMinDistanceFromDestroyer.x+" "+tankMinDistanceFromDestroyer.y+" 300");
            
            else            
                System.out.println(doofMinDistanceFromDestroyer.x+" "+doofMinDistanceFromDestroyer.y+" 300");            

            //COMPORTAMENTO DEL DOOF
            int X = (enemies.get(0).x)+enemies.get(0).vx;
            int Y = (enemies.get(0).y)+enemies.get(0).vy;

            if(myRage>=160 && playerEntity.distance(X,Y)>1000)            
                System.out.println("SKILL "+X+" "+Y);
            
            else
                System.out.println(enemies.get(0).x+" "+enemies.get(0).y+" 300");
        }
    }
}