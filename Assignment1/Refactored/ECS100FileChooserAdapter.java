package Assignment1.Refactored;
import java.nio.file.Path;

import ecs100.UIFileChooser;

public class ECS100FileChooserAdapter implements FileChooserAdapter {

    @Override
    public Path openFile(String title) {
        return Path.of(UIFileChooser.open(title));
        
    }

    @Override
    public Path saveFile(String title) {
        return Path.of(UIFileChooser.save(title));
    }
    
}
