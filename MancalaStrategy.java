
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Uses the Mancala board strategy
 * @author Ashton Headley
 */
public class MancalaStrategy implements MancalaFormatter {

	/**
	 * Starts a mancala game by allowing users to select a board style and how many stones per pit
	 */
    @Override
    public MancalaGame createMancalaGame() {
        Model model = new Model();
        BoardStrategy chosenBoard;

        String[] boardOptionArray = {"Default", "Beach"};
        int boardOption = JOptionPane.showOptionDialog(null, "Select the board style.", "Options Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, boardOptionArray, boardOptionArray[0]);
        if (boardOption == 1)
        {
            chosenBoard = new BeachBoard(model);
        } else {
            chosenBoard = new DefaultBoard(model);
        }

        MancalaGame game = new MancalaGame(model, chosenBoard);
        game.setBackground(chosenBoard.backgroundColor);

        // TODO Auto-generated method stub
        String[] optionArray = {"Three", "Four"};
        int option = JOptionPane.showOptionDialog(null, "Select the number of stones per pit.", "Options Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, optionArray, optionArray[0]);
        if (option == 0)
        {
            game.model.setInitialStonesPerPit(3);
        }
        else
        {
            game.model.setInitialStonesPerPit(4);
        }
        game.repaint();

        return game;
    }


}