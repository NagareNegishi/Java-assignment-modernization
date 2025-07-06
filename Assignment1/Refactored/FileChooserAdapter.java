package Assignment1.Refactored;

import java.nio.file.Path;

public interface FileChooserAdapter {
    Path openFile(String title);
    Path saveFile(String title);
}
