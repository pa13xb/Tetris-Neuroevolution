class GeneticAlgorithm {
    private double[] weights = new double[12];
    private int highScore;

    GeneticAlgorithm(){
        weights = generateRandomWeights();
        highScore = 0;
    }//first constructor

    GeneticAlgorithm(double[] weights){
        this.weights = weights;
        highScore = 0;
    }//second constructor

    double[] generateRandomWeights(){
        double[] result = new double[weights.length];
        for(int weightNum = 0; weightNum < weights.length; weightNum++){
            result[weightNum] = Math.random() * 2 - 1;
        }
        return result;
    }

    /**
     * Creates a number of mutated weights
     * @param numChildren
     * @param numMutations
     * @param keepParent
     * @param parent
     * @param numRandomMembers
     * @return
     */
    double[][] mutate(int numChildren, int numMutations, boolean keepParent, double[] parent, int numRandomMembers){
        double[][] result;
        int populationSize = numChildren + numRandomMembers;
        if(keepParent) populationSize++;
        result = new double[populationSize][weights.length]; //+1 for parent
        for(int mutation = 0; mutation < numChildren; mutation++){ //initialize results:
            double[] mutatedWeights = new double[weights.length];
            for(int weightNum = 0; weightNum < weights.length; weightNum++){
                mutatedWeights[weightNum] = weights[weightNum];
            }
            for(int i = 0; i < numMutations; i++){
                int randIndex = (int)(Math.random() * weights.length);
                mutatedWeights[randIndex] = applyMutation();
            }
            result[mutation] = mutatedWeights;
        }
        if(keepParent) {
            result[numChildren] = parent;
            numChildren++;
        }
        for(int i = 0; i < numRandomMembers; i++){ //add new random members to the population
            result[numChildren + i] = generateRandomWeights();
        }
        return result;
    }//mutate

    /**
     * Apply a mutation (this could be modified to include new mutation techniques in the future)
     * @return double the mutated value
     */
    private double applyMutation(){
        return Math.random() * 2 - 1; //random value between -1 and 1
    }//applyMutation

    String train(int maxEpochs, int scoreGoal, int numGamesPerEpoch, boolean keepParent, int numChildren, int numMutations, int numRandomMembers, boolean tetrominoPosInput, boolean usingScore){
        String results = "";
        int epoch = 0;
        for(;;){ //each epoch
            double[] parent = weights;
            double[][] weightPopulation = mutate(numChildren, numMutations, keepParent, parent, numRandomMembers);
            double[] aveNetworkScores = new double[weightPopulation.length];
            for(int networkNum = 0; networkNum < weightPopulation.length; networkNum++){
                setWeights(weightPopulation[networkNum]); //set the weights to this network one for Tetris calculation purposes
                double averageError = 0.0;
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
                aveNetworkScores[networkNum] = averageError / (double)numGamesPerEpoch;
            }
            double bestScore = 0;
            for(int weightNum = 0; weightNum < weightPopulation.length; weightNum++){
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
        }//epoch end
        return results;
    }

    double[] getWeights() {
        return weights;
    }

    void setWeights(double[] weights) {
        this.weights = weights;
    }

    public int getHighScore() {
        return highScore;
    }
}
