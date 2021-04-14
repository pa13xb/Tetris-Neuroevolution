import javax.swing.*;
import java.awt.*;

class Display extends JPanel {

    private int tileSize;
    private int[][] gameBoard;
    private int width;
    private int height;
    private boolean gameOver = false;
    private int score = 0;
    private int timeSurvived = 0;

    Display(int width, int height, int tileSize, int[][] gameBoard){
        this.tileSize = tileSize;
        this.width = width;
        this.height = height-2;
        setSize(width*tileSize+1,this.height*tileSize+1);
        setBounds(0,0,width*tileSize+1,this.height*tileSize+1);
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
                int colorIndex = 0;
                if(gameBoard != null){
                    colorIndex = gameBoard[row+2][col];
                }
                Color c = null;
                if(colorIndex == 0) c = Color.black;
                else if(colorIndex == 1) c = new Color(167, 0, 255); //CornerLeft
                else if(colorIndex == 2) c = Color.green;   //SquiggleRight
                else if(colorIndex == 3) c = Color.red;     //SquiggleLeft
                else if(colorIndex == 4) c = Color.yellow;  //Square
                else if(colorIndex == 5) c = Color.cyan;    //Line
                else if(colorIndex == 6) c = Color.magenta; //Tshaped
                else if(colorIndex == 7) c = Color.orange;  //CornerRight
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

    public void gameOver(int score, int timeSurvived){
        this.gameOver = true;
        this.score = score;
        this.timeSurvived = timeSurvived;
    }

    public void reset(){
        score = 0;
        gameOver = false;
    }

    public void setGameBoard(int[][] gameBoard){
        this.gameBoard = gameBoard;
    }
}
