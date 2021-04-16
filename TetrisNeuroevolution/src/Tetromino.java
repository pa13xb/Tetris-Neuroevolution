/**
 * This is the Tetrimino interface, which handles the Tetrimino type, rotation and position
 * of any Tetrimino piece.
 *
 * COSC 4P80 Final Project, Brock University
 * April 16, 2021
 * @author Philip Akkerman, 5479613, pa13xb@brocku.ca
 * @author David Hasler, 6041321, dh15pd@brocku.ca
 */
public interface Tetromino {
    
    public void changePos(int x, int y);
    
    public int[][] getBlocks();
    
    public int getPosX();
    
    public int getPosY();
    
    public int getColour();

    public int getRotation();

    public int[][] rotate();
}
