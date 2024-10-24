// imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.CountDownLatch;

/**
 * GoLGUI:
 * grafisk brukergrensesnitt for Game of Life
 * 
 * @author <a href="https://github.com/sosttal">Sondre S Talleraas</a> TODO: use this format for author globally?
 */
public class View {
    // fields
    Controller CTRL;
    CountDownLatch rowLock;
    
    // frame
    JFrame frame;
    
    //  panels
    JPanel mainPanel;
    JPanel menuBar;
    JPanel statTracker;
    JPanel buttonBar;
    JPanel grid;
    
    //  menubar elements
    //      lables:
    JLabel livingCells;    // "Alive cells: "
    JLabel amtLabel;      // for living counter

    //      buttons:
    JButton startStopBtn;   // to start/stop update loop
    JButton regenBtn;       // to reset game-grid (new 0th generation)
    JButton clearBtn;       // to clear game-grid (sets all cells to dead)
    JButton exitBtn;        // to exit program

    //  game-grid:
    JButton[][] cellBtns;
    boolean[][] cellStatus; // for å lagre status på celler (true hvis levende)

    // style related constants
    String FONT = "Monospaced";
    Color MENU = new Color(35,35,35);
    Color MENU_BTN_BG = Color.MAGENTA;     //new Color(0,0,0); // black
    Color MENU_BTN_FG = new Color(0,0,0);
    Color STAT = Color.GREEN;
    Dimension FULLSCREEN = Toolkit.getDefaultToolkit().getScreenSize(); //TODO find nice implementation

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
            if (!cellStatus[row][col]){
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
        cellStatus[row][col] = true;
        cellBtns[row][col].setBackground(Color.GREEN);
        
    }

    // metode for å endre status på angitt celle til død og oppdatere statussymbol på knapp
    public void setDead(int row, int col){
        // sett til død
        cellStatus[row][col] = false;
        cellBtns[row][col].setBackground(Color.BLACK);

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
     * Method to initiate all components and render the GUI.
     * 
     * @param controller - the controller object to be used
     * @param rowCount - nuber of rows of the game-grid
     * @param colCount - number of columns of the game grid
     */
    public void init(Controller controller, int rowCount, int colCount){
        this.CTRL = controller;

        // init frame
        this.frame = new JFrame("Game of Life");
        this.frame.setIconImage(new ImageIcon("img/icon.png").getImage());
        this.frame.setPreferredSize(new Dimension(750, 750));
        // this.frame.setUndecorated(true); // //TODO useful for fullscreen feature
        this.frame.setBackground(Color.BLACK);

        // init and add main panel to frame
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.setBackground(Color.BLACK);
        this.frame.add(this.mainPanel);
        
        // init subpanels:
        //      menubar:
        this.menuBar = new JPanel(); // panel for meny-knapper
        this.menuBar.setBackground(this.MENU);
        this.menuBar.setBorder(BorderFactory.createLineBorder(this.MENU, 5));
        //          stat tracker (living cells counter) TODO: add generation tracker?
        this.statTracker = new JPanel();
        this.statTracker.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        this.statTracker.setPreferredSize(new Dimension(200, 50));
        this.statTracker.setBackground(Color.BLACK);
        //          button bar:
        this.buttonBar = new JPanel();
        this.buttonBar.setOpaque(false);
        //      game-grid:
        this.grid = new JPanel();
        this.grid.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10));
        this.grid.setBackground(Color.BLACK);
        
        // layouts for subpanels
        this.grid.setLayout(new GridLayout(rowCount,colCount)); // setter layout på rutenett-panel til verdier angitt av parametre
        this.menuBar.setLayout(new BorderLayout());

        // add 1st level subpanels
        this.mainPanel.add(this.menuBar, BorderLayout.NORTH);
        this.mainPanel.add(this.grid, BorderLayout.CENTER);

        // add menubar subpanels
        this.menuBar.add(this.statTracker, BorderLayout.WEST);
        this.menuBar.add(this.buttonBar, BorderLayout.CENTER);

        // set close op
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // init menu-components
        //      living cells label + counter:
        //          label:
        this.livingCells = new JLabel("Living cells: ");
        this.livingCells.setFont(new Font(this.FONT, Font.BOLD, 16)); 
        this.livingCells.setForeground(this.STAT);
        //          counter:
        this.amtLabel = new JLabel();
        this.amtLabel.setFont(new Font(this.FONT, Font.BOLD, 16)); 
        this.amtLabel.setForeground(this.STAT);
        //      buttons:
        //          start/stop:
        this.startStopBtn = new JButton("Start");
        this.startStopBtn.addActionListener(new StartStop());
        //          regen:
        this.regenBtn = new JButton("Regen");
        this.regenBtn.addActionListener(new Regen());
        //          clear:
        this.clearBtn = new JButton("Clear");
        this.clearBtn.addActionListener(new Clear());
        //          exit:
        this.exitBtn = new JButton("Exit");
        this.exitBtn.addActionListener(new Exit());
        
        // add living counter to menubar
        this.statTracker.add(this.livingCells);
        this.statTracker.add(this.amtLabel);

        // menu-buttons styled and added to menubar
        JButton[] menuBtns = {startStopBtn, regenBtn, clearBtn, exitBtn};
        for (JButton b : menuBtns){
            b.setForeground(this.MENU_BTN_FG);
            b.setBackground(this.MENU_BTN_BG);
            b.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            b.setFont(new Font(this.FONT, Font.BOLD, 16));
            b.setBorderPainted(true);
            b.setFocusPainted(false);
            b.setPreferredSize(new Dimension(75,25));
            this.buttonBar.add(b);
        }
        
        // initierer array for celleknapper
        cellBtns = new JButton[rowCount][colCount];
        cellStatus = new boolean[rowCount][colCount];

        this.rowLock = new CountDownLatch(rowCount);

        // legger til elementer i rutenett-panel: løkke for å legge til riktig antall rader og riktig antall knapper per rad (kolonner) // TODO optimize with threading
        for (int row = 0; row < rowCount; row++){
            int r = row;

            Thread gridBuilder = new Thread(() -> {
                for (int col = 0; col < colCount; col++){
                    JButton cellBtn = new JButton();
                    cellBtn.setFocusPainted(false);
                    cellBtn.setBorderPainted(false);
                    cellBtn.setBackground(Color.BLACK);
                    cellBtns[r][col] = cellBtn;
                    
                    cellBtn.addActionListener(new FlipStatus(r, col));
                    this.grid.add(cellBtn);
                }
                this.rowLock.countDown();

            });
            gridBuilder.start();

        }

        try { this.rowLock.await(); } catch(InterruptedException e) {}
        
        this.CTRL.updateGUI();

        // init amount-living counter
        this.amtLabel.setText(String.format("%d", this.CTRL.livingCount()));

        // pakker, posisjonerer og synliggjør vinduet
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
