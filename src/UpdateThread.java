/**
 * Thread-class to handle the update-loop.
 */
public class UpdateThread extends Thread {
    // fields
    long DELAY;
    Controller CTRL;

    public UpdateThread(long delay, Controller c){
        this.DELAY = delay;
        this.CTRL = c;

    }

    @Override
    public void run(){
        // TODO: do update loop here
    }
}
