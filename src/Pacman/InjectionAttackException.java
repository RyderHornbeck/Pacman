package Pacman;

public class InjectionAttackException extends Exception{

    //String myId;
    String myAttackCode;
    public InjectionAttackException( String AttackCode){
        super();
       // myId =ID;
        myAttackCode=AttackCode;
    }
    public String getMessage(){
        return " AttackCode:"+myAttackCode;
    }
}
