package PacmanBackend;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserTimeDAO extends BaseDataAccessObject{
    final static DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public UserTimeDAO(Connection conn){
        super(conn);
    }
    public void TrackUserTime(int id){
       try {
           LocalDateTime currentTime = LocalDateTime.now();
           String time = currentTime.format(myFormatter);
           executeWithoutReturn("INSERT INTO USER_TIME VALUES (\"" + time +"\" ," + id +" )");

       }
       catch(SQLException sql){
           System.out.println(sql.getMessage());
       }
    }

}
