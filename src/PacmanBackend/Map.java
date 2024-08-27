package PacmanBackend;
import java.util.ArrayList;

public class Map {
    private Entities [][] GridSpaces;
    Entities [] GhostsBeforeGridSpace;

    Entities [] GhostNextObject;
    int numRow;
    int numCol;
public Map(int numRow, int numCol) {
        this.GridSpaces = new Entities[numRow][numCol];
        this.numRow= numRow;
        this.numCol =numCol;
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
    public void Ghostmove(int i,int x, int y, int dx, int dy)throws InvalidMoveException {
        Entities currentobject = GridSpaces[x][y];

        if (!(currentobject instanceof Movers)) {
            throw new InvalidMoveException(" this is an InvalidMoveException, you are trying move a nonMover");

        }
        else if( (dx != 0)&& (dy!=0)){
            throw new InvalidMoveException(" this is an InvalidMoveException, you are trying to move diagonally");

        }
        else if(GridSpaces[x+dx][y+dy] instanceof Wall){
            ///if you try to move into a wall nothing happens

        }
        else{

            GridSpaces[x][y] = GhostNextObject[i];
            GhostNextObject[i] = GridSpaces[x+dx][y+dy];
            GridSpaces[x + dx][y + dy] = currentobject;
        }

    }

    public Entities GhostLeaveCage(int ghostPlaceTracker)throws InvalidMoveException {

        Entities outsideCage = GridSpaces[13][11];
        if(outsideCage instanceof Ghost){
            return null;
        }

        ArrayList <Entities>  ghostArr = new ArrayList<Entities>();
            if(GridSpaces[12+ghostPlaceTracker][14] instanceof Ghost){
                ghostArr.add(GridSpaces[12+ghostPlaceTracker][14]);

            }


        Entities currentobject = ghostArr.get(0);

        if (!(currentobject instanceof Movers)) {
            throw new InvalidMoveException(" this is an InvalidMoveException, you are trying move a nonMover");

        }
        else if(outsideCage instanceof Wall){
            ///if you try to move into a wall nothing happens

        }
        else{
            GridSpaces[13][11] = currentobject;
            GridSpaces[12+ghostPlaceTracker][14]=null;


     }

    return GridSpaces[13][11];
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
    public int getNumRow() {

    return numRow;
    }
    public int getNumCol(){

    return numCol;
    }

}
