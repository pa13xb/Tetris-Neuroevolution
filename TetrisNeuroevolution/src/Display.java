import javax.swing.*;
import java.awt.*;

/**
 * This is the Display class which extends JPanel and performs the Tetris game display.
 *
 * COSC 4P80 Final Project, Brock University
 * April 16, 2021
 * @author Philip Akkerman, 5479613, pa13xb@brocku.ca
 * @author David Hasler, 6041321, dh15pd@brocku.ca
 */
class Display extends JPanel {

    private int tileSize;
    private int[][] gameBoard;
    private int width;
    private int height;
    private boolean gameOver = false;
    private int score = 0;
    private int timeSurvived = 0;

    /**
     * Constructor which initialized the Display JPanel
     * @param width the width in tiles
     * @param height the height in tiles
     * @param tileSize the size of each time
     * @param gameBoard the gameBoard, a 2D integer array of tile colour indexes
     */
    Display(int width, int height, int tileSize, int[][] gameBoard){
        this.tileSize = tileSize;
        this.width = width;
        this.height = height-2;
        setSize(width*tileSize+1,this.height*tileSize+1);
        setBounds(0,0,width*tileSize+1,this.height*tileSize+1);
        this.gameBoard = gameBoard;
    }//Display Constructor

    /**This is the function called automatically when Swing paints the window
     * It is called from the repaint() function
     *
     * @param graphics the default graphics item
     */
    public void paint(Graphics graphics){
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                int colorIndex = 0;
                if(gameBoard != null){
                    colorIndex = gameBoard[row+2][col];
                }
                Color c;
                if(colorIndex == 0) c = Color.black; //empty space
                else if(colorIndex == 1) c = new Color(167, 0, 255); //CornerLeft
                else if(colorIndex == 2) c = Color.green;   //SquiggleRight
                else if(colorIndex == 3) c = Color.red;     //SquiggleLeft
                else if(colorIndex == 4) c = Color.yellow;  //Square
                else if(colorIndex == 5) c = Color.cyan;    //Line
                else if(colorIndex == 6) c = Color.magenta; //Tshaped
                else c = Color.orange;  //CornerRight
                graphics.setColor(c);
                graphics.fillRect(col*tileSize,row*tileSize, tileSize, tileSize);
                graphics.drawRect(col*tileSize,row*tileSize, tileSize, tileSize);
                graphics.setColor(Color.white);
                graphics.drawRect(col*tileSize,row*tileSize, tileSize, tileSize);
            }
        }
        if(gameOver){
            graphics.setColor(Color.black);
            graphics.fillRect(10,(height / 2) * tileSize - 45, width*tileSize - 20, tileSize * 6);
            graphics.setColor(Color.white);
            graphics.drawRect(10,(height / 2) * tileSize - 45, width*tileSize - 20, tileSize * 6);
            graphics.setFont(new Font("Arial", Font.PLAIN, 40));
            graphics.drawString("Game Over!", 45, (height / 2) * tileSize);
            graphics.setFont(new Font("Arial", Font.PLAIN, 30));
            graphics.drawString("Score = "+score, 20, ((height / 2) * tileSize) + tileSize * 2);
            graphics.setFont(new Font("Arial", Font.PLAIN, 22));
            graphics.drawString("Time Survived = "+timeSurvived, 20, ((height / 2) * tileSize) + tileSize * 4);
        }
    }//paint

    /**
     * Sets the game over state and populates the related variables
     * @param score the score to display
     * @param timeSurvived the time survived to display
     */
    public void gameOver(int score, int timeSurvived){
        this.gameOver = true;
        this.score = score;
        this.timeSurvived = timeSurvived;
    }//gameOver

    /**
     * Setter for gameBoard
     * @param gameBoard the integer array of colour indexes, represented by the gameBoard
     */
    public void setGameBoard(int[][] gameBoard){
        this.gameBoard = gameBoard;
    }
}
