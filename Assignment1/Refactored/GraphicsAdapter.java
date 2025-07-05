package Assignment1.Refactored;
import java.awt.Color;

public interface GraphicsAdapter {
    void drawImage(String filename, double x, double y, double width, double height);
    void drawRect(double x, double y, double width, double height);
    void setColor(Color color);
    void fillRect(double x, double y, double width, double height);
    void clearGraphics();
}
