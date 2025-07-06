package Assignment1.Refactored;

import java.nio.file.Path;

public interface FileChooserAdapter {
    Path open(String title);
    String save(String title);
}
