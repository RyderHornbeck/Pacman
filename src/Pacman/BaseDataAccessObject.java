package Pacman;

import java.sql.*;

public abstract class BaseDataAccessObject {

    private Connection conn;


    public BaseDataAccessObject(String url) {

        try  {
            conn = DriverManager.getConnection(url);
            if (conn == null) {
                throw new SQLException("failed to establish connection");

            }
        } catch (SQLException  e) {
            System.out.println(e.getMessage());
        }
    }
    public BaseDataAccessObject(Connection conn){
        this.conn = conn;
    }
    public Connection getConn(){
        return this.conn;
    }
    public BaseDataAccessObject() {
        this("jdbc:sqlite:C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanDatabase.db");
    }

    protected void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        }
        catch (SQLException  e) {
            System.out.println(e.getMessage());
        }
    }
    public ResultSet execute(String sql) throws SQLException{

        PreparedStatement statement = conn.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        return result;



    }
    public void executeWithoutReturn(String sql) throws SQLException {

        Statement statement = conn.createStatement();
        statement.executeUpdate(sql);
    }
    public void checkInjectionAttack(String userInput) throws InjectionAttackException{
        if (userInput.contains(";")) {
            throw new InjectionAttackException(userInput);
        }
    }
}