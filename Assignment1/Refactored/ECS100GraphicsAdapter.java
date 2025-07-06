package Assignment1.Refactored;
import java.awt.Color;

import ecs100.UI;
/**
 * ECS100GraphicsAdapter is an adapter class that implements the GraphicsAdapter interface.
 */
public class ECS100GraphicsAdapter implements GraphicsAdapter {
    @Override
    public void drawImage(String filename, double x, double y, double width, double height) {
        UI.drawImage(filename, x, y, width, height);
    }

    @Override
    public void drawRect(double x, double y, double width, double height) {
        UI.drawRect(x, y, width, height);
    }

    @Override
    public void setColor(Color color) {
        UI.setColor(color);
    }

    @Override
    public void fillRect(double x, double y, double width, double height) {
        UI.fillRect(x, y, width, height);
    }

    @Override
    public void clearGraphics() {
        UI.clearGraphics();
    }
}
