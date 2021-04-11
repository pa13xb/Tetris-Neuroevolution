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
    private boolean showDisplay;

    Tetris(boolean showDisplay) {
        this.showDisplay = showDisplay;
        if (showDisplay) setupDisplay();
        else display = null;
        playGame();
    }//constructor

    private void setupDisplay() {
        jFrame = new JFrame("Tetris");
        jFrame.setSize(width * tileSize + 7, height * tileSize + 36);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(3);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setAlwaysOnTop(true);
        display = new Display(width, height, tileSize, null);
        jFrame.add(display);
        display.setVisible(true);
        display.repaint();
    }

    private void playGame() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                gameBoard[row][col] = 0;
            }
        }
        if (showDisplay) {
            display.setGameBoard(gameBoard);
            display.repaint();
        }
        int animationDelay = 10;
        boolean alive = true;
        boolean newBlock = true;
        long startTime = System.currentTimeMillis();
        Tetromino tetromino = null;
        while (alive) {
            long currentTime = System.currentTimeMillis();
            if(currentTime - startTime >= animationDelay){
                //System.out.println("Frame entered");
                startTime = currentTime;
                if(newBlock){
                    tetromino = getNewTetromino();
                    newBlock = false;
                    if(checkCollision(tetromino)) {
                        System.out.println("Collision");
                        alive = false;
                    }
                } else{ //moveTetromino
                    int[][] blocks = tetromino.getBlocks();
                    for (int block = 0; block < 4; block++) { //place new tetromino
                        int posX = blocks[block][0];
                        int posY = blocks[block][1];
                        gameBoard[posY][posX] = 0;
                    }
                    tetromino.changePos(tetromino.getPosX(),tetromino.getPosY() + 1);
                    if(checkCollision(tetromino)) {
                        //System.out.println("Collision");
                        tetromino.changePos(tetromino.getPosX(),tetromino.getPosY() - 1);
                        newBlock = true;
                    }
                }
                int[][] blocks = tetromino.getBlocks();
                for (int block = 0; block < 4; block++) { //place new tetromino
                    int posX = blocks[block][0];
                    int posY = blocks[block][1];
                    gameBoard[posY][posX] = tetromino.getColour();
                }

                if(showDisplay){
                    display.setGameBoard(gameBoard);
                    display.repaint();
                }
                //if(checkCollision(tetromino)) alive = false;
            }
        }
        if(showDisplay) {
            display.setGameOver(true);
            display.repaint();
        }
        System.out.println("Game over!");
    }

    private Tetromino getNewTetromino() {
        int tetrominoIndex = (int) (Math.random() * 7 + 1);
        Tetromino newTetromino;
        if (tetrominoIndex == 1) newTetromino = new CornerLeft();    //CornerLeft
        else if (tetrominoIndex == 2) newTetromino = new SquiggleRight();   //SquiggleRight
        else if (tetrominoIndex == 3) newTetromino = new SquiggleLeft();     //SquiggleLeft
        else if (tetrominoIndex == 4) newTetromino = new Square();  //Square
        else if (tetrominoIndex == 5) newTetromino = new Line();    //Line
        else if (tetrominoIndex == 6) newTetromino = new Tshaped(); //Tshaped
        else newTetromino = new CornerRight();  //CornerRight
        return newTetromino;
    }

    private boolean checkCollision(Tetromino tetromino){
        int[][] blocks = tetromino.getBlocks();
        for(int blockNum = 0; blockNum < 4; blockNum++){
            int blockCol = blocks[blockNum][0];
            int blockRow = blocks[blockNum][1];
            if(blockCol < 0 || blockCol >= width) return true; //out of bounds
            if(blockRow < 0 || blockRow >= height) return true; //out of bounds
            if(gameBoard[blockRow][blockCol] != 0) return true; //a block is there already
        }
        return false;
    }
}
