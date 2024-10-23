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
    JFrame vindu;
    
    //  panels
    JPanel mainPanel;
    JPanel menyBar;
    JPanel rutenett;
    
    //  menubar elements
    //      lables:
    JLabel antLevendeLabel;
    JLabel antallLabel;
    String antLevendeTxt;
    int antLevende = 0;
    //      buttons:
    JButton startStoppKnapp;
    JButton avsluttKnapp;
    JButton resetButton;

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
                startStoppKnapp.setText("Stop");
                // sender startsignal til kontroller
                CTRL.startsignal(true);
                
            } else{
                start = false;
                // oppdaterer knappetekst
                startStoppKnapp.setText("Start");
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

    // tilbakestill-knapp: setter alle celler til doed
    class Reset implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            reset();
            CTRL.regenererCeller();
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
        this.antLevende = ant;
        this.antLevendeTxt = String.format("%d ", this.antLevende);
        this.antallLabel.setText(antLevendeTxt);
    }

    // tilbakestiller spillebrettet (dreper alle celler)
    public void reset(){
        this.CTRL.killAll();
        this.CTRL.oppdaterGUI();
    }

    // to init gui
    public void init(Controller controller, int antRad, int antKol){
        // lagrer angitt Kontroller-objekt
        this.CTRL = controller;

        // oppretter vindu
        this.vindu = new JFrame("Game of Life");
        this.vindu.setPreferredSize(new Dimension(500,500));
        this.vindu.setBackground(Color.BLACK);

        // oppretter hovedpanel (for å holde de andre panelene) og legger det til i vinduet
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.setBackground(Color.BLACK);
        this.vindu.add(this.mainPanel);
        
        // oppretter underpaneler
        this.menyBar = new JPanel(); // panel for "meny"-knapper
        this.menyBar.setBackground(Color.BLACK); // bg of menubar set to black
        this.rutenett = new JPanel(); // panel for rutenett

        // setter layout på rutenett-panelet
        this.rutenett.setLayout(new GridLayout(antRad,antKol)); // setter layout på rutenett-panel til verdier angitt av parametre

        // legger til og posisjonerer underpaneler i hovedpanel
        this.mainPanel.add(this.menyBar, BorderLayout.NORTH);
        this.mainPanel.add(this.rutenett, BorderLayout.CENTER);

        // sørger for at klikk på x avslutter programmet
        this.vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // oppretter og legger til elementer i menyknapp-panelet
        this.antLevendeLabel = new JLabel("Alive cells: ");
        this.antallLabel = new JLabel(" ");
        this.antLevendeLabel.setForeground(Color.WHITE);
        this.startStoppKnapp = new JButton("Start");
        this.antallLabel.setForeground(Color.WHITE);
        this.startStoppKnapp.addActionListener( new StartStopp());
        this.resetButton = new JButton("Reset");
        this.resetButton.addActionListener(new Reset());
        this.avsluttKnapp = new JButton("Exit");
        this.avsluttKnapp.addActionListener(new Avslutt());
        this.menyBar.add(this.antLevendeLabel);
        this.menyBar.add(this.antallLabel);
        this.menyBar.add(this.startStoppKnapp);
        this.menyBar.add(this.resetButton);
        this.menyBar.add(this.avsluttKnapp);
        
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
                this.rutenett.add(celleknapp);
            }
        }
        this.CTRL.oppdaterGUI();

        // initierer antall levende-label (siden metoden oppdaterGUI påvirker variablene)
        this.antLevende = this.CTRL.antLevende();
        this.antLevendeTxt = String.format("%d", this.antLevende);
        this.antallLabel.setText(this.antLevendeTxt);

        // pakker, posisjonerer og synliggjør vinduet
        vindu.pack();
        vindu.setLocationRelativeTo(null);
        vindu.setVisible(true);

    }
}
