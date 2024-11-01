/**
 * Initiates Game of Life.
 * 
 * <p> The game is constructed using the MVC-pattern, and this program handels initializing the controller.
 * 
 * @author <a href="https://github.com/sosttal">Sondre S Talleraas</a>
 */
public class GOLMain {
    public static void main(String[] args){
        // default settings for game
        final int ANT_RAD = 36;
        final int ANT_KOL = 36;
        final long DELAY = 444;
        
        Controller c; // game controller
        
        // arg handeling (used to determine settings of game)
        if(args.length == 1){
            // one arg is interpreted as update-frequency
            c = new Controller(ANT_RAD, ANT_KOL, Long.parseLong(args[0]));

        } else if(args.length == 2){
            // two args interpreted as dimensions of game-grid (rows x columns)
            c = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]), DELAY); 

        } else if(args.length == 3){
            // three args interpreted as rows, columns and update-freq
            c = new Controller(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Long.parseLong(args[2])); 

        } else{
            // if 0 or > 3 args default settings are used
            c = new Controller(ANT_RAD, ANT_KOL, DELAY);

        }

        // init game-controller
        c.init();     
        
    }
}
