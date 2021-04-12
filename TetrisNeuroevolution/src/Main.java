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
        neuralNet.train(100,1000,10,true,10,30);
        System.out.println("Highest score in training was "+neuralNet.getHighScore());
        //Tetris tetris = new Tetris(true, true, null);
        Tetris tetris = new Tetris(true, false, neuralNet);
    }//constructor

    private void getNeuralNetwork(){
        int[] layerAndNodes = {200*2 + 4 + 7, 150, 30, 4};
        neuralNet = new Neuroevolution(layerAndNodes);
    }

    public static void main(String[] args) {
        new Main();
    }
}