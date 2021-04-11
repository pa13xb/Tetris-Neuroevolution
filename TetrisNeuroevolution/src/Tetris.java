import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Tetris {

    int width = 10;
    int height = 20;
    int tileSize = 30;
    int gameBoard[][] = new int[height][width]; //0: empty, otherwise int represents colour
    Display display;
    JFrame jFrame = new JFrame();

    Tetris(boolean showDisplay){
        if(showDisplay) setupDisplay();
        else display = null;
    }//constructor

    private void setupDisplay(){
        jFrame = new JFrame("Tetris");
        jFrame.setSize(width*tileSize+6,height*tileSize+35);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(3);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setAlwaysOnTop(true);
        Display display = new Display(width, height, tileSize, null);
        jFrame.add(display);
        display.setVisible(true);
        display.repaint();
        int gameBoard[][];
        gameBoard = new int[height][width];
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                gameBoard[row][col] = (int)(Math.random() * 8.0);
            }
        }
        display.setGameBoard(gameBoard);
        display.repaint();
    }

    
}
