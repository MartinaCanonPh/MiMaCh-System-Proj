import java.util.*;

import javax.swing.text.Position;

import java.awt.Point;
import java.io.*;
import java.math.*;

class Cell{
    int x;
    int y;

    public Cell(int x, int y){
        this.x=x;
        this.y=y;
    } 
}

class Pellet{

    int x;
    int y;
    int value;

    public Pellet() {
    }

    public Pellet(int x, int y, int value){
        this.x=x;
        this.y=y;
        this.value=value;
    }
}

class Map{
    char[][] mappa;
    //int[][] mappa;
    int width;
    int height;
    ArrayList<Pellet> visible;

    public Map(int w, int h){
        visible=new ArrayList<Pellet>();
        this.width=w;
        this.height=h;
        mappa=new char[h][w];
        //mappa=new int[h][w]
    }

    void setVisible(ArrayList<Pellet> visiblePellet){
        this.visible=visiblePellet;
        
        //if char mappa
        for(Pellet p: this.visible){
            if(p.value==10)
                mappa[p.y][p.x]='O';
            else
            mappa[p.y][p.x]='o';
        }

        /*if int mappa
        for(Pellet p: this.visible){
            mappa[p.y][p.x]=p.value;
        }
        */

    }

    void addVisible(Pellet p){

        visible.add(p);
        
        if(p.value==10)
            mappa[p.y][p.x]='O';
        else
            mappa[p.y][p.x]='o';

    }

    void setRow(String row, int i){
        //if char mappa
        for(int j=0; j<width; j++)
            mappa[i][j]=row.charAt(j);
        
        /* if int mappa
        for(int j=0; j<width; j++){
            if(row.charAt(j)=='#')
                mappa[i][j]=11;
            else
                mappa[i][j]=-1;

        }
        */
    }

    void stampa(){
        for(int i=0; i<height; i++)
        System.err.println(mappa[i]);
    }

    boolean checkPellet(int x, int y){
        for(Pellet p: visible)
            if(p.x==x && p.y==y)
                return true;
        return false;
    }
}

class Pacman{

    int pacId;
    int x; 
    int y;
    String typeId;
    int speedTurnsLeft;
    int abilityCooldown;
    ArrayList<Direction> possiblesMoves;
    Direction choice;
    Cell explore;
    String action;

    public Pacman(int pacId, int x, int y, String typeId, int speedTurnsLeft, int abilityCooldown) {
        this.pacId = pacId;
        this.x = x;
        this.y = y;
        this.typeId = typeId;
        this.speedTurnsLeft = speedTurnsLeft;
        this.abilityCooldown = abilityCooldown;
        possiblesMoves=new ArrayList<Direction>();
        choice=null;
        explore=null;
        action="";
    }

    void bestDirection(){
        double max=0.0f;
        for(Direction d: possiblesMoves){
            if(d.pointsOnTime>max){
                max=d.pointsOnTime;
                this.choice=d;
                this.action="MOVE";
            }
        }
        //System.err.println(this.pacId+" "+this.choice.direction);
    }
    
    void tryToWin(Pacman oppo) {
    	if(oppo.typeId.equals("SCISSORS"))    	
    		this.action="SWITCH " +Integer.toString(this.pacId)+" ROCK";
    	else if(oppo.typeId.equals("ROCK"))
    		this.action="SWITCH " +Integer.toString(this.pacId)+" PAPER";
    	else if(oppo.typeId.equals("PAPER"))
    		this.action="SWITCH " +Integer.toString(this.pacId)+" SCISSORS";
    }
}

class Direction{
    String direction;
    double pointsOnTime;

    public Direction(String d, int points, int time){
        direction=d;

        pointsOnTime=(double)points/time;
    }

    void setDirection(String d){
        direction=d;
    }

    void setPointsOnTime(int points, int time){
        pointsOnTime=(Double)pointsOnTime/time;
    }
}

class GameManager{
    Map board;
    ArrayList<Pacman> myPacmans;
    ArrayList<Pacman> opponents;
    
    ArrayList<Pacman> myLastPosition;
    ArrayList<Pacman> opponentsLastPosition;
    
    int myScore;
    int opponentScore;
    String output;

    public GameManager(int w, int h){
        board=new Map(w, h);
        myPacmans=new ArrayList<Pacman>();
        opponents=new ArrayList<Pacman>();
        output="";
    }

    void stampaMappa(){
        board.stampa();
    }

    void setBoard(String line, int row){
        board.setRow(line, row);
    }

    void addMyPacman(Pacman p){
        myPacmans.add(p);
    }

    void addOppoPacman(Pacman p){
        opponents.add(p);
    }

    void setScores(int my, int oppo){
        myScore=my;
        opponentScore=oppo;
    }

    void setVisible(ArrayList<Pellet> visible){
        board.setVisible(visible);
    }

    void addvisible(Pellet p){
        board.addVisible(p);
    }

    void clearAll(){
        this.output="";
        myLastPosition=myPacmans;
        opponentsLastPosition=opponents;
        myPacmans=new ArrayList<Pacman>();
        opponents=new ArrayList<Pacman>();
        board.visible=new ArrayList<Pellet>();
    }

    void updateMap(){
        for(Pacman p: myPacmans){
            board.mappa[p.y][p.x]='X';
            boolean up=true, down=true, right=true, left=true;
            int pointsUp=0, pointsDown=0, pointsRight=0, pointsLeft=0;
            int i=1;
            while(up || down || right || left){
                
                if(up){
                    int y=Math.floorMod(p.y - i, board.height);

                    if(board.mappa[y][p.x]=='#'){
                        up=false;
                        p.possiblesMoves.add(new Direction("up", pointsUp, i));
                    }

                    else if((board.mappa[y][p.x]=='o' || board.mappa[y][p.x]=='O') && board.checkPellet(p.x, y)==false){
                        board.mappa[y][p.x]='X';
                    }

                    else if(board.mappa[y][p.x]=='o')
                        pointsUp++;
                    
                    else if(board.mappa[y][p.x]=='O')
                        pointsUp+=10;

                    
                }
    
                if(down){
                    int y=Math.floorMod(p.y + i, board.height);

                    if(board.mappa[y][p.x]=='#'){
                        down=false;
                        p.possiblesMoves.add(new Direction("down", pointsDown, i));
                    }

                    else if((board.mappa[y][p.x]=='o' || board.mappa[y][p.x]=='O') && board.checkPellet(p.x, y)==false){
                        board.mappa[y][p.x]='X';
                    }

                    else if(board.mappa[y][p.x]=='o')
                        pointsDown++;
                    
                    else if(board.mappa[y][p.x]=='O')
                        pointsDown+=10;

                }
    
                if(right){
                    int x=Math.floorMod(p.x + i, board.width);

                    if(board.mappa[p.y][x]=='#'){
                        right=false;
                        p.possiblesMoves.add(new Direction("right", pointsRight, i));
                    }

                    else if((board.mappa[p.y][x]=='o' || board.mappa[p.y][x]=='O') && board.checkPellet(x, p.y)==false){
                        board.mappa[p.y][x]='X';
                    }    

                    else if(board.mappa[p.y][x]=='o')
                        pointsRight++;
                    
                    else if(board.mappa[p.y][x]=='O')
                        pointsRight+=10;

                }
    
                if(left){
                    int x=Math.floorMod(p.x - i, board.width);

                    if(board.mappa[p.y][x]=='#'){
                        left=false;
                        p.possiblesMoves.add(new Direction("left", pointsLeft, i));
                    }

                    else if((board.mappa[p.y][x]=='o' || board.mappa[p.y][x]=='O') && board.checkPellet(x, p.y)==false){
                        board.mappa[p.y][x]='X';
                    }

                    else if(board.mappa[p.y][x]=='o')
                        pointsLeft++;
                    
                    else if(board.mappa[p.y][x]=='O')
                        pointsLeft+=10;
                }
                
                i++;
            }
        }
        this.stampaMappa();
    }

    void chooseDirection(){
        for(Pacman p: myPacmans){
            p.bestDirection();
        }
    }

    void checkForNull(){
        for(Pacman p: myPacmans){
            // if(p.choice==null && p.abilityCooldown==0)
            // 	p.action="SPEED "+Integer.toString(p.pacId);
            // else
            //     explore(p);
            if(p.choice==null)
                explore(p);
        }
    }
    

    void explore(Pacman p){
        ArrayList<Cell> cell = new ArrayList<Cell>();
        ArrayList<Cell> visited = new ArrayList<Cell>();
        cell.add(new Cell(p.x, p.y));
        boolean found=false;
        while(found==false){
            ArrayList<Cell> temp = new ArrayList<Cell>();

            for(Cell c: cell){
                if(board.mappa[Math.floorMod(c.y-1, board.height)][c.x]!='#')
                    temp.add(new Cell(c.x, Math.floorMod(c.y-1, board.height)));
                if(board.mappa[Math.floorMod(c.y+1, board.height)][c.x]!='#')
                    temp.add(new Cell(c.x, Math.floorMod(c.y+1, board.height)));
                if(board.mappa[c.y][Math.floorMod(c.x+1, board.width)]!='#')
                    temp.add(new Cell(Math.floorMod(c.x+1, board.width), c.y));
                if(board.mappa[c.y][Math.floorMod(c.x-1, board.width)]!='#')
                    temp.add(new Cell(Math.floorMod(c.x-1, board.width), c.y));

            }
            visited.addAll(cell);
            cell=new ArrayList<Cell>();

            for(Cell po: temp){

                if(board.mappa[po.y][po.x]=='o' || board.mappa[po.y][po.x]=='O' || board.mappa[po.y][po.x]==' '){
                    p.explore=new Cell(po.x, po.y);
                    if(p.explore == null)
                        System.err.println("IO ho null: "+p.pacId);
                    found=true;
                }
                else if(presente(visited, po)==false)
                    cell.add(po);
            }
            //cell=temp;            
        }
        p.action="MOVE";
    }

    boolean presente(ArrayList<Cell> cell, Cell c){
        for(Cell k: cell){
            if(k.x==c.x && k.y==c.y)
                return true;
        }
        return false;
    }
    
    void checkForFight() {
    	for(Pacman p: myPacmans) {
    		Pacman near=isOpponentNear(p);
    		if(near!=null) {
    			if(p.abilityCooldown==0 && near.abilityCooldown!=0) {
    				if(fightResult(p, near)==1) //vittoria
    					p.action="MOVE";
    				else // sconfita o pareggio
    					p.tryToWin(near);
    			}
    			else if(p.abilityCooldown==0 && near.abilityCooldown==0) {
    				if(fightResult(p, near)==1) //vittoria
    					p.action="WAIT";
    				else //sconfita o pareggio
    					p.action="SPEED "+Integer.toString(p.pacId);
    			}
    			else if(p.abilityCooldown!=0 && near.abilityCooldown!=0) {
    				if(fightResult(p, near)==1) //vittoria
    					p.action="MOVE";
    				
    			}
    		}
    	}
    }
    
    int fightResult(Pacman my, Pacman oppo) {
    	if(my.typeId.equals(oppo.typeId))
    		return 0;
    	else if((my.typeId.equals("ROCK") && oppo.typeId.equals("SCISSORS")) || (my.typeId.equals("PAPER") && oppo.typeId.equals("ROCK")) || (my.typeId.equals("SCISSORS") && oppo.typeId.equals("PAPER")))
    		return 1;
    	else 
    		return -1;
    }
    
    Pacman isOpponentNear(Pacman p) {
    	Pacman op=null;
    	for(Pacman oppo: opponents) {
    		if((p.y==oppo.y && (p.x+1 == oppo.x || p.x-1 == oppo.x)))
    			op=oppo;
    		else if((p.x==oppo.x && (p.y+1 == oppo.y || p.y-1 == oppo.y)))
    			op=oppo;
    	}
    	
    	return op;
    }




    void genOutput(){
        String temp="";
        for(Pacman p: myPacmans){
            if(p.typeId.equals("DEAD")==false){
            	
            	if(p.action.equals("MOVE")) {
            		temp+="| " + p.action+ " " + Integer.toString(p.pacId) + " ";

                    if(p.choice==null){
                        if(p.explore==null)
                            System.err.println("SONO IO: "+p.pacId);
                        temp+=Integer.toString(p.explore.x) + " " + Integer.toString(p.explore.y) + " ";

                    }
                    else if(p.choice.direction.equals("up"))
                        temp+=Integer.toString(p.x) + " " + Integer.toString(Math.floorMod(p.y-1, board.height)) + " ";
                    
                    else if(p.choice.direction.equals("down"))
                        temp+=Integer.toString(p.x) + " " + Integer.toString(Math.floorMod(p.y+1, board.height)) + " ";
                        
                    else if(p.choice.direction.equals("right"))
                        temp+=Integer.toString(Math.floorMod(p.x+1, board.width)) + " " + Integer.toString((p.y)) + " ";
                    
                    else if(p.choice.direction.equals("left"))
                        temp+=Integer.toString(Math.floorMod(p.x-1, board.width)) + " " + Integer.toString((p.y)) + " ";
            	}
            	else if(p.action.substring(0, 6).equals("SWITCH")) {
            		temp+="| " + p.action + " ";
            	}
            	else if(p.action.substring(0, 5).equals("SPEED")) {
            		temp+="| " + p.action + " ";
            	}
            	else if(p.action.equals("WAIT")) {
            		temp+="| MOVE " + Integer.toString(p.pacId) + " ";
            		temp+=Integer.toString(p.x) + " " + Integer.toString(p.y) + " ";
            	}               
            }
        }

        output=temp.substring(2);
    }
    

    void play(){
        //osserva e scegli
        updateMap();
        chooseDirection();

        //check pacman directions
        checkForNull();
        checkForFight(); // TODO se vediamo un nemico nel corridoio entro 4/3 caselle, controllare se ha il cooldown dell'abilita' !=0 
        				 //e fare la differenza fra le posizioni passate e correnti, perche' potrebbe mangiarci senza darci il tempo di trasformarci

        //move
        genOutput();
    }
}

class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        int width = in.nextInt(); // size of the grid
        int height = in.nextInt(); // top left corner is (x=0, y=0)

        GameManager gm=new GameManager(width, height);

        if (in.hasNextLine()) {
            in.nextLine();
        }
        for (int i = 0; i < height; i++) {
            String row = in.nextLine(); // one line of the grid: space " " is floor, pound "#" is wall
            gm.setBoard(row, i);
        }
        gm.stampaMappa();

        // game loop
        while (true) {
            int myScore = in.nextInt();
            int opponentScore = in.nextInt();
            int visiblePacCount = in.nextInt(); // all your pacs and enemy pacs in sight

            gm.setScores(myScore, opponentScore);


            for (int i = 0; i < visiblePacCount; i++) {
                int pacId = in.nextInt(); // pac number (unique within a team)
                boolean mine = in.nextInt() != 0; // true if this pac is yours
                int x = in.nextInt(); // position in the grid
                int y = in.nextInt(); // position in the grid
                String typeId = in.next(); // unused in wood leagues
                int speedTurnsLeft = in.nextInt(); // unused in wood leagues
                int abilityCooldown = in.nextInt(); // unused in wood leagues

                Pacman p=new Pacman(pacId, x, y, typeId, speedTurnsLeft, abilityCooldown);
                
                if(mine)
                    gm.addMyPacman(p);
                else
                    gm.addOppoPacman(p);
            }


            int visiblePelletCount = in.nextInt(); // all pellets in sight
            for (int i = 0; i < visiblePelletCount; i++) {
                int x = in.nextInt();
                int y = in.nextInt();
                int value = in.nextInt(); // amount of points this pellet is worth

                Pellet p=new Pellet(x, y, value);
                gm.addvisible(p);

            }
            
            gm.play();
            
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println(gm.output); // MOVE <pacId> <x> <y>
            gm.clearAll();
        }
    }
}