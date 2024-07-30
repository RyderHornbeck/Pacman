package Pacman;
import java.util.Arrays;
public class Map {
    private Entities [][] GridSpaces;





    Entities [] GhostsBeforeGridSpace;

    Entities [] GhostNextObject;
public Map(int numRow, int numCol) {
        this.GridSpaces = new Entities[numRow][numCol];





        Entities [] GhostNextObject = new Entities[4];
        Entities [] GhostsGridSpace = {GridSpaces[12][11],GridSpaces[13][11],GridSpaces[14][11],GridSpaces[15][11]};


    //GridSpaces is in Row Major Order
        //aka Gridspaces is an array of row arrays


}

    public void setGridSpaces(Entities [][] GridSpaces) {
        this.GridSpaces = GridSpaces;
    }

    public Entities[][] getGridSpaces() {
        return GridSpaces;
    }

    public void SetEachPositionGrid(int xvalue, int yvalue,Entities EN){
        GridSpaces[xvalue][yvalue] = EN;
    }
    public Entities GetEachPositionGrid(int xvalue,int yvalue){
        return  GridSpaces[xvalue][yvalue];
    }

    public Entities move(int x, int y, int dx, int dy)throws InvalidMoveException {
        Entities currentobject = GridSpaces[x][y];
        Entities nextObject = GridSpaces[x+dx][y+dy];
        if (!(currentobject instanceof Movers)) {
            throw new InvalidMoveException(" this is an InvalidMoveException, you are trying move a nonMover");

        }
        else if( (dx != 0)&& (dy!=0)){
            throw new InvalidMoveException(" this is an InvalidMoveException, you are trying to move diagonally");

        }
        else if(nextObject instanceof Wall){
            ///if you try to move into a wall nothing happens

        }
        else{
            GridSpaces[x + dx][y + dy] = currentobject;
            GridSpaces[x][y] = null;

        }
        return nextObject;
    }

    public Entities GhostLeaveCage(int ghostPlaceTracker)throws InvalidMoveException {
        Entities outsideCage = GridSpaces[13][11];
        Entities [] ghostArr = {GridSpaces[12][14],GridSpaces[13][14],GridSpaces[14][14],GridSpaces[15][14]};

        Entities currentobject = ghostArr[ghostPlaceTracker];

        if (!(currentobject instanceof Movers)) {
            throw new InvalidMoveException(" this is an InvalidMoveException, you are trying move a nonMover");

        }
        else if(outsideCage instanceof Wall){
            ///if you try to move into a wall nothing happens

        }
        else{
            GridSpaces[13][11] = currentobject;

             if(currentobject == ghostArr[0]){
                GridSpaces[12][14]=null;
             }
            else if(currentobject == ghostArr[1]) {
                 GridSpaces[13][14]=null;
            }
             else if(currentobject == ghostArr[2]) {
                 GridSpaces[14][14]=null;
             }
             else if(currentobject == ghostArr[3]) {
                 GridSpaces[15][14]=null;
             }
     }

    return GridSpaces[13][11];
    }
    public void AfterGhostLeaveCage(){
        if((GridSpaces[13][11] instanceof Ghost)&&(GridSpaces[12][11] instanceof Ghost)&&(GridSpaces[14][11] instanceof Ghost)&&(GridSpaces[15][11] instanceof Ghost)){
            //do nothing, this marks the end of the ghosts leaving the cage
        }
        else if((GridSpaces[13][11] instanceof Ghost)&&(GridSpaces[14][11] instanceof Ghost)&&(GridSpaces[15][11] instanceof Ghost)){
            Entities TempGhostTracker = GridSpaces[13][11];
            GridSpaces[12][11] = TempGhostTracker;
            GridSpaces[13][11] = null;
        }
        else if((GridSpaces[13][11] instanceof Ghost)&&(GridSpaces[14][11] instanceof Ghost)){
            Entities AnotherGhostTracker = GridSpaces[14][11];
            GridSpaces[15][11] = AnotherGhostTracker;
            GridSpaces[14][11] = null;
            Entities TempGhostTracker = GridSpaces[13][11];
            GridSpaces[14][11] = TempGhostTracker;
            GridSpaces[13][11] = null;
        }
        else if(GridSpaces[13][11] instanceof Ghost){
            Entities TempGhostTracker = GridSpaces[13][11];
            GridSpaces[14][11] = TempGhostTracker;
            GridSpaces[13][11] = null;
        }

    }


// checks surrounding entities and puts them into an array
public Entities [] checkSurroundings(int x,int y,Entities [] ghost){
    if(GridSpaces[x][y] instanceof Wall){
        ///if you try to move into a wall nothing happens
        for(int i =0; i<4;i++){
            ghost[i] = GridSpaces[x][y];
        }
    }
    else {
        ghost[0] = GridSpaces[x][y - 1];
        ghost[1] = GridSpaces[x + 1][y];
        ghost[2] = GridSpaces[x][y + 1];
        ghost[3] = GridSpaces[x - 1][y];
    }



    return ghost;
}


}
