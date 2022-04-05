import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Unit {

    public Unit(int unitId, int unitType, int player, float mass, int radius, int x, int y, int vx, int vy, int extra, int extra2) {
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
    public Unit()
    {

    }
    int unitId;
    int unitType;
    int player ;
    float mass ;
    int radius ;
    int x ;
    int y ;
    int vx ;



    int vy;
    int extra ;
    int extra2 ;

    double distance(int x2,int y2)
    {
	int resX=this.x-x2;
	if(resX<0)
	{
		resX=resX*-1;
	}
	resX=resX*resX;
	int resY=this.y-y2;
	if(resY<0)
	{
		resY=resY*-1;
	}
	resY=resY*resY;
	int result=resX+resY;
	return java.lang.Math.sqrt(result);
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
            List<Unit> watter=new ArrayList<Unit>();
            List<Unit> enemy=new ArrayList<Unit>();
            Unit playerUnit=new Unit();
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
                Unit temp=new Unit(unitId,unitType,player,mass,radius,x,y,vx,vy,extra,extra2);
                if(unitType==4)
                {
                    watter.add(temp);
                }
                if(player==0)
                {
                    playerUnit=temp;
                }
                if(unitType==0 && player!=0)
                {
                    enemy.add(temp);
                }
            }
            /*System.err.println("Le nostre watter");
            for(Unit w:watter)
            {
                System.err.println("x:"+w.x+" y:"+w.y);
            }

            System.err.println("the enemies");
            for(Unit e:enemy)
            {
                System.err.println("x:"+e.x+" y:"+e.y);
            }

            System.err.println("our player");
            {
                System.err.println("x:"+playerUnit.x+" y:"+playerUnit.y);
            }*/
            double distance=12000;
            Unit watterMinDistance=new Unit();
            /*if(watter.size()>=3){
                for(Unit w:watter)
                {   
                    if(distance>playerUnit.distance(w.x,w.y) && !isOnWatter(w,enemy))
                    {
                    distance= playerUnit.distance(w.x,w.y);
                    watterMinDistance=w;
                    }

                }
            }
            else
            {*/
                for(Unit w:watter)
                {   
                    if(distance>playerUnit.distance(w.x,w.y))
                    {
                    distance= playerUnit.distance(w.x,w.y);
                    watterMinDistance=w;
                    }

                }
            



            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");


            System.out.println(watterMinDistance.x+" "+watterMinDistance.y+" 200");
            System.out.println("WAIT");
            System.out.println("WAIT");
        }
        
    }
    static boolean isOnWatter(Unit watter,List<Unit> enemies)
        {
        for(Unit e:enemies)
        {
            if(watter.distance(e.x,e.y)+e.radius<watter.radius);
            return true;
        }
        return false;
        
        }
    double distance(int x1,int y1,int x2,int y2)
    {
	int resX=x1-x2;
	if(resX<0)
	{
		resX=resX*-1;
	}
	resX=resX*resX;
	int resY=y1-y2;
	if(resY<0)
	{
		resY=resY*-1;
	}
	resY=resY*resY;
	int result=resX+resY;
	return java.lang.Math.sqrt(result);
    }
}