/**
 * This is the Square class, which handles the Tetrimino type, rotation and position
 * of a square piece.
 *
 * COSC 4P80 Final Project, Brock University
 * April 16, 2021
 * @author Philip Akkerman, 5479613, pa13xb@brocku.ca
 * @author David Hasler, 6041321, dh15pd@brocku.ca
 */
public class Square implements Tetromino{

    private int posX;
    private int posY;
    private int[][] blocks;

    public Square(){
        posX = 4;
        posY = 2;
        blocks = new int[4][2];
        setBlocks(posX, posY);
    }//constructor

    public void changePos(int newX, int newY){
        posX = newX;
        posY = newY;
        setBlocks(newX, newY);
    }//changePos

    private void setBlocks(int x, int y){
        blocks[0][0] = x;
        blocks[0][1] = y;
        
        blocks[1][0] = x + 1;
        blocks[1][1] = y;

        blocks[2][0] = x;
        blocks[2][1] = y + 1;

        blocks[3][0] = x + 1;
        blocks[3][1] = y + 1;
    }//setBlocks

    public int[][] getBlocks() {
        return blocks;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }
    
    public int getColour(){
        return 4;
    }

    public int getRotation(){ return 0;}
    
    public int[][] rotate(){ return blocks; }//rotate
}
