/**
 * TODO docc
 * Hovedprogram:
 * 
 * @author Sondre S Talleraas
 */
public class Main {
    public static void main(String[] args){
        // konstanter for standardinstillinger
        final int ANT_RAD = 15;
        final int ANT_KOL = 15;
        final long DELAY = 2000;
        
        Controller c; // game controller
        
        if(args.length == 1){ // TODO: this is unnecessary(?)
            c = new Controller(ANT_RAD, ANT_KOL, Long.parseLong(args[0])); // hvis kun ett arg sendes ved kjøring settes oppdateringsfrekvensen (i ms) til det

        } else if(args.length == 2){
            c = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]), DELAY); // hvis to arg tolkes de som dimensjoner

        } else if(args.length == 3){
            c = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Long.parseLong(args[2])); // hvis tre eller flere arg tolkes de tre første som antRad, antKol og delay, resten ignoreres

        } else{
            c = new Controller(ANT_RAD, ANT_KOL, DELAY);

        }

        // init game
        c.init();     
        
    }
}
