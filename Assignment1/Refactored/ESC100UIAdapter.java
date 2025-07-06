package Assignment1.Refactored;
import ecs100.UI;

public class ESC100UIAdapter implements UIAdapter {

    @Override
    public void addButton(String text, Runnable action) {
        UI.addButton(text, () -> {
            action.run();
        });
    }

    @Override
    public void setMouseListener(MouseListener listener) {
        UI.setMouseListener((action, x, y) -> listener.handle(action, x, y));
    }

    @Override
    public void setWindowSize(int width, int height) {
        UI.setWindowSize(width, height);
    }

    @Override
    public void setDivider(double position) {
        UI.setDivider(position);
    }

    @Override
    public void println(String message) {
        UI.println(message);
    }

    @Override
    public void quit() {
        UI.quit();
    }
}
