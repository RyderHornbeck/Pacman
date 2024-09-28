package PacmanBackend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HighScoreDAO extends BaseDataAccessObject{
    public HighScoreDAO(Connection conn){

        super(conn);
    }
    public int getHighScore(int id){
        try{
            ResultSet result = execute("SELECT HIGH_SCORE FROM USERS WHERE ID = " + id);
            int highscore =  result.getInt("HIGH_SCORE");
            return highscore;
        }
        catch(SQLException SQL){
            SQL.getMessage();
        }
        return -1;
    }
    public int getEveryonesHighScore(){
        try{
            ResultSet result = execute("SELECT MAX(HIGH_SCORE)AS max_highscore FROM USERS");
            int highscore =  result.getInt("max_highscore");
            return highscore;
        }
        catch(SQLException SQL){
            SQL.getMessage();
        }
        return -1;
    }
    public void setHighScore(int id , int HS){
          String settingHighScore ="UPDATE USERS SET HIGH_SCORE ="+ HS +" WHERE ID ="+id;
        try {
            executeWithoutReturn(settingHighScore);

        }
        catch(SQLException sql){
           System.out.println("sql exception caught trying to insert new highscore");
        }
    }
}
