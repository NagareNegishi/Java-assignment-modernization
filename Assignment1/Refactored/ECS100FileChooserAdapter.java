package Assignment1.Refactored;
import java.nio.file.Path;

import ecs100.UIFileChooser;

public class ECS100FileChooserAdapter implements FileChooserAdapter {

    @Override
    public Path open(String title) {
        return Path.of(UIFileChooser.open(title));
        
    }

    @Override
    public String save(String title) {
        return UIFileChooser.save(title);
    }
    
}
