/**
 * Cell-class to be used by the game grid ({@link #Model}).
 * 
 * <p> Represents individual Cell-objects
 * 
 * @author Sondre S Talleraas
 */
public class Cell {
    // felter (ikke satt til private for å unngå problemer med tester)
    boolean levende;
    Cell[] naboer;
    int antNaboer;
    int antLevendeNaboer;
    
    // konstruktør for GoLCelle-klassen - initierer instansvariabler
    public Cell(){
        this.levende = false;
        this.naboer = new Cell[8];
        this.antNaboer = 0;
        this.antLevendeNaboer = 0;
    }

    // metode for å sette cellestatus til død
    public void setDead(){
        this.levende = false;
    }

    // metode for å sette cellestatus til levende
    public void setAlive(){
        this.levende = true;
    }

    // metode for å sjekke cellestatus
    public boolean isAlive(){
        return this.levende;
    }

    // metode for å hente statustegn
    public char hentStatusTegn(){
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
        this.naboer[this.antNaboer] = nabo;
        // oppdaterer ant naboer
        this.antNaboer += 1;
    }

    // metode for å telle antall levende naboer
    public void countNeighbours(){
        // tilbakestiller antLevendeNaboer før telling
        this.antLevendeNaboer = 0;
        //  itererer over indekser på naboer-arr - for hver nabo, teller nabo hvis den ikke er null og er levende
        for (int  i = 0; i < this.naboer.length; i++){
            if (this.naboer[i] != null && this.naboer[i].isAlive()){
                this.antLevendeNaboer ++;
            }
        }
    }

    // metode for å oppdatere status basert på ant levende naboer
    public void updateStatus(){
        // sjekker først om celle er levende (eller død)
        if (this.isAlive()){
            if (this.antLevendeNaboer < 2 || this.antLevendeNaboer > 3){ // færre enn to (underpop) / flere enn tre (overpop) naboer -> celle dør
                this.levende = false;
            }
        } 
        else { // celle er død
            if (this.antLevendeNaboer == 3){ // nøyaktig tre naboer -> celle lever (reproduksjon)
                this.levende = true;
            }
        }
    }
}

