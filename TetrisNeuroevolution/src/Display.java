import javax.swing.*;
import java.awt.*;

class Display extends JPanel {

    private int tileSize;
    private int[][] gameBoard;
    private int width;
    private int height;

    Display(int width, int height, int tileSize, int[][] gameBoard){
        this.tileSize = tileSize;
        this.width = width;
        this.height = height;
        setSize(width*tileSize+1,height*tileSize+1);
        setBounds(0,0,width*tileSize+1,height*tileSize+1);
        this.gameBoard = gameBoard;
    }//Display Constructor

    /**This is the function called automatically when Swing paints the window
     *
     * @param graphics the default graphics item
     */
    public void paint(Graphics graphics){
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                //Color c = new Color(255,125,125);
                int colorIndex = gameBoard[row][col];
                Color c = null;
                if(colorIndex == 0) c = Color.black;
                else if(colorIndex == 1) c = Color.blue;
                else if(colorIndex == 2) c = Color.green;
                else if(colorIndex == 3) c = Color.red;
                else if(colorIndex == 4) c = Color.yellow;
                else if(colorIndex == 5) c = Color.cyan;
                else if(colorIndex == 6) c = Color.magenta;
                else if(colorIndex == 7) c = Color.pink;
                graphics.setColor(c);
                graphics.fillRect(col*tileSize,row*tileSize, tileSize, tileSize);
                graphics.drawRect(col*tileSize,row*tileSize, tileSize, tileSize);
            }
        }
    }//paint

    public void setGameBoard(int[][] gameBoard){
        this.gameBoard = gameBoard;
    }
}
