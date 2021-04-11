import java.util.Scanner;

class Main {

    private Scanner scanner;
    private Neuroevolution neuralNet;

    private Main(){
        scanner = new Scanner(System.in);
        getNeuralNetwork();
    }//constructor

    private void getNeuralNetwork(){
        int[] layerAndNodes = {40, 20, 5};
        neuralNet = new Neuroevolution(layerAndNodes);
    }

    public static void main(String[] args) {
        new Main();
    }
}