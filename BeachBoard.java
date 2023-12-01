
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

/**
 * Beach mancala board layout
 * @author Ashton Headley
 */
public class BeachBoard extends BoardStrategy {
    private static final Color playerOneColor = Color.blue;
    private static final Color playerTwoColor = Color.red;
    private static final Color brownColor = new Color(120,70,0);

    /**
     * Initialize the class
     * @param game instance of MancalaGame
     */
    public BeachBoard(Model game) {
        super(game, playerOneColor, playerTwoColor,
                15, 20, 50,
                75, 90,
                80, 205, new Color(194, 178, 128));
    }

    /**
     * Copy constructor of BeachBoard class
     * @param other - object, from which we deep-copy values
     */
    public BeachBoard(BeachBoard other) {
        super(other);
    }

    /**
     * Returns a copy of the board layout
     */
    @Override
    protected BoardStrategy getCopy() {
        return new BeachBoard(this);
    }

    /**
     * Draws each pit as if it was drawn in a beach
     */
    protected void drawPit(Graphics2D g2, int x, int y) {
        Color startColor = g2.getColor();
        g2.setColor(brownColor);
        g2.fillOval(x, y + paddingFromTop, pitWidth, pitHeight);
        int dotRadius = 10;
        int dotWidthPadding = 20;
        int dotHeightPadding = 20;
        drawPitDot(g2, x + dotWidthPadding, y + paddingFromTop + dotHeightPadding, dotRadius);
        dotHeightPadding = 5;
        drawPitDot(g2, x + dotWidthPadding, y + paddingFromTop + dotHeightPadding, dotRadius);
        dotWidthPadding = 5;
        dotHeightPadding = 2;
        drawPitDot(g2, x + dotWidthPadding, y + paddingFromTop + dotHeightPadding, dotRadius);

        g2.setColor(startColor);
    }

    /**
     * Draws the stones within a pit
     * @param g2 - Graphics object
     * @param x - x position
     * @param y - y position
     * @param radius - radius of a stone
     */
    private void drawPitDot(Graphics2D g2, int x, int y, int radius) {
        g2.setColor(Color.BLACK);
        g2.fillOval(x, y + paddingFromTop, radius, radius);
    }

    /**
     * Draw the storage spaces
     * @param g Graphics object
     */
    protected void drawStores(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int round = 30;
        int resize = 20;

        // begin first mancala at padding position
        g2.setColor(getCurrentPlayerColor());
        RoundRectangle2D store1 = new RoundRectangle2D.Double(
                outerPadding, outerPadding + resize+paddingFromTop,
                storeWidth, storeHeight - resize*2,
                round, round
        );
        g2.draw(store1);
        g2.drawString("Mancala" + game.getOtherPlayer(), outerPadding+innerPadding, (outerPadding*3) + resize);

        /* second mancala must be after all six boxes,
         * plus the first mancala, plus padding */
        int x = outerPadding + storeWidth + 6 * ( innerPadding + pitWidth );

        g2.setColor(getOtherPlayerColor());
        RoundRectangle2D store2 = new RoundRectangle2D.Double(
                x, outerPadding + resize+paddingFromTop,
                storeWidth, storeHeight - resize*2,
                round, round
        );
        g2.draw(store2);
        g2.drawString("Mancala" + game.getCurrentPlayer(), x+outerPadding, (outerPadding*3) + resize);

        Font currentFont = g2.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
        g2.setFont(newFont);
    }


}
