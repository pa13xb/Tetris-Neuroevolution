import javax.swing.*;
import java.awt.event.*;
/**
 * This is the Tetris class, which implements the game of Tetris. It holds functions which allow
 * a human player to play Tetris or an AI using the neuroevolution class or the genetic algorithm
 * class to learn and play Tetris as well. It also allows a game to be played with the moves calculated
 * from the evaluation function, with a given set of weights.
 *
 * COSC 4P80 Final Project, Brock University
 * April 16, 2021
 * @author Philip Akkerman, 5479613, pa13xb@brocku.ca
 * @author David Hasler, 6041321, dh15pd@brocku.ca
 */
class Tetris {

    private int width = 10; //default Tetris width
    private int height = 22; //default Tetris height + 2 to allow for rotation at the top of the board
    private int tileSize = 30; //size of a tile for image display
    private int gameBoard[][] = new int[height][width]; //0: empty, otherwise int represents colour
    private Display display; //the JPanel extension class that handles displaying the Tetris game
    private JFrame jFrame = new JFrame(); //The JFrame which holds the Display
    private Tetromino tetromino = null; //The currently controlled Tetrimino
    private boolean showDisplay; //boolean whether to display the Tetris game being played
    private boolean newBlock; //boolean indicating whether a Tetrimino has collided and a new one must be made
    private boolean downArrow = false; //boolean indicating that the down arrow is being held to speed up animation
    private boolean gameOver; //boolean indicating that the game has finished
    private boolean quit; //boolean to indicate that the quit button has been selected
    private int animationDelay = 300; //The animation delay which controls animation speed
    private int originalAnimationDelay = animationDelay; //Storage of the original animation delay for when down arrow changes it
    private int newBlockDelay = 0; //A delay is used to spawn a new block if the down arrow is being held
    private int score = 0; //The current score of the game
    private int timeSurvived = 0; //The current time survived
    private boolean controlArrows; //Boolean whether the AI controls arrow keys or one of the 40 possible moves

    /**
     * Constructor
     * @param showDisplay boolean to enable game display
     * @param humanPlayer boolean to enable human interaction
     * @param neuroevolution the neural network used to play a game
     * @param tetrominoPosInput boolean of whether to include a binary array representing the current tetramino's position
     * @param controlArrows boolean to choose between AI controlling arrow keys or total moves (column + rotation)
     * @param supervisedAI boolean to allow the external supervised learning class to control the game for training
     * @param useOptimalMoves boolean to play a game using just the heuristic evaluation function's chosen moves
     */
    Tetris(boolean showDisplay, boolean humanPlayer, Neuroevolution neuroevolution, GeneticAlgorithm GA, boolean tetrominoPosInput, boolean controlArrows, boolean supervisedAI, boolean useOptimalMoves, double[] weights) {
        this.showDisplay = showDisplay;
        if (showDisplay) setupDisplay();
        else display = null;
        this.controlArrows = controlArrows;
        if(GA != null) {
            playOptimalMoveGame(GA.getWeights());
        }
        else if(!supervisedAI) {
            if(useOptimalMoves) playOptimalMoveGame(weights);
            else if (humanPlayer) humanPlayGame();
            else AIPlayGame(neuroevolution, tetrominoPosInput);
        }
    }//constructor

    /**
     * A function to setup the jFrame and display to show the game being played
     */
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

    /**
     * Carries out one right move
     */
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
    }//moveRight

    /**
     * Carries out one left move
     */
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
    }//moveLeft

    /**
     * Carries out one rotation
     */
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
    }//rotate

    /**
     * Carries out the spaceBar move (all the way down)
     */
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
    }//moveSpaceBar

    /**
     * The main game-play loop for a human-played game
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

    /**
     * Plays a whole game using a neuroevolution AI (for unsupervised learning)
     * @param neuroevolution  the AI to play the game
     * @param tetrominoPosInput boolean whether to include the binary array describing the tetromino's position
     */
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

    /**
     * Plays a whole game using the evaluation function's move choices
     */
    private void playOptimalMoveGame(double[] weights){
        AISetupGameBoard();
        boolean usingDisplay = showDisplay;
        while(!gameOver) {
            timeSurvived++;
            playTurn();
            if (tetromino != null && !gameOver) { //need to wait for a new tetramino to be placed
                if(usingDisplay) showDisplay = false;
                int move = findOptimalMove(weights);
                if(usingDisplay) showDisplay = true;
                makeAIMove(move);
            }
        }
        if(usingDisplay) {
            display.gameOver(getScore(), getTimeSurvived());
            display.repaint();
        }
    }//playOptimalMoveGame

    /**
     * Carries out a move out of the 40 possible move choices (columns and rotations)
     * Handles cases where the display is used or not
     * @param move the move to carry out
     */
    private void makeAIMove(int move){
        if(!showDisplay) {
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
        }
        else{
            long startTime = System.currentTimeMillis();
            long currentTime;
            int blockRotation = move % 4;
            for (int r = 0; r < blockRotation;) {
                currentTime = System.currentTimeMillis();
                if (currentTime - startTime > animationDelay / 4) {
                    startTime = System.currentTimeMillis();
                    rotate();
                    r++;
                    display.setGameBoard(gameBoard);
                    display.repaint();
                }
            }
            int column = move / 4;
            int moveAmount = 4 - column;
            if (moveAmount >= 0) { //we're going left
                for (int i = 0; i < moveAmount;){
                    currentTime = System.currentTimeMillis();
                    if (currentTime - startTime > animationDelay / 4) {
                        startTime = System.currentTimeMillis();
                        moveLeft();
                        i++;
                        display.setGameBoard(gameBoard);
                        display.repaint();
                    }
                }
            } else { //we're going right
                for (int i = 0; i < -moveAmount;) {
                    currentTime = System.currentTimeMillis();
                    if (currentTime - startTime > animationDelay / 4) {
                        startTime = System.currentTimeMillis();
                        moveRight();
                        i++;
                        display.setGameBoard(gameBoard);
                        display.repaint();
                    }
                }
            }
            for(;;){
                currentTime = System.currentTimeMillis();
                if (currentTime - startTime > animationDelay / 4) {
                    moveSpaceBar();
                    display.setGameBoard(gameBoard);
                    display.repaint();
                    break;
                }
            }
        }
    }//makeAIMove

    /**
     * Sets up a gameBoard when not using the typical game loop
     */
    void AISetupGameBoard() {
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

    /**
     * Performs one turn of the AI when using supervised training
     * @param neuroevolution the AI used to calculate the move
     * @return //the error of the AI's move compared to the evaluation function
     */
    double AIPlaySupervisedMove(Neuroevolution neuroevolution, double[] weights) {
        timeSurvived++;
        playTurn();
        if (tetromino != null && !gameOver) { //need to wait for a new tetramino to be placed
            int[] inputArray = new int[4 + 7 + width * height * 2];
            {
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
            } //getting input Array
            int move = neuroevolution.calculate(inputArray); //use the input array to calculate a move
            double score = calculateMoveScore(move, weights); //higher score = bad
            int optimalMove = findOptimalMove(weights);
            double optimalScore = calculateMoveScore(optimalMove, weights);
            {//perform the optimal move
                if (controlArrows) { //4 outputs
                    if (optimalMove == 0) moveRight();
                    else if (optimalMove == 1) moveLeft();
                    else if (optimalMove == 2) rotate();
                    else moveSpaceBar();
                } else { //40 outputs, 4 rotations per column, 10 columns total
                    makeAIMove(optimalMove);
                }
            }//perform a move
            //return optimalScore - score; //error (higher score = bad)
            return score - optimalScore; //error (higher score = bad)
        }
        return -1;
    }//AIPlaySupervisedMove

    /**
     * Performs one frame of a turn, moving the tetromino down one space, managing collisions,
     * making a tetromino if needed, deleting and incrementing score if a line is cleared.
     */
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
    }//playTurn

    /**
     * Checks all 40 possible moves and returns the move with the lowest move score
     *
     * @param weights the weights used to find the optimal move
     * @return the index of the move with the lowest score
     */
    private int findOptimalMove(double[] weights){
        double minScore = Double.MAX_VALUE;
        int moveIndex = 0;
        for(int i = 0; i < 40; i++) {
            double score = calculateMoveScore(i, weights);
            if(score < minScore) {
                minScore = score;
                moveIndex = i;
            }
        }
        return moveIndex;
    }//findOptimalMove

    /**
     * Calculates the score of a move using an heuristic evaluation function of the board's layout after the move
     * @param move the move to calculate
     * @param weights the weights to use to weigh each evaluation attribute
     * @return the score (higher = worse) of the move
     */
    private double calculateMoveScore(int move, double weights[]) {
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
        result += weights[0] * holes;
        result += weights[1] * blocks;
        result += weights[2] * weightedBlocks;
        result += weights[3] * rowTransitions;
        result += weights[4] * removedLines;
        result += weights[5] * connectedHoles;
        result += weights[6] * columnTransitions;
        result += weights[7] * altitudeDifference;
        result += weights[8] * maximumWellDepth;
        result += weights[9] * sumWellDepths;
        result += weights[10] * pileHeight;
        result += weights[11] * landingHeight;
        return result;
    }//calculateMoveScore

    /** Creates a new random Tetromino
    *
    * @return Tetrimino representing the new random Tetrimino
    */
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

    int getScore() {
        return score;
    }//getScore

    boolean getGameOver(){
        return gameOver;
    }//getGameOver

    int getTimeSurvived() {
        return timeSurvived;
    }//getTimeSurvived

    void close() {
        jFrame.dispose();
    }
}
