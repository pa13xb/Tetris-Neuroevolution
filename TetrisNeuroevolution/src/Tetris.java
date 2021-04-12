import javax.rmi.ssl.SslRMIClientSocketFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Tetris {

    private int width = 10;
    private int height = 20;
    private int tileSize = 30;
    private int gameBoard[][] = new int[height][width]; //0: empty, otherwise int represents colour
    private Display display;
    private JFrame jFrame = new JFrame();
    private Tetromino tetromino = null;
    private boolean showDisplay;
    private boolean newBlock;
    private boolean downArrow = false;
    private boolean gameOver;
    private boolean quit;
    private int animationDelay = 300;
    private int originalAnimationDelay = animationDelay;
    private int newBlockDelay = 0;
    private int score = 0;

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
        jFrame.addKeyListener(addKeyboardListener());
    }

    private KeyListener addKeyboardListener(){
        KeyListener keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                /*if(keyCode == 10){ //enter key
                    System.out.println("Check");
                    display.reset();
                    playGame();
                }//enter key*/
                if(keyCode == 27){ //escape key
                    quit = true;
                }//escape key
                if(keyCode == 39){ //right key
                    if(tetromino != null) {
                        int[][] blocks = tetromino.getBlocks();
                        for (int block = 0; block < 4; block++) { //delete tetromino from board
                            int posX = blocks[block][0];
                            int posY = blocks[block][1];
                            gameBoard[posY][posX] = 0;
                        }
                        tetromino.changePos(tetromino.getPosX() + 1, tetromino.getPosY());
                        if (checkCollision(tetromino)) { //can't go to the right
                            tetromino.changePos(tetromino.getPosX() - 1, tetromino.getPosY());
                        }
                        for (int block = 0; block < 4; block++) { //place new tetromino
                            int posX = blocks[block][0];
                            int posY = blocks[block][1];
                            gameBoard[posY][posX] = tetromino.getColour();
                        }
                        display.setGameBoard(gameBoard);
                        display.repaint();
                    }
                }//right key
                else if(keyCode == 37) { //left key
                    if(tetromino != null) {
                        int[][] blocks = tetromino.getBlocks();
                        for (int block = 0; block < 4; block++) { //delete tetromino from board
                            int posX = blocks[block][0];
                            int posY = blocks[block][1];
                            gameBoard[posY][posX] = 0;
                        }
                        tetromino.changePos(tetromino.getPosX() - 1, tetromino.getPosY());
                        if (checkCollision(tetromino)) { //can't go to the right
                            tetromino.changePos(tetromino.getPosX() + 1, tetromino.getPosY());
                        }
                        for (int block = 0; block < 4; block++) { //place new tetromino
                            int posX = blocks[block][0];
                            int posY = blocks[block][1];
                            gameBoard[posY][posX] = tetromino.getColour();
                        }
                        display.setGameBoard(gameBoard);
                        display.repaint();
                    }
                }//left key
                else if(keyCode == 38) { //up key (for rotate)
                    if(tetromino != null) {
                        int[][] blocks = tetromino.getBlocks();
                        for (int block = 0; block < 4; block++) { //delete tetromino from board
                            int posX = blocks[block][0];
                            int posY = blocks[block][1];
                            gameBoard[posY][posX] = 0;
                        }
                        blocks = tetromino.rotate(); //the new orientation
                        if (checkCollision(tetromino)) { //can't rotate
                            tetromino.rotate();
                            tetromino.rotate();
                            blocks = tetromino.rotate(); //the original orientation
                        }
                        for (int block = 0; block < 4; block++) { //place new tetromino
                            int posX = blocks[block][0];
                            int posY = blocks[block][1];
                            gameBoard[posY][posX] = tetromino.getColour();
                        }
                        display.setGameBoard(gameBoard);
                        display.repaint();
                    }
                }//up key (for rotate)
                else if(keyCode == 40) { //down key (increases animation speed)
                    if(tetromino != null) {
                        animationDelay = originalAnimationDelay / 5;
                        newBlockDelay = 500;
                        downArrow = true;
                    }
                } //down key (increases animation speed)
                else if(keyCode == 32) { //spacebar key
                    if(tetromino != null) {
                        int[][] blocks = tetromino.getBlocks();
                        for (int block = 0; block < 4; block++) { //delete tetromino
                            int posX = blocks[block][0];
                            int posY = blocks[block][1];
                            gameBoard[posY][posX] = 0;
                        }
                        boolean collision = false;
                        while(!collision) {
                            tetromino.changePos(tetromino.getPosX(), tetromino.getPosY() + 1);
                            if (checkCollision(tetromino)) {
                                tetromino.changePos(tetromino.getPosX(), tetromino.getPosY() - 1);
                                collision = true;
                            }
                        }
                        for (int block = 0; block < 4; block++) { //place new tetromino
                            int posX = blocks[block][0];
                            int posY = blocks[block][1];
                            gameBoard[posY][posX] = tetromino.getColour();
                        }
                        display.setGameBoard(gameBoard);
                        display.repaint();
                        int numRowsCleared = 0;
                        for(int row = 0; row < height; row++) {
                            if(checkFullLine(row)) numRowsCleared++; //check for full lines to delete
                        }
                        if(numRowsCleared == 1) score += 40;
                        if(numRowsCleared == 2) score += 100;
                        if(numRowsCleared == 3) score += 300;
                        if(numRowsCleared == 4) score += 1200;
                        newBlock = true;
                    }
                }//spacebar key
            }
            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if(keyCode == 40) { //down key
                    animationDelay = originalAnimationDelay;
                    downArrow = false;
                    newBlockDelay = 0;
                }
            }
        };
        return keyListener;
    }//addKeyboardListener

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
        score = 0;
        gameOver = false;
        newBlock = true;
        long startTime = System.currentTimeMillis();
        while (!gameOver && !quit) {
            long currentTime = System.currentTimeMillis();
            boolean nextFrame = false;
            if (newBlock && currentTime - startTime > newBlockDelay) {
                nextFrame = true;
                if (!downArrow) newBlockDelay = 0;
            }
            if (newBlockDelay == 0 && newBlock) nextFrame = true;
            if (!newBlock) {
                if (currentTime - startTime >= animationDelay) nextFrame = true;
                else nextFrame = false;
            }
            if (nextFrame) {
                startTime = currentTime;
                boolean collision = false;
                if (newBlock) {
                    tetromino = getNewTetromino();
                    newBlock = false;
                    if (checkCollision(tetromino)) {
                        System.out.println("Collision");
                        gameOver = true;
                    }
                } else { //moveTetromino
                    int[][] blocks = tetromino.getBlocks();
                    for (int block = 0; block < 4; block++) { //delete tetromino
                        int posX = blocks[block][0];
                        int posY = blocks[block][1];
                        gameBoard[posY][posX] = 0;
                    }
                    tetromino.changePos(tetromino.getPosX(), tetromino.getPosY() + 1);
                    if (checkCollision(tetromino)) {
                        collision = true;
                        tetromino.changePos(tetromino.getPosX(), tetromino.getPosY() - 1);
                        newBlock = true;
                    }
                }
                int[][] blocks = tetromino.getBlocks();
                for (int block = 0; block < 4; block++) { //place new tetromino
                    int posX = blocks[block][0];
                    int posY = blocks[block][1];
                    gameBoard[posY][posX] = tetromino.getColour();
                }

                if (collision) {
                    int numRowsCleared = 0;
                    for (int row = 0; row < height; row++) {
                        if (checkFullLine(row)) numRowsCleared++; //check for full lines to delete
                    }
                    if (numRowsCleared == 1) score += 40;
                    if (numRowsCleared == 2) score += 100;
                    if (numRowsCleared == 3) score += 300;
                    if (numRowsCleared == 4) score += 1200;
                }

                if (showDisplay) {
                    display.setGameBoard(gameBoard);
                    display.repaint();
                }
                //if(checkCollision(tetromino)) alive = false;
            }
        }
        if (showDisplay) {
            display.gameOver(score);
            display.repaint();
        }
        System.out.println("Game over!");
    }//playGame

    public int getScore(){
        return score;
    }//getScore

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
    }//getNewTetromino

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
    }//checkCollision

    private boolean checkFullLine(int checkRow){
        for(int col = 0; col < width; col++){
            if(gameBoard[checkRow][col] == 0) return false;
        }
        for(int col = 0; col < width; col++){
            gameBoard[checkRow][col] = 0;
        }
        for(int row = checkRow; row > 0; row--) {
            for (int col = 0; col < width; col++) {
                gameBoard[row][col] = gameBoard[row - 1][col];
            }
        }
        display.setGameBoard(gameBoard);
        display.repaint();
        return true;
    }//checkFullLine
}
