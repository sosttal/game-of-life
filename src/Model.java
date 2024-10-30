import java.util.concurrent.CountDownLatch;

/**
 * Game of Life simulation.
 * 
 * <p> Keeps track of all cells, and their status.
 * 
 * @author <a href="https://github.com/sosttal">Sondre S Talleraas</a>
 */
public class Model {
    // fields
    int rowCount;
    int colCount;
    Cell[][] grid;
    CountDownLatch rowLock;

    /**
     * Creates a new Game of Life model/simulation.
     * 
     * <p> Takes dimensions of game-grid as parameters.
     * 
     * @param rowCount - number of rows
     * @param colCount - number of columns
     */
    public Model(int rowCount, int colCount){
        this.rowCount = rowCount;
        this.colCount = colCount;
        // init grid
        this.grid = new Cell[rowCount][colCount];
    }

    /**
     * Helper method for generating 0th gen.
     * 
     * @param row - row of new cell
     * @param col - column of new cell
     */
    private void createCell(int row, int col){
        Cell newCell = new Cell();

        // 1/3 chance to be alive
        if (Math.random() <= 0.333){ 
            newCell.setAlive();
        }
        grid[row][col] = newCell;
    }

    /**
     * Generates 0th generation of cells.
     */
    public void randomStartGrid(){
        this.rowLock = new CountDownLatch(this.rowCount);

        for (int row = 0; row < this.rowCount; row++){
            int r = row;
            
            Thread cellGod = new Thread(() -> {
                for (int col = 0; col < this.colCount; col++){
                    this.createCell(r, col);
                }  
                this.rowLock.countDown();

            });
            cellGod.start();
            
        }
        try { rowLock.await(); } catch(InterruptedException e) {}

    }

    // motode for å hente celle, returnerer Celle-objekt på angitt posisjon eller null
    /**
     * Gets Cell-object at given position.
     * 
     * @param row - row of cell
     * @param col - column of cell
     * @return - Cell-object at given position (if index is valid)
     */
    public Cell getCell(int row, int col){
        if (row < this.rowCount && row >= 0 && col < this.colCount && col >= 0){
            return grid[row][col];
        }
        else { // invalid index
            return null;
        }
    }

    /**
     * Method to print terminal-representation of game-grid.
     */
    public void drawGrid(){
        // padding before game-grid
        for (int i = 0; i <= 10; i++){
            System.out.println();
        }
        
        // fetches and prints status-symbol for each cell in grid TODO: optimize with threads?
        for (int row = 0; row < this.rowCount; row++){
            for (int col = 0; col < this.colCount; col++){
                char statusSym = this.grid[row][col].getStatusSymbol();
                System.out.print(statusSym);
            }
            System.out.println();
        }
    }

    /**
     * Updates neighbours for given cell.
     * 
     * @param row - row of cell to be updated
     * @param col - column og cell to be updated
     */
    public void setNeighbours(int row, int col){
        if (this.getCell(row, col) != null){
            int prvRow = row-1;
            int nxtRow = row+1;

            // neighbours at previous row
            for (int i = -1; i <= 1; i++){ 
                Cell neighbour = this.getCell(prvRow,col+i);
                if (neighbour != null){
                    grid[row][col].addNeighbour(neighbour);
                }
            }
        
            // neighbours at same row
            for (int i = -1; i <= 1; i+=2){
                Cell neighbour = this.getCell(row,col+i);
                if (neighbour != null){
                    grid[row][col].addNeighbour(neighbour);
                }
            }

            // neighbours at next row
            for (int i = -1; i <= 1; i++){
                Cell neighbour = this.getCell(nxtRow,col+i);
                if (neighbour != null){
                    grid[row][col].addNeighbour(neighbour);
                }
            }
            
        }
    }

    /**
     * Connects all cells in the grid.
     * 
     * <p> Calls {@link #setNeighbours} on all cells.
     */
    public void connectCells(){
        this.rowLock = new CountDownLatch(this.rowCount);

        for (int row = 0; row < this.rowCount; row++){
            int r = row;

            Thread neighbourhoodWatch = new Thread(() -> {
                for (int col = 0; col < this.colCount; col++){
                    this.setNeighbours(r, col);
                }
                this.rowLock.countDown();
            });
            neighbourhoodWatch.start();

        }
        try { this.rowLock.await(); } catch(InterruptedException e) {}
    }

    /**
     * Counts all living cells.
     * 
     * @return - nubmer of living cells
     */
    public int livingCount(){ // TODO do counting when updating/setting status? (alternatively optimize with threads)
        int amt = 0;

        for (int row = 0; row < this.rowCount; row++){
            for (int col = 0; col < this.colCount; col++){
                if (grid[row][col].isAlive()){ 
                    amt++;
                }
            }
        }
        return amt;
    }

    /**
     * Sets cell in given position to alive.
     * 
     * @param row - row of cell
     * @param col - column of cell
     */
    public void setAlive(int row, int col){
        this.grid[row][col].setAlive();
    }
    
    /**
     * Sets cell in given position to dead.
     * 
     * @param row - row of cell
     * @param col - column of cell
     */
    public void setDead(int row, int col){
        this.grid[row][col].setDead();
    }
}
