package Pacman;

import java.sql.Connection;
import java.util.ArrayList;

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
     final int RandomStepCount=12;
     final int ChaseStepCount=20;
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
     boolean [] GhostHasBeenSpawned;
    public Game(Connection conn){
        Default_MapDAO defaultMapDAO = new Default_MapDAO(conn);
        map = defaultMapDAO.getMap();
        highScoreDAO = new HighScoreDAO(conn);
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
        GhostY = new int  [] {11,11,11,11};
        GhostHasBeenSpawned = new boolean []{false,false,false,false};
        shortestDistances = new int[map.getNumRow()][map.getNumCol()];
        DefaultGhostX= new int [] {12,13,14,15};
        DefaultGhostY= new int [] {11,11,11,11};
        //TODO; make int arr
    }
    public void executeGame(int ID) {

        //Map gets put on screen
        printMap();
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
        //game ends
        //game tracks your highscore and stores it in sql
        highScoreDAO.setHighScore(ID,Score);

    }
    public void printMap(){

    }
    public RoundOutCome OneRound(){
        MoveMoversToStart();
        StepCounter = 0;
        while(PelletTracker()||PowerPelletTracker()){

            if(!OneStep()){
                StepCounter=0;
                return RoundOutCome.DIE;
            }
        }
        return RoundOutCome.WIN;
    }

    public boolean OneStep() {
        boolean PacDieOrWin = pacMove();
        if(PowerPelletModeActive()){
            if(StepCounter%2==0){
                ghostMove();
            }
        }
        else{
            ghostMove();

        }
        StepCounter++;
        return PacDieOrWin;

    }

    public void MoveMoversToStart(){
        ghostPlaceTracker =0;
    }
    public void PacMoveUp(){
        pacMove(keyboardDirections.up);
    }
    public void PacMoveRight(){

        pacMove(keyboardDirections.right);
    }
    public void PacMoveDown(){
        pacMove(keyboardDirections.down);
    }
    public void PacMoveLeft(){

        pacMove(keyboardDirections.left);
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
             if(Nextobj instanceof Pellet){
                Score= Score+10;
                Pellets--;
            }
            else if(Nextobj instanceof PowerPellet){
               PowerPellets--;
               PowerPelletEaten();
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

                    //TODO: code in how to tell java to stop tracking a specific ghost
                   for(int i =0; i<GhostX.length;i++){
                       if((GhostX[i]==PacX)&&(GhostY[i]==PacY)){
                            GhostX[i]=DefaultGhostX[i];
                            GhostY[i]=DefaultGhostY[i];

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


    public void ghostMove(){
            GhostAlgorithim();
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
                    return;
                }
            }
        }
    }
    public void GhostAlgorithim() {
        //call this like onestep, it works with one step each time

        for(int i=0;i<GhostX.length; i++){
           if (!GhostHasBeenSpawned[i]){
               try {
                    Entities check =map.GhostLeaveCage(i);
                    if(check != null){
                        GhostHasBeenSpawned[i]=true;
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



       if(currentGhostAlgorithim == GhostAlgorithim.RANDOM){
            try {
                RandomGhostMove();
            }
            catch(InvalidMoveException IME){
                System.out.println(IME.getMessage());
            }
        }
        else if(currentGhostAlgorithim == GhostAlgorithim.CHASE){
            try{
                chaseGhostMove();
            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }
        else if(currentGhostAlgorithim == GhostAlgorithim.FRIGHTENED){
            try{
                FrightenedGhostMove();
            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }




    }
    public void RandomGhostMove()throws InvalidMoveException {
        Entities [][] GhostSurroundings = new Entities[GhostX.length][4];
        for(int i =0; i<GhostX.length;i++) {
            // The surroundings go in order of North east south west
            GhostSurroundings[i] = map.checkSurroundings(GhostX[i], GhostY[i], GhostSurroundings[i]);
        }

        // Ghost1After and Ghost1 are arrays that check the surrounding Entities

        int Random  = 0;
        ArrayList <ArrayList <Integer>> WhichPathwaysAreNotWallsTracker = new ArrayList <ArrayList <Integer>>(GhostX.length);
        int [] RandomIndexes  =new int[GhostX.length];
        int [] RandomDirections = new int[GhostX.length];
        for(int i =0; i<GhostX.length;i++){
            WhichPathwaysAreNotWallsTracker.set(i, new ArrayList<Integer>());
            for(int j =0; j<4;j++){
            if(!(GhostSurroundings[i][j] instanceof Wall)&&!(GhostSurroundings[i][j] instanceof Ghost)) {

                WhichPathwaysAreNotWallsTracker.get(i).add(j);
               }
            }
            Random =(int)(Math.random()*WhichPathwaysAreNotWallsTracker.get(i).size());
            RandomIndexes[i] = Random;
            RandomDirections[i] =WhichPathwaysAreNotWallsTracker.get(i).get(RandomIndexes[i]);
        }
        //this whole random block has an arraylist of arraylists and this holds the 4 ghosts valid pathways.
        //and RandomIndexes holds 4 random indexes of the arraylists inside the bigger arraylist.
        // RandomDirection holds the 4 new directiions of the 4 ghosts by getting the actual pathway we want from the arraylists using our random indexes



        for(int i=0;i<GhostX.length;i++){
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
            if((GhostX[i]!=DefaultGhostX[i])&&(GhostY[i]!=DefaultGhostY[i])) {
                map.Ghostmove(i, GhostX[i], GhostY[i], dx, dy);
                GhostX[i] = GhostX[i] + dx;
                GhostY[i] = GhostY[i] + dy;

            }
        }


    }
    public void chaseGhostMove() throws InvalidMoveException {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        for (int j = 0; j < GhostX.length; j++) {
            int TempShortest = 2147483647;
            int TempShortestDirection = -1;
            for (int i = 0; i < dx.length; i++) {
                int neighborX = GhostX[j] + dx[i];
                int neighborY = GhostY[j] + dy[i];
                if ((neighborX < map.getNumRow() && neighborX >= 0) && (neighborY < map.getNumCol() && neighborY >= 0)) {
                    Entities neighbor = map.GetEachPositionGrid(neighborX, neighborY);

                    if (!(neighbor instanceof Wall)) {
                        int DistToPacMan = shortestDistances[neighborX][neighborY];
                        if (DistToPacMan < TempShortest) {
                            TempShortest = DistToPacMan;
                            TempShortestDirection = i;
                        }
                    }

                }
            }
            if (TempShortestDirection == -1) {
                throw new InvalidMoveException("No possible moves found");
            }
            if ((GhostX[j] != DefaultGhostX[j]) && (GhostY[j] != DefaultGhostY[j])) {

                map.Ghostmove(j, GhostX[j], GhostY[j], dx[TempShortestDirection], dy[TempShortestDirection]);
                GhostX[j] = GhostX[j] + dx[TempShortestDirection];
                GhostY[j] = GhostY[j] + dy[TempShortestDirection];
            }
        }
    }
    public void FrightenedGhostMove() throws InvalidMoveException {
        int[] dx = {0, 1, 0, -1};
        int[] dy = {1, 0, -1, 0};
        for (int j = 0; j < GhostX.length; j++) {
            int TempLongest = -1;
            int TempLongestDirection = -1;
            for (int i = 0; i < dx.length; i++) {
                int neighborX = GhostX[j] + dx[i];
                int neighborY = GhostY[j] + dy[i];
                if ((neighborX < map.getNumRow() && neighborX >= 0) && (neighborY < map.getNumCol() && neighborY >= 0)) {
                    Entities neighbor = map.GetEachPositionGrid(neighborX, neighborY);

                    if (!(neighbor instanceof Wall)) {
                        int DistToPacMan = shortestDistances[neighborX][neighborY];
                        if (DistToPacMan > TempLongest) {
                            TempLongest = DistToPacMan;
                            TempLongestDirection = i;
                        }
                    }

                }
            }
            if (TempLongestDirection == -1) {
                throw new InvalidMoveException("No possible moves found");
            }
            if((GhostX[j]!=DefaultGhostX[j])&&(GhostY[j]!=DefaultGhostY[j])) {

                map.Ghostmove(j, GhostX[j], GhostY[j], dx[TempLongestDirection], dy[TempLongestDirection]);
                GhostX[j] = GhostX[j] + dx[TempLongestDirection];
                GhostY[j] = GhostY[j] + dy[TempLongestDirection];
            }
        }
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
