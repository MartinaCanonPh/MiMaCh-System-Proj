import java.util.*;
import java.io.*;
import java.math.*;

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

    public Pacman(int pacId, int x, int y, String typeId, int speedTurnsLeft, int abilityCooldown) {
        this.pacId = pacId;
        this.x = x;
        this.y = y;
        this.typeId = typeId;
        this.speedTurnsLeft = speedTurnsLeft;
        this.abilityCooldown = abilityCooldown;
    }
}

class GameManager{
    Map board;
    ArrayList<Pacman> myPacmans;
    ArrayList<Pacman> opponents;
    int myScore;
    int opponentScore;
    String output;

    public GameManager(int w, int h){
        board=new Map(w, h);
        myPacmans=new ArrayList<Pacman>();
        opponents=new ArrayList<Pacman>();
        output="";
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
        myPacmans=null;
        opponents=null;
        board.visible=null;
    }

    void updateMap(){
        for(Pacman p: myPacmans){
            boolean up=true, down=true, right=true, left=true;
            int i=1;
            while(up || down || right || left){
                
                if(up){
                    if(p.y - i < 0 || board.mappa[p.y - i][p.x]=='#')
                        up=false;

                    else if((board.mappa[p.y - i][p.x]=='o' || board.mappa[p.y - i][p.x]=='O') && board.checkPellet(p.x, p.y - i)==false){
                        board.mappa[p.y - i][p.x]='X';
                    }
                }
    
                if(down){
                    if(p.y + i > board.height-1 || board.mappa[p.y + i][p.x]=='#')
                        down=false;

                    else if((board.mappa[p.y + i][p.x]=='o' || board.mappa[p.y + i][p.x]=='O') && board.checkPellet(p.x, p.y + i)==false){
                        board.mappa[p.y + i][p.x]='X';
                    }
                }
    
                if(right){
                    if(p.x + i > board.width-1 || board.mappa[p.y][p.x + i]=='#')
                        right=false;

                    else if((board.mappa[p.y][p.x + i]=='o' || board.mappa[p.y][p.x + i]=='O') && board.checkPellet(p.x + i, p.y)==false){
                        board.mappa[p.y][p.x + i]='X';
                    }    
                }
    
                if(left){
                    if(p.x - i < 0 || board.mappa[p.y][p.x - i]=='#')
                        left=false;

                    else if((board.mappa[p.y][p.x - i]=='o' || board.mappa[p.y][p.x - i]=='O') && board.checkPellet(p.x - i, p.y)==false){
                        board.mappa[p.y][p.x - i]='X';
                    }
                }
                
                i++;
            }
        }
    }

    void genOutput(){
        //TODO
    }

    void play(){
        //TODO
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

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("MOVE 0 15 10"); // MOVE <pacId> <x> <y>
        }
    }
}