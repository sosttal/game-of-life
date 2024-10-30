import java.util.concurrent.CountDownLatch;

/**
 * Controller for Game of Life: 
 * 
 * <p> Controls and connects game simulation ({@link #Model}) and GUI ({@link #View}).
 * 
 * @author <a href="https://github.com/sosttal">Sondre S Talleraas</a>
 */
public class Controller {
    // fields
    Model game;                 // game-simulation
    View gui;                   // GUI
    int genNum;                 // generation number
    int rowCount;               // number of rows
    int colCount;               // number of columns
    long delay;                 // delay for update-loop
    UpdateThread updateLoop;    // update-loop thread
    CountDownLatch rowLock;     // lock (sync barrier) for threads working on rows

    boolean running = false;    // controls the update-loop

    /**
     * 
     * @param rowCount
     * @param colCount
     * @param delay
     */
    public Controller(int rowCount, int colCount, long delay){
        // init vars
        this.rowCount = rowCount;
        this.colCount = colCount;
        this.delay = delay;
        this.genNum = 0; // TODO implement tracker in View
        this.game = new Model(rowCount, colCount);
        this.gui = new View();
        
        // generates 0th gen and connects cells
        this.game.randomStartGrid();
        this.game.connectCells();
    }

    /**
     * Method to draw the game-grid in terminal.
     */
    public void terminalDraw(){
        this.game.drawGrid();

        // prints gen number
        System.out.println("Generation: " + this.genNum);
    }

    /**
     * Method to update GUI:
     * 
     * <p> Iterates through cells and updates their status.
     */
    public void updateGUI(){
        this.rowLock = new CountDownLatch(rowCount); // init rowlock

        for (int row = 0; row < this.rowCount; row++){
            int r = row; // to enable use in lambda expression

            Thread guiUpdater = new Thread(() -> {
                for (int col = 0; col < this.colCount; col++){
                    if (this.game.getCell(r, col).isAlive()){ // gui-grid is updated according to a given cell's status
                        gui.setAlive(r, col);
    
                    } else{
                        gui.setDead(r, col);
    
                    }
                    this.rowLock.countDown();

                }

            });
            guiUpdater.start();

        }
        
        try{ this.rowLock.await(); } catch(InterruptedException e){} // wait for guiUpdater threads to finish

        gui.setLivingCount(this.livingCount());
    }

    /**
     * Method to set cell in given position to alive in the game-simulation.
     * 
     * @param row - row of cell
     * @param col - column of cell
     */
    public void setAlive(int row, int col){
        this.game.setAlive(row, col);
    }
    
    /**
     * Method to set cell in given position to dead in the game-simulation.
     * 
     * @param row - row of cell
     * @param col - column of cell
     */
    public void setDead(int row, int col){
        this.game.setDead(row, col);
    }

    /**
     * Method to regenerate 0th cell-generation
     */
    public void regenCells(){
        this.genNum = 0; // reset generation counter
        this.game.randomStartGrid();
        this.game.connectCells();
        this.updateGUI();
    }

    /**
     * Method for full update (simulation and gui).
     */
    public void update(){
        this.rowLock = new CountDownLatch(rowCount); // init rowlock
        
        // loop to recount each cells number of living neighbours (needed to determine new status)
        for (int row = 0; row < this.rowCount; row++){
            int r = row; // to enable use in lambda expression
            
            Thread neighbourChecker = new Thread(() -> {
                for (int col = 0; col < this.colCount; col++){
                    Cell cell = this.game.getCell(r, col); // get cell from current position
                    
                    cell.countNeighbours(); // perform recount

                }
                this.rowLock.countDown();

            });
            neighbourChecker.start();

        }

        try{ this.rowLock.await(); } catch(InterruptedException e){} // wait for neighbourChecker threads to finish

        this.rowLock = new CountDownLatch(rowCount); // reinit rowlock

        // loop to set new status of each cell
        for (int row = 0; row < this.rowCount; row++){
            int r = row; // to enable use in lambda expression

            Thread statusUpdater = new Thread(() -> {
                for (int col = 0; col < this.colCount; col++){
                    Cell cell = this.game.getCell(r, col); // get cell from current position

                    cell.updateStatus();

                }
                this.rowLock.countDown();

            });
            statusUpdater.start();

        }
        
        try{ this.rowLock.await(); } catch(InterruptedException e){} // wait for statusUpdater threads to finish

        this.genNum++;

        this.updateGUI();
    }

    /**
     * Wrapper-method to initialize GUI.
     */
    public void init(){
        this.gui.init(this, rowCount, colCount);
    }

    /**
     * Method to set user setting.
     */
    public void settings(){ // dimensions of grid, update freq , ...?
        // TODO implement settings prompt
    }

    /**
     * Method to retrieve living-count from simulation.
     * 
     * @return - number of living cells
     */
    public int livingCount(){
        return this.game.livingCount();
    }

    /**
     * Method to check if update-loop is running.
     * 
     * @return - true if update-loop is running, otherwise false
     */
    public boolean running(){
        return this.running;

    }

    /**
     * Method to start/stop update-loop (see {@link #UpdateThread}).
     */
    public void startStop(){
        if (this.running){
            this.updateLoop.interrupt();

            this.running = false;
        } else{
            this.updateLoop = new UpdateThread(this.delay, this);
            this.updateLoop.start();

            this.running = true;
        }
        
    }

    /**
     * Method to kill all cells (clear the game-grid).
     */
    public void killAll(){ // TODO optimize with threads?
        for (int row = 0; row < this.rowCount; row++){
            for (int col = 0; col < this.colCount; col++){
                this.game.setDead(row, col);
            }
        }
    }
    
}
