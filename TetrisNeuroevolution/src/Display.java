import javax.swing.*;
import java.awt.*;

class Display extends JPanel {

    private int tileSize;
    private int[][] gameBoard;
    private int width;
    private int height;

    Display(int xTiles, int yTiles, int tileSize, int[][] gameBoard){
        this.tileSize = tileSize;
        setSize(width*tileSize+1,height*tileSize+1);
        setBounds(0,0,xTiles*tileSize+1,yTiles*tileSize+1);
        this.gameBoard = gameBoard;
    }//Display Constructor

    /**This is the function called automatically when Swing paints the window
     *
     * @param graphics the default graphics item
     */
    public void paint(Graphics graphics){
        paintTiles(graphics);
    }//paint

    private void paintTiles(Graphics graphics){
        int xSize = map.getSizeX();
        int ySize = map.getSizeY();
        Image image;
        Tile[][] tiles = map.getTiles();
        Terrain[][] terrains = map.getTerrains();
        Building[][] buildings = map.getBuildings();
        Unit[][] units = map.getUnits();

        String path;
    }

    private Image getImage(String path,String folder){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        if(path != "") {
            return toolkit.getImage("C:\\Users\\phili\\Google_Drive\\Coding_Projects\\ColonizationRaw\\src\\Assets\\"+folder+"\\"+path+".png");
        }
        else return null;
    }//drawSprite

    public void setGameBoard(int[][] gameBoard){
        this.gameBoard = gameBoard;
    }

    public void mainMenu(Graphics graphics){

    }
}
