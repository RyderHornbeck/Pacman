package PacmanBackend;

import PacmanFrontend.Updater;

import java.io.OptionalDataException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

enum RoundOutCome{
    DIE,WIN

}
enum GhostAlgorithim{
    CHASE,RANDOM,FRIGHTENED
}
enum keyboardDirections{
    up,right,down,left
}
public class Game {


    int PacLives;
     int PowerPellets;
     int PacX;
     int PacY;
     int Pellets;
     int StepCounter;
     int PowerPelletModeCount;
     int Score;
     int GhostAlgorithimChanger;
     final int RandomStepCount=14;
     final int ChaseStepCount=25;
     int ghostPlaceTracker;
     int [] GhostX;
     int[] GhostY;
     int [][] shortestDistances;
     Entities [] GhostsOnTopOf;
     GhostAlgorithim currentGhostAlgorithim;
     Map map;
     HighScoreDAO highScoreDAO;
     final int [] DefaultGhostX;
     final int [] DefaultGhostY;
     final int DefaultPacX=13;
     final int DefaultPacY=23;
     int [] GhostJailSentence;
     keyboardDirections PacInput;
    Updater ScreenUpdater;
    Pacman pacmanClass;
    Random RandomNumGenerator;
    public Game(Connection conn, Updater screenUpdater){
        Default_MapDAO defaultMapDAO = new Default_MapDAO(conn);
        map = defaultMapDAO.getMap();
        highScoreDAO = new HighScoreDAO(conn);
        pacmanClass = new Pacman();
        Pellets = 240;
        PowerPellets = 4;
        PacLives =3;
        StepCounter =0;
        Score=0;
        PowerPelletModeCount=0;
        GhostAlgorithimChanger =RandomStepCount;
        ghostPlaceTracker =0;
        GhostsOnTopOf = new Entities[4];
        currentGhostAlgorithim = GhostAlgorithim.RANDOM;
        FindPac();
        GhostX = new int [] {12,13,14,15};
        GhostY = new int  [] {14,14,14,14};
        GhostJailSentence= new int []{5,10,15,20};
        shortestDistances = new int[map.getNumRow()][map.getNumCol()];
        DefaultGhostX= new int [] {12,13,14,15};
        DefaultGhostY= new int [] {14,14,14,14};
        PacInput = keyboardDirections.right;
        ScreenUpdater = screenUpdater;
        RandomNumGenerator = new Random(4);
    }
    public int   GetGhostx(int i){

        return GhostX[i];
    }
    public int  GetGhosty(int i){

        return GhostY[i];
    }
    public int GetGhostxLength(){

        return GhostX.length;
    }
    public int getNumCol(){

        return map.getNumCol();
    }
    public int getNumRow(){

        return map.getNumRow();
    }
    public Entities GetEachPositionGrid(int xvalue, int yvalue){

        return map.GetEachPositionGrid(xvalue,yvalue);
    }

    public void executeGame(int ID) {


        //game starts
        boolean winStatus = false;
        while((PacLives>=1)&&!(winStatus)){
                RoundOutCome OutCome =  OneRound();
                switch(OutCome){
                    case RoundOutCome.WIN:
                        System.out.println("YOU WIN");
                        winStatus = true;
                        //do something
                        break;
                    case RoundOutCome.DIE:
                        PacLives--;
                        //do something
                        break;
                    default:
                        //do something
                }
            //game plays
        }
        System.out.println("YOU DIED GAME OVER");
        //game ends
        //game tracks your highscore and stores it in sql
        highScoreDAO.setHighScore(ID,Score);

    }
    public void findandsetPacman(){
        int pac=0;
        //Todo:this method doesn't work
        for (int i=0;i<getNumRow();i++){
            for (int j=0;j<getNumCol();j++){
                if (GetEachPositionGrid(i,j) instanceof Pacman){
                    map.SetEachPositionGrid(i,j,null);
                    PacX =DefaultPacX;
                    PacY = DefaultPacY;
                    pac++;
                }
            }

        }
       System.out.println("num of pac = "+ pac);
    }
    public RoundOutCome OneRound(){
    //Todo:fix game, to many pacman. one round goes through all 3 rounds when only supposed to go go through one
        PowerPelletModeCount =0;
        StepCounter = 0;
        MoveMoversToStart();
        GhostAlgorithimChanger =RandomStepCount;
        currentGhostAlgorithim =GhostAlgorithim.RANDOM;
        GhostJailSentence= new int []{5,10,15,20};
            while(PelletTracker() || PowerPelletTracker()) {
                System.out.println("111");
                try {
                    TimeUnit.MILLISECONDS.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                if (!OneStep()) {
                    System.out.println("hello");
                    StepCounter = 0;
                    return RoundOutCome.DIE;


                }

            }
        return RoundOutCome.WIN;



    }


    public boolean OneStep() {
        System.out.println("Red Ghost:"+GhostX[0]+", "+GhostY[0]);
        if ((GhostX[0]==12)&&(GhostY[0]==11)){
            System.out.println("Red Ghost is at 12,11");

        }
        boolean PacDieOrWin = pacMove(PacInput);
        boolean GhostDieOrWin = true;
        if(PowerPelletModeActive()){
            if(StepCounter%2==0){
              GhostDieOrWin =  ghostMove();
            }
        }
        else{
            GhostDieOrWin = ghostMove();

        }
        StepCounter++;
        ScreenUpdater.update();

        return PacDieOrWin && GhostDieOrWin;

    }

    public void MoveMoversToStart(){
        for(int i =0; i<GhostX.length;i++){

                map.SetEachPositionGrid(GhostX[i],GhostY[i],map.GhostNextObject[i]);
                map.SetEachPositionGrid(DefaultGhostX[i],DefaultGhostY[i], new Ghost());
                map.GhostNextObject[i]= null;
                GhostX[i]=DefaultGhostX[i];
                GhostY[i]=DefaultGhostY[i];

        }
        map.SetEachPositionGrid(PacX,PacY,null);
        map.SetEachPositionGrid(DefaultPacX,DefaultPacY,new Pacman());
        PacX=DefaultPacX;
        PacY=DefaultPacY;
    }
    //TODO:WHen Pacman Eats a ghost make that ghost go to cage
    public void PacMoveUp(){

        PacInput =keyboardDirections.up;
    }
    public void PacMoveRight(){

        PacInput =keyboardDirections.right;
    }
    public void PacMoveDown(){

        PacInput =keyboardDirections.down;
    }
    public void PacMoveLeft(){

        PacInput =keyboardDirections.left;
    }
    public boolean pacMove(keyboardDirections thisDirection){
        int x = PacX;
        int y = PacY;
        int dx=0;
        int dy=0;
        if(thisDirection ==keyboardDirections.up){
            dy=1;
        }
       else  if(thisDirection==keyboardDirections.down){
            dy=-1;
        }
        else  if(thisDirection==keyboardDirections.left){
            dx =-1;
        }
        else  if(thisDirection== keyboardDirections.right){
            dx =+1;
        }
        try {
            Entities Nextobj = map.move(x, y, dx, dy);
            if(!(Nextobj instanceof Wall)) {
                PacX = PacX+dx;
                PacY =PacY+dy;
            }

            if(Nextobj instanceof PowerPellet){
               PowerPellets--;
               PowerPelletEaten();
            }
            else if(Nextobj instanceof Pellet){
                Score= Score+10;
                Pellets--;
            }
            else if(Nextobj == null){

            }
            else if(Nextobj instanceof Fruit){
                Score =Score + 100;
            }
            else if(Nextobj instanceof Ghost) {
                if (PowerPelletModeActive()) {
                    Score = Score + 200;
                    ghostPlaceTracker =0;

                   for(int i =0; i<GhostX.length;i++){
                       if((GhostX[i]==PacX)&&(GhostY[i]==PacY)){
                            GhostX[i]=DefaultGhostX[i];
                            GhostY[i]=DefaultGhostY[i];
                            map.SetEachPositionGrid(DefaultGhostX[i],DefaultGhostY[i],new Ghost());
                            GhostJailSentence[i] = StepCounter+10;
                            if((map.GhostNextObject[i]instanceof PowerPellet)) {
                                PowerPellets--;
                                PowerPelletEaten();
                            }
                           map.GhostNextObject[i] = null;
                       }
                   }

                } else {
                    return false;

                }

            }
            if (!((dx == 0) && (dy == 0))) {
                //If pacman actually moves, recompute the shortest distances.
                DetermineShortestDistances();
            }
        }
        catch(InvalidMoveException IME ){
           System.out.println( IME.getMessage());
        }
        return true;
    }


    public boolean ghostMove(){
            return GhostAlgorithim();

    }
    public boolean ifGhostShouldturnBlue(){
        if(currentGhostAlgorithim == GhostAlgorithim.FRIGHTENED){
            return true;
        }
        return false;
    }
    public boolean PowerPelletModeActive(){

        if(StepCounter<=PowerPelletModeCount){

            return true;

        }
        else{


            if(currentGhostAlgorithim== GhostAlgorithim.FRIGHTENED){
                currentGhostAlgorithim = GhostAlgorithim.CHASE;
                GhostAlgorithimChanger = StepCounter + ChaseStepCount;
            }
            return false;
        }
    }
     //public void Movement(){}
     //public int score(){}
     //public void StartGame(){}
     //public void EndGame(){}

    public boolean PelletTracker(){
        if(Pellets>=1){
            return true;
        }
        else{
        return false;
        }
    }
    public boolean PowerPelletTracker(){
        if(PowerPellets>=1){
            return true;
        }
        else{
            return false;
        }
    }

    public void PowerPelletEaten(){
        PowerPelletModeCount = 20+StepCounter;
        currentGhostAlgorithim = GhostAlgorithim.FRIGHTENED;
    }
    public void FindPac(){
        for (int x =0;x<28;x++){
            for(int y=0; y<31;y++){
                if (map.GetEachPositionGrid(x,y) instanceof Pacman){
                    PacX=x;
                    PacY =y;
                    System.out.println("Pacman is at"+x+", "+y);
                }
            }
        }
    }
    public boolean GhostAlgorithim() {
        //call this like onestep, it works with one step each time
       //TODo if you eat a powerpoellet and then after you die the ghost dont come out correctly just index 1 and 3 come out
        for(int i=0;i<GhostX.length; i++){
            System.out.println("Ghost:" + i+" is going through the for loooooop");
           if (GhostJailSentence[i]==StepCounter){
               System.out.println("Ghost:"+ i+" is leaving cage");
               try {
                    Entities check =map.GhostLeaveCage(i,GhostX[i],GhostY[i]);

                    if(check == null){
                      GhostJailSentence[i] +=1;
                    }
                    else{
                        GhostX[i]= map.DefaultSpawnPointGhostX;
                        GhostY[i]= map.DefaultSpawnPointGhostY;
                    }
               }
               catch(InvalidMoveException Ime){
                   System.out.println(Ime.getMessage());
               }
               break;

           }
        }

        if (StepCounter == GhostAlgorithimChanger) {


            if (currentGhostAlgorithim == GhostAlgorithim.RANDOM) {
            currentGhostAlgorithim = GhostAlgorithim.CHASE;
            GhostAlgorithimChanger += ChaseStepCount;

         }   else if (currentGhostAlgorithim == GhostAlgorithim.CHASE) {
                currentGhostAlgorithim = GhostAlgorithim.RANDOM;
                GhostAlgorithimChanger += RandomStepCount;

            }
        }

        boolean  Temp = true;

       if(currentGhostAlgorithim == GhostAlgorithim.RANDOM){
            try {
               Temp = RandomGhostMove();
            }
            catch(InvalidMoveException IME){
                System.out.println(IME.getMessage());
            }
        }
        else if(currentGhostAlgorithim == GhostAlgorithim.CHASE){
            try{
               Temp = chaseGhostMove();
            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }
        else if(currentGhostAlgorithim == GhostAlgorithim.FRIGHTENED){
            try{
                Temp = FrightenedGhostMove();
            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }



      return Temp;
    }
    public boolean RandomGhostMove()throws InvalidMoveException {
        boolean Temp = true;
        int [] RandomDirections = new int[GhostX.length];
        for(int i =0; i<GhostX.length;i++) {
            ArrayList<Integer> ValidDirections = new ArrayList<Integer>(4);
            Entities[] GhostSurroundings = map.checkSurroundings(GhostX[i], GhostY[i]);
            for (int j = 0; j < 4; j++) {
                if (!((GhostSurroundings[j] instanceof Wall) || (GhostSurroundings[j] instanceof Ghost))) {
                    ValidDirections.add(j);
                }
            }
            int Random = (int) (RandomNumGenerator.nextDouble() * ValidDirections.size());
            RandomDirections[i] = ValidDirections.get(Random);

       //for(int i=0;i<GhostX.length;i++){
            int dx =0;
            int dy =0;
            if(RandomDirections[i]==0){
                dy=-1;

            }
            else if(RandomDirections[i]==1){
                dx=1;

            }
            else if(RandomDirections[i]==2){
                dy=+1;

            }
            else if(RandomDirections[i]==3){
                dx=-1;

            }
            if((GhostX[i]!=DefaultGhostX[i])||(GhostY[i]!=DefaultGhostY[i])) {
                if(map.GetEachPositionGrid(GhostX[i]+dx,GhostY[i]+dy)instanceof Pacman){
                    Temp = false;
                }
                map.Ghostmove(i, GhostX[i], GhostY[i], dx, dy);
                GhostX[i] = GhostX[i] + dx;
                GhostY[i] = GhostY[i] + dy;

            }
            if(!Temp){
                return Temp;

            }
        }

    return Temp;
    }
    public boolean chaseGhostMove() throws InvalidMoveException {
        boolean Temp = true;
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        for (int j = 0; j < GhostX.length; j++) {
            if(!((GhostX[j]== DefaultGhostX[j])&&(GhostY[j]== DefaultGhostY[j]))) {
                int TempShortest = 2147483647;
                int TempShortestDirection = -1;
                for (int i = 0; i < dx.length; i++) {
                    System.out.println("Checking Ghost: " + j + " neighbor: " + i);

                    int neighborX = GhostX[j] + dx[i];
                    int neighborY = GhostY[j] + dy[i];

                    if ((neighborX < map.getNumRow() && neighborX >= 0) && (neighborY < map.getNumCol() && neighborY >= 0)) {
                        Entities neighbor = map.GetEachPositionGrid(neighborX, neighborY);

                        if (!((neighbor instanceof Wall) || (neighbor instanceof Ghost))) {
                            int DistToPacMan = shortestDistances[neighborX][neighborY];
                            System.out.println("The Dist to pacman is " + DistToPacMan);

                            if (DistToPacMan < TempShortest) {
                                TempShortest = DistToPacMan;
                                TempShortestDirection = i;
                            }
                        } else {
                            System.out.println("This neighbor position is a wall or ghost");

                        }

                    } else {
                        System.out.println("This neighbor position is outside grid");
                    }
                }
                if (TempShortestDirection == -1) {
                    throw new InvalidMoveException("No possible moves found Chase " + j);
                }
                if ((GhostX[j] != DefaultGhostX[j]) || (GhostY[j] != DefaultGhostY[j])) {
                    if(map.GetEachPositionGrid(GhostX[j]+dx[TempShortestDirection],GhostY[j]+dy[TempShortestDirection])instanceof Pacman){
                        Temp = false;
                    }
                    map.Ghostmove(j, GhostX[j], GhostY[j], dx[TempShortestDirection], dy[TempShortestDirection]);
                    GhostX[j] = GhostX[j] + dx[TempShortestDirection];
                    GhostY[j] = GhostY[j] + dy[TempShortestDirection];
                }
            }
            else{
                System.out.print("Ghost:"+j+" is still in the box");

            }
            if(!Temp){
                return Temp;

            }
        }
        return Temp;
    }
    public boolean FrightenedGhostMove() throws InvalidMoveException {
        boolean Temp = true;

        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        for (int j = 0; j < GhostX.length; j++) {
            if (!((GhostX[j] == DefaultGhostX[j]) && (GhostY[j] == DefaultGhostY[j]))) {
                int TempLongest = -1;
                int TempLongestDirection = -1;
                for (int i = 0; i < dx.length; i++) {
                    int neighborX = GhostX[j] + dx[i];
                    int neighborY = GhostY[j] + dy[i];
                    if ((neighborX < map.getNumRow() && neighborX >= 0) && (neighborY < map.getNumCol() && neighborY >= 0)) {
                        Entities neighbor = map.GetEachPositionGrid(neighborX, neighborY);

                        if (!((neighbor instanceof Wall) || (neighbor instanceof Ghost))) {
                            int DistToPacMan = shortestDistances[neighborX][neighborY];
                            if (DistToPacMan > TempLongest) {
                                TempLongest = DistToPacMan;
                                TempLongestDirection = i;
                            }
                        }
                    }
                }
                if (TempLongestDirection == -1) {
                    throw new InvalidMoveException("No possible moves found Frightened " + j);
                }

                  //  map.SetEachPositionGrid(GhostX[i],GhostY[i],new BlueGhost());



                if ((GhostX[j] != DefaultGhostX[j]) || (GhostY[j] != DefaultGhostY[j])) {
                    if(map.GetEachPositionGrid(GhostX[j]+dx[TempLongestDirection],GhostY[j]+dy[TempLongestDirection])instanceof Pacman){
                        Temp = false;
                    }
                    map.Ghostmove(j, GhostX[j], GhostY[j], dx[TempLongestDirection], dy[TempLongestDirection]);

                    GhostX[j] = GhostX[j] + dx[TempLongestDirection];
                    GhostY[j] = GhostY[j] + dy[TempLongestDirection];
                }
            }
            else{
                System.out.print("Ghost:"+j+" is still in the box Frightened");
            }
            if(!Temp){
                return Temp;

            }
        }
        return Temp;
    }
    private void UpdateNeighboringShortestDistances(int x, int y) {
        int myValue = shortestDistances[x][y];
        int neighboringValue = myValue + 1;

        //Top Neighbor
        if (y != map.getNumCol()-1){
            if ((neighboringValue < shortestDistances[x][y+1]) && !(map.GetEachPositionGrid(x,y+1) instanceof Wall)){
                shortestDistances[x][y+1] = neighboringValue;
                UpdateNeighboringShortestDistances(x,y+1);
            }
        }
        //Bottom Neighbor
        if (y != 0){
            if ((neighboringValue < shortestDistances[x][y-1]) && !(map.GetEachPositionGrid(x,y-1) instanceof Wall)) {
                shortestDistances[x][y-1] = neighboringValue;
                UpdateNeighboringShortestDistances(x,y-1);
            }
        }
        //Left Neighbor
        if (x != 0){
            if ((neighboringValue < shortestDistances[x-1][y]) && !(map.GetEachPositionGrid(x-1,y) instanceof Wall)) {
                shortestDistances[x-1][y] = neighboringValue;
                UpdateNeighboringShortestDistances(x-1,y);
            }
        }
        //Right Neighbor
        if (x != map.getNumRow()-1){
            if ((neighboringValue < shortestDistances[x+1][y]) && !(map.GetEachPositionGrid(x+1,y) instanceof Wall)) {
                shortestDistances[x+1][y] = neighboringValue;
                UpdateNeighboringShortestDistances(x+1,y);
            }
        }
    }

    private void DetermineShortestDistances(){
        //First Reset the shortestDistances array
        for (int x = 0; x < map.getNumRow(); x++){
            for (int y = 0; y < map.getNumCol(); y++) {
                shortestDistances[x][y] = 2147483647; //The largest possible int
            }
        }

        //Next set the value of shortestDistances at pacman's current position to zero
        shortestDistances[PacX][PacY] = 0;

        //Starting at this zero position, update all neighboring positions if they are shorter than the currently held value.
        //  Do this recursively (i.e. for each updated position, repeat the process) if the value was updated.
        //THIS ALGORITHIM IS "DYNAMIC PROGRAMMING"
        UpdateNeighboringShortestDistances(PacX,PacY);
    }



}
