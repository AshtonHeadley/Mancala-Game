
import java.awt.Color;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Set up the GUI
 * @author Ashton Headley
 */
public class MancalaTest {

    /**
     * This method is run when the program starts
     * @param args arguments passed to the program
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater( () -> {
            createAndShowGUI();
        });
    }

    /**
     * Create and display the GUI
     */
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Mancala");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        MancalaFormatter strat = new MancalaStrategy();
        MancalaGame game = strat.createMancalaGame();

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));

        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(actionEvent -> {
            game.performUndoAction();
        });
        buttonsPanel.add(undoButton);

        JButton confirmTurnButton = new JButton("Confirm turn");
        confirmTurnButton.addActionListener(actionEvent -> {
            game.performConfirmTurnAction();
        });
        buttonsPanel.add(confirmTurnButton);


        frame.add(buttonsPanel);
        frame.add(game);

        frame.pack();
        frame.setVisible(true);
    }
}