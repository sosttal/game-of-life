// imports
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.concurrent.CountDownLatch;

/**
 * View: GUI class for Game of Life
 * 
 * @author <a href="https://github.com/sosttal">Sondre S Talleraas</a>
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
    //      stat-lables:
    JLabel livingCells;     // "Alive cells: "
    JLabel amtLabel;        // living counter

    //      buttons:
    JButton startStopBtn;   // to start/stop update loop
    JButton regenBtn;       // to reset game-grid (new 0th generation)
    JButton clearBtn;       // to clear game-grid (sets all cells to dead)
    JButton exitBtn;        // to exit program

    //  game-grid:
    JButton[][] cellBtns;
    boolean[][] cellStatus;

    // style related constants
    Font BTN_FONT = new Font("Monospaced", Font.BOLD, 20);                                          // font used for menu buttons
    Font STAT_FONT = new Font("Monospaced", Font.BOLD, 16);                                         // font used for 
    Color MENU = new Color(35,35,35);                                                               // bg color of menubar
    Color BTN_BG = new Color(150, 0, 200);                                                          // bg color of buttons
    Color BTN_FG = Color.WHITE;                                                                     // fg (text) color of buttons
    Color STAT_TXT_COLOR = Color.GREEN;                                                             // fg (text) color for stat display
    Dimension FULLSCREEN = Toolkit.getDefaultToolkit().getScreenSize();                             // get system fullscreeen dimensions
    Dimension BTN_SIZE = new Dimension(75,35);                                                      // size of menu buttons
    Dimension STAT_DISPLAY_SIZE = new Dimension(200, 50);                                           // size of stat display
    Dimension FRAME_SIZE = new Dimension(this.FULLSCREEN.height-200,this.FULLSCREEN.height-200);    // default frame size limited by height of screen

    // ActionListeners:
    /**
     * Defines behaviour of start/stop button.
     * 
     * <p> Alternates between starting and stopping the update-loop.
     * 
     */
    class StartStop implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            if (!CTRL.running()){
                // update button text
                startStopBtn.setText("STOP");

                // sends startsignal to controller
                CTRL.startStop();
                    
            } else{
                // update button text
                startStopBtn.setText("START");

                // sends startsignal to controller
                CTRL.startStop();

            }
        }
    }

    /**
     * Defines behaviour of exit button.
     * 
     * <p> Terminates the program.
     */
    class Exit implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            System.exit(0);
        }
    }

    /**
     * Defines behaviour of cell "buttons".
     * 
     * <p> Changes status of related cell, and updates GUI.
     */
    class FlipStatus implements ActionListener{
        // fields
        int row;
        int col;

        /**
         * Stores position of its related Cell-object.
         * 
         * @param r - row of cell
         * @param c - column of cell
         */
        public FlipStatus(int r, int c){
            this.row = r;
            this.col = c;
        }
        
        @Override
        public void actionPerformed(ActionEvent e){
            // checks current status and calls relevant methods based on result
            if (!cellStatus[row][col]){
                setAlive(row,col);
                CTRL.setAlive(row, col);

            } else{
                setDead(row,col);
                CTRL.setDead(row, col);

            }
            
            CTRL.updateGUI();
        }
    }

    /**
     * Defines behaviour of regen button
     * 
     * <p> Generates a fresh 0th generation of cells for the game grid.
     */
    class Regen implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            CTRL.regenCells();
        }
    }

    /**
     * Defines behaviour of clear button.
     * 
     * <p> Clears the game grid of living cells.
     */
    class Clear implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            clear();
        }
    }
    
    /**
     * Updtaes GUI to display the given cell as alive.
     * 
     * @param row
     * @param col
     */
    public void setAlive(int row, int col){
        cellStatus[row][col] = true;
        cellBtns[row][col].setBackground(Color.GREEN);
        
    }

    /**
     * Updates GUI to display the given cell as dead.
     * 
     * @param row - row of cell
     * @param col - column of cell
     */
    public void setDead(int row, int col){
        cellStatus[row][col] = false;
        cellBtns[row][col].setBackground(Color.BLACK);

    }

    /**
     * Sets living-counter to given amount.
     * 
     * @param amt - amount
     */
    public void setLivingCount(int amt){
        this.amtLabel.setText(String.format("%d", amt));

    }

    /**
     * Clears the grid of living cells.
     */
    public void clear(){
        this.CTRL.killAll();
        this.CTRL.updateGUI();
    }

    /**
     * Initiates all components and render the GUI.
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
        this.frame.setPreferredSize(this.FRAME_SIZE);
        this.frame.setResizable(false);
        this.frame.setUndecorated(true);
        this.frame.setBackground(Color.BLACK);

        // init and add main panel to frame
        this.mainPanel = new JPanel();
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));
        this.frame.add(this.mainPanel);
        
        // init subpanels:
        //      menubar:
        this.menuBar = new MotionPanel(this.frame);
        this.menuBar.setBackground(this.MENU);
        this.menuBar.setBorder(BorderFactory.createLineBorder(this.MENU, 5));
        //          stat tracker (living cells counter) TODO: add generation tracker?
        this.statTracker = new JPanel();
        this.statTracker.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        this.statTracker.setPreferredSize(this.STAT_DISPLAY_SIZE);
        this.statTracker.setBackground(Color.BLACK);
        //          button bar:
        this.buttonBar = new JPanel();
        this.buttonBar.setOpaque(false);
        this.buttonBar.setFocusCycleRoot(true);
        //      game-grid:
        this.grid = new JPanel();
        this.grid.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 10));
        //this.grid.setMaximumSize(new Dimension(this.FULLSCREEN.height, this.FULLSCREEN.height)); // TODO add limitations to width of grid panel?
        this.grid.setBackground(Color.BLACK);

        // layouts for subpanels
        this.grid.setLayout(new GridLayout(rowCount,colCount));
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
        this.livingCells.setFont(this.STAT_FONT); 
        this.livingCells.setForeground(this.STAT_TXT_COLOR);
        //          counter:
        this.amtLabel = new JLabel();
        this.amtLabel.setFont(this.STAT_FONT); 
        this.amtLabel.setForeground(this.STAT_TXT_COLOR);
        //      buttons:
        //          start/stop:
        this.startStopBtn = new JButton("START");
        this.startStopBtn.addActionListener(new StartStop());
        //          regen:
        this.regenBtn = new JButton("REGEN");
        this.regenBtn.addActionListener(new Regen());
        //          clear:
        this.clearBtn = new JButton("CLEAR");
        this.clearBtn.addActionListener(new Clear());
        //          exit:
        this.exitBtn = new JButton("EXIT");
        this.exitBtn.addActionListener(new Exit());
        
        // add living-counter to stat tracker display
        this.statTracker.add(this.livingCells);
        this.statTracker.add(this.amtLabel);

        // menu-buttons styled and added to menubar
        JButton[] menuBtns = {startStopBtn, regenBtn, clearBtn, exitBtn};
        for (JButton b : menuBtns){
            b.setForeground(this.BTN_FG);
            b.setBackground(this.BTN_BG);
            b.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            b.setFont(this.BTN_FONT);
            b.setBorderPainted(true);
            b.setFocusPainted(false);
            b.setPreferredSize(this.BTN_SIZE);
            this.buttonBar.add(b);
        }
        
        // init array for cell-button grid
        cellBtns = new JButton[rowCount][colCount];
        cellStatus = new boolean[rowCount][colCount];
        
        //this.rowLock = new CountDownLatch(rowCount);

        // configures and adds cell-buttons to grid panel
        for (int row = 0; row < rowCount; row++){ // TODO: can stable threading be implemented?
            for (int col = 0; col < colCount; col++){
                JButton cellBtn = new JButton();
                cellBtn.setFocusPainted(false);
                cellBtn.setBorderPainted(false);
                cellBtn.setFocusable(false);
                cellBtn.setBackground(Color.BLACK);
                cellBtns[row][col] = cellBtn;
                
                cellBtn.addActionListener(new FlipStatus(row, col));
                this.grid.add(cellBtn);
            }
        }
        this.CTRL.updateGUI();

        // init amount-living counter
        this.amtLabel.setText(String.format("%d", this.CTRL.livingCount()));

        // packs, positions and displays frame
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }
}
