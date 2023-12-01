
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Draw a Mancala board
 * @author Ashton Headley
 */
public abstract class BoardStrategy {

    protected final int outerPadding, innerPadding;
    protected final int paddingFromTop;
    protected final int pitWidth, pitHeight;
    protected final int storeWidth, storeHeight;
    protected final Color playerOneColor, playerTwoColor;
    protected final Color backgroundColor;
    // private MancalaGame game;
    protected Model game;

    /**
     * Initialize the class
     *
     * @param game            instance of MancalaGame
     * @param playerOneColor  color to draw player one's pits in
     * @param playerTwoColor  color to draw player two's pits in
     * @param backgroundColor
     */
    protected BoardStrategy(Model game, Color playerOneColor, Color playerTwoColor, int outerPadding, int innerPadding, int paddingFromTop, int pitWidth, int pitHeight,
                            int storeWidth, int storeHeight, Color backgroundColor) {
        this.game = game;
        this.playerOneColor = playerOneColor;
        this.playerTwoColor = playerTwoColor;
        this.outerPadding = outerPadding;
        this.innerPadding = innerPadding;
        this.paddingFromTop = paddingFromTop;
        this.pitWidth = pitWidth;
        this.pitHeight = pitHeight;
        this.storeHeight = storeHeight;
        this.storeWidth = storeWidth;
        this.backgroundColor = backgroundColor;
    }

    /**
     * Copy constructor
     * @param other - object, from which we deep-copy values
     */
    protected BoardStrategy(BoardStrategy other) {
        this.game = new Model(other.game);
        this.playerOneColor = other.playerOneColor;
        this.playerTwoColor = other.playerTwoColor;
        this.outerPadding = other.outerPadding;
        this.innerPadding = other.innerPadding;
        this.paddingFromTop = other.paddingFromTop;
        this.pitWidth = other.pitWidth;
        this.pitHeight = other.pitHeight;
        this.storeHeight = other.storeHeight;
        this.storeWidth = other.storeWidth;
        this.backgroundColor = other.backgroundColor;
    }

    /**
     * Draw one pit depending on x and y coordinates
     * @param g2 - 2d Graphics, which is responsible for rendering 2D figures, text and images
     * @param x - initial coordinate x
     * @param y - initial coordinate y
     */
    protected abstract void drawPit(Graphics2D g2, int x, int y);

    /**
     * Draw the storage spaces
     * @param g Graphics object
     */
    protected abstract void drawStores(Graphics g);

    /**
     * @return a deep copy of a specific board strategy
     */
    protected abstract BoardStrategy getCopy();

    /**
     * Get the player color for the current player
     * @return the player's color
     */
    protected Color getCurrentPlayerColor() {
        return game.getCurrentPlayer() == 1 ? playerOneColor : playerTwoColor;
    }

    /**
     * Get the player color for the other player
     * @return the player's color
     */
    protected Color getOtherPlayerColor() {
        return game.getOtherPlayer() == 1 ? playerOneColor : playerTwoColor;
    }

    /**
     * Get the size of the board as a Dimension object
     * @return size of the board
     */
    public Dimension getSize() {
        int height = 3 * (outerPadding + pitHeight) + innerPadding +20;
        int width = 6 * (pitWidth + innerPadding ) + 2 * (storeWidth + outerPadding);
        return new Dimension(width, height);
    }

    /**
     * Draw a row of pits
     * @param g Graphics object
     * @param x Beginning X position of row
     * @param y Beginning Y position of row
     */
    protected void drawRow(Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < 6; ++i ) {
            drawPit(g2, x, y);
            x += pitWidth + outerPadding;
        }
    }

    /**
     * Draw the board pits and stores
     * @param g Graphics object
     */
    public void drawBoard(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        drawStores(g2);

        int rowX = storeWidth + innerPadding * 2;

        g2.setColor(getCurrentPlayerColor());
        drawRow(g2, rowX, outerPadding);

        g2.setColor(getOtherPlayerColor());
        drawRow(g2, rowX, outerPadding + pitHeight + innerPadding );

        g2.setColor(Color.BLACK);
        drawPitLabels(g2);
    }

    /**
     * Draw the labels for each pit
     * @param g Graphics object
     */
    public void drawPitLabels(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (game.getCurrentPlayer() == 1)
        {
            for (int i = 0; i < 6; ++i ) {
                g2.drawString("B" + i, getPitCenterX(i), storeWidth - innerPadding);
                g2.drawString("A" + i, getPitCenterX(i), getPitY(i)+(pitHeight*2)+ innerPadding-10);
            }
        }

        if (game.getCurrentPlayer() == 2)
        {
            for (int i = 0; i < 6; ++i ) {
                g2.drawString("A" + i, getPitCenterX(i), storeWidth - innerPadding);
                g2.drawString("B" + i, getPitCenterX(i), getPitY(i)+(pitHeight*2) +innerPadding-10);
            }
        }
    }

    /**
     * Retrieve the X position of a pit
     * @param pit a pit number
     * @return the pit's X position
     */
    public int getPitX(int pit) {
        int x;

        // check if pit is a store
        if ( pit == 6 || pit == 13 ) {
            x = outerPadding + storeWidth / 2;

            // subtract pit x from screen width
            x = (pit == 6) ? getSize().width - x : x;
        } else {

            // reverse the top row numbers
            if (pit > 6) pit = -pit + 12;

            // begin with outside padding + mancala
            x = outerPadding + storeWidth;

            // add padding for each box
            x += outerPadding * (pit+1);

            // add boxes
            x += pit * pitWidth;
        }

        return x;
    }

    /**
     * Retrieve the Y position of a pit
     * @param pit a pit number
     * @return the pit's Y position
     */
    public int getPitY(int pit) {

        // check if a pit is a store or in the second row
        if ( pit <= 6 || pit == 13 ) {
            return outerPadding * 2 + pitHeight;
        }

        return outerPadding;
    }

    /**
     * Get the X coordinate in the center of a pit
     * @param pit a pit number
     * @return X position
     */
    public int getPitCenterX(int pit) {
        int x = getPitX(pit);

        if (pit != 6 && pit != 13) {
            x += pitWidth/2;
        }

        return x;
    }

    /**
     * Get the Y coordinate in the center of a pit
     * @param pit a pit number
     * @return Y position
     */
    public int getPitCenterY(int pit) {
        int y = getPitY(pit);

        if (pit != 6 && pit != 13) {
            y += pitHeight/2;
        }

        return y;
    }

    public void setGame(Model game) {
        this.game = game;
    }
}