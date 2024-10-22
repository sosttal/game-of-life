/** IN1010 V24: Innlevering 5
 * @author sondrta
 * 
 * GoLModell-klasse:
 * Representerer rutenettet som utgjør spillbrettet
 */

public class Model {
    // felter
    int antRader;
    int antKolonner;
    Cell[][] rutene;

    // konstruktør - tar rutenett dimensjoner som parametere
    public Model(int antRader, int antKolonner){
        this.antRader = antRader;
        this.antKolonner = antKolonner;
        // initierer rutenett
        this.rutene = new Cell[antRader][antKolonner];
    }

    // metode for å lage celle
    public void lagCelle(int rad, int kol){
        // oppretter nytt Celle-objekt
        Cell nyCelle = new Cell();
        // 1/3 sjanse for å bli levende
        if (Math.random() <= 0.333){ 
            nyCelle.settLevende();
        }
        rutene[rad][kol] = nyCelle;
    }

    // metode for å generere 0te gen av celler (tilfeldige celler)
    public void fyllMedTilfeldigeCeller(){
        // itererer over alle radindekser
        for (int rad = 0; rad < this.antRader; rad++){
            // itererer over alle kolonneindekser
            for (int kol = 0; kol < this.antKolonner; kol++){
                this.lagCelle(rad, kol);
            }
        }
    }

    // motode for å hente celle, returnerer Celle-objekt på angitt posisjon eller null
    public Cell hentCelle(int rad, int kol){
        if (rad < this.antRader && rad >= 0 && kol < this.antKolonner && kol >= 0){ // sjekker om angitt posisjon er gyldig indeks i rutenettet - returnerer objektet som ligger der
            return rutene[rad][kol];
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
        
        for (int rad = 0; rad < this.antRader; rad++){
            for (int kol = 0; kol < this.antKolonner; kol++){
                // henter statustegn
                char statusTegn = this.rutene[rad][kol].hentStatusTegn();
                // skriver ut tegnet til terminalen
                System.out.print(statusTegn);
            }
            System.out.println();
        }
    }

    // metode for å oppdatere naboer for angitt celle
    public void settNaboer(int rad, int kol){
        if (this.hentCelle(rad, kol) != null){
            int forrigeRad = rad-1;
            int nesteRad = rad+1;

            for (int i = -1; i <= 1; i++){ // løkke for naboer på rad-1
                Cell nabo = this.hentCelle(forrigeRad,kol+i);
                if (nabo != null){
                    rutene[rad][kol].leggTilNabo(nabo);
                }
            }
        

            for (int i = -1; i <= 1; i+=2){ // løkke for naboer på samme rad (hopper over den aktuelle cellens egen indeks)
                Cell nabo = this.hentCelle(rad,kol+i);
                if (nabo != null){
                    rutene[rad][kol].leggTilNabo(nabo);
                }
            }

            for (int i = -1; i <= 1; i++){ // løkke for naboer på rad+1
                Cell nabo = this.hentCelle(nesteRad,kol+i);
                if (nabo != null){
                    rutene[rad][kol].leggTilNabo(nabo);
                }
            }
            
        }
    }

    // metode for å koble sammen celler
    public void kobleAlleCeller(){
        // itererer over alle rader
        for (int rad = 0; rad < this.antRader; rad++){
            // itererer over alle kolonner
            for (int kol = 0; kol < this.antKolonner; kol++){
                // kaller settNaboer-metode på gjeldende celle
                this.settNaboer(rad, kol);
            }
        }
    }

    // metode for å telle totalt antall levende celler
    public int antallLevende(){
        int antall = 0;
        // itererer over alle rader
        for (int rad = 0; rad < this.antRader; rad++){
            // itererer over alle kolonner
            for (int kol = 0; kol < this.antKolonner; kol++){
                if (rutene[rad][kol].erLevende()){ // hvis gjeldende celle lever -> inkrementerer teller
                    antall++;
                }
            }
        }
        return antall;
    }

    // setter angitt celle til levende
    public void settLevende(int rad, int kol){
        this.rutene[rad][kol].settLevende();
    }
    
    // setter angitt celle til død
    public void settDoed(int rad, int kol){
        this.rutene[rad][kol].settDoed();
    }
}
