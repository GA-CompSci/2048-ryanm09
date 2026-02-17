import java.util.ArrayList;
import java.util.Random;

/**
 * Game class for 2048
 * Contains all game logic and state management
 * 
 */
public class Game {
    private static final int BOARD_SIZE = 4;
    private static final int WIN_VALUE = 2048;
    
    private int[][] board;
    private int score;
    private Random random;
    private boolean hasWon;
    private boolean gameOver;
    
    /**
     * Constructor - initializes a new game
     */
    public Game() {
        random = new Random();
        resetGame();
    }
    
    /**
     * Resets the game to initial state
     */
    public void resetGame() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        score = 0;
        hasWon = false;
        gameOver = false;
        
        // Add two initial tiles
        addRandomTile();
        addRandomTile();
    }
    
    /**
     * Requirements:
     * - 90% chance of adding a 2
     * - 10% chance of adding a 4
     * - Should only add to empty cells
     * - Use the getEmptyCells() method to find empty positions
     * 
     * Hint: Use random.nextInt(10) < 9 for 90% probability
     */
    private void addRandomTile() {

        //check to see if there are any empty cells on the board
        ArrayList<int[]> emptyCells = getEmptyCells();
        if(emptyCells.isEmpty()) return; //shut off the method if board is full
        
        //emptyCells.get(0);
        //{row, col}

        //pick a random empty cell
        int[] spot = emptyCells.get((int)(Math.random() * emptyCells.size()));

        //90% chance of 2, 10% chance of 4
        //fancy method: tunary operator
        board[spot[0]][spot[1]] = (random.nextInt(10) < 9) ? 2 : 4;
        
    }  
    
    /**
     * Requirements:
     * - Return an ArrayList of int arrays [row, col] for each empty cell
     * - A cell is empty if its value is 0
     * 
     * Hint: Loop through the board and check each cell
     */
    private ArrayList<int[]> getEmptyCells() {
        ArrayList<int[]> emptyCells = new ArrayList<>();
        //2D Loop
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[0].length; col++){
                if(board[row][col] == 0) emptyCells.add(new int[] {row, col});
            }
        }   
        return emptyCells;
    }
    
    /**
     * Requirements:
     * - Slide all tiles to the left (remove gaps)
     * - Merge adjacent tiles with same value
     * - Each tile can only merge once per move
     * - Update score when tiles merge (add merged value to score)
     * - Add a random tile after a successful move
     * - Return true if any tiles moved, false otherwise
     * 
     * Algorithm hints:
     * 1. For each row:
     *    a. Compress tiles to the left (remove zeros)
     *    b. Merge adjacent equal tiles
     *    c. Check if the row changed
     * 2. If any row changed, add a random tile
     */
    public boolean moveLeft() {
        boolean moved = false;
        
        //check through every row
        for(int row = 0; row < board.length; row++){
            //temporary tool to reorganize the numbers
            int[] temp = new int[BOARD_SIZE];

            //SMARTER COPY: only copy over non-zeros, effectively shifting to the left
            int copyCount = 0;
            for(int col = 0; col < board[0].length; col++){
                if(board[row][col] != 0) temp[copyCount++] = board[row][col];
            }
            
            //HARD PART - MERGE AND STUFF
            for(int col = 0; col < board[0].length - 1; col++){
                //merge + scooch
                if(temp[col] == temp[col + 1]){
                    temp[col] = temp[col] * 2;
                    //merged items count for your total points
                    score += temp[col];
                    //scooch everything over once
                    for(int scooch = col + 1; scooch < board[0].length - 1; scooch++){
                        temp[scooch] = temp[scooch + 1];
                        
                    }
                    //add a zero at the end
                    temp[board[0].length - 1] = 0;
                }
            }
            
            //check for differences
            for(int col = 0; col < board[0].length; col++){
                if(temp[col] != board[row][col]){
                    moved = true;
                    board[row] = temp;
                }
            }
            
        }
        if(moved) addRandomTile();
        return moved;
    }
    
    /**
     * Requirements:
     * - Similar to moveLeft but in opposite direction
     * - Slide tiles to the right
     * - Merge from right to left
     * 
     * Hint: Process from right to left instead of left to right
     */
    public boolean moveRight() {
        boolean moved = false;
        
        //check through every row
        for(int row = 0; row < board.length; row++){
            //temporary tool to reorganize the numbers
            int[] temp = new int[BOARD_SIZE];

            //SMARTER COPY: only copy over non-zeros, effectively shifting to the right
            int copyCount = BOARD_SIZE - 1;
            for(int col = BOARD_SIZE - 1; col > -1; col--){
                if(board[row][col] != 0) 
                    temp[copyCount--] = board[row][col];
            }
            
            //HARD PART - MERGE AND STUFF
            for(int col = BOARD_SIZE - 1; col > 0; col--){
                // Are you the same as the number before?
                if(temp[col] == temp[col - 1]){
                    temp[col] = temp[col] * 2;
                    //merged items count for your total points
                    score += temp[col];
                    //scooch everything over once
                    for(int scooch = col-1; scooch > 0; scooch--){
                        temp[scooch] = temp[scooch - 1];
                        
                    }
                    //add a zero at the end
                    temp[0] = 0;
                }
            }
            
            //check for differences
            for(int col = BOARD_SIZE- 1; col > -1; col--){
                if(temp[col] != board[row][col]){
                    moved = true;
                    board[row] = temp;
                }
            }
            
        }
        if(moved) addRandomTile();
        return moved;
    }

    /**

     * Requirements:
     * - Similar logic to moveLeft but operates on columns
     * - Slide tiles up
     * - Merge from top to bottom
     * 
     * Hint: Work with columns instead of rows
     */
    public boolean moveUp() {
        boolean moved = false;

        //check through every column
        for(int col = 0; col < board[0].length; col++){
            //temporary tool to reorganize the numbers
            int[] temp = new int[BOARD_SIZE];

            //SMARTER COPY: only copy over non-zeros, effectively shifting to the left
            int copyCount = 0;
            for(int row = 0; row < board.length; row++){
                if(board[row][col] != 0) 
                    temp[copyCount++] = board[row][col];
            }
            
            //HARD PART - MERGE AND STUFF
            for(int row = 0; row < board.length - 1; row++){
                //merge + scooch
                if(temp[row] == temp[row + 1]){
                    temp[row] = temp[row] * 2;
                    //merged items count for your total points
                    score += temp[row];
                    //scooch everything over once
                    for(int scooch = row + 1; scooch < board.length; scooch++){
                        temp[scooch] = temp[scooch + 1];
                        
                    }
                    //add a zero at the end
                    temp[board[0].length - 1] = 0;
                }
            }
            
            //check for differences
            for(int row = 0; row < board[0].length; row++){
                if(temp[row] != board[row][col]){
                    moved = true;
                    board[row] = temp;
                }
            }
            // copy the contents of temp back to the column
            
            
        }

        if(moved) addRandomTile();
        return moved;
    }
    
    /**
     * TODO #6: Implement the moveDown method
     * Requirements:
     * - Similar to moveUp but in opposite direction
     * - Slide tiles down
     * - Merge from bottom to top
     */
    public boolean moveDown() {
        // TODO: Complete this method
        boolean moved = false;
        
        return moved;
    }
    
    /**
     * TODO #7: Implement method to check if the player has won
     * Requirements:
     * - Return true if any tile has value >= WIN_VALUE (2048)
     * - Once won, should continue returning true (use hasWon field)
     * 
     * Hint: Check all tiles and update the hasWon field
     */
    public boolean hasWon() {
        // TODO: Complete this method
        
        return false;
    }
    
    /**
     * TODO #8: Implement method to check if game is over
     * Requirements:
     * - Game is over when:
     *   1. No empty cells remain AND
     *   2. No adjacent tiles (horizontal or vertical) can be merged
     * - Update the gameOver field when game ends
     * 
     * Hint: First check for empty cells, then check all adjacent pairs
     */
    public boolean isGameOver() {
        // TODO: Complete this method
        
        return false;
    }
    
    // ===================== PROVIDED METHODS - DO NOT MODIFY =====================
    
    /**
     * Gets a copy of the current board state
     */
    public int[][] getBoard() {
        int[][] copy = new int[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }
    
    /**
     * Gets the current score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Gets the board size
     */
    public int getBoardSize() {
        return BOARD_SIZE;
    }
    
    /**
     * Helper method for debugging - prints the board to console
     */
    public void printBoard() {
        System.out.println("Score: " + score);
        System.out.println("-------------");
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.printf("%4d ", board[i][j]);
            }
            System.out.println();
        }
        System.out.println("-------------");
    }
}