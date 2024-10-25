/**
 * Cell-class used by the Game of Life sim ({@link #Model}).
 * 
 * <p> Represents individual Cell-objects.
 * 
 * @author Sondre S Talleraas
 */
public class Cell {
    // felter (ikke satt til private for å unngå problemer med tester)
    boolean alive;
    Cell[] neighbours;
    int neighbourCount;
    int livingNeighbourCount;
    
    // konstruktør for GoLCelle-klassen - initierer instansvariabler
    public Cell(){
        this.alive = false;
        this.neighbours = new Cell[8];
        this.neighbourCount = 0;
        this.livingNeighbourCount = 0;
    }

    // metode for å sette cellestatus til død
    public void setDead(){
        this.alive = false;
    }

    // metode for å sette cellestatus til levende
    public void setAlive(){
        this.alive = true;
    }

    // metode for å sjekke cellestatus
    public boolean isAlive(){
        return this.alive;
    }

    // metode for å hente statustegn
    public char getStatusSymbol(){
        if (this.isAlive()){
            return 'O';
        } 
        else {
            return '.';
        }
    }

    // metode for å legge til celle som nabo
    public void leggTilNabo(Cell nabo){
        // legger til nabo i naboer array - bruker antNaboer til indeksering
        this.neighbours[this.neighbourCount] = nabo;
        // oppdaterer ant naboer
        this.neighbourCount += 1;
    }

    // metode for å telle antall levende naboer
    public void countNeighbours(){
        // tilbakestiller antLevendeNaboer før telling
        this.livingNeighbourCount = 0;
        //  itererer over indekser på naboer-arr - for hver nabo, teller nabo hvis den ikke er null og er levende
        for (int  i = 0; i < this.neighbours.length; i++){
            if (this.neighbours[i] != null && this.neighbours[i].isAlive()){
                this.livingNeighbourCount ++;
            }
        }
    }

    // metode for å oppdatere status basert på ant levende naboer
    public void updateStatus(){
        // sjekker først om celle er levende (eller død)
        if (this.isAlive()){
            if (this.livingNeighbourCount < 2 || this.livingNeighbourCount > 3){ // færre enn to (underpop) / flere enn tre (overpop) naboer -> celle dør
                this.alive = false;
            }
        } 
        else { // celle er død
            if (this.livingNeighbourCount == 3){ // nøyaktig tre naboer -> celle lever (reproduksjon)
                this.alive = true;
            }
        }
    }
}

