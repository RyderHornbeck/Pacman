package PacmanFrontend;

import PacmanBackend.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import javax.swing.*;


public class MainWindow implements UpdateAndCountDown{
    JLabel Label;
    JPanel Scorepanel;
    JLabel Scorelabel;
    JPanel Countdownpanel;
    JPanel PacManLives1;
    JLabel PacLives1;
    JPanel PacManLives2;
    JLabel PacLives2;
    JPanel PacManLives3;
    JLabel PacLives3;
    JLabel countdown;
    Connection conn;
    Game game;
    JPanel gameGrid;
    JLayeredPane LayeredPane;
    JLabel [][] LabelObj;
    ImageIcon CherryPic;
    ImageIcon BlackSquarePic;
    ImageIcon PowerPelletPic;
    ImageIcon BlueGhost1;
    ImageIcon [] GhostDecider;
    ImageIcon PelletPic;
    ImageIcon PacmanPic;
    ImageIcon PacmanPicLives;

    ImageIcon RightPacmanPic;
    ImageIcon LeftPacmanPic;
    ImageIcon DownPacmanPic;
    ImageIcon PacmanUpPic;
    ImageIcon PacmanPicDecider;
    ImageIcon Red3Pic;
    ImageIcon Red2Pic;
    ImageIcon Red1Pic;
    ImageIcon [] GhostsPics;
    public static void main(String[] args) {

        new MainWindow();

    }

    public MainWindow() {
        conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanDatabase.db");
            if (conn == null) {
                throw new SQLException("failed to establish connection");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
        GhostDecider = new ImageIcon[4];
        PacmanPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\PACMANwithdarkoutline.png");
        Image image = PacmanPic.getImage();
        Image newimg = image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        PacmanPic = new ImageIcon(newimg);

        PacmanPicLives = new ImageIcon("C:\\Users\\ryder\\Downloads\\PACMANwithdarkoutline.png");
        Image image2 = PacmanPicLives.getImage();
        Image newimg2 = image2.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        PacmanPicLives = new ImageIcon(newimg2);

        PacmanPicDecider =PacmanPic;

        PacmanUpPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\PACMANUP.png");
        Image Upimage = PacmanUpPic.getImage();
        Image Upnewimg = Upimage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        PacmanUpPic = new ImageIcon(Upnewimg);

        DownPacmanPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\PACMANDown - Copy.png");
        Image Downimage = DownPacmanPic.getImage();
        Image Downnewimg = Downimage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        DownPacmanPic = new ImageIcon(Downnewimg);

        LeftPacmanPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\PACMANLeft.png");
        Image Leftimage = LeftPacmanPic.getImage();
        Image Leftnewimg = Leftimage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        LeftPacmanPic = new ImageIcon(Leftnewimg);

        CherryPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\Cherry.png");
        Image Cherryimage = CherryPic.getImage();
        Image Cherrynewimg = Cherryimage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        CherryPic = new ImageIcon(Cherrynewimg);

        BlueGhost1 = new ImageIcon("C:\\Users\\ryder\\Downloads\\RealBlueGhost.png");
        Image Blueimage = BlueGhost1.getImage();
        Image Bluenewimg = Blueimage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        BlueGhost1 = new ImageIcon(Bluenewimg);

        BlackSquarePic = new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\BlackSquare.png");
        Image Wallimage = BlackSquarePic.getImage();
        Image Wallnewimg = Wallimage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        BlackSquarePic = new ImageIcon(Wallnewimg);

        PelletPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\Pellet.png");
        Image Pelletimage = PelletPic.getImage();
        Image Pelletnewimg = Pelletimage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        PelletPic = new ImageIcon(Pelletnewimg);

        PowerPelletPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\purplePowerPellet.jpg");
        Image PowerPelletimage = PowerPelletPic.getImage();
        Image PowerPelletnewimg = PowerPelletimage.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        PowerPelletPic = new ImageIcon(PowerPelletnewimg);

        Red3Pic = new ImageIcon("C:\\Users\\ryder\\Downloads\\red3.jpg");
        Image Red3image = Red3Pic.getImage();
        Image Red3newimg = Red3image.getScaledInstance(150, 250, Image.SCALE_SMOOTH);
        Red3Pic = new ImageIcon(Red3newimg);

        Red2Pic = new ImageIcon("C:\\Users\\ryder\\Downloads\\red2.jpg");
        Image Red2image = Red2Pic.getImage();
        Image Red2newimg = Red2image.getScaledInstance(150, 250, Image.SCALE_SMOOTH);
        Red2Pic = new ImageIcon(Red2newimg);

        Red1Pic = new ImageIcon("C:\\Users\\ryder\\Downloads\\RED1.jfif");
        Image Red1image = Red1Pic.getImage();
        Image Red1newimg = Red1image.getScaledInstance(150, 250, Image.SCALE_SMOOTH);
        Red1Pic = new ImageIcon(Red1newimg);

        GhostsPics = new ImageIcon[4];

        GhostsPics[0] = new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\RedGhost.png");
        Image Ghost1image = GhostsPics[0].getImage();
        Image Ghost1newimg = Ghost1image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        GhostsPics[0] = new ImageIcon(Ghost1newimg);

        GhostsPics[1] = new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\BlueGhost.png");
        Image Ghost2image = GhostsPics[1].getImage();
        Image Ghost2newimg = Ghost2image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        GhostsPics[1] = new ImageIcon(Ghost2newimg);

        GhostsPics[2] = new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\Orange Ghost.png");
        Image Ghost3image = GhostsPics[2].getImage();
        Image Ghost3newimg = Ghost3image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        GhostsPics[2] = new ImageIcon(Ghost3newimg);

        GhostsPics[3] = new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\PinkGhost.png");
        Image Ghost4image = GhostsPics[3].getImage();
        Image Ghost4newimg = Ghost4image.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        GhostsPics[3] = new ImageIcon(Ghost4newimg);

        int ID = Login(conn);


        game = new Game(conn, this);
        gameGrid = new JPanel(new GridLayout(game.getNumCol(), game.getNumRow()));
        LabelObj = new JLabel[game.getNumRow()][game.getNumCol()];
        LayeredPane = new JLayeredPane();
        for  (int j = 0; j < game.getNumCol(); j++) {
            for (int i = 0; i < game.getNumRow(); i++) {

                LabelObj[i][j] = new JLabel();
                gameGrid.add(LabelObj[i][j]);

            }
        }
        update();

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        countdown = new JLabel();
        Countdownpanel = new JPanel();
        Countdownpanel.setBackground(Color.GRAY);
        Countdownpanel.setVisible(false);
        Countdownpanel.setBounds(550, 200, 150, 250);
        Countdownpanel.add(countdown);
        LayeredPane.add(Countdownpanel,JLayeredPane.DEFAULT_LAYER);

        PacLives1 = new JLabel();
        PacManLives1 = new JPanel();
        PacManLives1.setBackground(Color.GRAY);
        PacManLives1.setVisible(false);
        PacManLives1.setBounds(20, 20, 70, 70);
        PacManLives1.add(PacLives1);
        LayeredPane.add(PacManLives1,JLayeredPane.DEFAULT_LAYER);


        PacLives2 = new JLabel();
        PacManLives2 = new JPanel();
        PacManLives2.setBackground(Color.GRAY);
        PacManLives2.setVisible(false);
        PacManLives2.setBounds(90, 20, 70, 70);
        PacManLives2.add(PacLives2);
        LayeredPane.add(PacManLives2,JLayeredPane.DEFAULT_LAYER);

        PacLives3 = new JLabel();
        PacManLives3 = new JPanel();
        PacManLives3.setBackground(Color.GRAY);
        PacManLives3.setVisible(false);
        PacManLives3.setBounds(160, 20, 70, 70);
        PacManLives3.add(PacLives3);
        LayeredPane.add(PacManLives3,JLayeredPane.DEFAULT_LAYER);

        Label = new JLabel("Test");
        Label.setText("HighScore:\n500");

        HighScoreDAO highScoreDAO = new HighScoreDAO(conn);

        JPanel HighScorepanel = new JPanel();
        JLabel HighScorelabel = new JLabel("HighScore:"+highScoreDAO.getEveryonesHighScore());
        HighScorelabel.setFont(new Font("0", Font.PLAIN, 40));
        HighScorepanel.add(HighScorelabel);
        HighScorepanel.setBounds(950,0,300,60);
        HighScorepanel.setVisible(true);

        Scorepanel = new JPanel();
        Scorelabel = new JLabel("Score:0");
        // Add the label to the panel
        Scorelabel.setFont(new Font("0", Font.PLAIN, 40));
        Scorepanel.add(Scorelabel);
        Scorepanel.setBounds(950,60,300,70);
        Scorepanel.setVisible(true);


        // Add the panel to the frame

//TODO:set window size
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        JFrame frame = new JFrame("Testing");


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameGrid.setBackground(Color.WHITE);
        gameGrid.setBounds(350, 10, 560, 650);
        LayeredPane.add(gameGrid,JLayeredPane.DEFAULT_LAYER);

        frame.add(HighScorepanel);
        frame.add(Scorepanel);

        frame.add(LayeredPane);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    game.PacMoveDown();
                    PacmanPicDecider = PacmanUpPic;

                  //  System.out.println("UP");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    game.PacMoveUp();
                    PacmanPicDecider = DownPacmanPic;
                   // System.out.println("DOWN");

                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    game.PacMoveRight();
                    PacmanPicDecider = PacmanPic;

                   // System.out.println("RIGHT");

                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    game.PacMoveLeft();
                    PacmanPicDecider = LeftPacmanPic;

                  //  System.out.println("LEFT");

                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                return;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                return;
            }


        });


        frame.setVisible(true);
        game.executeGame(ID);




    }

 public int Login(Connection conn){


         int ID = 0;
         //ask for Username
         try {
             UsernameDAO usernameDAO = new UsernameDAO(conn);
             ID = usernameDAO.login();

         } catch (InjectionAttackException IAE) {
             System.out.println(IAE.getMessage());

         }
         UserTimeDAO userTimeDAO = new UserTimeDAO(conn);

         userTimeDAO.TrackUserTime(ID);
        return ID;

     }
     public  void update(){


         for (int vert = 0; vert < game.getNumCol(); vert++) {

             for (int horz = 0; horz < game.getNumRow(); horz++) {

                 if(game.GetEachPositionGrid(horz,vert) instanceof Wall) {
                     LabelObj[horz][vert].setIcon(BlackSquarePic);
                 }
                 if(game.GetEachPositionGrid(horz,vert) instanceof Pacman) {
                     LabelObj[horz][vert].setIcon(PacmanPicDecider);
                 }

                 if(game.GetEachPositionGrid(horz,vert) instanceof PowerPellet) {
                     LabelObj[horz][vert].setIcon(PowerPelletPic);

                 }
                 else if(game.GetEachPositionGrid(horz,vert) instanceof Pellet) {
                     LabelObj[horz][vert].setIcon(PelletPic);

                 }



                 if(game.GetEachPositionGrid(horz,vert) instanceof Fruit) {
                     LabelObj[horz][vert].setIcon(CherryPic);

                 }
                 if(game.GetEachPositionGrid(horz,vert) == null) {
                     LabelObj[horz][vert].setIcon(null);

                 }
                 if(game.GetEachPositionGrid(horz,vert) instanceof Ghost) {
                     boolean GhostFound = false;
                     for (int i = 0; i < GhostsPics.length; i++) {
                         if(game.ifGhostShouldturnBlue()) {
                             GhostDecider[i]=BlueGhost1;

                         }
                         else if(!(game.ifGhostShouldturnBlue())){
                             GhostDecider[i]=GhostsPics[i];
                         }
                         if ((horz == game.GetGhostx(i)) && (vert == game.GetGhosty(i))) {
                             LabelObj[horz][vert].setIcon(GhostDecider[i]);
                             GhostFound = true;
                         }
                     }
                     if(GhostFound == false){
                         System.out.println("Ghost not found");
                     }
                 }

             }
         }
     }

    @Override
    public void startCountDown() {
         Countdownpanel.setVisible(true);
         countdown.setIcon(Red3Pic);
         try {
             TimeUnit.MILLISECONDS.sleep(1000);
         } catch (InterruptedException e) {
             throw new RuntimeException(e);
         }
        countdown.setIcon(Red2Pic);
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        countdown.setIcon(Red1Pic);
        try {
            TimeUnit.MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Countdownpanel.setVisible(false);
    }

    @Override
    public void PacLives(int lives) {
        if(lives ==0){
            PacManLives1.setBackground(Color.white);
            PacLives1.setVisible(false);
        }
        if(lives ==1){
            PacManLives1.setVisible(true);
            PacLives1.setIcon(PacmanPicLives);
            PacManLives2.setVisible(false);
            PacManLives3.setVisible(false);

        }
        else if(lives ==2){
            PacManLives1.setVisible(true);
            PacLives1.setIcon(PacmanPicLives);
            PacManLives2.setVisible(true);
            PacLives2.setIcon(PacmanPicLives);
            PacManLives3.setVisible(false);

        }
        else if(lives ==3) {
            PacManLives1.setVisible(true);
            PacLives1.setIcon(PacmanPicLives);
            PacManLives2.setVisible(true);
            PacLives2.setIcon(PacmanPicLives);
            PacManLives3.setVisible(true);
            PacLives3.setIcon(PacmanPicLives);
        }

    }

    public void UpdateScreenHighScore (int score){
        Scorelabel.setText("Score:"+score);
    }
    @Override
    public void getScore(int score){
        UpdateScreenHighScore(score);

    }




}
