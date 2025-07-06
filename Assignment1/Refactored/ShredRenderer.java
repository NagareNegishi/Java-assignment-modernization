package Assignment1.Refactored;

import java.awt.Color;

/**
 * ShredRenderer is responsible for rendering Shred objects.
 * It uses a GraphicsAdapter to draw images and shapes.
 */
public class ShredRenderer {
    private final GraphicsAdapter graphicsAdapter;

    /**
     * Constructs a ShredRenderer with the specified GraphicsAdapter.
     * @param graphicsAdapter the GraphicsAdapter to use for rendering
     */
    public ShredRenderer(GraphicsAdapter graphicsAdapter) {
        this.graphicsAdapter = graphicsAdapter;
    }

    /**
     * Draws a Shred at the specified coordinates.
     * @param shred the Shred to draw
     * @param x the x-coordinate for the Shred
     * @param y the y-coordinate for the Shred
     */
    public void drawShred(Shred shred, double x, double y) {
        graphicsAdapter.drawImage(shred.filename(), x, y, shred.size(), shred.size());
    }
    
    /**
     * Draws a Shred with a border at the specified coordinates.
     * @param shred the Shred to draw
     * @param x the x-coordinate for the Shred
     * @param y the y-coordinate for the Shred
     */
    public void drawShredWithBorder(Shred shred, double x, double y) {
        drawShred(shred, x, y);
        graphicsAdapter.drawRect(x, y, shred.size(), shred.size());
    }

    /**
     * Highlights a Shred by drawing a green border around it.
     * @param shred the Shred to highlight
     * @param x the x-coordinate for the Shred
     * @param y the y-coordinate for the Shred
     */
    public void highlightShred(Shred shred, double x, double y) {
        graphicsAdapter.setColor(Color.GREEN);
        graphicsAdapter.fillRect(x - shred.size() * 0.05, y - shred.size() * 0.05, shred.size() * 1.1, shred.size() * 1.1);
        graphicsAdapter.setColor(Color.BLACK); // Default border color
        drawShred(shred, x, y);
    }
}
