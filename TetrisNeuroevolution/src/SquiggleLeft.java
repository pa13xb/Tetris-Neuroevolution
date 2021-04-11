public class SquiggleLeft {
    private int posX;
    private int posY;
    private int[][] blocks;
    private int rotation;

    public SquiggleLeft() {
        rotation = 0;
        posX = 0;
        posY = 4;
        setBlocks(posX, posY);
    }//constructor

    public void changePos(int newX, int newY) {
        posX = newX;
        posY = newY;
        setBlocks(newX, newY);
    }//changePos

    private void setBlocks(int x, int y) {
        if (rotation == 0) {
            blocks[0][0] = x; //center
            blocks[0][1] = y;

            blocks[1][0] = x - 1; //left
            blocks[1][1] = y;

            blocks[2][0] = x; //down
            blocks[2][1] = y + 1;

            blocks[3][0] = x + 1; //down right
            blocks[3][1] = y + 1;
        } else {
            blocks[0][0] = x; //center
            blocks[0][1] = y;

            blocks[1][0] = x; //down
            blocks[1][1] = y + 1;

            blocks[2][0] = x + 1; //right
            blocks[2][1] = y;

            blocks[3][0] = x + 1; //up right
            blocks[3][1] = y - 1;
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

    public int[][] rotate() {
        rotation++;
        if (rotation == 2) rotation = 0;
        setBlocks(posX, posY);
        return blocks;
    }//rotate
}
