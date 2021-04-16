import java.util.LinkedList;

/**
 * This is the Genetic Algorithm class, which handles the training of a set of weights using genetic evolution.
 * When train() is called, an entire training experiment is run, with the average score of each parent returned as
 * a string. The final weight setup is saved in the private variable weights.
 *
 * COSC 4P80 Final Project, Brock University
 * April 16, 2021
 * @author Philip Akkerman, 5479613, pa13xb@brocku.ca
 * @author David Hasler, 6041321, dh15pd@brocku.ca
 */
class GeneticAlgorithm {
    private double[] weights = new double[12];
    private int highScore;
    private LinkedList<double[]> elites = null;

    GeneticAlgorithm(){
        weights = generateRandomWeights();
        highScore = 0;
    }//first constructor

    GeneticAlgorithm(double[] weights){
        this.weights = weights;
        highScore = 0;
    }//second constructor

    /**
     * Generates a random set of weights using a simple random function between -1 and 1
     * @return the set of weights
     */
    double[] generateRandomWeights(){
        double[] result = new double[weights.length];
        for(int weightNum = 0; weightNum < weights.length; weightNum++){
            result[weightNum] = Math.random() * 2 - 1;
        }
        return result;
    }//generateRandomWeights

    /**
     * Mutates a parent into a new population. Includes options for new random members, retaining the parent, and
     * retaining a number of elites from the previous generation
     * @param numChildren the number of mutated children to create
     * @param numMutations the number of mutations per child
     * @param keepParent whether to keep a copy of the parent in the next populatio n
     * @param parent the parent for the new population
     * @param numRandomMembers the number of additional random children to add
     * @param numElites the number of elites to add from the previous generation (elites are stored in a private variable)
     * @return the new population of weight setups
     */
    double[][] mutate(int numChildren, int numMutations, boolean keepParent, double[] parent, int numRandomMembers, int numElites){
        double[][] result;
        int populationSize = numChildren + numRandomMembers;
        if(elites != null) populationSize += numElites;
        if(keepParent) populationSize++;
        result = new double[populationSize][weights.length];
        for(int mutation = 0; mutation < numChildren; mutation++){ //initialize results:
            double[] mutatedWeights = new double[weights.length];
            for(int weightNum = 0; weightNum < weights.length; weightNum++){
                mutatedWeights[weightNum] = weights[weightNum];
            }
            for(int i = 0; i < numMutations; i++){
                int randIndex = (int)(Math.random() * weights.length);
                mutatedWeights[randIndex] = applyMutation(mutatedWeights[randIndex]); //apply the mutation
            }
            result[mutation] = mutatedWeights;
        }
        if(keepParent) { //add the parent to the population
            result[numChildren] = parent;
            numChildren++;
        }
        for(int i = 0; i < numRandomMembers; i++){ //add new random members to the population
            result[numChildren] = generateRandomWeights();
            numChildren++;
        }
        if(elites != null){
            for(int i = 0; i < numElites; i++){ //add the elites to the population
                result[numChildren] = elites.get(i);
                numChildren++;
            }
        }
        return result;
    }//mutate

    /**
     * Apply a mutation, randomly selected from six different mutation options
     * @param x the value to mutate
     * @return the mutated value
     */
    private double applyMutation(double x){
        int randomSelection = (int)(Math.random() * 6);
        switch(randomSelection){
            case 0:
                return Math.random() * 2 - 1; //random value between -1 and 1
            case 1:
                return Math.random() * 4 - 2; //random value between -2 and 2
            case 2:
                return x * Math.random(); //x * random value between 0 and 1
            case 3:
                return x / Math.random(); //x / random value between 0 and 1
            case 4:
                return (x*2)/Math.random(); // x * 2 / random value between 0 and 1
            case 5:
                return x - Math.random(); // x - random value between 0 and 1
        }
        return Math.random();
    }//applyMutation

    /**
     * This is the primary training method. Training parameters are selected as method parameters, and the results of
     * training are returned as a new-line delimited string. The highest score of all games is recored as a private
     * variable, as is the final weight setup found.
     * @param maxEpochs the number of epochs to run the training experiment
     * @param scoreGoal the score goal to stop training at
     * @param numGamesPerEpoch the number of games each child plays per epoch
     * @param keepParent whether to keep the parent in the next generation
     * @param numChildren the number of children to create each generation
     * @param numMutations the number of mutations per child
     * @param numRandomMembers the number of new random children to add
     * @param tetrominoPosInput whether to use tetrimino position binary board input (unused)
     * @param usingScore whether to use score or time survived as a metric
     * @param numElites number of elites to include in the next population
     * @return the results of training: the best average game score for each epoch as a new-line delimited string
     */
    String train(int maxEpochs, int scoreGoal, int numGamesPerEpoch, boolean keepParent, int numChildren, int numMutations, int numRandomMembers, boolean tetrominoPosInput, boolean usingScore, int numElites){
        String results = "";
        //elites = new double[numElites][weights.length];
        int epoch = 0;
        for(;;){ //each epoch
            double[] parent = weights;
            double[][] weightPopulation = mutate(numChildren, numMutations, keepParent, parent, numRandomMembers, numElites);
            double[] aveNetworkScores = new double[weightPopulation.length];
            for(int networkNum = 0; networkNum < weightPopulation.length; networkNum++){
                setWeights(weightPopulation[networkNum]); //set the weights to this network one for Tetris calculation purposes
                int averageScore = 0;
                for(int gameNum = 0; gameNum < numGamesPerEpoch; gameNum++){
                    Tetris tetris = new Tetris(false, false, null, this, tetrominoPosInput, false, true, false, weights);
                    if(usingScore) {
                        averageScore += tetris.getScore();
                        if(tetris.getScore() > highScore) highScore = tetris.getScore();
                    }
                    else{
                        averageScore += tetris.getTimeSurvived();
                        if(tetris.getTimeSurvived() > highScore) highScore = tetris.getTimeSurvived();
                    }
                }
                aveNetworkScores[networkNum] = averageScore / (double)numGamesPerEpoch;
            }
            double bestScore = 0;
            elites = new LinkedList<>();
            LinkedList<Double> eliteScores = new LinkedList<Double>();
            for(int weightNum = 0; weightNum < weightPopulation.length; weightNum++){
                if(elites.size() == 0){ //if elites is empty we add the first score we see
                    elites.add(weightPopulation[weightNum]);
                    eliteScores.add(aveNetworkScores[weightNum]);
                }
                else for(int i = 0; i < elites.size(); i++) {
                    if(aveNetworkScores[weightNum] > eliteScores.get(i)){
                        // else we check to see if elites as first position is less than next weight,
                        //
                        elites.add(i,weightPopulation[weightNum]);
                        eliteScores.add(i,aveNetworkScores[weightNum]);
                        if(elites.size() > numElites) {
                            elites.removeLast();
                            eliteScores.removeLast();
                        }
                        break;
                    }
                    else if(elites.size() < numElites){
                        elites.addLast(weightPopulation[weightNum]);
                        eliteScores.addLast(aveNetworkScores[weightNum]);
                        break;
                    }
                }
                if(aveNetworkScores[weightNum] > bestScore){
                    bestScore = aveNetworkScores[weightNum];
                    setWeights(weightPopulation[weightNum]); //make best one the parent for next time
                }
            }
            epoch++;
            results = results.concat(bestScore+"\n");
            System.out.println("Epoch "+epoch+" complete, best network's average score = "+bestScore);
            if(epoch >= maxEpochs && maxEpochs != -1) break;
            if(bestScore >= scoreGoal && scoreGoal != -1) break;
            for(int i = 0; i < weights.length; i++){
                System.out.print(weights[i]+"\t");
            }
            System.out.println();
        }//epoch end
        return results;
    }

    /**
     * Getter for weights
     * @return the weight setup
     */
    double[] getWeights() {
        return weights;
    }//getWeights

    /**
     * Setter for weights
     * @param weights the weight setup to set
     */
    void setWeights(double[] weights) {
        this.weights = weights;
    }//setWeights

    /**
     * Getter for highScore
     * @return the highest score achieved in any game during testing
     */
    public int getHighScore() {
        return highScore;
    }//getHighScore
}
