package PacmanBackend;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UsernameDAO extends BaseDataAccessObject  {
    public UsernameDAO(Connection conn){

        super(conn);
    }

    public String AskforUserName(){
        System.out.println("Type your username");
        Scanner scn = new Scanner(System.in);
        String Name = scn.nextLine();

        return Name;
    }
    public String AskforInput(){
        System.out.println("Yes or No");
        Scanner scn = new Scanner(System.in);
        String Answer = scn.nextLine();
        return Answer;
    }
    public boolean yesOrNo() {
        System.out.println("We dont have an account for this userName, would you like to make an account?");
        String answer = AskforInput();
        answer = answer.toLowerCase();

        if (answer.equalsIgnoreCase("yes")) {
            return true;
        }
        return false;
    }
    public void setUserName(String Name) throws InjectionAttackException{


      try {
              checkInjectionAttack(Name);
          executeWithoutReturn("INSERT INTO USERS VALUES (NULL,\""+Name+"\",NULL)");


      }
    catch(SQLException SQl){

        System.out.println(SQl.getMessage());
    }

    }

    public int getUserName(String username) throws InjectionAttackException{
        try {
            checkInjectionAttack(username);
            ResultSet result = execute("SELECT ID FROM USERS WHERE NAME = \"" + username+"\"");
            /// THIS EXECUTE ABOVE DOESNT ACTUALLY COLLECT OR SELECT DATA SAME WITH ALL THE EXECUTE CALLS, BECAUSE OF THE SQL EXCEPTION I THINK
            if (result.next()) {
                int Userid = result.getInt("ID");

                return Userid;
            }

        }
        catch(SQLException SQl){
          System.out.println(SQl.getMessage());
        }
        return -1;
    }
    public int login() throws InjectionAttackException{

            int keepTrying = 1;
            while (keepTrying > 0) {

                String nameDao = AskforUserName();

                int UserID = getUserName(nameDao);
                //test if there is already is an account
                if (UserID == -1) {
                    //if there isnt make a new username

                    if (yesOrNo()) {

                        //this if statement asks if you want to make an account, if true make a new account
                        System.out.println("print your username for this new account");
                        Scanner scn = new Scanner(System.in);
                        nameDao = scn.nextLine();

                        setUserName(nameDao);
                        UserID = getUserName(nameDao);
                        keepTrying = 0;
                        System.out.println("This is your userID: " + UserID);
                        return UserID;
                    } else if (UserID == -1) {
                        keepTrying++;
                    }

                }
                else{
                     System.out.println("You have successfully logged in,  your ID is "+ UserID);
                     keepTrying =0;
                     return UserID;
                    }


            }
        /// -1 should never be returned
        return -1;
    }
}
