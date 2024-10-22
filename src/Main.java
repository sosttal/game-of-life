/**
 * @author Sondre S Talleraas
 * TODO docc
 * Hovedprogram:
 */
public class Main {
    public static void main(String[] args){
        Controller c; // game controller
        
        // setting dimensions of game board / grid: can be set at launch with runargs (args[1] x args[2]), if no args standard size is 8 x 12 TODO: this is unnessecary(?) (also: use if instead of try/catch)
        try{
            c = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        } catch(ArrayIndexOutOfBoundsException e){
            c = new Controller(8, 12);
        }

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
