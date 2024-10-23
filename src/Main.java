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
        final long DELAY = 555;
        
        Controller c; // game controller
        
        if(args.length == 1){
            c = new Controller(ANT_RAD, ANT_KOL, Long.parseLong(args[0])); // hvis kun ett arg sendes ved kjøring settes oppdateringsfrekvensen (i ms) til det
        } else if(args.length == 2){
            c = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]), DELAY); // hvis to arg tolkes de som dimensjoner
        } else if(args.length == 3){
            c = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Long.parseLong(args[2])); // hvis tre eller flere arg tolkes de tre første som antRad, antKol og delay, resten ignoreres
        } else{
            c = new Controller(ANT_RAD, ANT_KOL, DELAY);
        }
        
        /*// setting dimensions of game board / grid: can be set at launch with runargs (args[1] x args[2]), if no args standard size is 8 x 12 TODO: this is unnessecary(?) (also: use if instead of try/catch)
        try{
            c = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } catch(ArrayIndexOutOfBoundsException e){
            c = new Controller(8, 12);
        }*/

        // init game
        c.init();     
        
        // løkke venter på at startknapp skal trykkes (ikke helt fornøyd med denne løsningen, men det var det jeg fikk til) TODO: find better solution
        boolean run = true;
        while (run){
            System.out.print(""); // funka ikke hvis jeg ikke hadde "tom" print
            c.sjekkStart();
        }
    }
}
