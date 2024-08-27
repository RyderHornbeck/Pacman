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
            ResultSet result = execute("SELECT HIGHSCORE FROM USERS WHERE ID = " + id);
            int highscore =  result.getInt("HIGHSCORE");
            return highscore;
        }
        catch(SQLException SQL){
            SQL.getMessage();
        }
        return -1;
    }
    public void setHighScore(int id , int HS){
        try {
            executeWithoutReturn("UPDATE USERS SET HIGHSCORE = " + HS + "WHERE ID = " + id);
        }
        catch(SQLException sql){
            sql.getMessage();
        }
    }
}
