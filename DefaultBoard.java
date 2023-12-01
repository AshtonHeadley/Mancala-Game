
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Default mancala board layout
 * @author Ashton Headley
 */
public class DefaultBoard extends BoardStrategy {

    private static final Color playerOneColor = Color.blue;
    private static final Color playerTwoColor = Color.red;

    /**
     * Initialize the class
     * @param game instance of MancalaGame
     */
    public DefaultBoard(Model game) {
        super(game, playerOneColor, playerTwoColor,
                15, 20, 50,
                75, 90,
                80, 205, Color.LIGHT_GRAY);
    }

    /**
     * Copy constructor of DefaultBoard class
     * @param other - object, from which we deep-copy values
     */
    public DefaultBoard(DefaultBoard other) {
        super(other);
    }

    /**
     * Returns a copy of the board layout
     */
    @Override
    protected BoardStrategy getCopy() {
        return new DefaultBoard(this);
    }

    /**
     * Draws each pit
     */
    protected void drawPit(Graphics2D g2, int x, int y) {
        Ellipse2D pitOval = new Ellipse2D.Double(x, y+paddingFromTop, pitWidth, pitHeight);
        g2.draw(pitOval);
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
