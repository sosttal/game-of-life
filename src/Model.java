import java.util.concurrent.CountDownLatch;

/**
 * GoLModell-klasse:
 * Representerer rutenettet som utgjør spillbrettet
 * 
 * @author Sondre S Talleraas
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
    public Cell getCell(int rad, int kol){
        if (rad < this.rowCount && rad >= 0 && kol < this.colCount && kol >= 0){ // sjekker om angitt posisjon er gyldig indeks i rutenettet - returnerer objektet som ligger der
            return grid[rad][kol];
        }
        else { // ved ugyldig indeks
            return null;
        }
    }

    // metode for å tegne rutenett
    public void tegnRutenett(){
        // printer tomme linjer for luft før spillbrettet
        for (int i = 0; i <= 10; i++){
            System.out.println();
        }
        
        for (int rad = 0; rad < this.rowCount; rad++){
            for (int kol = 0; kol < this.colCount; kol++){
                // henter statustegn
                char statusTegn = this.grid[rad][kol].hentStatusTegn();
                // skriver ut tegnet til terminalen
                System.out.print(statusTegn);
            }
            System.out.println();
        }
    }

    // metode for å oppdatere naboer for angitt celle
    public void setNeighbours(int row, int col){
        if (this.getCell(row, col) != null){
            int prvRow = row-1;
            int nxtRow = row+1;

            for (int i = -1; i <= 1; i++){ // løkke for naboer på rad-1
                Cell neighbour = this.getCell(prvRow,col+i);
                if (neighbour != null){
                    grid[row][col].leggTilNabo(neighbour);
                }
            }
        

            for (int i = -1; i <= 1; i+=2){ // løkke for naboer på samme rad (hopper over den aktuelle cellens egen indeks)
                Cell neighbour = this.getCell(row,col+i);
                if (neighbour != null){
                    grid[row][col].leggTilNabo(neighbour);
                }
            }

            for (int i = -1; i <= 1; i++){ // løkke for naboer på rad+1
                Cell neighbour = this.getCell(nxtRow,col+i);
                if (neighbour != null){
                    grid[row][col].leggTilNabo(neighbour);
                }
            }
            
        }
    }

    // metode for å koble sammen celler
    public void connectCells(){
        this.rowLock = new CountDownLatch(this.rowCount);

        for (int row = 0; row < this.rowCount; row++){
            int r = row;

            Thread neighbourhoodWatch = new Thread(() -> {
                for (int col = 0; col < this.colCount; col++){
                    // kaller settNaboer-metode på gjeldende celle
                    this.setNeighbours(r, col);
                }
                this.rowLock.countDown();
            });
            neighbourhoodWatch.start();

        }
        try { this.rowLock.await(); } catch(InterruptedException e) {}
    }

    // metode for å telle totalt antall levende celler
    public int livingCount(){ // TODO do counting when updating/setting status?
        int amt = 0;
        // itererer over alle rader
        for (int row = 0; row < this.rowCount; row++){
            // itererer over alle kolonner
            for (int col = 0; col < this.colCount; col++){
                if (grid[row][col].isAlive()){ // hvis gjeldende celle lever -> inkrementerer teller
                    amt++;
                }
            }
        }
        return amt;
    }

    // setter angitt celle til levende
    public void setAlive(int row, int col){
        this.grid[row][col].setAlive();
    }
    
    // setter angitt celle til død
    public void setDead(int row, int col){
        this.grid[row][col].setDead();
    }
}
