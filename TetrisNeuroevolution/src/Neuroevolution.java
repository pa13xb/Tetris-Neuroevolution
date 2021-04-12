/**
 * This class contains the code for the Neuroevolution neural network
 */
class Neuroevolution {

    private int[] layersAndNodes; //layersAndNodes.length = #layers, layersAndNodes[i] = #nodes in layer i
    private int numLayers;  //excludes the input layer
    private int highScore;
    private double[][][] weightsAndBiases;

    /**Constructor: initializes the network with the given architecture (includes input layers)
     *
     * @param layersAndNodes layersAndNodes.length = #layers, layersAndNodes[i] = #nodes in layer i
     */
    Neuroevolution(int[] layersAndNodes){
        this.layersAndNodes = layersAndNodes;
        numLayers = layersAndNodes.length - 1;
        generateRandomNetwork();
    }//constructor1

    /**Other Constructor: takes a preset neural network
     * 
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

    /**Generates a random network of weights and biases
     *
     * @return double[][][] the array of weights and biases
     */
    private void generateRandomNetwork(){
        weightsAndBiases = new double[numLayers][][];
        for(int layer = 0; layer < numLayers; layer++){
            weightsAndBiases[layer] = new double[layersAndNodes[layer] + 1][];
            for(int node = 0; node < weightsAndBiases[layer].length; node++){
                weightsAndBiases[layer][node] = new double[layersAndNodes[layer] + 1]; //+1 for bias
                for(int connection = 0; connection < layersAndNodes[layer] + 1; connection++){
                    weightsAndBiases[layer][node][connection] = Math.random() * 2 - 1; //random value from (-1, 1)
                }
            }
        }
    }//generateRandomNetwork

    /**Allows inputting a network into the class.
     *
     * @param weightsAndBiases the network weights and biases
     */
    void setWeightsAndBiases(double[][][] weightsAndBiases){
        this.weightsAndBiases = weightsAndBiases;
    }//setWeightsAndBiases

    /**Getter for the weights and biases network
     *
     * @return double[][][] getWeightsAndBiases
     */
    public double[][][] getWeightsAndBiases() {
        return weightsAndBiases;
    }//getWeightsAndBiases

    /**Getter for highscore
     * 
     * @return returns the highest score for all the training runs
     */
    public int getHighScore(){
        return highScore;
    }//getHighscore
    
    /**Generates a number of mutations of the neural network
     * 
     * @param numNetworks the number of new networks to create
     * @param numMutations the number of mutations to make on each new network
     * @return double[][][][] an array of network weightsAndBiases
     */
    double[][][][] mutate(int numNetworks, int numMutations, boolean keepParent, double[][][] parent){
        double[][][][] result;
        if(keepParent) result = new double[numNetworks + 1][weightsAndBiases.length][][]; //+1 for parent
        else result = new double[numNetworks][weightsAndBiases.length][][];
        for(int mutation = 0; mutation < numNetworks; mutation++){ //initialize results:
            double[][][] mutatedNetwork = new double[numLayers][][];
            for(int layer = 0; layer < numLayers; layer++){
                mutatedNetwork[layer] = new double[layersAndNodes[layer] + 1][];
                for(int node = 0; node < mutatedNetwork[layer].length; node++){
                    mutatedNetwork[layer][node] = new double[layersAndNodes[layer] + 1]; //+1 for bias
                    for(int connection = 0; connection < layersAndNodes[layer] + 1; connection++){
                        mutatedNetwork[layer][node][connection] = parent[layer][node][connection]; //random value from (-1, 1)
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
        if(keepParent) result[numNetworks] = parent;
        return result;
    }//mutate

    /**Apply a mutation (this could be modified to include new mutation techniques in the future)
     * 
     * @return double the mutated value
     */
    private double applyMutation(){
        return Math.random() * 2 - 1; //random value between -1 and 1
    }
    
    int calculate(int[] inputArray){
        return (int)(Math.random() * 4);
    }
    
    public void train(int maxEpochs, int scoreGoal, int numGamesPerEpoch, boolean keepParent, int numNetworks, int numMutations){
        if(maxEpochs == -1 && scoreGoal == -1) maxEpochs = 100;
        int epoch = 0;
        for(;;){
            double[][][] parent = getWeightsAndBiases();
            double[][][][] networkPopulation = mutate(numNetworks, numMutations, keepParent, parent);
            highScore = 0;
            int[] networkScores = new int[networkPopulation.length];
            for(int networkNum = 0; networkNum < networkPopulation.length; networkNum++){
                setWeightsAndBiases(networkPopulation[networkNum]); //set the weights to this network one for Tetris calculation purposes
                int averageScore = 0;
                for(int gameNum = 0; gameNum < numGamesPerEpoch; gameNum++){
                    Tetris tetris = new Tetris(false, false, this);
                    averageScore += tetris.getScore();
                    if(tetris.getScore() > highScore) highScore = tetris.getScore();
                }
                networkScores[networkNum] = averageScore;
            }
            int bestScore = 0;
            for(int networkNum = 0; networkNum < networkPopulation.length; networkNum++){
                if(networkScores[networkNum] > bestScore){
                    bestScore = networkScores[networkNum];
                    setWeightsAndBiases(networkPopulation[networkNum]); //make best one the parent for next time
                }
            }
            epoch++;
            if(epoch >= maxEpochs && maxEpochs != -1) break;
            if(bestScore >= scoreGoal && scoreGoal != -1) break;
        }
    }//train
}//Neuroevolution
