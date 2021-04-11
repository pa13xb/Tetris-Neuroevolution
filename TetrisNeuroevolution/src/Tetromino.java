public interface Tetromino {
    
    public void changePos(int x, int y);
    
    public int[][] getBlocks();
    
    public int getPosX();
    
    public int getPosY();
    
    public int[][] rotate();
}