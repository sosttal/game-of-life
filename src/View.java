// imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * GoLGUI:
 * grafisk brukergrensesnitt for Game of Life
 * 
 * @author <a href="https://github.com/sosttal">Sondre S Talleraas</a> TODO: use this format for author globally?
 */
public class View {
    // fields
    Controller CTRL;
    
    // frame
    JFrame frame;
    
    //  panels
    JPanel mainPanel;
    JPanel menuBar;
    JPanel grid;
    
    //  menubar elements
    //      lables:
    JLabel amtLivingLabel;  // for displaytext
    JLabel amtLabel;        // for living counter
    String amtLivingTxt;    // displaytext 
    int amtLiving = 0;      // living counter (amount of alive cells)
    //      buttons:
    JButton startStopBtn;   // to start/stop update loop
    JButton exitBtn;        // to exit program
    JButton resetBtn;       // to reset game-grid (new 0th generation)
    JButton clearBtn;       // to clear game-grid (sets all cells to dead)

    JButton[][] celleknapper;
    boolean[][] celleStatus; // for å lagre status på celler (true hvis levende)
    boolean start = false; // for å holde styr på om start-/stoppknapp skal starte eller stoppe. initieres til false siden programmet ikke skal starte før knapp trykkes en gang

    //ActionListeners:
    // start-/stoppknapp: veksler mellom å starte og stoppe programmet og oppdaterer knappetekst
    class StartStopp implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (!start){ 
                start = true;
                // oppdaterer knappetekst
                startStopBtn.setText("Stop");
                // sender startsignal til kontroller
                CTRL.startsignal(true);
                
            } else{
                start = false;
                // oppdaterer knappetekst
                startStopBtn.setText("Start");
                // sender stoppsignal til kontroller
                CTRL.startsignal(false);
            }
        }
    }

    // avslutte-knapp: avslutter programmet
    class Avslutt implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }

    // celleknapp: endrer status på celle som blir trykket på
    class EndreStatus implements ActionListener{
        // felter
        int rad;
        int kol;

        // konstruktør tar posisjon til rute (rad, kol) som arg, for å kunne finne riktig knappe- og celleobjekt
        public EndreStatus(int rad, int kol){
            this.rad = rad;
            this.kol = kol;
        }
        
        @Override
        public void actionPerformed(ActionEvent e){
            // sjekker status og kaller relevant metode
            if (!celleStatus[rad][kol]){
                settLevende(rad,kol);
            } else{
                settDoed(rad,kol);
            }
            // oppdaterer gui
            CTRL.oppdaterGUI();
        }
    }

    // for reset button: kills all cells and regens new 0th gen
    class Reset implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            // clear();
            CTRL.regenererCeller();
        }
    }

    // for reset button: kills all cells and regens new 0th gen
    class Clear implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            clear();
        }
    }
    
    // metode for å endre status på angitt celle til levende og oppdatere statussymbol på knapp
    public void settLevende(int rad, int kol){
        // sett til levende
        CTRL.settLevende(rad, kol);
        celleStatus[rad][kol] = true;
        celleknapper[rad][kol].setText("*");
        celleknapper[rad][kol].setBackground(Color.GREEN);
        
        CTRL.settLevende(rad, kol);
    }

    // metode for å endre status på angitt celle til død og oppdatere statussymbol på knapp
    public void settDoed(int rad, int kol){
        // sett til død
        celleStatus[rad][kol] = false;
        celleknapper[rad][kol].setText(" ");
        celleknapper[rad][kol].setBackground(Color.BLACK);

        CTRL.settDoed(rad, kol);
    }

    // legger til angitt antall (kall med negative argumenter for å trekke fra)
    public void settAntLevende(int ant){
        this.amtLiving = ant;
        this.amtLivingTxt = String.format("%d ", this.amtLiving);
        this.amtLabel.setText(amtLivingTxt);
    }

    // tilbakestiller spillebrettet (dreper alle celler)
    public void clear(){
        this.CTRL.killAll();
        this.CTRL.oppdaterGUI();
    }

    // to init gui
    public void init(Controller controller, int antRad, int antKol){
        // lagrer angitt Kontroller-objekt
        this.CTRL = controller;

        // oppretter vindu
        this.frame = new JFrame("Game of Life");
        this.frame.setPreferredSize(new Dimension(500,500));
        this.frame.setBackground(Color.BLACK);

        // oppretter hovedpanel (for å holde de andre panelene) og legger det til i vinduet
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.setBackground(Color.BLACK);
        this.frame.add(this.mainPanel);
        
        // oppretter underpaneler
        this.menuBar = new JPanel(); // panel for "meny"-knapper
        this.menuBar.setBackground(Color.BLACK); // bg of menubar set to black
        this.grid = new JPanel(); // panel for rutenett
        this.grid.setBackground(Color.BLACK);

        // setter layout på rutenett-panelet
        this.grid.setLayout(new GridLayout(antRad,antKol)); // setter layout på rutenett-panel til verdier angitt av parametre

        // legger til og posisjonerer underpaneler i hovedpanel
        this.mainPanel.add(this.menuBar, BorderLayout.NORTH);
        this.mainPanel.add(this.grid, BorderLayout.CENTER);

        // sørger for at klikk på x avslutter programmet
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // oppretter og legger til elementer i menyknapp-panelet
        this.amtLivingLabel = new JLabel("Alive cells: ");
        this.amtLabel = new JLabel(" ");
        this.amtLivingLabel.setForeground(Color.WHITE);
        this.startStopBtn = new JButton("Start");
        this.amtLabel.setForeground(Color.WHITE);
        this.startStopBtn.addActionListener( new StartStopp());
        this.resetBtn = new JButton("Reset");
        this.resetBtn.addActionListener(new Reset());
        this.clearBtn = new JButton("Clear");
        this.clearBtn.addActionListener(new Clear());
        this.exitBtn = new JButton("Exit");
        this.exitBtn.addActionListener(new Avslutt());
        this.menuBar.add(this.amtLivingLabel);
        this.menuBar.add(this.amtLabel);
        this.menuBar.add(this.startStopBtn);
        this.menuBar.add(this.resetBtn);
        this.menuBar.add(this.clearBtn);
        this.menuBar.add(this.exitBtn);
        
        // initierer array for celleknapper
        celleknapper = new JButton[antRad][antKol];
        celleStatus = new boolean[antRad][antKol];

        // legger til elementer i rutenett-panel: løkke for å legge til riktig antall rader og riktig antall knapper per rad (kolonner)
        for (int rad = 0; rad < antRad; rad++){
            for (int kol = 0; kol < antKol; kol++){
                JButton celleknapp = new JButton(" ");
                celleknapp.setPreferredSize(new Dimension(40,40)); // for at det statusikonet på knappen skal ha nok plass
                celleknapp.setBackground(Color.BLACK);
                celleknapper[rad][kol] = celleknapp;
                
                celleknapp.addActionListener(new EndreStatus(rad, kol));
                this.grid.add(celleknapp);
            }
        }
        this.CTRL.oppdaterGUI();

        // initierer antall levende-label (siden metoden oppdaterGUI påvirker variablene)
        this.amtLiving = this.CTRL.antLevende();
        this.amtLivingTxt = String.format("%d", this.amtLiving);
        this.amtLabel.setText(this.amtLivingTxt);

        // pakker, posisjonerer og synliggjør vinduet
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
