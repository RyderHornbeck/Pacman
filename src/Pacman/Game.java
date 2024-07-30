package Pacman;

import java.sql.Connection;

enum RoundOutCome{
    DIE,WIN

}
enum GhostAlgorithim{
    CHASE,RANDOM,LEAVE_CAGE,FRIGHTENED
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
     final int RandomStepCount=7;
     final int ChaseStepCount=20;
     GhostAlgorithim currentGhostAlgorithim;
     Map map;
     HighScoreDAO highScoreDAO;
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
        GhostAlgorithimChanger =4;

        currentGhostAlgorithim = GhostAlgorithim.LEAVE_CAGE;
        FindPac();
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

    }

    public boolean pacMove(){
        int x = PacX;
        int y = PacY;
        int dx=0;
        int dy=0;
        if(uparrow){
            dy=1;
        }
       else  if(downarrow){
            dy=-1;
        }
        else  if(leftarrow){
            dx =-1;
        }
        else  if(Rightarrow){
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
                    //TODO: code in how to tell java to stop tracking a specific ghost
                } else {
                    return false;

                }

            }
        }
        catch(InvalidMoveException IME ){
           System.out.println( IME.getMessage());
        }
        return true;
    }


    public void ghostMove(){

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

        if (StepCounter == GhostAlgorithimChanger) {
            if (currentGhostAlgorithim == GhostAlgorithim.LEAVE_CAGE) {
                currentGhostAlgorithim = GhostAlgorithim.RANDOM;
                GhostAlgorithimChanger += RandomStepCount;

            } else if (currentGhostAlgorithim == GhostAlgorithim.RANDOM) {
                currentGhostAlgorithim = GhostAlgorithim.CHASE;
                GhostAlgorithimChanger += ChaseStepCount;

            } else if (currentGhostAlgorithim == GhostAlgorithim.CHASE) {
                currentGhostAlgorithim = GhostAlgorithim.RANDOM;
                GhostAlgorithimChanger += RandomStepCount;

            }
        }
        if(currentGhostAlgorithim == GhostAlgorithim.LEAVE_CAGE){
            //TODO: TELEPORT THE GHOST OUTSIDE THE BOX ONE BY ONE
        }
        else if(currentGhostAlgorithim == GhostAlgorithim.RANDOM){
            //TODO: FIND NEIGHBORING EMPTY SPACE, CHOSE A RANDOM SPACE, MOVE GHOST TO THAT SPACE
        }
        else if(currentGhostAlgorithim == GhostAlgorithim.CHASE){
            //TODO:NATHAN WILL SEND THE CHASE ALGORITHIM
        }
        else if(currentGhostAlgorithim == GhostAlgorithim.FRIGHTENED){
            //TODO:NATHAN WILL SEND THE FRIGHTENED ALGORITHIM. THIS ALGORITHIM IS DYNAMIC PROGRAMMING
        }




    }

}
