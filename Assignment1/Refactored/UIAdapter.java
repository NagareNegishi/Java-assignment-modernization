package Assignment1.Refactored;

/**
 * MouseListener interface to handle mouse events.
 */
interface MouseListener {
    void handle(String action, double x, double y);
}

public interface UIAdapter {
    void addButton(String text, Runnable action);
    void setMouseListener(MouseListener listener);
    void setWindowSize(int width, int height);
    void setDivider(double position);
    void println(String message);
    void quit();
}
