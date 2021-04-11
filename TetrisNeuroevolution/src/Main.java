import javax.swing.*;
import java.util.Scanner;

class Main {

    private Scanner scanner;
    private Neuroevolution neuralNet;

    int tileSize = 30;
    int width = 10;
    int height = 20;

    private Main(){
        scanner = new Scanner(System.in);
        getNeuralNetwork();
        Tetris tetris = new Tetris(true);
    }//constructor

    private void getNeuralNetwork(){
        int[] layerAndNodes = {40, 20, 5};
        neuralNet = new Neuroevolution(layerAndNodes);
    }

    public static void main(String[] args) {
        new Main();
    }
}