package Assignment1.Refactored;
import java.awt.Color;

/**
 * GraphicsAdapter interface defines methods for drawing graphics.
 * This allows for different implementations of graphics rendering,
 * such as using ECS100 or other libraries.
 */
public interface GraphicsAdapter {
    /**
     * Draw an image at specified coordinates with given width and height.
     *
     * @param filename the name of the image file
     * @param x        the x-coordinate for the image
     * @param y        the y-coordinate for the image
     * @param width    the width of the image
     * @param height   the height of the image
     */
    void drawImage(String filename, double x, double y, double width, double height);
    
    /**
     * Draw a rectangle at specified coordinates with given width and height.
     *
     * @param x      the x-coordinate for the rectangle
     * @param y      the y-coordinate for the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    void drawRect(double x, double y, double width, double height);
    
    /**
     * Fill a rectangle at specified coordinates with given width and height.
     *
     * @param x      the x-coordinate for the rectangle
     * @param y      the y-coordinate for the rectangle
     * @param width  the width of the rectangle
     * @param height the height of the rectangle
     */
    void fillRect(double x, double y, double width, double height);
    
    /**
     * Set the current drawing color.
     *
     * @param color the color to set for drawing
     */
    void setColor(Color color);

    /**
     * Clear the graphics area.
     */
    void clearGraphics();
}
