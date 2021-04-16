import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This is the main class, which handles program setup and allows user input through command line.
 * There are many options and parameters to select. Once 0 is selected, runProgram is called
 * To play a human player game, ensure option 1 is true
 * To play an AI game using the optimal weights we found, ensure option 17 is true and 1 is false
 * To run a training experiment using the genetic algorithm, ensure option 20 is true
 * To run multiple (even thousands) test runs, modify the number of experiments (option 8) and ensure option 23 is true
 *
 * COSC 4P80 Final Project, Brock University
 * April 16, 2021
 * @author Philip Akkerman, 5479613, pa13xb@brocku.ca
 * @author David Hasler, 6041321, dh15pd@brocku.ca
 */
class Main {

    private Scanner scanner;

    //Parameters:
    /*0: run program */
    /*1*/ private boolean human = true;
    /*2*/ private boolean display = true;
    /*3*/ private int maxEpochs = 100;
    /*4*/ private int scoreGoal = -1;
    /*5*/ private int gamesPerEpoch = 10;
    /*6*/ private boolean keepParent = true;
    /*7*/ private int numNetworks = 10;
    /*8*/ private int numMutations = 30;
    /*9*/ private int[] layersAndNodes = {220 + 4 + 7, 160, 120, 40};
    /*10*/ private int numRandomMembers = 1; //number of new random networks to insert per epoch
    /*11*/ private int numExperiments = 1;
    /*12*/ private boolean tetrominoPosInput = false; //toggles input of the tetromino's position binary gameboard
    /*13*/ private boolean useScore = false; //toggles evaluation using score or time survived
    /*14*/ private boolean controlArrows = false; //toggles evaluation using score or time survived
    /*15*/ private boolean supervisedAI = true; //toggles evaluation using score or time survived
    /*16*/ private double errorGoal = 0.5;
    /*17*/ private boolean useOptimalMoves = true; //toggles usage of the optimal move function to play a game
    /*18*/ private int movesPerEpoch = 250; //number of games to play and evaluate for supervised learning
    /*19*/ //private double[] weights = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; //weights for optimizing fitness
    /*19*/private double[] weights = { //Weights from test 1 (the best weight setup found)
            -0.45891319930,
            -0.02082584336,
            0.79958303395,
            0.56152148032,
            -1.24236565997,
            0.57727685301,
            0.46491728127,
            -1.62548398385,
            0.33399294123,
            0.23761344856,
            0.72508749642,
            2.44337355687
    };
    /*20*/ private boolean useGA = true; //toggles usage of the genetic algorithm to refine weights
    /*21*/ private int numElites = 3; // number of elite games added into mutations
    /*22*/ private boolean saveBestGAResult = false;
    /*23*/ private boolean saveBestNNResult = false;
    /*23*/ private boolean testGASetup = false;
    /*99: quit program */

    /**
     * Main constructor. Controls the parameters and program setup with user input
     */
    private Main() {
        if(false){//small function to print NN results from overnight test
            try {
                File inputFile = new File("C:\\Users\\phili\\Documents\\Github_Repos\\Tetris-Neuroevolution\\TetrisNeuroevolution\\NN0,true,100,0.5,250,10,30,1,false");
                FileInputStream fileInputStream = new FileInputStream(inputFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                System.out.print((String) objectInputStream.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("IO Exception caught in loading weights file ");
                e.printStackTrace();
            }
        }//print NN results from overnight test
        else if (false) {//small function to print GA results from overnight test
            try {
                File inputFile = new File("C:\\Users\\phili\\Documents\\Github_Repos\\Tetris-Neuroevolution\\TetrisNeuroevolution\\GA11080,40,30,10,1,1");
                FileInputStream fileInputStream = new FileInputStream(inputFile);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                weights = (double[]) objectInputStream.readObject();
                for (double d : weights) {
                    System.out.print(d + "\t");
                }
                System.out.println();
                inputFile = new File("C:\\Users\\phili\\Documents\\Github_Repos\\Tetris-Neuroevolution\\TetrisNeuroevolution\\GA11080 training data");
                fileInputStream = new FileInputStream(inputFile);
                objectInputStream = new ObjectInputStream(fileInputStream);
                System.out.print((String) objectInputStream.readObject());
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("IO Exception caught in loading weights file ");
                e.printStackTrace();
            }
        }//print GA results from overnight test
        else {
            scanner = new Scanner(System.in);
            boolean quit = false;
            while (!quit) {
                int input;
                System.out.println("0: Run new game/experiments\n99: Quit");
                System.out.println("Or choose a parameter to modify:");
                System.out.println("1: Human player = " + human);
                System.out.println("2: Display a game played by the best AI after the experiments = " + display);
                System.out.println("3: Max epochs = " + maxEpochs);
                System.out.println("4: Score goal = " + scoreGoal);
                System.out.println("5: Games per epoch (for unsupervised learning) = " + gamesPerEpoch);
                System.out.println("6: Keep parent in population = " + keepParent);
                System.out.println("7: Number of new networks per population = " + numNetworks);
                System.out.println("8: Number of mutations per new network = " + numMutations);
                System.out.println("9: Modify neural network architecture. Current architecture:");
                System.out.println("Input Layer = " + layersAndNodes[0]);
                for (int hiddenNum = 1; hiddenNum < layersAndNodes.length - 1; hiddenNum++) {
                    System.out.println("Hidden Layer " + hiddenNum + " = " + layersAndNodes[hiddenNum]);
                }
                System.out.println("Output layer = " + layersAndNodes[layersAndNodes.length - 1]);
                System.out.println("10: Number of new random networks to insert per epoch = " + numRandomMembers);
                System.out.println("11: Number of experiment repetitions = " + numExperiments);
                System.out.println("12: Include the tetromino's board position as an input = " + tetrominoPosInput);
                System.out.println("13: Evaluate using score (true) or time survived (false) = " + useScore);
                System.out.println("14: AI controls arrows (true) or selections positions (false) = " + controlArrows);
                System.out.println("15: Use supervised AI training = " + supervisedAI);
                System.out.println("16: Error goal (for supervised learning) = " + errorGoal);
                System.out.println("17: Use optimal moves to play a game = " + useOptimalMoves);
                System.out.println("18: Number of moves per epoch (for supervised learning) = " + movesPerEpoch);
                System.out.println("19: Manually modify GA weights");
                System.out.println("20: Use a genetic algorithm to optimize weights = " + useGA);
                System.out.println("21: Specify number of elites to bring to next generation = " + numElites);
                System.out.println("22: Run GA experiments overnight and save results = " + saveBestGAResult);
                System.out.println("23: Run NN experiments overnight and save results = " + saveBestNNResult);
                System.out.println("24: Run a number of experiments (games) to test a GA weight setup = " + testGASetup);
                try {
                    input = scanner.nextInt();
                    switch (input) {
                        case 0:
                            runProgram();
                            break;
                        case 1:
                            human = !human;
                            break;
                        case 2:
                            display = !display;
                            break;
                        case 3:
                            System.out.println("Enter max epochs:");
                            input = scanner.nextInt();
                            maxEpochs = input;
                            break;
                        case 4:
                            System.out.println("Enter score goal:");
                            input = scanner.nextInt();
                            scoreGoal = input;
                            break;
                        case 5:
                            System.out.println("Enter games per epoch:");
                            input = scanner.nextInt();
                            gamesPerEpoch = input;
                            break;
                        case 6:
                            keepParent = !keepParent;
                            break;
                        case 7:
                            System.out.println("Enter number of new networks per population:");
                            input = scanner.nextInt();
                            numNetworks = input;
                            break;
                        case 8:
                            System.out.println("Enter number of mutations per new network:");
                            input = scanner.nextInt();
                            numMutations = input;
                            break;
                        case 9:
                            System.out.println("Enter number of hidden layers:");
                            input = scanner.nextInt();
                            int[] newLayersAndNodes = new int[input + 2];
                            newLayersAndNodes[0] = layersAndNodes[0]; //input nodes stay the same
                            newLayersAndNodes[newLayersAndNodes.length - 1] = layersAndNodes[layersAndNodes.length - 1]; //output stays the same
                            for (int layer = 1; layer < newLayersAndNodes.length - 1; layer++) {
                                System.out.println("Enter number of nodes for hidden layer " + layer);
                                input = scanner.nextInt();
                                newLayersAndNodes[layer] = input;
                            }
                            layersAndNodes = newLayersAndNodes;
                            break;
                        case 10:
                            System.out.println("Enter number of additional new random members per population:");
                            input = scanner.nextInt();
                            numRandomMembers = input;
                            break;
                        case 11:
                            System.out.println("Enter number of experiments (repeating the whole setup) to run:");
                            input = scanner.nextInt();
                            numExperiments = input;
                            break;
                        case 12:
                            tetrominoPosInput = !tetrominoPosInput;
                            if (!tetrominoPosInput) layersAndNodes[0] = 220 + 4 + 7;
                            else layersAndNodes[0] = 220 * 2 + 4 + 7;
                            break;
                        case 13:
                            useScore = !useScore;
                            break;
                        case 14:
                            controlArrows = !controlArrows;
                            if (!controlArrows) {
                                layersAndNodes[1] = 160;
                                layersAndNodes[2] = 120;
                                layersAndNodes[3] = 40;
                            } else {
                                layersAndNodes[1] = 150;
                                layersAndNodes[2] = 30;
                                layersAndNodes[3] = 4;
                            }
                            break;
                        case 15:
                            supervisedAI = !supervisedAI;
                            break;
                        case 16:
                            System.out.println("Enter number (floating point) for error goal:");
                            errorGoal = scanner.nextDouble();
                            break;
                        case 17:
                            useOptimalMoves = !useOptimalMoves;
                            break;
                        case 18:
                            System.out.println("Enter number of moves per epoch");
                            input = scanner.nextInt();
                            movesPerEpoch = input;
                            break;
                        case 19:
                            System.out.println("Change weights (floating point inputs)");
                            String[] labels = {
                                    "holes",
                                    "blocks",
                                    "weightedBlocks",
                                    "rowTransitions",
                                    "removedLines",
                                    "connectedHoles",
                                    "columnTransitions",
                                    "altitudeDifference",
                                    "maximumWellDepth",
                                    "sumWellDepths",
                                    "pileHeight",
                                    "landingHeight"};
                            for (int i = 0; i < weights.length; i++) {
                                System.out.println("Enter weight " + i + ": " + labels[i]);
                                weights[i] = scanner.nextDouble();
                            }
                            break;
                        case 20:
                            useGA = !useGA;
                            break;
                        case 21:
                            System.out.println("Enter number of elites");
                            numElites = scanner.nextInt();
                            break;
                        case 22:
                            saveBestGAResult = !saveBestGAResult;
                            break;
                        case 23:
                            saveBestNNResult = !saveBestNNResult;
                            break;
                        case 24:
                            testGASetup = !testGASetup;
                            break;
                        case 99:
                            quit = true;
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Input must be an integer, try again");
                }
            }
            System.out.println("Program exited by user");
        }
    }//constructor

    /**
     * This function runs the Tetris program and/or AI training depending on the user options selecte
     */
    private void runProgram() {
        if(testGASetup){
            int highscore = 0;
            double averageScore = 0.0;
            for(int i = 0 ; i < numExperiments; i++){
                Tetris tetris = new Tetris(false,false,null,null,false,false,false,true,weights);
                int score = tetris.getScore();
                averageScore += score;
                if(score > highscore) highscore = score;
                System.out.println("Game "+i+" finished, score = "+score);
            }
            averageScore = averageScore / numExperiments;
            System.out.println("Highscore = "+highscore+", Average Score = "+averageScore);
        }
        else if (useOptimalMoves) { //to test our evaluation function
            Tetris tetris = new Tetris(true, false, null, null, tetrominoPosInput, controlArrows, false, useOptimalMoves, weights);
            System.out.println("Enter any key to close Tetris window");
            scanner.next();
            tetris.close();
        }
        else if (saveBestGAResult) {
            int numRuns = 12;
            useScore = true;
            maxEpochs = 40;
            scoreGoal = -1;
            int[] gamesPerEpochList =   {30,65, 100, 30, 30, 30, 30, 30, 30, 30, 30, 30};
            int[] numNetworksList =     {10,10,  10, 10, 20, 30, 10, 10, 10, 10, 10, 10};
            int[] numMutationsList =    {1,  1,   1,  1,  1,  1,  1,  3,  5,  1,  1,  1};
            int[] numRandomMembersList ={1,  1,   1,  1,  1,  1,  1,  1,  1,  1,  3,  5,};
            for (int run = 0; run < numRuns; run++) {
                gamesPerEpoch = gamesPerEpochList[run];
                numNetworks = numNetworksList[run];
                numMutations = numMutationsList[run];
                numRandomMembers = numRandomMembersList[run];
                GeneticAlgorithm GA = new GeneticAlgorithm();
                String results = GA.train(maxEpochs, scoreGoal, gamesPerEpoch, keepParent, numNetworks, numMutations, numRandomMembers, tetrominoPosInput, useScore, numElites);
                int bestScore = GA.getHighScore();
                try {
                    File outputFile = new File("GA"+bestScore + "," + maxEpochs + "," + gamesPerEpoch + "," + numNetworks + "," + numMutations + "," + numRandomMembers);
                    FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                    ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                    outputStream.writeObject(GA.getWeights());
                    outputFile = new File("GA"+bestScore + " training data");
                    fileOutputStream = new FileOutputStream(outputFile);
                    outputStream = new ObjectOutputStream(fileOutputStream);
                    outputStream.writeObject(results);
                } catch (IOException e) {
                    System.out.println("IO Exception caught in saving GA");
                }
            }
        }
        else if (saveBestNNResult) {
            int numRuns = 16;
            maxEpochs = 40;
            scoreGoal = -1;
            errorGoal = -1;
            keepParent = true;
            useScore = true;
            boolean[] supervisedList = {false,false,false,false,false,false,false,false,false,false,true,true,true,true,true,true};
            int[] movesPerEpochList = {30, 30, 30, 30, 30, 30, 30, 65, 100, 30, 30, 30, 30, 30, 30, 30};
            int[] gamesPerEpochList = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 65, 100};
            int[] numNetworksList = {10, 10, 10, 10, 10, 10, 10, 10, 20, 30, 10, 10, 10, 10, 10, 10};
            int[] numMutationsList = {30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 165, 300, 30, 30};
            boolean[] useScoreList = {false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false};
            boolean[] controlArrowsList = {false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false};
            int[] numRandomMembersList = {1, 3, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
            for (int run = 0; run < numRuns; run++) {
                movesPerEpoch = movesPerEpochList[run];
                gamesPerEpoch = gamesPerEpochList[run];
                numNetworks = numNetworksList[run];
                numMutations = numMutationsList[run];
                useScore = useScoreList[run];
                controlArrows = controlArrowsList[run];
                numRandomMembers = numRandomMembersList[run];
                Neuroevolution NN = new Neuroevolution(layersAndNodes,new double[]{1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0});
                String results;
                if(supervisedList[run]) results = NN.trainSupervised(maxEpochs, errorGoal, movesPerEpoch, keepParent, numNetworks, numMutations, numRandomMembers, tetrominoPosInput, controlArrows);
                else results = NN.train(maxEpochs, scoreGoal, gamesPerEpoch, keepParent, numNetworks, numMutations, numRandomMembers, tetrominoPosInput, useScore, controlArrows);
                int bestScore = NN.getHighScore();
                try {
                    File outputFile;
                    if(supervisedList[run]) outputFile = new File("NN"+bestScore + "," + supervisedAI + "," + maxEpochs + "," + errorGoal + "," + movesPerEpoch + "," + numNetworks+ "," + numMutations+ "," + numRandomMembers+ "," + controlArrows);
                    else outputFile = new File("NN"+bestScore+","+supervisedAI + "," + maxEpochs + "," + errorGoal + "," + movesPerEpoch + "," + numNetworks+ "," + numMutations+ "," + numRandomMembers+ "," + controlArrows);
                    FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                    ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                    outputStream.writeObject(results);
                } catch (IOException e) {
                    System.out.println("IO Exception caught in saving Neural Network");
                }
            }
        }
        else if (human) {
            Tetris tetris = new Tetris(true, true, null, null, tetrominoPosInput, controlArrows, false, useOptimalMoves, weights);
            System.out.println("Enter any key to close Tetris window");
            scanner.next();
            tetris.close();
        }
        else if (useGA) {
            GeneticAlgorithm GA = null;
            GeneticAlgorithm[] GAList = new GeneticAlgorithm[numExperiments];
            String results = "";
            for (int expNum = 0; expNum < numExperiments; expNum++) {
                System.out.println("====================================\nExperiment #" + (expNum + 1) +
                        " of " + numExperiments + " beginning\n====================================");
                GA = new GeneticAlgorithm();
                results = results.concat(GA.train(maxEpochs, scoreGoal, gamesPerEpoch, keepParent, numNetworks, numMutations, numRandomMembers, tetrominoPosInput, true, numElites));
                GAList[expNum] = GA;
            }
            int bestScore = -1;
            for (int expNum = 0; expNum < numExperiments; expNum++) {
                if (GAList[expNum].getHighScore() > bestScore) {
                    bestScore = GAList[expNum].getHighScore();
                    GA = GAList[expNum];
                }
            }
            System.out.println("Printing results now: \n====================================\n");
            System.out.println(results);
            System.out.println("\n====================================\n");
            System.out.println("Best score achieved = " + bestScore);
            if (display) {
                Tetris tetris = new Tetris(display, false, null, null, tetrominoPosInput, controlArrows, false, true, GA.getWeights());
                System.out.println("Enter any key to close Tetris window");
                scanner.next();
                tetris.close();
            }
        }
        else {
            Neuroevolution neuralNet = null;
            Neuroevolution[] neuralNetworks = new Neuroevolution[numExperiments];
            String results = "";
            for (int expNum = 0; expNum < numExperiments; expNum++) {
                System.out.println("====================================\nExperiment #" + (expNum + 1) +
                        " of " + numExperiments + " beginning\n====================================");
                neuralNet = new Neuroevolution(layersAndNodes, weights);
                if (!supervisedAI)
                    results = results.concat(neuralNet.train(maxEpochs, scoreGoal, gamesPerEpoch, keepParent, numNetworks, numMutations, numRandomMembers, tetrominoPosInput, useScore, controlArrows) + "\n");
                else
                    results = results.concat(neuralNet.trainSupervised(maxEpochs, errorGoal, movesPerEpoch, keepParent, numNetworks, numMutations, numRandomMembers, tetrominoPosInput, controlArrows) + "\n");
                neuralNetworks[expNum] = neuralNet;
            }
            int bestScore = -1;
            for (int expNum = 0; expNum < numExperiments; expNum++) {
                if (neuralNetworks[expNum].getHighScore() > bestScore) {
                    bestScore = neuralNetworks[expNum].getHighScore();
                    neuralNet = neuralNetworks[expNum];
                }
            }
            System.out.println("Printing results now: \n====================================\n");
            System.out.println(results);
            System.out.println("\n====================================\n");
            System.out.println("Best score achieved = " + bestScore);
            if (display) {
                Tetris tetris = new Tetris(display, human, neuralNet, null, tetrominoPosInput, controlArrows, false, useOptimalMoves, weights);
                System.out.println("Enter any key to close Tetris window");
                scanner.next();
                tetris.close();
            }
        } //Neural net
    }

    public static void main(String[] args) {
        new Main();
    }
}