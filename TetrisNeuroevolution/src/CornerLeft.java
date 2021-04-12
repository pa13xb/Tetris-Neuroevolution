public class CornerLeft implements Tetromino{

    private int posX;
    private int posY;
    private int rotation;
    private int[][] blocks;

    public CornerLeft(){
        rotation = 0;
        posX = 4;
        posY = 0;
        blocks = new int[4][2];
        setBlocks(posX, posY);
    }//constructor

    public void changePos(int newX, int newY){
        posX = newX;
        posY = newY;
        setBlocks(newX, newY);
    }//changePos

    private void setBlocks(int x, int y){
        if(rotation == 0) {
            blocks[0][0] = x;
            blocks[0][1] = y;

            blocks[1][0] = x - 1;
            blocks[1][1] = y;

            blocks[2][0] = x + 1;
            blocks[2][1] = y;

            blocks[3][0] = x - 1;
            blocks[3][1] = y + 1;
        }
        else if(rotation == 1){
            blocks[0][0] = x;
            blocks[0][1] = y;

            blocks[1][0] = x;
            blocks[1][1] = y - 1;

            blocks[2][0] = x;
            blocks[2][1] = y + 1;

            blocks[3][0] = x - 1;
            blocks[3][1] = y - 1;
        }
        else if(rotation == 2){
            blocks[0][0] = x;
            blocks[0][1] = y;

            blocks[1][0] = x - 1;
            blocks[1][1] = y;

            blocks[2][0] = x + 1;
            blocks[2][1] = y;

            blocks[3][0] = x + 1;
            blocks[3][1] = y - 1;
        }
        else {
            blocks[0][0] = x;
            blocks[0][1] = y;

            blocks[1][0] = x;
            blocks[1][1] = y - 1;

            blocks[2][0] = x;
            blocks[2][1] = y + 1;

            blocks[3][0] = x + 1;
            blocks[3][1] = y + 1;
        }
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
        return 1;
    }

    public int getRotation(){ return rotation;}

    public int[][] rotate(){
        rotation++;
        if(rotation == 4) rotation = 0;
        setBlocks(posX, posY);
        return blocks;
    }//rotate
}