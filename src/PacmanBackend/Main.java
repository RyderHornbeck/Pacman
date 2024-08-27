package PacmanBackend;

import java.sql.*;

public class Main {
/*


    public static void main(String[] args) {

        Connection conn = null;
        try {
            int ID = 0;
            //ask for Username
            try {
                UsernameDAO usernameDAO = new UsernameDAO();
                conn = usernameDAO.getConn();
                ID = usernameDAO.login();

            } catch (InjectionAttackException IAE) {
                System.out.println(IAE.getMessage());
                return;
            }
            //username gets stored in sql database, log the time the user signed in
            UserTimeDAO userTimeDAO = new UserTimeDAO(conn);

            userTimeDAO.TrackUserTime(ID);

            //Map gets initialized
            Default_MapDAO defaultMapDAO = new Default_MapDAO(conn);
            Map map = defaultMapDAO.getMap();
            //Map gets put on screen
            //game starts
            //game plays

            //game ends
            //game tracks your highscore and stores it in sql
            HighScoreDAO highScoreDAO = new HighScoreDAO(conn);
            int highscore = 100;
            highScoreDAO.setHighScore(ID, highscore);

            //game closes
            highScoreDAO.close();

        }
        finally{
            if(conn!= null){
                try{
                conn.close();


                }
                catch(SQLException sql){

                }
            }
        }
    }
*/
}