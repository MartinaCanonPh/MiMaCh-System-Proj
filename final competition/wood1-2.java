import java.util.*;
import java.io.*;
import java.math.*;

class Pacman {

    //variabili con visibilità di file
    int pacId;
    boolean mine;
    int x;
    int y;

    //costruttore con parametri
    public Pacman(int pacId, boolean mine, int x, int y) {
        this.pacId = pacId;
        this.mine = mine;
        this.x = x;
        this.y = y;
    }

    //costruttore senza parametri
    public Pacman()
    {

    }

    //calcolo della distanza tra due punti
    double distance(int x2,int y2)
    {
        return java.lang.Math.sqrt((x2-this.x)*(x2-this.x) + (y2-this.y)*(y2-this.y));
    }

    Pellet findAdj(ArrayList<Pellet> pellets)
    {
        for(Pellet p : pellets)
        {
            if((p.y == this.y && (p.x == this.x+1 || p.x == this.x-1)) || (p.x == this.x && (p.y == this.y+1 || p.y == this.y-1)))
            {
                System.err.println("Pellet Vicino Trovato!");
                return p;
            }
        }
        return pellets.get(0); //oppure, se non abbiamo vicini?
    }

    Pellet findNear(ArrayList<Pellet> pellets)
    {
        Pellet minPellet = null;
        double minD = 99999999.f;
        for(Pellet p: pellets)
        {
            if(this.distance(p.x, p.y)<minD)
            {
                minD=this.distance(p.x, p.y);
                minPellet=p;
            }
        }
        return minPellet;
    }
}

class Pellet {

    //variabili con visibilità di file
    int x;
    int y;
    int value;

    //costruttore con parametri
    public Pellet(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    //costruttore senza parametri
    public Pellet()
    {

    }

    //calcolo della distanza tra due punti
    double distance(int x2,int y2)
    {
        return java.lang.Math.sqrt((x2-this.x)*(x2-this.x) + (y2-this.y)*(y2-this.y));
    }
}

/**
 * Grab the pellets as fast as you can!
 **/
class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int width = in.nextInt(); // size of the grid
        int height = in.nextInt(); // top left corner is (x=0, y=0)
        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String row = in.nextLine(); // one line of the grid: space " " is floor, pound "#" is wall
        }

        // game loop
        while (true) {
            int myScore = in.nextInt();
            int opponentScore = in.nextInt();
            int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight
            ArrayList<Pacman> myPacmans = new ArrayList<Pacman>();
            ArrayList<Pacman> enemies = new ArrayList<Pacman>();
            for (int i = 0; i < visiblePacCount; i++) {
                int pacId = in.nextInt(); // pac number (unique within a team)
                boolean mine = in.nextInt() != 0; // true if this pac is yours
                int x = in.nextInt(); // position in the grid
                int y = in.nextInt(); // position in the grid
                String typeId = in.next(); // unused in wood leagues
                int speedTurnsLeft = in.nextInt(); // unused in wood leagues
                int abilityCooldown = in.nextInt(); // unused in wood leagues
                
                Pacman p = new Pacman(pacId,mine,x,y);
                if(mine)
                    myPacmans.add(p);
                else
                    enemies.add(p);

            }

            ArrayList<Pellet> pellets = new ArrayList<Pellet>();
            int visiblePelletCount = in.nextInt(); // all pellets in sight
            for (int i = 0; i < visiblePelletCount; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                int value = in.nextInt(); // amount of points this pellet is worth

                pellets.add(new Pellet(x,y,value));
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            ArrayList<Pellet> superP = new ArrayList<Pellet>();
            for(Pellet p : pellets)
            {
                if(p.value == 10)
                    superP.add(p);
            }

            ArrayList<Pellet> firstSuperPellets = new ArrayList<Pellet>();
            for(Pacman p: myPacmans)
            {
                Pellet firstSuperPellet = p.findNear(superP);
                if(firstSuperPellet == null)            
                    firstSuperPellet = p.findNear(pellets);
                firstSuperPellets.add(firstSuperPellet);
            }
            
            String output="";
            for(int i=0; i<myPacmans.size(); i++)
            {
                if(i==0)
                {
                    output+="MOVE ";
                }
                else 
                {
                    output+=" | MOVE ";
                }
                output+=Integer.toString(i);
                output+=" ";
                output+=Integer.toString(firstSuperPellets.get(i).x);
                output+=" ";
                output+=Integer.toString(firstSuperPellets.get(i).y);
            }
            System.out.println(output); // MOVE <pacId> <x> <y>
        }
    }
}