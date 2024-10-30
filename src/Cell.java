/**
 * Cell-class used by the Game of Life simulation ({@link #Model}).
 * 
 * <p> Represents individual Cell-objects.
 * 
 * @author <a href="https://github.com/sosttal">Sondre S Talleraas</a>
 */
public class Cell {
    // fields
    boolean alive;
    Cell[] neighbours;
    int neighbourCount;
    int livingNeighbourCount;
    
    /**
     * Initiates new Cell.
     * 
     */
    public Cell(){
        this.alive = false;
        this.neighbours = new Cell[8];
        this.neighbourCount = 0;
        this.livingNeighbourCount = 0;
    }

    /**
     * Sets the cell's status to dead.
     */
    public void setDead(){
        this.alive = false;
    }

    /**
     * Sets the cell's status to alive.
     */
    public void setAlive(){
        this.alive = true;
    }

    /**
     * To check status of cell.
     * 
     * @return - true if cell is alive, false if it's dead
     */
    public boolean isAlive(){
        return this.alive;
    }

    /**
     * Get status-symbol corresponding to cells status.
     * 
     * @return - 'O' if cell is alive, '.' if it's dead
     */
    public char getStatusSymbol(){
        if (this.isAlive()){
            return 'O';
        } 
        else {
            return '.';
        }
    }

    /**
     * To add a given cell to this cell's neihbour-list.
     * 
     * @param neighbour - cell to be added as neighbour
     */
    public void addNeighbour(Cell neighbour){
        this.neighbours[this.neighbourCount] = neighbour;
        this.neighbourCount += 1;
    }

    /**
     * Checks and updates number of living neighbours.
     */
    public void countNeighbours(){
        // resets count before recount
        this.livingNeighbourCount = 0;
        
        for (int  i = 0; i < this.neighbours.length; i++){
            if (this.neighbours[i] != null && this.neighbours[i].isAlive()){
                this.livingNeighbourCount ++;
            }
        }
    }

    /**
     * Updates a cell's status based on the game rules:
     * 
     * <p> If cell is alive AND has less than two (underpopulation) 
     * or more than 3 (overpopulation), the cell is killed.
     * 
     * <p> If cell is dead AND has exactly 3 living neighbours (repopulation), the cell is revived.
     * 
     */
    public void updateStatus(){
        if (this.isAlive()){
            if (this.livingNeighbourCount < 2 || this.livingNeighbourCount > 3){
                this.alive = false;
            }
        } 
        else {
            if (this.livingNeighbourCount == 3){ 
                this.alive = true;
            }
        }
    }
}
