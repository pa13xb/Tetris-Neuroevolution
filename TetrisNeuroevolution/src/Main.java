import javax.swing.*;
import java.util.InputMismatchException;
import java.util.Scanner;

class Main {

    private Scanner scanner;

    int tileSize = 30;
    int width = 10;
    int height = 20;

    //Parameters:
    /*0: run program */
    /*1*/ private boolean human = false;
    /*2*/ private boolean display = true;
    /*3*/ private int maxEpochs = 100;
    /*4*/ private int scoreGoal = -1;
    /*5*/ private int gamesPerEpoch = 10;
    /*6*/ private boolean keepParent = true;
    /*7*/ private int numNetworks = 10;
    /*8*/ private int numMutations = 30;
    /*9*/ private int[] layersAndNodes = {220+4+7, 160, 120, 40};
    /*10*/ private int numRandomMembers = 1; //number of new random networks to insert per epoch
    /*11*/ private int numExperiments = 1;
    /*12*/ private boolean tetrominoPosInput = false; //toggles input of the tetromino's position binary gameboard
    /*13*/ private boolean useScore = false; //toggles evaluation using score or time survived
    /*14*/ private boolean controlArrows = false; //toggles evaluation using score or time survived
    /*15*/ private boolean supervisedAI = true; //toggles evaluation using score or time survived
    /*16*/ private double errorGoal = 0.5;
    /*17*/ private boolean useOptimalMoves = false; //toggles usage of the optimal move function to play a game
    /*18*/ private int movesPerEpoch = 250; //number of games to play and evaluate for supervised learning
    /*19*/ private double[] weights = {1,1,1,1,1,1,1,1,1,1,1,1}; //weights for optimizing fitness
    /*20*/ private boolean useGA = false; //toggles usage of the genetic algorithm to refine weights
    /*21*/ private int numElites = 3; // number of elite games added into mutations
    /*99: quit program */

    private Main(){
        scanner = new Scanner(System.in);
        boolean quit = false;
        while(!quit){
            int input;
            System.out.println("0: Run new game/experiments\n99: Quit");
            System.out.println("Or choose a parameter to modify:");
            System.out.println("1: Human player = "+human);
            System.out.println("2: Display a game played by the best AI after the experiments = "+display);
            System.out.println("3: Max epochs = "+maxEpochs);
            System.out.println("4: Score goal = "+scoreGoal);
            System.out.println("5: Games per epoch (for unsupervised learning) = "+gamesPerEpoch);
            System.out.println("6: Keep parent in population = "+keepParent);
            System.out.println("7: Number of new networks per population = "+numNetworks);
            System.out.println("8: Number of mutations per new network = "+numMutations);
            System.out.println("9: Modify neural network architecture. Current architecture:");
            System.out.println("Input Layer = "+layersAndNodes[0]);
            for(int hiddenNum = 1; hiddenNum < layersAndNodes.length - 1; hiddenNum++){
                System.out.println("Hidden Layer "+hiddenNum+" = "+layersAndNodes[hiddenNum]);
            }
            System.out.println("Output layer = "+layersAndNodes[layersAndNodes.length-1]);
            System.out.println("10: Number of new random networks to insert per epoch = "+numRandomMembers);
            System.out.println("11: Number of experiment repetitions = "+numExperiments);
            System.out.println("12: Include the tetromino's board position as an input = "+tetrominoPosInput);
            System.out.println("13: Evaluate using score (true) or time survived (false) = "+useScore);
            System.out.println("14: AI controls arrows (true) or selections positions (false) = "+controlArrows);
            System.out.println("15: Use supervised AI training = "+supervisedAI);
            System.out.println("16: Error goal (for supervised learning) = "+errorGoal);
            System.out.println("17: Use optimal moves to play a game = "+useOptimalMoves);
            System.out.println("18: Number of moves per epoch (for supervised learning) = "+movesPerEpoch);
            System.out.println("19: Manually modify GA weights");
            System.out.println("20: Use a genetic algorithm to optimize weights = "+useGA);
            System.out.println("21: Specify number of elites to bring to next generation = "+numElites);
            try {
                input = scanner.nextInt();
                switch(input){
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
                        newLayersAndNodes[newLayersAndNodes.length - 1] = layersAndNodes[layersAndNodes.length-1]; //output stays the same
                        for(int layer = 1; layer < newLayersAndNodes.length - 1; layer++){
                            System.out.println("Enter number of nodes for hidden layer "+layer);
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
                        if(!tetrominoPosInput) layersAndNodes[0] = 220 + 4 + 7;
                        else layersAndNodes[0] = 220 * 2 + 4 + 7;
                        break;
                    case 13:
                        useScore = !useScore;
                        break;
                    case 14:
                        controlArrows = !controlArrows;
                        if(!controlArrows) {
                            layersAndNodes[1] = 160;
                            layersAndNodes[2] = 120;
                            layersAndNodes[3] = 40;
                        }
                        else {
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
                        for(int i = 0; i < weights.length; i++){
                            System.out.println("Enter weight "+i+": "+labels[i]);
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
                    case 99:
                        quit = true;
                        break;
                }
            } catch(InputMismatchException e){
                System.out.println("Input must be an integer, try again");
            }
        }
        System.out.println("Program exited by user");
    }//constructor

    private void runProgram(){
        if(useOptimalMoves){ //to test our evaluation function
            Tetris tetris = new Tetris(true,false, null, null, tetrominoPosInput, controlArrows, false, useOptimalMoves, weights);
            System.out.println("Enter any key to close Tetris window");
            scanner.next();
            tetris.close();
        }
        else if(useGA){
            GeneticAlgorithm GA = null;
            GeneticAlgorithm[] GAList = new GeneticAlgorithm[numExperiments];
            String results = "";
            for(int expNum = 0; expNum < numExperiments; expNum++) {
                System.out.println("====================================\nExperiment #"+(expNum+1)+
                        " of "+numExperiments+" beginning\n====================================");
                GA = new GeneticAlgorithm();
                results = results.concat(GA.train(maxEpochs,scoreGoal,gamesPerEpoch,keepParent,numNetworks,numMutations,numRandomMembers,tetrominoPosInput,useScore, numElites));
                GAList[expNum] = GA;
            }
            int bestScore = -1;
            for(int expNum = 0; expNum < numExperiments; expNum++){
                if(GAList[expNum].getHighScore() > bestScore){
                    bestScore = GAList[expNum].getHighScore();
                    GA = GAList[expNum];
                }
            }
            System.out.println("Printing results now: \n====================================\n");
            System.out.println(results);
            System.out.println("\n====================================\n");
            System.out.println("Best score achieved = "+bestScore);
            if (display) {
                Tetris tetris = new Tetris(display, false, null, null, tetrominoPosInput, controlArrows, false, true, GA.getWeights());
                System.out.println("Enter any key to close Tetris window");
                scanner.next();
                tetris.close();
            }
        }
        else if(human){
            Tetris tetris = new Tetris(true,true, null, null,tetrominoPosInput, controlArrows, false, useOptimalMoves, weights);
            System.out.println("Enter any key to close Tetris window");
            scanner.next();
            tetris.close();
        }
        else{
            Neuroevolution neuralNet = null;
            Neuroevolution[] neuralNetworks = new Neuroevolution[numExperiments];
            String results = "";
            for(int expNum = 0; expNum < numExperiments; expNum++) {
                System.out.println("====================================\nExperiment #"+(expNum+1)+
                        " of "+numExperiments+" beginning\n====================================");
                neuralNet = new Neuroevolution(layersAndNodes, weights);
                if(!supervisedAI) results = results.concat(neuralNet.train(maxEpochs, scoreGoal, gamesPerEpoch, keepParent, numNetworks, numMutations, numRandomMembers, tetrominoPosInput, useScore, controlArrows)+"\n");
                else results = results.concat(neuralNet.trainSupervised(maxEpochs, errorGoal, movesPerEpoch, keepParent, numNetworks, numMutations, numRandomMembers, tetrominoPosInput, controlArrows)+"\n");
                neuralNetworks[expNum] = neuralNet;
            }
            int bestScore = -1;
            for(int expNum = 0; expNum < numExperiments; expNum++){
                if(neuralNetworks[expNum].getHighScore() > bestScore){
                    bestScore = neuralNetworks[expNum].getHighScore();
                    neuralNet = neuralNetworks[expNum];
                }
            }
            System.out.println("Printing results now: \n====================================\n");
            System.out.println(results);
            System.out.println("\n====================================\n");
            System.out.println("Best score achieved = "+bestScore);
            if (display) {
                Tetris tetris = new Tetris(display, human, neuralNet, null, tetrominoPosInput, controlArrows, false, useOptimalMoves, weights);
                System.out.println("Enter any key to close Tetris window");
                scanner.next();
                tetris.close();
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}