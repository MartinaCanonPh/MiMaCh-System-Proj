import java.util.*;

import javax.swing.text.Position;

import java.awt.Point;
import java.io.*;
import java.math.*;
/*classe Cell per gestire le posizioni sulla mappa*/
class Cell{
    int x;
    int y;

    public Cell(){}

    public Cell(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void setCoord(int x, int y){
        this.x=x;
        this.y=y;
    } 
}
/*classe Pellet per gestire i pellet presenti sulla mappa*/
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
/*classe Map, gestisce la mappa come una matrice di char e i pellet visibili su di essa.*/
class Map{ char[][] mappa;
    int width;
    int height;
    ArrayList<Pellet> visible;

    public Map(int w, int h){
        visible=new ArrayList<Pellet>();
        this.width=w;
        this.height=h;
        mappa=new char[h][w];
    }

    /*inserisce i pellet visibili nella matrice. 
    i super pellet vengono inseriti come O mentre i pellet normali come o.*/
    void setVisible(ArrayList<Pellet> visiblePellet){  
        this.visible=visiblePellet;
        
        for(Pellet p: this.visible){
            if(p.value==10)
                mappa[p.y][p.x]='O';
            else
            mappa[p.y][p.x]='o';
        }
    }

    void addVisible(Pellet p){

        visible.add(p);
        
        if(p.value==10)
            mappa[p.y][p.x]='O';
        else
            mappa[p.y][p.x]='o';
    }

    /*converte la riga passata in input da String a char ed inserite nella mappa*/
    void setRow(String row, int i){
        for(int j=0; j<width; j++)
            mappa[i][j]=row.charAt(j);
    }

    /*stampa l'intera mappa'*/
    void stampa(){
        for(int i=0; i<height; i++)
            System.err.println(mappa[i]);
    }

    /*controlla se nelle posizioni passate esiste un pellet*/
    boolean checkPellet(int x, int y){
        for(Pellet p: visible)
            if(p.x==x && p.y==y)
                return true;
        return false;
    }
}

/*classe Pacman, gestisce i nostri pacman*/
class Pacman{

    int pacId;
    int x; 
    int y;
    String typeId;
    int speedTurnsLeft;
    int abilityCooldown;
    ArrayList<Direction> possiblesMoves; //le direzioni su, giù, sinistra e destra.
    Direction choice; //direzione scelta, se null il pacman esplora la mappa. 
    Cell explore;   //cella da esplorare dal pacman.
    String action;  //azione intrapresa dal pacman (MOVE, SPEED, SWITCH, WAIT).
    String switchTo; //variabilew che indica in cosa il pacman si deve trasformare.
    boolean iChoose; //buleana che indica se il pacman ha scelto una direzione.
    boolean locked; //buleana che indica se il pacman è rimasto boccato nella stessa posizione per più turni. collisione?

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
        switchTo="";
        iChoose=false;
        locked=false;
    }

    void setLocked(boolean b)
    {
        this.locked=b;
    }

    /*restituisce la distanza fra il pacman e un'altra posizione*/
    double distance(int x2,int y2)
    {
        return java.lang.Math.sqrt((x2-this.x)*(x2-this.x) + (y2-this.y)*(y2-this.y));
    }

    /*restituisce se un un altro pacman e vicino al pacman corrente*/
    boolean isNear(Pacman p, int i){
        int d1=this.x-p.x;
        int d2=this.y-p.y;
        return ((d1<=i && d1>=-i) && (d2<=i && d2>=-i));
    }

    /*metodo che sceglie la direzione migliore per il pacman*/
    void bestDirection(){
        double max=0.0f;
        for(Direction d: possiblesMoves){
            if(d.pointsOnTime>max){
                max=d.pointsOnTime;
                this.choice=d;
                this.action="MOVE";
                this.iChoose=true;
            }
        }
    }
    /*metodo che, dato un avversario, il nostro pacman sceglie in cosa trasformarsi per vincere uno scontro.*/
    void tryToWin(Pacman oppo) {
    	if(oppo.typeId.equals("SCISSORS")){
            this.action="SWITCH";
            this.switchTo="ROCK";
        }    	
    		
    	else if(oppo.typeId.equals("ROCK")){
            this.action="SWITCH";
            this.switchTo="PAPER";
        }
    		
    	else if(oppo.typeId.equals("PAPER")){
            this.action="SWITCH";
            this.switchTo="SCISSORS";
        }    		
    }

    /*metodo uguale al bestDirection(), ma evita di andare nella direzione passata*/
    void changeDirection(String notDir){
        double max=0.0f;
        for(Direction d: possiblesMoves){
            if(d.pointsOnTime>max && d.direction.equals(notDir)==false){
                max=d.pointsOnTime;
                this.choice=d;
                this.action="MOVE";
                this.iChoose=true;
            }
        }        
    }
}

/*classe direction, serve per gestire la direzione scelta dei pacman*/
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

    /*metodo che decide una direzione in base a dove si trova un avversario*/
    void setDirection(Pacman my, Pacman op){
        if(my.y==op.y){
            if(my.x-op.x>=1)
                direction="left";
            else
                direction="right";
        }
        else if(my.x==op.y){
            if(my.y-op.y>=1)
                direction="up";
            else
                direction="down";
        }
    }
}

/*classe core che gestisce la maggior parte della logica*/
class GameManager{
    Map board; //mappa
    ArrayList<Pacman> myPacmans;//lista dei nostri pacman
    ArrayList<Pacman> opponents;//lista dei pacman avversari
    
    ArrayList<Pacman> myLastPosition;//lista dei nostri pacman del turno precedente
    ArrayList<Pacman> opponentsLastPosition;//lista dei pacman avversari del turno precedente

    ArrayList<Pellet> superPellets; //lista dei super pellets
    
    int myScore;//nostro punteggio
    int opponentScore;//punteggio avversario
    String output;//stringa di output da passare ad ogni fine turno

    public GameManager(int w, int h){
        board=new Map(w, h);
        myPacmans=new ArrayList<Pacman>();
        opponents=new ArrayList<Pacman>();
        superPellets=new ArrayList<Pellet>();
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
        if(p.value==10)
            superPellets.add(p);
        board.addVisible(p);
    }

    /*metodo che ripulisce tutto ciò che cambia fra un turno e l'altro, (es. lista dei pacman)*/
    void clearAll(){
        this.output="";
        myLastPosition=myPacmans;
        opponentsLastPosition=opponents;
        myPacmans=new ArrayList<Pacman>();
        opponents=new ArrayList<Pacman>();
        board.visible=new ArrayList<Pellet>();
        superPellets=new ArrayList<Pellet>();
    }

    /*i pacman contrasegnano la loro ultima posizione con un X sulla mappa 
    per indicare che è stata già visitata la cella*/
    void clearPos(){
        if(myLastPosition!=null){
            for(Pacman p:myLastPosition){
                board.mappa[p.y][p.x]='X';
            }

        }
        for(Pacman p:myPacmans){
            board.mappa[p.y][p.x]='P';
        }
    }

    /*aggiorna la mappa ad ogni inizio turno, tutti i pacman cancellano i pellet 
    che non esistono più dalla mappa e contano i punti che vedono in tutte le loro direzioni.
    i pacman non contano i punti in una determinata oltre un alleato, se è presente, 
    in quella direzione poiché potrebbe ostruire il loro cammino.*/
    void updateMap(){

        for(Pacman p: myPacmans){
            boolean up=true, down=true, right=true, left=true;
            int pointsUp=0, pointsDown=0, pointsRight=0, pointsLeft=0;
            int i=1;
            while(up || down || right || left){
                
                if(up){
                    int y=Math.floorMod(p.y - i, board.height);
                    if(board.mappa[y][p.x]=='#' || board.mappa[y][p.x]=='P'){
                        up=false;
                        p.possiblesMoves.add(new Direction("up", pointsUp, i));
                    }

                    else if((board.mappa[y][p.x]=='o' || board.mappa[y][p.x]=='O') && board.checkPellet(p.x, y)==false){
                        board.mappa[y][p.x]='X';
                    }

                    else if(board.mappa[y][p.x]==' ')
                        board.mappa[y][p.x]='X';

                    else if(board.mappa[y][p.x]=='o')
                        pointsUp++;
                    
                    else if(board.mappa[y][p.x]=='O')
                        pointsUp+=10;
                }
    
                if(down){
                    int y=Math.floorMod(p.y + i, board.height);
                    if(board.mappa[y][p.x]=='#' || board.mappa[y][p.x]=='P'){
                        down=false;
                        p.possiblesMoves.add(new Direction("down", pointsDown, i));
                    }

                    else if((board.mappa[y][p.x]=='o' || board.mappa[y][p.x]=='O' ) && board.checkPellet(p.x, y)==false){
                        board.mappa[y][p.x]='X';
                    }

                    else if(board.mappa[y][p.x]==' ')
                        board.mappa[y][p.x]='X';

                    else if(board.mappa[y][p.x]=='o')
                        pointsDown++;
                    
                    else if(board.mappa[y][p.x]=='O')
                        pointsDown+=10;
                }
    
                if(right){
                    int x=Math.floorMod(p.x + i, board.width);
                    if(board.mappa[p.y][x]=='#' || board.mappa[p.y][x]=='P'){
                        right=false;
                        p.possiblesMoves.add(new Direction("right", pointsRight, i));
                    }

                    else if((board.mappa[p.y][x]=='o' || board.mappa[p.y][x]=='O') && board.checkPellet(x, p.y)==false){
                        board.mappa[p.y][x]='X';
                    }  
                    
                    else if(board.mappa[p.y][x]==' ')
                        board.mappa[p.y][x]='X';

                    else if(board.mappa[p.y][x]=='o')
                        pointsRight++;
                    
                    else if(board.mappa[p.y][x]=='O')
                        pointsRight+=10;
                }
    
                if(left){
                    int x=Math.floorMod(p.x - i, board.width);
                    if(board.mappa[p.y][x]=='#' || board.mappa[p.y][x]=='P'){
                        left=false;
                        p.possiblesMoves.add(new Direction("left", pointsLeft, i));
                    }

                    else if((board.mappa[p.y][x]=='o' || board.mappa[p.y][x]=='O') && board.checkPellet(x, p.y)==false){
                        board.mappa[p.y][x]='X';
                    }

                    else if(board.mappa[p.y][x]==' ')
                    board.mappa[p.y][x]='X';

                    else if(board.mappa[p.y][x]=='o')
                        pointsLeft++;
                    
                    else if(board.mappa[p.y][x]=='O')
                        pointsLeft+=10;
                }
                i++;
            }
        }
    }


    /*confronta, per ogni pacman, la posizione corrente e quella precedente
    se sono uguali e inoltre l'action precedente e quella attuale sono uguali a MOVE
    allora vuol dire che il pacman è bloccato in una collisione.
    se un pacman è bloccato controllo se è perchè ha vicino un alleato, anch'esso bloccato
    allora l'action che secondo pacman bloccato sarà WAIT, in modo da far passare il primo.
    Se non è bloccato da un alleato, allora il primo sarà in WAIT
    */
    void checkCollision(){
        if(myLastPosition!=null){
            for(Pacman p_curr: myPacmans){
                boolean locked=false;
                for(Pacman p_prec: myLastPosition){
                    if(p_curr.pacId==p_prec.pacId && p_curr.x==p_prec.x && p_curr.y==p_prec.y && p_curr.action.equals("MOVE") && p_prec.action.equals("MOVE"))
                    {
                        locked=true;
                        p_curr.setLocked(locked);
                        break;
                    }
                }
                if(locked)
                {
                    boolean b=false;
                    for(Pacman p2: myPacmans){                     
                        if(p_curr.pacId!=p2.pacId && (p_curr.isNear(p2,1) || p_curr.isNear(p2, 2)) && p2.locked)
                        {
                            b=true;
                            p2.action="WAIT";
                            break;
                        }
                    }
                    if(!b)
                        p_curr.action="WAIT";
                }
            }
        }
    }

    void chooseDirection(){
        for(Pacman p: myPacmans){
            p.bestDirection();
        }
    }

    /*controlla se i pacman hanno scelto una direzione.
    ciò non accade sempre poiché prima o poi i pellet scarsegiano
    e non sono sempre visibili a tutti i pacman.
    se il pacman non ha potuto effettuare una bestDirection(), lo si manda ad esplorare.*/
    void checkForNull(){
        for(Pacman p: myPacmans){
            if(p.iChoose==false && p.action.equals("")){
                if(p.abilityCooldown==0)
            	    p.action="SPEED";
                else{
                    explore(p);
                }
            }
        }
    }
    
/*metodo che permette ai pacman di esplorare la mappa.
i pacman controllano le loro celle adiacenti alla ricerca di pellet o celle 
mai esploratre (ovvero che sono contrassegnate sulla mappa come spazio vuoto ' '),
se non trovano celle da esplorare al primo tentativo, si ripete il metodo eseguendo 
gli stessi controlli alle celle adiacenti al pacman e così via finché non
viene trovata una cella. Quando è stata trovata la cella da esplorare si verifica che
non sia già stata scelta da un altro pacman, se il controllo va a buon fine si 
cerca una cella che permette di vedere in anticipo se effetivamente è presente 
un pellet o meno, senza obbligare il pacman ad andare fino in fondo alla cella 
per varificarlo. (es. sull mappa e presente un pelle alla fine di un corridoio 
a vicolo cieco, al pacman per vedere il corridio intero basta arrivare 
all'inizio di esso e se è presente un pellet lo raggiungerà con la bestDirection().')*/
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
                    boolean ok=true;
                    for(Pacman pac:myPacmans){
                        if(pac.pacId!=p.pacId && pac.explore!= null && (pac.explore.x==po.x && pac.explore.y==po.y))
                            ok=false;
                    }
                    if(ok){
                        p.explore=new Cell(po.x, po.y);
                        found=true;                
                    }
                }
                else if(presente(visited, po)==false)
                    cell.add(po);
            }
        }
        ArrayList<Cell> celle=new ArrayList<Cell>();
        if(p.explore!=null){
        boolean up=true, down=true, right=true, left=true;
        int i=1;
            while(up || down || right || left){
                if(up){
                    int y=Math.floorMod(p.explore.y - i, board.height);
                    if(board.mappa[y][p.explore.x]=='#'){
                        up=false;
                    }
                    else if(board.mappa[y][p.explore.x+1]!='#' || board.mappa[y][p.explore.x-1]!='#'){
                        celle.add(new Cell(p.explore.x, y));
                    }
                }
                if(down){
                    int y=Math.floorMod(p.explore.y + i, board.height);
                    if(board.mappa[y][p.explore.x]=='#'){
                        down=false;
                    }
                    else if(board.mappa[y][p.explore.x+1]!='#' || board.mappa[y][p.explore.x-1]!='#'){
                        celle.add(new Cell(p.explore.x, y));
                    }
                }
                if(right){
                    int x=Math.floorMod(p.explore.x + i, board.width);
                    if(board.mappa[p.explore.y][x]=='#'){
                        right=false;
                    }
                    else if(board.mappa[p.explore.y+1][x]!='#' || board.mappa[p.explore.y-1][x]!='#'){
                        celle.add(new Cell(x, p.explore.y));
                    }
                }
                if(left){
                    int x=Math.floorMod(p.explore.x - i, board.width);
                    if(board.mappa[p.explore.y][x]=='#'){
                        left=false;
                    }
                    else if(board.mappa[p.explore.y+1][x]!='#' || board.mappa[p.explore.y-1][x]!='#'){
                        celle.add(new Cell(x, p.explore.y));
                    }
                }

                i++;
            }
        }
        closestCell(celle, p);
        p.action="MOVE";           
    }

    /*metodo che indica la cella più vicina la pacman passato*/
    void closestCell(ArrayList<Cell> cells, Pacman p){
        if(cells.isEmpty()==false){
            double minDist=p.distance(cells.get(0).x, cells.get(0).y);
            Cell closestCell=cells.get(0);
            for(Cell c:cells)
            {   
                double temp=p.distance(c.x, c.y);
                if(temp<minDist)
                {
                    minDist=temp;
                    closestCell=c;
                }
            }
            p.explore=closestCell;
        }
    }

    /*controllo per verificare la presenza di una cella all'interno di un vettore di celle'*/
    boolean presente(ArrayList<Cell> cell, Cell c){
        for(Cell k: cell){
            if(k.x==c.x && k.y==c.y)
                return true;
        }
        return false;
    }

    /*metodo che permette ai nostri pacman di allontanarsi dagli avversari.*/
    void goAway(Pacman p, Pacman near){
        int i=1;
        if(p.speedTurnsLeft>0)
            i=2;
        if(p.y == near.y){
            if(p.x-near.x == -i){
                int newX=Math.floorMod(p.x-i, board.width);
                if(board.mappa[p.y][newX]!='#')
                    p.choice=new Direction("left", 0, 0);
                else if(board.mappa[p.y+i][p.x]!='#')
                    p.choice=new Direction("down", 0, 0);
                else if(board.mappa[p.y-i][p.x]!='#')
                    p.choice=new Direction("up", 0, 0);
                p.action="MOVE";
            }
            else if(p.x-near.x == i){
                int newX=Math.floorMod(p.x+i, board.width);
                if(board.mappa[p.y][newX]!='#')
                    p.choice=new Direction("right", 0, 0);
                else if(board.mappa[p.y+i][p.x]!='#')
                    p.choice=new Direction("down", 0, 0);
                else if(board.mappa[p.y-i][p.x]!='#')
                    p.choice=new Direction("up", 0, 0);
                p.action="MOVE";
            }
        }
        else if(p.x == near.x){
            int newX=Math.floorMod(p.x-i, board.width);
            int newX2=Math.floorMod(p.x+i, board.width);
            int newY=Math.floorMod(p.y-i, board.height);
            int newY2=Math.floorMod(p.y+i, board.height);
            if(p.y-near.y == -i){
                if(board.mappa[newY][p.x]!='#')
                    p.choice=new Direction("up", 0, 0);
                else if(board.mappa[p.y][newX2]!='#')
                    p.choice=new Direction("right", 0, 0);
                else if(board.mappa[p.y][newX]!='#')
                    p.choice=new Direction("left", 0, 0);
                p.action="MOVE";
            }
            else if(p.y-near.y == i){
                if(board.mappa[newY2][p.x]!='#')
                    p.choice=new Direction("down", 0, 0);
                else if(board.mappa[p.y][newX2]!='#')
                    p.choice=new Direction("right", 0, 0);
                else if(board.mappa[p.y][newX]!='#')
                    p.choice=new Direction("left", 0, 0);
                p.action="MOVE";
            }
        }
    }
    
    /*metodo che controlla la presenza di un avversario o meno 
    nelle vicinanze dei nostri pacman , in caso sia presente un 
    avversario si contralla una serie di casistiche per permetterci 
    di vincere un duello o di mettere in salvo i nostri pacman.*/
    void checkForFight() {
    	for(Pacman p: myPacmans) {
    		Pacman near=isOpponentNear(p);
    		if(near!=null) {
    			if(p.abilityCooldown==0 && near.abilityCooldown!=0) {
    				if(fightResult(p, near)==1){ //vittoria
    					p.action="MOVE";
                        if(p!=null && near!=null){
                            p.choice=null;
                            p.explore=new Cell(near.x, near.y);
                        }
                    }
    				else // sconfita o pareggio
    					p.tryToWin(near);
    			}
    			else if(p.abilityCooldown==0 && near.abilityCooldown==0) {
    				if(fightResult(p, near)==1){ //vittoria
    					p.action="WAIT";
                    }
    				else //sconfita o pareggio
    					goAway(p, near);
    			}
    			else if(p.abilityCooldown!=0 && near.abilityCooldown!=0) {
    				if(fightResult(p, near)==1){ //vittoria
    					p.action="MOVE";
                        p.choice=null;
                        p.explore=new Cell(near.x, near.y);
                    }
                    else //pareggio o sconfitta
                        goAway(p, near);
    			}
                else if(p.abilityCooldown!=0 && near.abilityCooldown==0) {
                    goAway(p, near);    				
    			}
    		}
    	}
    }
    
    /*dato un avversario e un nostro pacman, restituisca 1 se il nostro pacman vince 
    -1 altrimenti, 0 in caso di pareggio*/
    int fightResult(Pacman my, Pacman oppo) {
    	if(my.typeId.equals(oppo.typeId))
    		return 0;
    	else if((my.typeId.equals("ROCK") && oppo.typeId.equals("SCISSORS")) || (my.typeId.equals("PAPER") && oppo.typeId.equals("ROCK")) || (my.typeId.equals("SCISSORS") && oppo.typeId.equals("PAPER")))
    		return 1;
    	else 
    		return -1;
    }
    
    /*verifica la presenza di un avversario nei dintorni di un nostro pacman*/
    Pacman isOpponentNear(Pacman p) {
    	Pacman op=null;
        int i=1;
        if(p.speedTurnsLeft>0)
            i=2;
    	for(Pacman oppo: opponents) {
    		if((p.y==oppo.y && (p.x+i == oppo.x || p.x-i == oppo.x)))
    			op=oppo;
    		else if((p.x==oppo.x && (p.y+i == oppo.y || p.y-i == oppo.y)))
    			op=oppo;
    	}
    	return op;
    }

    /*dato un super pellet, cerca un pacman più vicino a lui. 
    METODO NON IN USO*/
    void findPacman(Pellet p){
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
                if(board.mappa[po.y][po.x]=='P'){ 
                    for(Pacman pac:myPacmans){
                        if(pac.x==po.x && pac.y==po.y){
                            if(pac.explore==null || (pac.explore!= null && isSuperPellet(pac.explore.x, pac.explore.y)==false)){
                                pac.explore=new Cell(p.x, p.y);
                                found=true;
                                pac.choice=null;
                                System.err.println("PACID: "+pac.pacId+" CELL: "+pac.explore.x+"-"+pac.explore.y);
                                pac.action="MOVE";
                                break; 
                            }
                        }                              
                    }
                }
                if(presente(visited, po)==false)
                    cell.add(po);
            }
        }
    }

    /*verifica se una cella è un super pellet.*/
    boolean isSuperPellet(int x, int y){
        for(Pellet p: superPellets){
            if(p.x==x && p.y==y)
                return true;
        }
        return false;
    }

    /*se sono presenti super pellet sulla mappa , i pacman danno priorità a loro. 
    METODO NON IN USO*/
    void checkForSuperPellets(){
        if(superPellets.isEmpty()==false){
            int count=0;
            for(Pellet p: superPellets){
                if(count<myPacmans.size()){
                    findPacman(p);
                    count++;
                }        
            }
        }
    }

    /*metodo che in base all'action di ogni pacman genera una stringa 
    che verrà data in output ogni fine turno.*/
    void genOutput(int turn){
        String temp="";
        for(Pacman p: myPacmans){
            if(p.typeId.equals("DEAD")==false){

                if(p.action.equals("MOVE")) {
                    
                    int i=1;
                    if(p.speedTurnsLeft>0){
                        i=2;
                    }

            		temp+="| " + p.action+ " " + Integer.toString(p.pacId) + " ";

                    if(p.choice==null){
                        if(p.explore!=null)
                            temp+=Integer.toString(p.explore.x) + " " + Integer.toString(p.explore.y) + " EXP "+Integer.toString(p.explore.x)+"-"+Integer.toString(p.explore.y);
                        else
                            temp+=Integer.toString(p.x) + " " + Integer.toString(p.y) + " ";
                    }
                    else if(p.choice.direction.equals("up")){
                        int newY=Math.floorMod(p.y-i, board.height);
                        temp+=Integer.toString(p.x) + " " + Integer.toString(newY) + " ";
                    }
                    else if(p.choice.direction.equals("down")){
                        int newY=Math.floorMod(p.y+i, board.height);
                        temp+=Integer.toString(p.x) + " " + Integer.toString(newY) + " ";
                    }
                    else if(p.choice.direction.equals("right")){
                        int newX=Math.floorMod(p.x+i, board.width);
                        temp+=Integer.toString(newX) + " " + Integer.toString((p.y)) + " ";
                    }
                    else if(p.choice.direction.equals("left")){
                        int newX=Math.floorMod(p.x-i, board.width);
                        temp+=Integer.toString(newX) + " " + Integer.toString((p.y)) + " ";
                    }
                }
                else if(p.action.equals("WAIT")) {
            		temp+="| MOVE " + Integer.toString(p.pacId) + " ";
            		temp+=Integer.toString(p.x) + " " + Integer.toString(p.y) + " ";
            	} 
                else if(p.action.equals("SPEED")) {
            		temp+="| " + p.action + " " + Integer.toString(p.pacId)+ " ";
            	}
            	else if(p.action.equals("SWITCH")) {
            		temp+="| " + p.action + " " + Integer.toString(p.pacId)+ " " + p.switchTo + " ";
            	}
            }
        }
        output=temp.substring(2,temp.length());
    }

    /*attiva la speed di ogni pacman appena possibile.*/
    void activeSpeed(){
        for(Pacman p: myPacmans){
            if(p.abilityCooldown==0){
                p.action="SPEED";
            }
        }
    }

    void play(int turn){
        //osserva e scegli
        clearPos(); //pulizia vecchie posizioni
        updateMap(); //aggiornamento mappa
        chooseDirection(); //scelta della miglior direzione
        checkForNull(); //se scelta è null allora esplora
        //checkForSuperPellets();
        checkCollision(); //verifica di collisioni
        //check pacman directions        
        checkForFight(); //verifica di possibile duello

        activeSpeed(); //attiva la speed se possibile
        genOutput(turn); //genera l'output
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
        int turn=0;
        while (true) {
            turn++;
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
            gm.play(turn);
            //gm.stampaMappa();
            

            System.out.println(gm.output); // MOVE <pacId> <x> <y>
            gm.clearAll();
        }
    }
}