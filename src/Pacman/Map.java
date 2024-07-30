package Pacman;

public class Map {
    private Entities [][] GridSpaces;

    public Map(int numRow, int numCol) {
        this.GridSpaces = new Entities[numRow][numCol];


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
    public Entities GhostMove(int x, int y, int dx, int dy)throws InvalidMoveException {
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
            //how to make ghost go over pellets but not eat them and for the pellets to just stay where they are
        }
        return nextObject;
    }
}
