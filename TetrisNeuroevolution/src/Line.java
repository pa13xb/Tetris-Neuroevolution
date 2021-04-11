public class Line implements Tetromino{
    private int posX;
    private int posY;
    private int[][] blocks;
    private int rotation;

    public Line() {
        rotation = 0;
        posX = 4;
        posY = 0;
        blocks = new int[4][2];
        setBlocks(posX, posY);
    }//constructor

    public void changePos(int newX, int newY) {
        posX = newX;
        posY = newY;
        setBlocks(newX, newY);
    }//changePos

    private void setBlocks(int x, int y) {
        if (rotation == 0) {
            blocks[3][0] = x - 1; //Left 1
            blocks[3][1] = y;

            blocks[0][0] = x; //center
            blocks[0][1] = y;

            blocks[1][0] = x + 1; //right 1
            blocks[1][1] = y;

            blocks[2][0] = x + 2; //right 2
            blocks[2][1] = y;
        } else {
            blocks[3][0] = x;
            blocks[3][1] = y - 2; //top

            blocks[2][0] = x;
            blocks[2][1] = y - 1; //up 1

            blocks[0][0] = x; //center
            blocks[0][1] = y;

            blocks[1][0] = x; //bottom
            blocks[1][1] = y + 1;
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
        return 5;
    }

    public int[][] rotate() {
        rotation++;
        if (rotation == 2) rotation = 0;
        setBlocks(posX, posY);
        return blocks;
    }//rotate
}
