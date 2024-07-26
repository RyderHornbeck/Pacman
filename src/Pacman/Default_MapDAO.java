package Pacman;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Default_MapDAO extends BaseDataAccessObject{


    public Map getMap(){
      int numRow=getRow();
      int numCol=getCol();
      Map  myMap = new Map(numRow,numCol);


      for(int x =1; x<=numRow; x++ ) {
          for (int y = 1; y <= numCol; y++) {


            try{
                String Command = "SELECT TYPE FROM DEFAULT_MAP WHERE XVALUE ="+x+" AND YVALUE =" + y;
                ResultSet result1 =  execute(Command);
                String type = result1.getString("TYPE");
                if(type == "WALL"){
                    Wall wall = new Wall();
                    myMap.SetEachPositionGrid(x,y,wall);

                }
                else if(type == "PELLET"){
                    Pellet pellet = new Pellet();
                    myMap.SetEachPositionGrid(x,y,pellet);

                }
                else if(type == "PACMAN"){
                    Pacman pacman = new Pacman();
                    myMap.SetEachPositionGrid(x,y,pacman);

                }
                else if(type == null){

                    myMap.SetEachPositionGrid(x,y,null);

                }
                else if(type == "POWER_PELLET"){
                    PowerPellet Powerpellet = new PowerPellet();
                    myMap.SetEachPositionGrid(x,y,Powerpellet);

                }
                else if(type == "GHOSTS"){
                    Ghost ghost = new Ghost();
                    myMap.SetEachPositionGrid(x,y,ghost);

                }
                //TODO:Finish these if statements, pass in null for setpos when theres blank spaces
            }
            catch(SQLException sql){
               System.out.println(sql.getMessage());
            }
          }
      }

      return myMap;
    }

    public Default_MapDAO(Connection conn){
        super(conn);
    }
    public int getRow(){
          try {
              ResultSet result = execute("SELECT MAX(XVALUE) FROM DEFAULT_MAP; ");

              return result.getInt("MAX(XVALUE)");
          }
          catch(SQLException e){
              System.out.println(e.getMessage());

          }
      return -1;
    }


    public int getCol(){
            try {
                ResultSet result = execute("SELECT MAX(YVALUE) FROM DEFAULT_MAP; ");

                return result.getInt("MAX(YVALUE)");
            }
            catch(SQLException e){
                System.out.println(e.getMessage());

            }
        return -1;
    }

}
