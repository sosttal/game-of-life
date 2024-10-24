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
    JLabel aliveCells;    // "Alive cells: "
    JLabel amtLabel;      // for living counter

    //      buttons:
    JButton startStopBtn;   // to start/stop update loop
    JButton regenBtn;       // to reset game-grid (new 0th generation)
    JButton clearBtn;       // to clear game-grid (sets all cells to dead)
    JButton exitBtn;        // to exit program

    //  game-grid:
    JButton[][] cellBtns;
    boolean[][] celleStatus; // for å lagre status på celler (true hvis levende)

    //ActionListeners:
    // start-/stoppknapp: veksler mellom å starte og stoppe programmet og oppdaterer knappetekst
    class StartStop implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (!CTRL.running()){
                // update button text
                startStopBtn.setText("Stop");

                // sender startsignal til kontroller
                CTRL.startStop();
                    
            } else{
                // update button text
                startStopBtn.setText("Start");

                // sender stoppsignal til kontroller
                CTRL.startStop();

            }
        }
    }

    // avslutte-knapp: avslutter programmet
    class Exit implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }

    // celleknapp: endrer status på celle som blir trykket på
    class FlipStatus implements ActionListener{
        // felter
        int row;
        int col;

        // konstruktør tar posisjon til rute (rad, kol) som arg, for å kunne finne riktig knappe- og celleobjekt
        public FlipStatus(int r, int c){
            this.row = r;
            this.col = c;
        }
        
        @Override
        public void actionPerformed(ActionEvent e){
            // sjekker status og kaller relevant metode
            if (!celleStatus[row][col]){
                setAlive(row,col);
                CTRL.setAlive(row, col);

            } else{
                setDead(row,col);
                CTRL.setDead(row, col);

            }
            // oppdaterer gui
            CTRL.updateGUI();
        }
    }

    // for reset button: kills all cells and regens new 0th gen
    class Regen implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            // clear();
            CTRL.regenCells();
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
    public void setAlive(int row, int col){
        // sett til levende
        celleStatus[row][col] = true;
        cellBtns[row][col].setText("*");
        cellBtns[row][col].setBackground(Color.GREEN);
        
        //CTRL.setAlive(row, col); //TODO not needed?
    }

    // metode for å endre status på angitt celle til død og oppdatere statussymbol på knapp
    public void setDead(int row, int col){
        // sett til død
        celleStatus[row][col] = false;
        cellBtns[row][col].setText(" ");
        cellBtns[row][col].setBackground(Color.BLACK);

        //CTRL.setDead(row, col); //TODO not needed?
    }

    // legger til angitt antall (kall med negative argumenter for å trekke fra)
    public void setLivingCount(int amt){
        this.amtLabel.setText(String.format("%d", amt));

    }

    // tilbakestiller spillebrettet (dreper alle celler)
    public void clear(){
        this.CTRL.killAll();
        this.CTRL.updateGUI();
    }

    /**
     * 
     * @param controller - the controller object to be used
     * @param rowCount
     * @param colCount
     */
    public void init(Controller controller, int rowCount, int colCount){
        this.CTRL = controller;

        // init frame
        this.frame = new JFrame("Game of Life");
        this.frame.setIconImage(new ImageIcon("img/icon.png").getImage()); // TODO: make/find fitting icon
        this.frame.setPreferredSize(new Dimension(750,750));
        this.frame.setForeground(Color.BLACK);

        // oppretter hovedpanel (for å holde de andre panelene) og legger det til i vinduet
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.setBackground(Color.BLACK);
        this.frame.add(this.mainPanel);
        
        // oppretter underpaneler
        this.menuBar = new JPanel(); // panel for meny-knapper
        this.menuBar.setBackground(Color.DARK_GRAY); // bg of menubar set to dark grey
        this.grid = new JPanel(); // panel for rutenett
        this.grid.setBackground(Color.BLACK);

        // setter layout på rutenett-panelet
        this.grid.setLayout(new GridLayout(rowCount,colCount)); // setter layout på rutenett-panel til verdier angitt av parametre

        // legger til og posisjonerer underpaneler i hovedpanel
        this.mainPanel.add(this.menuBar, BorderLayout.NORTH);
        this.mainPanel.add(this.grid, BorderLayout.CENTER);

        // sørger for at klikk på x avslutter programmet
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // init menu-components
        this.aliveCells = new JLabel("Alive cells: ");
        this.aliveCells.setFont(new Font("Monospaced", Font.BOLD, 16)); 
        this.amtLabel = new JLabel();
        this.amtLabel.setFont(new Font("Monospaced", Font.BOLD, 16)); 
        this.aliveCells.setForeground(Color.WHITE);
        this.amtLabel.setForeground(Color.WHITE);
        this.startStopBtn = new JButton("Start");
        this.startStopBtn.addActionListener(new StartStop());
        this.regenBtn = new JButton("Regen");
        this.regenBtn.addActionListener(new Regen());
        this.clearBtn = new JButton("Clear");
        this.clearBtn.addActionListener(new Clear());
        this.exitBtn = new JButton("Exit");
        this.exitBtn.addActionListener(new Exit());
        
        // add menu-components to menubar
        this.menuBar.add(this.aliveCells);
        this.menuBar.add(this.amtLabel);

        // menu-buttons styled and added to menubar
        JButton[] menuBtns = {startStopBtn, regenBtn, clearBtn, exitBtn};
        for (JButton b : menuBtns){
            b.setForeground(Color.BLACK);
            b.setBackground(Color.WHITE);
            b.setFont(new Font("Monospaced", Font.BOLD, 16)); //TODO edit font?
            b.setBorderPainted(false);
            // b.setFocusPainted(false);
            this.menuBar.add(b);
        }
        
        // initierer array for celleknapper
        cellBtns = new JButton[rowCount][colCount];
        celleStatus = new boolean[rowCount][colCount];

        // legger til elementer i rutenett-panel: løkke for å legge til riktig antall rader og riktig antall knapper per rad (kolonner)
        for (int rad = 0; rad < rowCount; rad++){
            for (int kol = 0; kol < colCount; kol++){
                JButton celleknapp = new JButton(" ");
                celleknapp.setFocusPainted(false);
                celleknapp.setBorderPainted(false); // TODO: what looks: best borders vs no borders?
                celleknapp.setPreferredSize(new Dimension(40,40)); // for at det statusikonet på knappen skal ha nok plass
                celleknapp.setBackground(Color.BLACK);
                cellBtns[rad][kol] = celleknapp;
                
                celleknapp.addActionListener(new FlipStatus(rad, kol));
                this.grid.add(celleknapp);
            }
        }
        this.CTRL.updateGUI();

        // initierer antall levende-label (siden metoden oppdaterGUI påvirker variablene)
        this.amtLabel.setText(String.format("%d", this.CTRL.livingCount()));

        // pakker, posisjonerer og synliggjør vinduet
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
