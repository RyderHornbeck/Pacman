package PacmanFrontend;

import javax.swing.*;

public interface UpdateAndCountDown extends Updater,Countdown,Lives,Score{
    void getScore(int score);

}
