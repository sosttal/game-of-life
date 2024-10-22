// imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** IN1010 V24: Innlevering 5
 * @author sondrta
 * 
 * GoLGUI:
 * grafisk brukergrensesnitt for Game of Life
 */
public class View {
    // fields
    Controller kontroller;
    
    JFrame vindu;
    
    JPanel mainPanel;
    JPanel menyBar;
    JPanel rutenett;
    
    JLabel antLevendeLabel;
    JLabel antallLabel;
    String antLevendeTxt;
    int antLevende = 0;

    JButton startStoppKnapp;
    JButton avsluttKnapp;

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
                startStoppKnapp.setText("Stopp");
                // sender startsignal til kontroller
                kontroller.startsignal(true);
                
            } else{
                start = false;
                // oppdaterer knappetekst
                startStoppKnapp.setText("Start");
                // sender stoppsignal til kontroller
                kontroller.startsignal(false);
            }
        }
    }

    // avslutte-knapp: avslutter programmet
    class Avslutt implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(1);
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
            kontroller.oppdaterGUI();
        }
    }
    
    // metode for å endre status på angitt celle til levende og oppdatere statussymbol på knapp
    public void settLevende(int rad, int kol){
        // sett til levende
        kontroller.settLevende(rad, kol);
        celleStatus[rad][kol] = true;
        celleknapper[rad][kol].setText("*");
        celleknapper[rad][kol].setBackground(Color.GREEN);
        
        kontroller.settLevende(rad, kol);
    }

    // metode for å endre status på angitt celle til død og oppdatere statussymbol på knapp
    public void settDoed(int rad, int kol){
        // sett til død
        celleStatus[rad][kol] = false;
        celleknapper[rad][kol].setText(" ");
        celleknapper[rad][kol].setBackground(Color.BLACK);

        kontroller.settDoed(rad, kol);
    }

    // legger til angitt antall (kall med negative argumenter for å trekke fra)
    public void settAntLevende(int ant){
        this.antLevende = ant;
        this.antLevendeTxt = String.format("%d ", this.antLevende);
        this.antallLabel.setText(antLevendeTxt);
    }

    // metode for å initialisere GUI
    public void init(Controller kontroller, int antRad, int antKol){
        // lagrer angitt Kontroller-objekt
        this.kontroller = kontroller;

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
        this.menyBar.setBackground(Color.BLACK);
        this.rutenett = new JPanel(); // panel for rutenett

        // setter layout på rutenett-panelet
        this.rutenett.setLayout(new GridLayout(antRad,antKol)); // setter layout på rutenett-panel til verdier angitt av parametre

        // legger til og posisjonerer underpaneler i hovedpanel
        this.mainPanel.add(this.menyBar, BorderLayout.NORTH);
        this.mainPanel.add(this.rutenett, BorderLayout.CENTER);

        // sørger for at klikk på x avslutter programmet
        this.vindu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // oppretter og legger til elementer i menyknapp-panelet
        this.antLevendeLabel = new JLabel("Antall levende: ");
        this.startStoppKnapp = new JButton("Start");
        this.antallLabel = new JLabel(" ");
        this.startStoppKnapp.addActionListener( new StartStopp());
        this.avsluttKnapp = new JButton("Avslutt");
        this.avsluttKnapp.addActionListener(new Avslutt());
        this.menyBar.add(this.antLevendeLabel);
        this.menyBar.add(this.antallLabel);
        this.menyBar.add(this.startStoppKnapp);
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
        this.kontroller.oppdaterGUI();

        // initierer antall levende-label (siden metoden oppdaterGUI påvirker variablene)
        this.antLevende = this.kontroller.antLevende();
        this.antLevendeTxt = String.format("%d", antLevende);
        this.antallLabel.setText(antLevendeTxt);

        // pakker, posisjonerer og synliggjør vinduet
        vindu.pack();
        vindu.setLocationRelativeTo(null);
        vindu.setVisible(true);

    }
}
