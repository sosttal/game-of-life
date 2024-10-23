/**
 * Kontroller: 
 * Styrer og samkjører spillbrettet (GoLModell) og brukergrensesnitt (GoLGUI)
 * Funksjonalitet for å oppdatere verden til neste generasjon, dvs endre status på alle celler i henhold til spillregler
 * 
 * @author Sondre S Talleraas
 */
public class Controller {
    // fields
    Model rutenett;
    View gui;
    int genNr;
    int antRad;
    int antKol;
    long delay;
    UpdateThread updateLoop;

    boolean running = false;

    // konstruktør
    public Controller(int antRad, int antKol, long delay){
        // initierer instansvariabler
        this.antRad = antRad;
        this.antKol = antKol;
        this.delay = delay;
        this.genNr = 0; // TODO brukes denne til noe? (terminal tror jeg)
        this.rutenett = new Model(antRad, antKol);
        this.gui = new View();
        
        // generates 0th gen and 
        this.rutenett.fyllMedTilfeldigeCeller();
        this.rutenett.kobleAlleCeller();
    }

    // metode for å tegne rutenettet : for konsollgrensesnitt
    public void tegn(){
        this.rutenett.tegnRutenett();

        // skriver ut genNr
        System.out.println("Generasjon: " + this.genNr);
    }

    // går gjennom alle celler og overfører statusen til gui
    public void oppdaterGUI(){
        for (int rad = 0; rad < this.antRad; rad++){ // itererer over alle rader
            for (int kol = 0; kol < this.antKol; kol++){ // itererer over alle kolonner
                // gui-rutenett oppdateres i henhold til celles status
                if (this.rutenett.hentCelle(rad, kol).erLevende()){ // hvis levende
                    gui.settLevende(rad,kol);

                } else{ // hvis død (ikke-levende)
                    gui.settDoed(rad, kol);

                }
            }
        }

        gui.settAntLevende(this.antLevende());
    }

    // bytter status på angitt celle
    public void settLevende(int rad, int kol){
        rutenett.settLevende(rad, kol);
    }

    public void settDoed(int rad, int kol){
        rutenett.settDoed(rad, kol);
    }

    // method to regenerate 0th gen
    public void regenererCeller(){
        this.rutenett.fyllMedTilfeldigeCeller();
        this.rutenett.kobleAlleCeller();
        this.oppdaterGUI();
    }

    // metode for å oppdatere rutenett TODO make more efficient?
    public void oppdatering(){
        // løkke for å oppdatere antall levende naboer
        for (int rad = 0; rad < this.antRad; rad++){ // itererer over alle rader
            for (int kol = 0; kol < this.antKol; kol++){ // itererer over alle kolonner
                // henter celle fra gjeldende posisjon
                Cell celle = this.rutenett.hentCelle(rad, kol);

                celle.tellLevendeNaboer();
            }
        }
        // løkke for å oppdatere status
        for (int rad = 0; rad < this.antRad; rad++){ // itererer over alle rader
            for (int kol = 0; kol < this.antKol; kol++){ // itererer over alle kolonner
                // henter celle fra gjeldende posisjon
                Cell celle = this.rutenett.hentCelle(rad, kol);

                celle.oppdaterStatus();
            }
        }
        // inkrementerer generasjonsnummer
        this.genNr++;

        this.oppdaterGUI();
    }

    // metode for å initiere/starte GUI
    public void init(){
        this.gui.init(this, antRad, antKol);
    }

    public void settings(){ // dimensions of grid, update frequency , ...?
        // TODO implement settings prompt
    }

    // henter antall levende
    public int antLevende(){
        return this.rutenett.antallLevende();
    }

    // metode som tar imot signal fra start- / stoppknapp
    public void startsignal(boolean signal){
        this.running = signal;
        return;
    }

    public boolean running(){
        return this.running;

    }

    // metode for å avgjøre når oppdaterings-løkke skal starte TODO: remove
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

    public void killAll(){
        for (int rad = 0; rad < this.antRad; rad++){ // itererer over alle rader
            for (int kol = 0; kol < this.antKol; kol++){ // itererer over alle kolonner
                this.rutenett.settDoed(rad, kol);
            }
        }
    }
    
}
