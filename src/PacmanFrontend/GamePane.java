package PacmanFrontend;

import PacmanBackend.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GamePane extends JFrame {
/*
        Image CherryPic;
        Image BlackSquarePic;
        Image PowerPelletPic;
        Image PelletPic;
        Image PacmanPic;
        Image [] GhostsPics;

        public GamePane() {
            GhostsPics = new Image [4];
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }
        public void update(){
            paintComponent(getGraphics());
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
           // super.paintComponent(g);
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
*/


}
