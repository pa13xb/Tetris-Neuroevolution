/**
 * This class contains the code for the Neuroevolution neural network
 */
class Neuroevolution {

    int[] layersAndNodes; //layersAndNodes.length = #layers, layersAndNodes[i] = #nodes in layer i
    int numHiddenLayers;
    double[][][] weightsAndBiases;

    /**Constructor: initializes the network with the given architecture (includes input layers)
     *
     * @param layersAndNodes layersAndNodes.length = #layers, layersAndNodes[i] = #nodes in layer i
     */
    Neuroevolution(int[] layersAndNodes){
        this.layersAndNodes = layersAndNodes;
        numHiddenLayers = layersAndNodes.length - 1;
        weightsAndBiases = generateRandomNetwork();
    }//constructor

    /**Generates a random network of weights and biases
     *
     * @return double[][][] the array of weights and biases
     */
    double[][][] generateRandomNetwork(){
        return null;
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

    /**Generates a number of mutations of the neural network
     *
     * @param numMutations the number of mutations to create
     * @return double[][][][] an array of network weightsAndBiases
     */
    double[][][][] mutate(int numMutations){
        double[][][][] result = new double[numMutations][numHiddenLayers][][];
        for(int mutation = 0; mutation < numMutations; mutation++){ //initialize results:
            for(int layer = 0; layer < numHiddenLayers; layer++){
                result[mutation][layer] = new double[layersAndNodes[layer]][];
                for(int node = 0; node < layersAndNodes[layer]; node++){
                    result[mutation][layer][node] = new double[layersAndNodes[layer - 1] + 1]; //add 1 for bias
                }
            }
        }
        return result;
    }//mutate
}
