import javax.swing.*;
import java.awt.event.*;

class Tetris {

    private int width = 10;
    private int height = 22;
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
    private int timeSurvived = 0;
    private boolean controlArrows;

    Tetris(boolean showDisplay, boolean humanPlayer, Neuroevolution neuroevolution, boolean tetrominoPosInput, boolean controlArrows, boolean supervisedAI) {
        this.showDisplay = showDisplay;
        if (showDisplay) setupDisplay();
        else display = null;
        this.controlArrows = controlArrows;
        if(!supervisedAI) {
            if (humanPlayer) humanPlayGame();
            else AIPlayGame(neuroevolution, tetrominoPosInput);
        }
    }//constructor

    private void setupDisplay() {
        jFrame = new JFrame("Tetris");
        jFrame.setSize(width * tileSize + 7, (height - 2) * tileSize + 36);
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(3);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
        jFrame.setAlwaysOnTop(true);
        display = new Display(width, (height), tileSize, null);
        jFrame.add(display);
        display.setVisible(true);
        display.repaint();
        jFrame.addKeyListener(makeKeyboardListener());
    }//setupDisplay

    /**
     * The keyboard listener functions to enable user control when playing using arrow keys and spacebar
     *
     * @return KeyListener the keyboard listener function to attach to a Jframe object
     */
    private KeyListener makeKeyboardListener() {
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
                }*///enter key
                if (keyCode == 27) { //escape key
                    quit = true;
                    close();
                }//escape key
                else if (keyCode == 39) { //right key
                    if (tetromino != null) {
                        moveRight();
                        display.setGameBoard(gameBoard);
                        display.repaint();
                    }
                }//right key
                else if (keyCode == 37) { //left key
                    if (tetromino != null) {
                        moveLeft();
                        display.setGameBoard(gameBoard);
                        display.repaint();
                    }
                }//left key
                else if (keyCode == 38) { //up key (for rotate)
                    if (tetromino != null) {
                        rotate();
                        display.setGameBoard(gameBoard);
                        display.repaint();
                    }
                }//up key (for rotate)
                else if (keyCode == 40) { //down key (increases animation speed)
                    if (tetromino != null) {
                        animationDelay = originalAnimationDelay / 5;
                        newBlockDelay = 500;
                        downArrow = true;
                    }
                } //down key (increases animation speed)
                else if (keyCode == 32) { //spacebar key
                    if (tetromino != null) {
                        moveSpaceBar();
                        display.setGameBoard(gameBoard);
                        display.repaint();
                        int numRowsCleared = 0;
                        for (int row = 0; row < height; row++) {
                            if (checkFullLine(row)) numRowsCleared++; //check for full lines to delete
                        }
                        if (numRowsCleared == 1) score += 40;
                        if (numRowsCleared == 2) score += 100;
                        if (numRowsCleared == 3) score += 300;
                        if (numRowsCleared == 4) score += 1200;
                        newBlock = true;
                    }
                }//spacebar key
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == 40) { //down key
                    animationDelay = originalAnimationDelay;
                    downArrow = false;
                    newBlockDelay = 0;
                }
            }
        };
        return keyListener;
    }//addKeyboardListener

    private void moveRight() {
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
    }

    private void moveLeft() {
        int[][] blocks = tetromino.getBlocks();
        for (int block = 0; block < 4; block++) { //delete tetromino from board
            int posX = blocks[block][0];
            int posY = blocks[block][1];
            gameBoard[posY][posX] = 0;
        }
        tetromino.changePos(tetromino.getPosX() - 1, tetromino.getPosY());
        if (checkCollision(tetromino)) { //can't go to the left
            tetromino.changePos(tetromino.getPosX() + 1, tetromino.getPosY());
        }
        for (int block = 0; block < 4; block++) { //place new tetromino
            int posX = blocks[block][0];
            int posY = blocks[block][1];
            gameBoard[posY][posX] = tetromino.getColour();
        }
    }

    private void rotate() {
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
    }

    private void moveSpaceBar() {
        int[][] blocks = tetromino.getBlocks();
        for (int block = 0; block < 4; block++) { //delete tetromino
            int posX = blocks[block][0];
            int posY = blocks[block][1];
            gameBoard[posY][posX] = 0;
        }
        boolean collision = false;
        while (!collision) {
            tetromino.changePos(tetromino.getPosX(), tetromino.getPosY() + 1);
            timeSurvived++;
            if (checkCollision(tetromino)) {
                tetromino.changePos(tetromino.getPosX(), tetromino.getPosY() - 1);
                timeSurvived--;
                collision = true;
            }
        }
        for (int block = 0; block < 4; block++) { //place new tetromino
            int posX = blocks[block][0];
            int posY = blocks[block][1];
            gameBoard[posY][posX] = tetromino.getColour();
        }
    }

    /**
     * The main game-play loop
     */
    private void humanPlayGame() {
        score = 0;
        gameOver = false;
        newBlock = true;
        quit = false;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                gameBoard[row][col] = 0;
            }
        }
        display.setGameBoard(gameBoard);
        display.repaint();
        long startTime = System.currentTimeMillis();
        while (!gameOver && !quit) {
            long currentTime = System.currentTimeMillis();
            boolean nextFrame;
            nextFrame = false; //next bunch of lines control if the next frame is ready
            if (newBlock && currentTime - startTime > newBlockDelay) {
                nextFrame = true;
                if (!downArrow) newBlockDelay = 0;
            }
            if (newBlockDelay == 0 && newBlock) nextFrame = true;
            if (!newBlock) {
                if (currentTime - startTime >= animationDelay) nextFrame = true;
            }
            if (nextFrame) {
                timeSurvived++;
                //System.out.println(timeSurvived);
                startTime = currentTime;
                playTurn();
            }
        }
        display.gameOver(score, timeSurvived);
        display.repaint();
    }//humanPlayGame

    private void AIPlayGame(Neuroevolution neuroevolution, boolean tetrominoPosInput) {
        score = 0;
        gameOver = false;
        newBlock = true;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                gameBoard[row][col] = 0;
            }
        }
        long startTime = System.currentTimeMillis();
        while (!gameOver) {
            boolean nextFrame = false;
            if (showDisplay) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime > animationDelay / 4) {
                    nextFrame = true;
                    startTime = System.currentTimeMillis();
                }
            } else nextFrame = true;
            if (nextFrame) {
                timeSurvived++;
                if (showDisplay) {
                    display.setGameBoard(gameBoard);
                    display.repaint();
                }
                playTurn();
                //Get the input array for the AI:
                /*if(wholeBoard){

                }
                else{ //topography

                }*/
                if (tetromino != null && !gameOver) { //need to wait for a new tetramino to be placed
                    int[] inputArray = new int[4 + 7 + width * height * 2];
                    int index = 0;
                    int[] rotation = {0, 0, 0, 0};
                    rotation[tetromino.getRotation()] = 1;
                    for (int i = 0; i < 4; i++) {
                        inputArray[index] = rotation[i];
                        index++;
                    }
                    int[] type = {0, 0, 0, 0, 0, 0, 0};
                    type[tetromino.getColour() - 1] = 1; //-1 since colour indexes go from 1 to 7
                    for (int i = 0; i < 7; i++) {
                        inputArray[index] = type[i];
                        index++;
                    }
                    for (int row = 0; row < height; row++) {
                        for (int col = 0; col < width; col++) {
                            if (gameBoard[row][col] == 0) inputArray[index] = 0;
                            else inputArray[index] = 1;
                            index++;
                        }
                    }
                    int[][] blockLocations = new int[height][width];
                    for (int row = 0; row < height; row++) {
                        for (int col = 0; col < width; col++) {
                            blockLocations[row][col] = 0;
                        }
                    }
                    int[][] blocks = tetromino.getBlocks();
                    for (int[] block : blocks) {
                        int blockCol = block[0];
                        int blockRow = block[1];
                        blockLocations[blockRow][blockCol] = 1;
                    }
                    for (int row = 0; row < height; row++) {
                        for (int col = 0; col < width; col++) {
                            inputArray[index] = blockLocations[row][col];
                            index++;
                        }
                    }
                    int move = neuroevolution.calculate(inputArray); //use the input array to calculate a move
                    if (controlArrows) { //4 outputs
                        if (move == 0) moveRight();
                        else if (move == 1) moveLeft();
                        else if (move == 2) rotate();
                        else moveSpaceBar();
                    } else { //40 outputs, 4 rotations per column, 10 columns total
                        makeAIMove(move);
                    }
                }
            }
        }
        if (showDisplay) {
            display.gameOver(score, timeSurvived);
            display.repaint();
        }
    }//AIPlayGame

    private void makeAIMove(int move){
        int blockRotation = move % 4;
        for (int r = 0; r < blockRotation; r++) {
            rotate();
        }
        int column = move / 4;
        int moveAmount = 4 - column;
        if (moveAmount >= 0) { //we're going left
            for (int i = 0; i < moveAmount; i++) moveLeft();
        } else { //we're going right
            for (int i = 0; i < -moveAmount; i++) moveRight();
        }
        moveSpaceBar();
    }//makeAIMove

    public void AISetupGameBoard() {
        score = 0;
        timeSurvived = 0;
        gameOver = false;
        newBlock = true;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                gameBoard[row][col] = 0;
            }
        }
    }//AISetupGameBoard

    public double AIPlaySupervisedMove(Neuroevolution neuroevolution) {
        timeSurvived++;
        playTurn();
        if (tetromino != null && !gameOver) { //need to wait for a new tetramino to be placed
            int[] inputArray = new int[4 + 7 + width * height * 2];
            int index = 0;
            int[] rotation = {0, 0, 0, 0};
            rotation[tetromino.getRotation()] = 1;
            for (int i = 0; i < 4; i++) {
                inputArray[index] = rotation[i];
                index++;
            }
            int[] type = {0, 0, 0, 0, 0, 0, 0};
            type[tetromino.getColour() - 1] = 1; //-1 since colour indexes go from 1 to 7
            for (int i = 0; i < 7; i++) {
                inputArray[index] = type[i];
                index++;
            }
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (gameBoard[row][col] == 0) inputArray[index] = 0;
                    else inputArray[index] = 1;
                    index++;
                }
            }
            int[][] blockLocations = new int[height][width];
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    blockLocations[row][col] = 0;
                }
            }
            int[][] blocks = tetromino.getBlocks();
            for (int[] block : blocks) {
                int blockCol = block[0];
                int blockRow = block[1];
                blockLocations[blockRow][blockCol] = 1;
            }
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    inputArray[index] = blockLocations[row][col];
                    index++;
                }
            }
            int move = neuroevolution.calculate(inputArray); //use the input array to calculate a move
            double score = calculateMoveScore(move);
            int optimalMove = findOptimalMove();
            double optimalScore = calculateMoveScore(optimalMove);
            {//perform a move
                if (controlArrows) { //4 outputs
                    if (optimalMove == 0) moveRight();
                    else if (optimalMove == 1) moveLeft();
                    else if (optimalMove == 2) rotate();
                    else moveSpaceBar();
                } else { //40 outputs, 4 rotations per column, 10 columns total
                    makeAIMove(optimalMove);
                }
            }//perform a move
            return optimalScore - score; //error
        }
        return -1;
    }//AIPlaySupervisedMove

    private void playTurn() {
        boolean collision = false;
        if (newBlock) {
            tetromino = getNewTetromino();
            newBlock = false;
            if (checkCollision(tetromino)) {
                gameOver = true;
            }
        } else { //lower the Tetromino
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
            tetromino = null;
        }

        if (showDisplay) {
            display.setGameBoard(gameBoard);
            display.repaint();
        }
        //if(checkCollision(tetromino)) alive = false;
    }

    /**
     * Getter for score
     *
     * @return the score
     */
    int getScore() {
        return score;
    }//getScore
    
    boolean getGameOver(){
        return gameOver;
    }

    private int findOptimalMove(){
        double maxScore = 0.0;
        int moveIndex = -0;
        for(int i = 0; i < 40; i++) {
            double score = calculateMoveScore(i);
            if(score > maxScore) {
                maxScore = score;
                moveIndex = i;
            }
        }
        return moveIndex;
    }//findOptimalMove

    private double calculateMoveScore(int move) {
        int holes, blocks, weightedBlocks, rowTransitions,removedLines,connectedHoles,columnTransitions;
        int altitudeDifference,maximumWellDepth,sumWellDepths,pileHeight,landingHeight;
        //save the previous board state and global variables:
        Tetromino prevTetromino = tetromino;
        int prevX = tetromino.getPosX();
        int prevY = tetromino.getPosY();
        int prevRotation = tetromino.getRotation();
        int prevScore = score;
        boolean savedNewBlockState = newBlock;
        int[][] savedGameBoard = new int[height][width];
        for(int row = 0; row < height; row++){
            for(int col = 0; col < width; col++){
                savedGameBoard[row][col] = gameBoard[row][col];
            }
        }//finished saving previous board state and global variables
        makeAIMove(move); //record the move
        landingHeight = height - tetromino.getPosY();
        playTurn(); //perform game logic on the move
        //evaluate the board position after the move:
        pileHeight = 0;
        for (int row = 2; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (gameBoard[row][col] != 0) {
                    pileHeight = height - row;
                    row = height;
                    col = width;
                }
            }
        }
        holes = 0;
        int startingRow = height - pileHeight;
        boolean above = false;
        blocks = 0;
        weightedBlocks = 0;
        boolean occupied = true;
        rowTransitions = 0;
        for (int row = startingRow; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (gameBoard[row][col] == 0) {
                    holes++;
                    if(occupied){
                        occupied = false;
                        rowTransitions++;
                    }
                }
                else {
                    blocks++;
                    weightedBlocks += (height - row);
                    if(!occupied){
                        occupied = true;
                        rowTransitions++;
                    }
                }
            }
            if(!occupied){
                occupied = true;
                rowTransitions++;
            }
        }
        connectedHoles = 0;
        boolean connectedHole = false;
        occupied = false;
        columnTransitions = 0;
        for (int col = 0; col < width; col++) {
            for (int row = startingRow; row < height; row++) {
                if (gameBoard[row][col] == 0){
                    if(!connectedHole) connectedHole = true;
                    if(occupied){
                        occupied = false;
                        columnTransitions++;
                    }
                }
                if (gameBoard[row][col] != 0){
                    if(connectedHole) {
                        connectedHole = false;
                        connectedHoles++;
                    }
                    if(!occupied){
                        occupied = true;
                        columnTransitions++;
                    }
                }

            }
            if(!occupied){
                occupied = true;
                columnTransitions++;
            }
        }
        removedLines = 0;
        if(score - prevScore == 40) removedLines = 1;
        else if(score - prevScore == 100) removedLines = 2;
        else if(score - prevScore == 300) removedLines = 3;
        else if(score - prevScore == 1200) removedLines = 4;
        altitudeDifference = 0;
        for (int col = 0; col < width; col++) {
            int colAltitude = 0;
            for (int row = (height - pileHeight); row < height; row++) {
                if (gameBoard[row][col] == 0) {
                    colAltitude++;
                }
                if (gameBoard[row][col] != 0) {
                    break;
                }
            }
            if(colAltitude > altitudeDifference) altitudeDifference = colAltitude;
        }
        maximumWellDepth = 0;
        int prev = 0;
        int[] columnHeights = new int[width];
        for (int col = 0; col < width; col++) {
            for (int row = (height - pileHeight); row < height; row++) {
                if (gameBoard[row][col] != 0) {
                    columnHeights[col] = height - row;
                    break;
                }
            }
        }
        sumWellDepths = 0;
        for(int col = 0; col < width; col++){
            int prevHeight = height;
            int nextHeight = height;
            int currentHeight = columnHeights[col];
            int wellDepth = 0;
            if(col > 0) prevHeight = columnHeights[col - 1];
            if(col < width - 1) nextHeight = columnHeights[col + 1];
            if(currentHeight < prevHeight && currentHeight < nextHeight){
                if(prevHeight < nextHeight) wellDepth = prevHeight - currentHeight;
                else wellDepth = nextHeight - currentHeight;
                if(maximumWellDepth < wellDepth) maximumWellDepth = wellDepth;
                sumWellDepths += wellDepth;
            }
        }
        //evaluation finished
        gameBoard = savedGameBoard;
        score = prevScore;
        newBlock = savedNewBlockState;
        tetromino = prevTetromino;
        tetromino.changePos(prevX, prevY);
        for(int rotNum = 0; rotNum < tetromino.getRotation() - prevRotation; rotNum++){
            tetromino.rotate();
        }
        double result = 0;
        //holes, blocks, weightedBlocks, rowTransitions, removedLines, connectedHoles, columnTransitions;
        //altitudeDifference, maximumWellDepth, sumWellDepths, pileHeight, landingHeight;
        result += holes;
        result += weightedBlocks;
        result += rowTransitions;
        result += removedLines;
        result += connectedHoles;
        result += columnTransitions;
        result += altitudeDifference;
        result += maximumWellDepth;
        result += sumWellDepths;
        result += pileHeight;
        result += landingHeight;
        return result;
    }//calculateMoveScore

    /**
     * Getter for time survived
     *
     * @return the number of frames survived
     */
    public int getTimeSurvived() {
        return timeSurvived;
    }//getTimeSurvived

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
    
    /**
     * Checks for a collision of blocks, returns true if a collision occurred
     *
     * @param tetromino the Tetromino to compare to the gameBoard
     * @return boolean representing if a collision was found
     */
    private boolean checkCollision(Tetromino tetromino) {
        int[][] blocks = tetromino.getBlocks();
        for (int blockNum = 0; blockNum < 4; blockNum++) {
            int blockCol = blocks[blockNum][0];
            int blockRow = blocks[blockNum][1];
            if (blockCol < 0 || blockCol >= width) return true; //out of bounds
            if (blockRow < 0 || blockRow >= height) return true; //out of bounds
            if (gameBoard[blockRow][blockCol] != 0) return true; //a block is there already
        }
        return false;
    }//checkCollision

    /**
     * Checks for a row of full blocks, deletes the row and moves above rows down
     * Returns true if the row was full, false otherwise
     *
     * @param checkRow the row to check if full
     * @return boolean representing if a row was full or not
     */
    private boolean checkFullLine(int checkRow) {
        for (int col = 0; col < width; col++) {
            if (gameBoard[checkRow][col] == 0) return false;
        }
        for (int col = 0; col < width; col++) {
            gameBoard[checkRow][col] = 0;
        }
        for (int row = checkRow; row > 0; row--) {
            for (int col = 0; col < width; col++) {
                gameBoard[row][col] = gameBoard[row - 1][col];
            }
        }
        if (showDisplay) {
            display.setGameBoard(gameBoard);
            display.repaint();
        }
        return true;
    }//checkFullLine

    void close() {
        jFrame.dispose();
    }
}
