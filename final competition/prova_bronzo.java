import java.util.*;
import java.io.*;
import java.math.*;

class Pacman {

    //variabili con visibilità di file
    int pacId;
    boolean mine;
    int x;
    int y;
    String typeId;
    int speedTurnsLeft;
    int abilityCooldown;

    //costruttore con parametri
    public Pacman(int pacId, boolean mine, int x, int y, String typeId, int speedTurnsLeft, int abilityCooldown) {
        this.pacId = pacId;
        this.mine = mine;
        this.x = x;
        this.y = y;
        this.typeId=typeId;
        this.speedTurnsLeft=speedTurnsLeft;
        this.abilityCooldown=abilityCooldown;
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

    // trova adiacenti
    Pellet findAdj(ArrayList<Pellet> pellets)
    {
        for(Pellet p : pellets)
        {
            if((p.y == this.y && (p.x == this.x+1 || p.x == this.x-1)) || (p.x == this.x && (p.y == this.y+1 || p.y == this.y-1)))
            {
                System.err.println("Pellet Adiacente Trovato!");
                return p;
            }
        }
        return pellets.get(0); //oppure, se non abbiamo vicini?
    }

    // trova in base alla distanza minima
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

	public String getAction(boolean b, Pellet pellet) {

        if(b)
        {
            // nuova forma casuale
            Random rd = new Random();
            Integer i = rd.nextInt()%3;
            if(i==0)
                return "SWITCH "+this.pacId+" SCISSORS";
            else if(i==2)
                return "SWITCH "+this.pacId+" PAPER";
            else if(i==3)
                return "SWITCH "+this.pacId+" ROCK";
        }
        return "MOVE "+this.pacId+" "+pellet.x+" "+pellet.y;
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

        //conteggio dei turni
        int count=0;

        // game loop
        while (true) {
            count++;
            int myScore = in.nextInt();
            int opponentScore = in.nextInt();
            int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight

            // calcoli utili da fare al primo turno (1 secondo di tempo)
            //if(count==0)

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
                
                Pacman p = new Pacman(pacId, mine, x, y, typeId, speedTurnsLeft, abilityCooldown);
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
            boolean nuovaforma=false;
            if(count%10==0 && count>=20)
                nuovaforma=true;

            String output="";
            for(Pacman p: myPacmans)    //poi i pacman muoiono
            {
                // if(p.equals(myPacmans.get(myPacmans.size()-1)))
                //     output+=p.getAction(nuovaforma, firstSuperPellets.get(p.pacId));
                // else
                System.err.println("FIRST "+firstSuperPellets.get(p.pacId));
                System.err.println("PELLET get 0"+pellets.get(0));
                    if(firstSuperPellets.get(p.pacId)!=null)
                        output+=p.getAction(nuovaforma, firstSuperPellets.get(p.pacId))+"|";
                    else
                        output+=p.getAction(nuovaforma, pellets.get(0))+"|";
            }
            System.out.println(output); // MOVE <pacId> <x> <y>
        }
    }
}