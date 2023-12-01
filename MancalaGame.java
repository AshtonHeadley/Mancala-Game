
import java.awt.Color;
import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;


/**
 * This class handles all game operations
 * @author Ashton Headley
 */
@SuppressWarnings("serial")
class MancalaGame extends JPanel implements MouseListener {

    /**
     * Holds an instance of the Board class
     */
    BoardStrategy boardStrategy;
    Model model;
    BackupCommand backup;
    boolean turnAvailable = true;

    /**
     * Initialize the class
     */
    public MancalaGame() {
        model = new Model();
        boardStrategy = new DefaultBoard(model);
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(this);

        backup = new BackupCommand();
    }

    /**
     * Initialize the class
     */
    public MancalaGame(Model model, BoardStrategy boardStrategy) {
        this.model = model;
        this.boardStrategy = boardStrategy;
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(this);

        backup = new BackupCommand();
    }

    /**
     * Undo a move and restores the previous state of the game
     */
    public void performUndoAction() {
        backup.undo();
        turnAvailable = true;
        repaint();
    }

    /**
     * A player confirmed to end their turn and switches to the other player
     */
    public void performConfirmTurnAction() {
        boolean success = model.confirmTurn();
        System.out.println("Confirm action: " + success);
        if (success) {
            backup.clear();
            turnAvailable = true;
            repaint();
        }
    }

    /**
     * Set the size of the window to the size of the board
     * @return the size of the Mancala board
     */
    @Override
    public Dimension getPreferredSize() {
        return boardStrategy.getSize();
    }

    /**
     * Draw the stones in the mancala stores
     * @param g frame Graphics object
     */
    protected void drawStonesStores(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int cx, cy; // extra centering correction

        for (int pit = 0; pit < model.getPitStones().length; ++pit) {
            if (pit == 6 || pit == 13) {
                cx = -3;
                cy = 0;

                for (int i = 0; i < model.getPitStones()[pit]; i++)
                {
                    if (i % 5 == 0)
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx-40, boardStrategy.getPitY(pit) + (i*5)-20, 15, 15);
                        g2.fill(stone);
                    }
                    else if (i % 5 == 1)
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx-25, boardStrategy.getPitY(pit) + (i*5)-20, 15, 15);
                        g2.fill(stone);
                    }
                    else if (i % 5 == 2)
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx-10, boardStrategy.getPitY(pit) + (i*5)-40, 15, 15);
                        g2.fill(stone);
                    }
                    else if (i % 5 == 3)
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx+5, boardStrategy.getPitY(pit) + (i*5)-50, 15, 15);
                        g2.fill(stone);
                    }
                    else
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx+20, boardStrategy.getPitY(pit) + cy+ (i*5)-40, 15, 15);
                        g2.fill(stone);
                    }


                    g2.drawString( Integer.toString(model.getPitStones()[pit]), boardStrategy.getPitCenterX(pit) + cx, boardStrategy.getPitCenterY(pit) + cy +150);
                }
            }

        }

    }

    /**
     * Draw the stones in the pits
     * @param g frame Graphics object
     */
    protected void drawStonesPits(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int cx, cy; // extra centering correction

        for (int pit = 0; pit < model.getPitStones().length; ++pit) {
            if (pit == 6 || pit == 13) {
                cx = -3;
                cy = 0;
            } else if (pit > 9) {
                cx = 3;
                cy = 6;
            } else {
                cx = 7;
                cy = 9;
            }

            if (pit != 6 && pit != 13)
            {
                for (int i = 0; i < model.getPitStones()[pit]; i++)
                {
                    if (i <= 2)
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx + (i*16), boardStrategy.getPitCenterY(pit) + cy +10, 15, 15);
                        g2.fill(stone);
                    }
                    else if (i > 2 && i <= 6)
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx + (i*16)-60, boardStrategy.getPitCenterY(pit) + cy +25, 15, 15);
                        g2.fill(stone);
                    }
                    else if (i > 6 && i <= 10)
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx + (i*16)-130, boardStrategy.getPitCenterY(pit) + cy +40, 15, 15);
                        g2.fill(stone);
                    }
                    else
                    {
                        Ellipse2D stone = new Ellipse2D.Double(boardStrategy.getPitCenterX(pit)- cx + (i*16)-190, boardStrategy.getPitCenterY(pit) + cy +55, 15, 15);
                        g2.fill(stone);
                    }
                }

                g2.drawString( Integer.toString(model.getPitStones()[pit]), boardStrategy.getPitCenterX(pit) + cx, boardStrategy.getPitCenterY(pit) + cy +107);
            }

        }

    }

    /**
     * Draw the information on all the players
     * @param g Graphics object
     */
    protected void drawPlayerInfo(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if ( model.getWinningPlayer() < 0 ) {
            g2.drawString("Player " + model.getCurrentPlayer() + "'s turn", 20, 20);

            // labeling the player on which side
            g2.drawString("Player " + model.getCurrentPlayer() + " ->", 350, 350);
            g2.drawString("<- "+"Player " + model.getOtherPlayer(), 350, 30);
        } else {
            if (model.getWinningPlayer() == 0) {
                g2.drawString("Draw!", 20, 20);
            } else {
                g2.drawString("Player " + model.getWinningPlayer() + " wins!", 20, 20);
            }
        }
    }


    /**
     * Draw the board and stones on the screen
     * @param g frame Graphics object
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.black);

        boardStrategy.drawBoard(g2);

        g2.setColor(Color.DARK_GRAY);
        drawStonesPits(g2);
        drawStonesStores(g2);

        g2.setColor(Color.black);
        drawPlayerInfo(g2);

    }

    /**
     * Watch for when the player selects a pit and perform the turn
     * @param e the mouse click event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (turnAvailable) {
            int x, y;
            int mx = e.getX();
            int my = e.getY();

            // loop through all pits in the bottom row
            for (int pit = 0; pit < 6; ++pit) {
                x = boardStrategy.getPitX(pit);
                y = boardStrategy.getPitY(pit);

                // check if the click was inside the pit area.
                if (mx > x && mx < x + boardStrategy.pitWidth && my > y && my < y + boardStrategy.pitHeight + 50) {
                    backup.makeBackup();
                    model.doPlayerTurn(pit);
                    repaint();
                    if (model.getSwitchTurnAvailable())
                    {
                    	turnAvailable = false;
                    }
                    
                }
            }
        }
    }

    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}

    /**
     * Returns a snapshot containing the game, strategy layout, and model
     * @return a snapshot of the state of the game
     */
    private GameSnapshot createSnapshot() {
        return new GameSnapshot(this, boardStrategy, model);
    }

    /**
     * Creates a snapshot of the current game state
     */
    class GameSnapshot {
        private MancalaGame game;
        private BoardStrategy boardStrategy;
        private Model model;

        public GameSnapshot(MancalaGame game, BoardStrategy boardStrategy, Model model) {
            this.game = game;
            this.boardStrategy = boardStrategy.getCopy();
            this.model = new Model(model);
            this.boardStrategy.setGame(this.model);
        }

        /**
         * Sets the current model and board layout to its old state 
         */
        public void restore() {
            game.model = model;
            game.boardStrategy = boardStrategy;
        }
    }

    /**
     * Controls the undo function
     */
    class BackupCommand {
        private int timesChanged = 0;
        private GameSnapshot backup;
        private boolean changed;

        public void makeBackup() {
            backup = createSnapshot();
            changed = true;
        }

        /**
         * undo and restores the previous state of the game if it hasn't been changed and the number of undo is less than 3 
         */
        public void undo() {
            if (backup != null && changed && timesChanged < 3) {
                backup.restore();
                changed = false;
                timesChanged++;
            }
        }

        /**
         * Clears the saved state and restarts the undo counter to 0
         */
        public void clear() {
            backup = null;
            changed = false;
            timesChanged = 0;
        }
    }

    /**
     * Sets the board layout
     * @param boardStrategy
     */
    public void setBoardStrategy(BoardStrategy boardStrategy) {
        this.boardStrategy = boardStrategy;
    }

    /**
     * Returns the model of the game containing data
     * @return
     */
    public Model getModel() {
        return model;
    }
}