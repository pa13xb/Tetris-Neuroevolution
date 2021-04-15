/**
 * This class contains the code for the Neuroevolution neural network
 */
class Neuroevolution {

    private int[] layersAndNodes; //layersAndNodes.length = #layers, layersAndNodes[i] = #nodes in layer i
    private int numLayers;  //excludes the input layer
    private int highScore;
    private double[][][] weightsAndBiases;
    private double[] weights;

    /**
     *Constructor: initializes the network with the given architecture (includes input layers)
     * @param layersAndNodes layersAndNodes.length = #layers, layersAndNodes[i] = #nodes in layer i
     */
    Neuroevolution(int[] layersAndNodes, double[] weights){
        this.weights = weights;
        this.layersAndNodes = layersAndNodes;
        numLayers = layersAndNodes.length - 1;
        weightsAndBiases = generateRandomNetwork();
    }//constructor1

    /**
     * Other Constructor: takes a preset neural network
     * @param weightsAndBiases the preset neural network of weights and biases
     */
    Neuroevolution(double[][][] weightsAndBiases){
        this.weightsAndBiases = weightsAndBiases;
        numLayers = weightsAndBiases.length;
        layersAndNodes = new int[numLayers + 1];
        layersAndNodes[0] = weightsAndBiases[0][0].length; //input layer = number of connections in the first layer's nodes
        for(int layer = 1; layer < numLayers + 1; layer++){
            layersAndNodes[layer] = weightsAndBiases[layer].length;
        }
    }//constructor2

    /**
     *Generates a random network of weights and biases
     * @return double[][][] the array of weights and biases
     */
    private double[][][] generateRandomNetwork(){
        double[][][] weightsAndBiases = new double[numLayers][][];
        for(int layer = 0; layer < numLayers; layer++){
            weightsAndBiases[layer] = new double[layersAndNodes[layer + 1]][];
            for(int node = 0; node < weightsAndBiases[layer].length; node++){
                weightsAndBiases[layer][node] = new double[layersAndNodes[layer] + 1]; //+1 for bias
                for(int connection = 0; connection < layersAndNodes[layer] + 1; connection++){
                    weightsAndBiases[layer][node][connection] = Math.random() * 2 - 1; //random value from (-1, 1)
                }
            }
        }
        return weightsAndBiases;
    }//generateRandomNetwork

    /**
     *Allows inputting a network into the class.
     * @param weightsAndBiases the network weights and biases
     */
    void setWeightsAndBiases(double[][][] weightsAndBiases){
        this.weightsAndBiases = weightsAndBiases;
    }//setWeightsAndBiases

    /**
     *Getter for the weights and biases network
     * @return double[][][] getWeightsAndBiases
     */
    public double[][][] getWeightsAndBiases() {
        return weightsAndBiases;
    }//getWeightsAndBiases

    /**
     * Getter for highscore
     * @return returns the highest score for all the training runs
     */
    public int getHighScore(){
        return highScore;
    }//getHighscore
    
    /**
     * Generates a number of mutations of the neural network
     * @param numNetworks the number of new networks to create
     * @param numMutations the number of mutations to make on each new network
     * @return double[][][][] an array of network weightsAndBiases
     */
    double[][][][] mutate(int numNetworks, int numMutations, boolean keepParent, double[][][] parent, int numRandomMembers){
        double[][][][] result;
        int populationSize = numNetworks + numRandomMembers;
        if(keepParent) populationSize++;
        result = new double[populationSize][weightsAndBiases.length][][]; //+1 for parent
        for(int mutation = 0; mutation < numNetworks; mutation++){ //initialize results:
            double[][][] mutatedNetwork = new double[numLayers][][];
            for(int layer = 0; layer < numLayers; layer++){
                mutatedNetwork[layer] = new double[layersAndNodes[layer + 1]][];
                for(int node = 0; node < mutatedNetwork[layer].length; node++){
                    mutatedNetwork[layer][node] = new double[layersAndNodes[layer] + 1]; //+1 for bias
                    for(int connection = 0; connection < layersAndNodes[layer] + 1; connection++){
                        mutatedNetwork[layer][node][connection] = parent[layer][node][connection];
                    }
                }
            }
            for(int i = 0; i < numMutations; i++){
                int randLayer = (int)(Math.random() * weightsAndBiases.length);
                int randNode = (int)(Math.random() * weightsAndBiases[randLayer].length);
                int randConnection = (int)(Math.random() * weightsAndBiases[randLayer][randNode].length);
                mutatedNetwork[randLayer][randNode][randConnection] = applyMutation();
            }
            result[mutation] = mutatedNetwork;
        }
        if(keepParent) {
            result[numNetworks] = parent;
            numNetworks++;
        }
        for(int i = 0; i < numRandomMembers; i++){ //add new random members to the population
            result[numNetworks + i] = generateRandomNetwork();
        }
        return result;
    }//mutate

    /**
     * Apply a mutation (this could be modified to include new mutation techniques in the future)
     * @return double the mutated value
     */
    private double applyMutation(){
        return Math.random() * 2 - 1; //random value between -1 and 1
    }

    int calculate(int[] inputArray){
        double[][] activations = new double[weightsAndBiases.length][]; //includes input layer
        {//Feed Forward
            activations[0] = new double[weightsAndBiases[0].length];
            for (int node = 0; node < weightsAndBiases[0].length; node++) { //compute each node in first hidden layer
                activations[0][node] = 0.0;
                for (int connection = 0; connection < weightsAndBiases[0][node].length - 1; connection++) {
                    activations[0][node] += weightsAndBiases[0][node][connection] * inputArray[connection]; //add weights * connections
                }
                activations[0][node] += weightsAndBiases[0][node][weightsAndBiases[0][node].length - 1]; //add bias
                activations[0][node] = activationFunction(activations[0][node]);
            }
            for (int layer = 1; layer < weightsAndBiases.length; layer++) { //start at the first hidden layer
                activations[layer] = new double[weightsAndBiases[layer].length]; //Activations are as long as # of node in this layer
                for (int node = 0; node < activations[layer].length; node++) {
                    activations[layer][node] = 0.0;
                    for (int connection = 0; connection < activations[layer - 1].length; connection++) {
                        activations[layer][node] += weightsAndBiases[layer][node][connection] * activations[layer - 1][connection];//add weights * connections
                    }
                    activations[layer][node] += weightsAndBiases[layer][node][activations[layer - 1].length]; //add bias
                    activations[layer][node] = activationFunction(activations[layer][node]);
                }
            }
        }//Feed Forward
        int highestIndex = -1;
        double highestValue = Double.MIN_VALUE;
        for(int outputNum = 0; outputNum < activations[activations.length - 1].length; outputNum++){
            if(activations[activations.length - 1][outputNum] > highestValue) {
                highestValue = activations[activations.length - 1][outputNum];
                highestIndex = outputNum;
            }
        }
        return highestIndex;
    }//calculate

    /**
     *This function performs the activation function in the neural network. Parameters can be changed here
     * @param x the value to perform the activation function on
     * @return double: the result of the activation function
     */
    private double activationFunction(double x){
        //sigmoid:
        return 1.0 / (1.0 + Math.exp(-x));
    }//activationFunction

    /**The train function which trains the neuroevolution network by mutating the network into a new population,
     * running the Tetris game with those networks, and using the best performer to repopulate the next population.
     *
     * @param maxEpochs the maximum epochs of each experiment
     * @param scoreGoal the maximum goal of each experiment
     * @param numGamesPerEpoch the number of games run in each epoch
     * @param keepParent whether to include the parent in the next population
     * @param numNetworks the number of new networks to mutate
     * @param numMutations the number of mutations per new network
     * @param numRandomMembers the number of additional random networks to add to the population
     * @return a newline-delimited String of the best results per epoch
     */
    public String train(int maxEpochs, int scoreGoal, int numGamesPerEpoch, boolean keepParent, int numNetworks, int numMutations, int numRandomMembers, boolean tetrominoPosInput, boolean usingScore, boolean controlArrows){
        if(maxEpochs == -1 && scoreGoal == -1) maxEpochs = 100;
        highScore = 0;
        String results = "";
        int epoch = 0;
        for(;;){
            double[][][] parent = getWeightsAndBiases();
            double[][][][] networkPopulation = mutate(numNetworks, numMutations, keepParent, parent, numRandomMembers);
            float[] aveNetworkScores = new float[networkPopulation.length];
            for(int networkNum = 0; networkNum < networkPopulation.length; networkNum++){
                setWeightsAndBiases(networkPopulation[networkNum]); //set the weights to this network one for Tetris calculation purposes
                int averageScore = 0;
                for(int gameNum = 0; gameNum < numGamesPerEpoch; gameNum++){
                    Tetris tetris = new Tetris(false, false, this, null, tetrominoPosInput, controlArrows, false, false, weights);
                    if(usingScore) {
                        averageScore += tetris.getScore();
                        if(tetris.getScore() > highScore) highScore = tetris.getScore();
                    }
                    else{
                        averageScore += tetris.getTimeSurvived();
                        if(tetris.getTimeSurvived() > highScore) highScore = tetris.getTimeSurvived();
                    }
                }
                aveNetworkScores[networkNum] = (float)averageScore / (float)numGamesPerEpoch;
            }
            float bestScore = 0;
            for(int networkNum = 0; networkNum < networkPopulation.length; networkNum++){
                if(aveNetworkScores[networkNum] > bestScore){
                    bestScore = aveNetworkScores[networkNum];
                    setWeightsAndBiases(networkPopulation[networkNum]); //make best one the parent for next time
                }
            }
            epoch++;
            results = results.concat(bestScore+"\n");
            System.out.println("Epoch "+epoch+" complete, best network's average score = "+bestScore);
            if(epoch >= maxEpochs && maxEpochs != -1) break;
            if(bestScore >= scoreGoal && scoreGoal != -1) break;
        }
        return results;
    }//train

    public String trainSupervised(int maxEpochs, double errorGoal, int movesPerEpoch, boolean keepParent, int numNetworks, int numMutations, int numRandomMembers, boolean tetrominoPosInput, boolean controlArrows){
        highScore = 0;
        String results = "";
        int epoch = 0;
        for(;;){
            double[][][] parent = getWeightsAndBiases();
            double[][][][] networkPopulation = mutate(numNetworks, numMutations, keepParent, parent, numRandomMembers);
            double[] aveNetworkErrors = new double[networkPopulation.length];
            for(int networkNum = 0; networkNum < networkPopulation.length; networkNum++){
                setWeightsAndBiases(networkPopulation[networkNum]); //set the weights to this network one for Tetris calculation purposes
                double averageError = 0.0;
                int numMovesPlayed = 0;
                while(numMovesPlayed < movesPerEpoch){
                    Tetris tetris = new Tetris(false, false, this,null, tetrominoPosInput, controlArrows, true, false, weights);
                    tetris.AISetupGameBoard();
                    while(!tetris.getGameOver()){
                        double error = tetris.AIPlaySupervisedMove(this, weights);
                        if(error != -1){
                            numMovesPlayed++;
                            averageError += error;
                        }
                        if(numMovesPlayed > movesPerEpoch) break;
                    }
                }
                aveNetworkErrors[networkNum] = averageError / (double)numMovesPlayed;
            }
            double lowestError = Double.MAX_VALUE;
            for(int networkNum = 0; networkNum < networkPopulation.length; networkNum++){
                if(aveNetworkErrors[networkNum] < lowestError){
                    lowestError = aveNetworkErrors[networkNum];
                    setWeightsAndBiases(networkPopulation[networkNum]); //make best one the parent for next time
                }
            }
            epoch++;
            results = results.concat(lowestError+"\n");
            System.out.println("Epoch "+epoch+" complete, best network's average error = "+lowestError);
            if(epoch >= maxEpochs && maxEpochs != -1) break;
            if(errorGoal >= lowestError && errorGoal != -1) break;
        }
        return results;
    }
}//Neuroevolution