
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * The model hold the array of pits and information about the players
 * @author Ashton Headley
 */
public class Model {
    /**
     * Defines the amount of stones in the pits
     */
    private int[] pitStones;

    /**
     * The player currently having a turn.
     * Cannot be any number besides 1 or 2
     */
    private int currentPlayer = 1;

    /**
     * Determines when the game is won and who by
     *
     * Valid values:
     * -1 = game has not ended
     *  0 = game ended in tie
     *  1 = player 1 won
     *  2 = player 2 won
     */
    private int winningPlayer = -1;
    private int initialStones;

    private ArrayList<ChangeListener> listeners;

    private boolean switchTurnAvailable = false;

    /**
     * Initialize the mancala model
     */
    public Model()
    {
        this.pitStones = new int[14];
        listeners = new ArrayList<ChangeListener>();
    }

    /**
     * copy constructor
     * @param other - object, from which we deep-copy values
     */
    public Model(Model other) {
        this.pitStones = Arrays.copyOf(other.pitStones, other.pitStones.length);
        this.currentPlayer = other.currentPlayer;
        this.winningPlayer = other.winningPlayer;
        this.initialStones = other.initialStones;
        this.listeners = new ArrayList<>(other.listeners);
        this.switchTurnAvailable = other.switchTurnAvailable;
    }

    /**
     * Set the number of stones per pit at the beginning of the game
     * default number of stones is four
     * @param initialStones
     */
    public void setInitialStonesPerPit(int initialStones)
    {

        // bottom row
        for (int i = 0; i < 6; i++)
        {
            pitStones[i] = initialStones;
        }
        // top row
        for (int i = 7; i < 13; i++)
        {
            pitStones[i] = initialStones;
        }
        // player 1 mancala
        pitStones[6] = 0;

        // player 2 mancala
        pitStones[13] = 0;

        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener: listeners)
        {
            listener.stateChanged(event);
        }
    }

    /**
	 * Returns the initial number of stones per pit at the start of game
	 * @return number of stones per pit 
	 */
    public int getInitialStones()
    {
        return initialStones;
    }

    /**
     * Retrieve the player who is currently having a turn
     * @return the current player number
     * @throws RuntimeException if the player does not have a valid number
     */
    public int getCurrentPlayer() throws RuntimeException {
        if ( currentPlayer != 1 && currentPlayer != 2 ) {
            throw new RuntimeException("currentPlayer must be either 1 or 2");
        }

        return currentPlayer;
    }

    /**
     * Retrieve the player who is *not* currently having a turn
     * @return the other player number
     */
    public int getOtherPlayer() {
        return currentPlayer == 1 ? 2 : 1;
    }

    /**
     * Begin the other player's turn
     */
    public void switchTurn() {

        // Change the active player
        currentPlayer = getOtherPlayer();

        // Reverse the pit positions
        int[] newStones = new int[14];
        System.arraycopy(pitStones, 7, newStones, 0, 7);
        System.arraycopy(pitStones, 0, newStones, 7, 7);

        pitStones = newStones;
    }

    /**
     * Check if a player has won the game
     */
    public void checkForWin() {
        boolean topRowEmpty = true, bottomRowEmpty = true;

        // Check if the bottom row contains any stones
        for (int i = 0; i < 6; ++i) {
            if (pitStones[i] > 0) {
                bottomRowEmpty = false;
                break;
            }
        }

        // Check if the top row contains any stones
        for (int i = 7; i < 13; ++i) {
            if (pitStones[i] > 0) {
                topRowEmpty = false;
                break;
            }
        }

        // Take the stones from the non-empty row and add them to that player's store
        if (topRowEmpty || bottomRowEmpty) {
            if (topRowEmpty && ! bottomRowEmpty) {
                for (int i = 0; i < 6; ++i) {
                    pitStones[6] += pitStones[i];
                    pitStones[i] = 0;
                }
            } else if (! topRowEmpty && bottomRowEmpty) {
                for (int i = 7; i < 13; ++i) {
                    pitStones[13] += pitStones[i];
                    pitStones[i] = 0;
                }
            }

            // Determine which player holds the most stones
            if (pitStones[6] > pitStones[13] ) {
                winningPlayer = getCurrentPlayer();
            } else if (pitStones[6] < pitStones[13]) {
                winningPlayer = getOtherPlayer();
            } else {
                // tie
                winningPlayer = 0;
            }

        }

    }

    /**
     * Perform a player's turn
     * @param pit the pit selected by the player
     */
    public void doPlayerTurn(int pit) {
    	switchTurnAvailable = false;
    	// perform the player's action
    	boolean	result = moveStones(pit);
    	
        // make sure that a player hasn't run out of stones
        checkForWin();

        // change the player if the current turn is ended
        if ( ! result && winningPlayer < 0 ) {
            switchTurnAvailable = true;
        }
    }

    /**
     * Whether the player has confirmed to end their turn
     * @return confirmation of a player ending their turn
     */
    public boolean confirmTurn() {
        if (switchTurnAvailable) {
            switchTurn();
            switchTurnAvailable = false;
            return true;
        }
        return false;
    }
    
    /**
     * Gets whether a player's turn ends and can be switched to the other player
     * @return switchTurnAvailable
     */
    public boolean getSwitchTurnAvailable()
    {
    	return switchTurnAvailable;
    }

    /**
     * Perform a player's turn by moving the stones between pits
     * @param pit the pit selected by the user
     * @return whether the user's turn is ended
     */
    protected boolean moveStones(final int pit) {
        int pointer = pit;

        // return if pit has no stones
        if ( pitStones[pit] < 1 ) {
            return true;
        }

        // take stones out of pit
        int stones = pitStones[pit];
        pitStones[pit] = 0;

        while ( stones > 0 ) {
            ++pointer;

            // skip other player's storage pit and reset pointer
            if (pointer == 13) {
                pointer = 0;
            } else {
                pitStones[pointer]++;
                stones--;
            }

        }

        // set to point to the opposite pit
        int inversePointer = -pointer + 12;

        // Check for capture
        if (pointer < 6 && pitStones[pointer] == 1 && pitStones[inversePointer] > 0) {

            // Transfer this stone along with opposite pit's stones to store
            pitStones[6] += pitStones[inversePointer] + 1;

            // Clear the pits
            pitStones[pointer] = 0;
            pitStones[inversePointer] = 0;
        }

        
        // return true if the turn ended in storage pit
        return pointer == 6;
    }

    /**
     * 
     * @return
     */
    public int[] getPitStones()
    {
        return pitStones;
    }

    public int getWinningPlayer()
    {
        return winningPlayer;
    }

    public void setWinningPlayer(int winner)
    {
        winningPlayer = winner;
    }

    /**
     Attach a listener to the Model
     @param c the listener
     */
    public void attach(ChangeListener c)
    {
        listeners.add(c);
    }

}