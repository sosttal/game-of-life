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
    int genNr;
    int antRad;
    int antKol;
    long delay;
    View gui;

    boolean signal;

    // konstruktør
    public Controller(int antRad, int antKol, long delay){
        // initierer instansvariabler
        this.antRad = antRad;
        this.antKol = antKol;
        this.delay = delay;
        this.rutenett = new Model(antRad, antKol);
        this.genNr = 0;
        gui = new View();
        
        // genererer 0te generasjon med celler og kobler dem sammen
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

    // TODO:(test/implement) metode for å generere ny førstegen(0te)
    public void regenererCeller(){
        this.rutenett.fyllMedTilfeldigeCeller();
        this.rutenett.kobleAlleCeller();
        this.oppdaterGUI();
    }

    // metode for å oppdatere rutenett
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

    // henter antall levende
    public int antLevende(){
        return this.rutenett.antallLevende();
    }

    // metode som tar imot signal fra start- / stoppknapp
    public void startsignal(boolean signal){
        this.signal = signal;
        return;
    }

    // metode for å avgjøre når oppdaterings-løkke skal starte
    public void sjekkStart(){

        // oppdaterings-løkke
        while (signal){
            this.oppdatering();
            try{
                Thread.sleep(this.delay);
            } catch(Exception e){
                return;
            }
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
