public class Square {

    private int posX;
    private int posY;
    private int[][] blocks;

    public Square(){
        posX = 0;
        posY = 4;
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

    public int[][] rotate(){ return blocks; }//rotate
}
