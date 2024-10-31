import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JComponent;

public class ButtonFocus implements FocusListener{
    JComponent host;
    Color hostBG, hostFG;

    public ButtonFocus(JComponent host){
        this.host = host;
        this.hostBG = host.getBackground();
        this.hostFG = host.getForeground();
    }

    public void focusGained(FocusEvent e){
        this.host.setBackground(Color.BLACK);
        this.host.setForeground(Color.GREEN);
    }

    public void focusLost(FocusEvent e){
        this.host.setBackground(this.hostBG);
        this.host.setForeground(this.hostFG);
    }
}