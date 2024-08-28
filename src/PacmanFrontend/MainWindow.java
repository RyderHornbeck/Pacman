package PacmanFrontend;

import PacmanBackend.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MainWindow implements Updater{
    Connection conn;
    Game game;
    JPanel gameGrid;
    JLabel [][] LabelObj;
    ImageIcon CherryPic;
    ImageIcon BlackSquarePic;
    ImageIcon PowerPelletPic;
    ImageIcon PelletPic;
    ImageIcon PacmanPic;
    ImageIcon [] GhostsPics;
    public static void main(String[] args) {

        new MainWindow();

    }

    public MainWindow() {
        conn =null;
        try  {
            conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanDatabase.db");
            if (conn == null) {
                throw new SQLException("failed to establish connection");

            }
        } catch (SQLException  e) {
            System.out.println(e.getMessage());
            return;
        }

            PacmanPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\PACMANwithdarkoutline.png");
            Image image = PacmanPic.getImage();
            Image newimg =image.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            PacmanPic = new ImageIcon(newimg);

            CherryPic =new ImageIcon("C:\\Users\\ryder\\Downloads\\Cherry.png");
            Image Cherryimage = CherryPic.getImage();
            Image Cherrynewimg =Cherryimage.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            CherryPic = new ImageIcon(Cherrynewimg);

            BlackSquarePic = new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\BlackSquare.png");
            Image Wallimage = BlackSquarePic.getImage();
            Image Wallnewimg =Wallimage.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            BlackSquarePic = new ImageIcon(Wallnewimg);

            PelletPic = new ImageIcon("C:\\Users\\ryder\\Downloads\\Pellet.png");
            Image Pelletimage =PelletPic.getImage();
            Image Pelletnewimg =Pelletimage.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            PelletPic= new ImageIcon(Pelletnewimg);

            PowerPelletPic =new ImageIcon("C:\\Users\\ryder\\Downloads\\purplePowerPellet.jpg");
            Image PowerPelletimage = PowerPelletPic.getImage();
            Image PowerPelletnewimg =PowerPelletimage.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            PowerPelletPic = new ImageIcon(PowerPelletnewimg);

            GhostsPics = new ImageIcon [4];

            GhostsPics[0] = new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\RedGhost.png");
            Image Ghost1image = GhostsPics[0].getImage();
            Image Ghost1newimg =Ghost1image.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            GhostsPics[0] = new ImageIcon(Ghost1newimg);

            GhostsPics[1] =  new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\BlueGhost.png");
            Image Ghost2image = GhostsPics[1].getImage();
            Image Ghost2newimg =Ghost2image.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            GhostsPics[1] = new ImageIcon(Ghost2newimg);

            GhostsPics[2] =  new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\Orange Ghost.png");
            Image Ghost3image = GhostsPics[2].getImage();
            Image Ghost3newimg =Ghost3image.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            GhostsPics[2] = new ImageIcon(Ghost3newimg);

            GhostsPics[3] = new ImageIcon("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\PinkGhost.png");
            Image Ghost4image = GhostsPics[3].getImage();
            Image Ghost4newimg =Ghost4image.getScaledInstance(20,20, Image.SCALE_SMOOTH);
            GhostsPics[3] = new ImageIcon(Ghost4newimg);

        int ID = Login(conn);


        game = new Game(conn, this);
        gameGrid = new JPanel( new GridLayout(game.getNumCol(), game.getNumRow()));
        LabelObj = new JLabel[game.getNumRow()][game.getNumCol()];
        for(int j=0;j<game.getNumCol();j++){
            for(int i=0;i<game.getNumRow();i++){

                LabelObj[i][j]= new JLabel();
                gameGrid.add(LabelObj[i][j]);

            }
        }
        update();

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);





                frame.add(gameGrid);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.addKeyListener(new KeyListener(){
                    @Override
                    public void keyPressed(KeyEvent e) {
                        if(e.getKeyCode() == KeyEvent.VK_UP){
                            game.PacMoveUp();
                            System.out.println("UP");
                        }
                        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                            game.PacMoveDown();
                            System.out.println("DOWN");

                        }
                        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                            game.PacMoveRight();
                            System.out.println("RIGHT");

                        }
                        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                            game.PacMoveLeft();
                            System.out.println("LEFT");

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
        });
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
     public void update(){


         for (int vert = 0; vert < game.getNumCol(); vert++) {

             for (int horz = 0; horz < game.getNumRow(); horz++) {

                 if(game.GetEachPositionGrid(horz,vert) instanceof Wall) {
                     LabelObj[horz][vert].setIcon(BlackSquarePic);
                 }
                 if(game.GetEachPositionGrid(horz,vert) instanceof Pacman) {
                     LabelObj[horz][vert].setIcon(PacmanPic);
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
                 if(game.GetEachPositionGrid(horz,vert) instanceof Ghost) {
                     for (int i = 0; i < GhostsPics.length; i++) {
                         if((horz==game.GetGhostx(i)) && (vert ==game.GetGhosty(i))) {
                             LabelObj[horz][vert].setIcon(GhostsPics[i]);

                         }
                     }
                 }

             }
         }
     }
}
