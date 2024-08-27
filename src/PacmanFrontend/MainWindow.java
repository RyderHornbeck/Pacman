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

public class MainWindow {
    Connection conn;
    Game game;
    GamePane gameGrid;


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
        int ID = Login(conn);
        game = new Game(conn);
        gameGrid = new GamePane();



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
                        }
                        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                            game.PacMoveDown();
                        }
                        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                            game.PacMoveRight();
                        }
                        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
                            game.PacMoveLeft();
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
    public class GamePane extends JPanel {
        Image CherryPic;
        Image BlackSquarePic;
        Image PowerPelletPic;
        Image PelletPic;
        Image PacmanPic;
        Image [] GhostsPics;

        public GamePane() {
            GhostsPics = new Image [game.GetGhostxLength().length];
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        protected void paintComponent(Graphics g) {
            try {
                PacmanPic = ImageIO.read(new File("C:\\Users\\ryder\\Downloads\\PACMANwithdarkoutline.png"));
                CherryPic = ImageIO.read(new File("C:\\Users\\ryder\\Downloads\\Cherry.png"));
               BlackSquarePic = ImageIO.read(new File("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\BlackSquare.png"));
                PelletPic =ImageIO.read(new File("C:\\Users\\ryder\\Downloads\\Pellet.png"));
                PowerPelletPic=ImageIO.read(new File("C:\\Users\\ryder\\Downloads\\purplePowerPellet.jpg"));

                GhostsPics[0]=ImageIO.read(new File("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\RedGhost.png"));
                GhostsPics[1]=ImageIO.read(new File("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\BlueGhost.png"));
                GhostsPics[2]=ImageIO.read(new File("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\Orange Ghost.png"));
                GhostsPics[3] =ImageIO.read(new File("C:\\Users\\ryder\\IdeaProjects\\Pacman\\src\\PacmanFrontend\\PinkGhost.png"));

            }
            catch(IOException IOE){
                System.out.print(IOE.getMessage());
            }
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            int size = Math.min(getWidth() - 4, getHeight() - 4) / 30;
            int width = getWidth() - (size *2);
            int height = getHeight() - (size *2);

            int y = (getHeight() - (size * 30)) / 2;
            for (int vert = 0; vert < game.getNumCol(); vert++) {
                int x = (getWidth() - (size * 30)) /2;
                for (int horz = 0; horz < game.getNumRow(); horz++) {
                    g.drawRect(x, y, size, size);

                   if(game.GetEachPositionGrid(horz,vert) instanceof Wall) {
                      g.drawImage(BlackSquarePic, x, y, size, size, this);
                  }
                   if(game.GetEachPositionGrid(horz,vert) instanceof Pacman) {
                           g.drawImage(PacmanPic, x, y, size, size, this);
                   }

                    if(game.GetEachPositionGrid(horz,vert) instanceof PowerPellet) {
                        g.drawImage(PowerPelletPic, x, y,size,size, this);
                    }
                    else if(game.GetEachPositionGrid(horz,vert) instanceof Pellet) {
                        g.drawImage(PelletPic, x, y, size, size, this);
                    }

                    if(game.GetEachPositionGrid(horz,vert) instanceof Fruit) {
                        g.drawImage(CherryPic, x, y, size, size, this);
                    }
                    //TODO: play around with for loop, grab from default map where they initialize the ghost
                    if(game.GetEachPositionGrid(horz,vert) instanceof Ghost) {
                        for (int i = 0; i < GhostsPics.length; i++) {
                            if((horz==game.GetGhostx(i)) && (vert ==game.GetGhosty(i))) {
                                g.drawImage(GhostsPics[i], x, y, size, size, this);
                            }
                        }
                    }
                    //Todo:fix database inputs, make Ghost getters,use breakpoints to debug why pellets and others dont show up
                    x += size;
                }
                y += size;
            }
            g2d.dispose();
        }

    }
}
